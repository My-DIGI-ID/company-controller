/*
 * Copyright 2021 Bundesrepublik Deutschland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.ssi.controller.company.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.activation.DataHandler;
import javax.imageio.ImageIO;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.ibm.ssi.controller.company.client.ACAPYClient;
import com.ibm.ssi.controller.company.client.model.ConnectionRecordDTO;
import com.ibm.ssi.controller.company.client.model.CredAttrSpec;
import com.ibm.ssi.controller.company.client.model.CredentialOfferRequestDTO;
import com.ibm.ssi.controller.company.client.model.CredentialPreview;
import com.ibm.ssi.controller.company.client.model.InvitationRequestDTO;
import com.ibm.ssi.controller.company.client.model.InvitationResultDTO;
import com.ibm.ssi.controller.company.domain.Employee;
import com.ibm.ssi.controller.company.repository.EmployeeRepository;
import com.ibm.ssi.controller.company.service.EmailTemplateService;
import com.ibm.ssi.controller.company.service.EmployeeService;
import com.ibm.ssi.controller.company.service.IssuedCredentialService;
import com.ibm.ssi.controller.company.service.QRCodeGeneratorService;
import com.ibm.ssi.controller.company.service.dto.EmailTemplateDTO;
import com.ibm.ssi.controller.company.service.dto.EmployeeDTO;
import com.ibm.ssi.controller.company.service.exceptions.ConnectionNotFoundException;
import com.ibm.ssi.controller.company.service.exceptions.CredentialForEmployeeAlreadyExistsException;
import com.ibm.ssi.controller.company.service.exceptions.EMailGenerationException;
import com.ibm.ssi.controller.company.service.exceptions.EmployeeAlreadyExistsException;
import com.ibm.ssi.controller.company.service.exceptions.EmployeeNotFoundException;
import com.ibm.ssi.controller.company.service.exceptions.IssuedCredentialNotFoundException;
import com.ibm.ssi.controller.company.service.exceptions.QRCodeGenerationException;
import com.ibm.ssi.controller.company.service.mapper.EmployeeMapper;
import com.ibm.ssi.controller.company.util.ResourceReader;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    IssuedCredentialService issuedCredentialService;

    @Autowired
    EmailTemplateService emailTemplateService;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    ACAPYClient acapyClient;

    @Autowired
    QRCodeGeneratorService qrCodeGeneratorService;

    @Value("${ssibk.company.controller.agent.credential_definition_id}")
    private String credentialDefinitionId;

    @Value("${ssibk.company.controller.agent.apikey}")
    private String apiKey;

    @Value("${ssibk.company.controller.agent.imageurl}")
    private String imageUrlReference;

    @Value("classpath:templates/mail/invitation-email-template.html")
    Resource defaultEmailTemplate;

    @Value("${ssibk.company.controller.invitation_email.template_path}")
    String emailTemplatePath;

    /**
     * Save a Employee in the Database.
     *
     * @param employeeDTO the entity to save.
     * @return the persisted entity.
     * @throws EmployeeAlreadyExistsException
     */
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) throws EmployeeAlreadyExistsException, CredentialForEmployeeAlreadyExistsException {

        if (this.employeeRepository.existsById(employeeDTO.getEmployeeId())) {
            throw new EmployeeAlreadyExistsException();
        }

        if (this.issuedCredentialService.existsById(employeeDTO.getEmployeeId())) {
            throw new CredentialForEmployeeAlreadyExistsException();
        }

        Employee employee = employeeMapper.employeeDTOToEmployee(employeeDTO);
        this.employeeRepository.insert(employee);

        return employeeDTO;
    }

    /**
     * Update an Employee in the Database.
     *
     * @param employeeDTO the entity to save.
     * @return the persisted entity.
     * @throws EmployeeNotFoundException
     */
    @Override
    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO) throws EmployeeNotFoundException {

        log.debug("Update Employee: {}", employeeDTO);

        Optional<Employee> existingEmployee = this.employeeRepository.findById(employeeDTO.getEmployeeId());

        if (existingEmployee.isPresent()) {

            this.employeeRepository.save(this.employeeMapper.employeeDTOToEmployee(employeeDTO));

            return employeeDTO;

        } else {
            throw new EmployeeNotFoundException();
        }
    }

    /**
     * Create an employee invitation.
     *
     * return the entity.
     *
     * @throws EmployeeNotFoundException
     *
     * @throws MalformedURLException
     *
     * @throws InterruptedException
     */
    public String createEmployeeInvitation(String id) throws EmployeeNotFoundException {

        log.debug("Create new Employee invitation: {}");

        Optional<Employee> existingEmployee = this.employeeRepository.findById(id);
        if (!existingEmployee.isPresent()) {
            throw new EmployeeNotFoundException();
        }

        InvitationResultDTO invitation = this.acapyClient.createInvitation(apiKey, id, new InvitationRequestDTO());
        String invitationURL = invitation.getInvitationUrl();

        if (this.imageUrlReference != null) {
            invitationURL = this.addImageURLToInvitation(invitation.getInvitationUrl());
        }

        return invitationURL;
    }

    public byte[] createEmployeeInvitationAsQRCode(String id) throws EmployeeNotFoundException, QRCodeGenerationException {

        Optional<Employee> existingEmployee = this.employeeRepository.findById(id);

        if (!existingEmployee.isPresent()) {
            throw new EmployeeNotFoundException();
        }

        String url = createEmployeeInvitation(id);
        BufferedImage image = this.qrCodeGeneratorService.generateQRCodeImage(url);

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, "png", os);

            return os.toByteArray();
        } catch (IOException e) {
            throw new QRCodeGenerationException(e);
        }
    }

    public byte[] createEmployeeInvitationAsEmail(String id) throws EmployeeNotFoundException, EMailGenerationException {
        
        if (!this.employeeRepository.findById(id).isPresent()) {
            throw new EmployeeNotFoundException();
        }

        Employee employee = this.employeeRepository.findById(id).get();

        try {
            Resource emailTemplateResource = new FileSystemResource(this.emailTemplatePath);

            if (!emailTemplateResource.exists()) {
                emailTemplateResource = this.defaultEmailTemplate;
            }

            String emailTemplate = ResourceReader.asString(emailTemplateResource);
            EmailTemplateDTO emailTemplateDTO = this.emailTemplateService.getEmailTemplate();

            emailTemplate = emailTemplate.replace("{{SUBJECT}}", emailTemplateDTO.getSubject());
            emailTemplate = emailTemplate.replace("{{BODY_BEFORE_QR_CODE}}", emailTemplateDTO.getBodyBeforeQR());
            emailTemplate = emailTemplate.replace("{{BODY_AFTER_QR_CODE}}", emailTemplateDTO.getBodyAfterQR());
            emailTemplate = this.replaceEmailTemplateVariables(emailTemplate, employee);
            
            Message message = new MimeMessage(Session.getInstance(System.getProperties()));
            InternetAddress[] addresses = {};

            if (employee.getEmail() != null) {
                addresses = InternetAddress.parse(employee.getEmail());
            }

            message.setRecipients(Message.RecipientType.TO, addresses);
            message.setSubject(this.replaceEmailTemplateVariables(emailTemplateDTO.getSubject(), employee));
            message.setHeader("X-Unsent", "1");

            Multipart multipart = new MimeMultipart();
            // first part (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(emailTemplate, "text/html; charset=utf-8");
            multipart.addBodyPart(messageBodyPart);
            // second part (the qr-code)
            byte[] qrCodeImage = this.createEmployeeInvitationAsQRCode(id);
            ByteArrayDataSource imageSource = new ByteArrayDataSource(qrCodeImage, MediaType.IMAGE_PNG_VALUE);
            BodyPart imagePart = new MimeBodyPart();
            imagePart.setHeader("Content-ID", "qr-code");
            imagePart.setDisposition(MimeBodyPart.INLINE);
            imagePart.setDataHandler(new DataHandler(imageSource));
            multipart.addBodyPart(imagePart);

            message.setContent(multipart);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            message.writeTo(os);
            
            return os.toByteArray();
        } catch (IOException | MessagingException | QRCodeGenerationException e) {
            throw new EMailGenerationException(e);
        }
    }

    /**
     * Get all the stored employees.
     *
     * @return the list of entities.
     */
    @Override
    public List<EmployeeDTO> getAllEmployees() {

        log.debug("Get all Employees");

        return this.employeeRepository.findAll().stream().map(employeeMapper::employeeToEmployeeDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Get one employee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<EmployeeDTO> getEmployeeById(String id) {

        log.debug("Get Employee by Id {}", id);

        return this.employeeRepository.findById(id).map(employeeMapper::employeeToEmployeeDTO);

    }

    /**
     * Delete the employee by Id.
     *
     * @param id of the entity.
     */
    public void deleteEmployeeById(String id) {

        log.debug("Delete Employee with ID: {}", id);

        employeeRepository.deleteById(id);

    }

    /**
     * Issues a credential for the employee withe the given Id.
     *
     * @param id           of the employee.
     * @param connectionId of the connection to the employee's wallet
     * @throws EmployeeNotFoundException
     * @throws EmployeeAlreadyExistsException
     */
    public void sendCredentialOffer(String id, String connectionId) throws EmployeeNotFoundException {

        Optional<EmployeeDTO> findEmployee = getEmployeeById(id);
        if (!findEmployee.isPresent()) {
            throw new EmployeeNotFoundException();
        } else {
            EmployeeDTO existingEmployee = findEmployee.get();
            CredentialOfferRequestDTO credentialOfferRequestDTO = this.prepareCredentialOfferRequestDTO(connectionId,
                    credentialDefinitionId, existingEmployee);
            this.acapyClient.sendCredentialOffer(apiKey, credentialOfferRequestDTO);
        }
    }

    private CredentialOfferRequestDTO prepareCredentialOfferRequestDTO(String connectionId,
            String credentialDefinitionId, EmployeeDTO employee) {
        List<CredAttrSpec> attributes = new ArrayList<CredAttrSpec>();
        attributes.add(new CredAttrSpec("firstName", employee.getFirstName()));
        attributes.add(new CredAttrSpec("lastName", employee.getLastName()));
        attributes.add(new CredAttrSpec("firmName", employee.getFirmName()));
        attributes.add(new CredAttrSpec("firmSubject", employee.getFirmSubject()));
        attributes.add(new CredAttrSpec("firmStreet", employee.getFirmStreet()));
        attributes.add(new CredAttrSpec("firmCity", employee.getFirmCity()));
        attributes.add(new CredAttrSpec("firmPostalcode", employee.getFirmPostalCode().toString()));

        CredentialPreview credentialPreview = new CredentialPreview(attributes);

        CredentialOfferRequestDTO credentialOfferRequestDTO = new CredentialOfferRequestDTO(connectionId,
                credentialDefinitionId, credentialPreview);
        return credentialOfferRequestDTO;
    }

    private String addImageURLToInvitation(String invitation_url) {

        try {
            String[] elements = invitation_url.split("=", 2);
            byte[] decodedBytes = Base64.getDecoder().decode(elements[1]);
            String decodedString = new String(decodedBytes);
            JSONObject jsonObj = new JSONObject(decodedString);
            jsonObj.put("imageUrl", imageUrlReference);
            String encodedString = Base64.getEncoder().encodeToString(jsonObj.toString().getBytes());
            elements[1] = encodedString;
            invitation_url = String.join("=", elements);
        } catch (JSONException e) {
            this.log.error("failed to add image to inviationUrl", e);
        }

        return invitation_url;
    }

    @Override
    public void resendCredentialOffer(EmployeeDTO employee) throws EmployeeNotFoundException, ConnectionNotFoundException, IssuedCredentialNotFoundException, EmployeeAlreadyExistsException, CredentialForEmployeeAlreadyExistsException {
            ConnectionRecordDTO[] connections = this.acapyClient.getConnectionsRecordByAlias(apiKey, employee.getEmployeeId()).getConnections();
            log.debug("Found {} existing connections with alias {}", connections.length, employee.getEmployeeId());

            // find the connection with equal alias (substrings are also matched)
            String connectionId = "";
            for (int i = 0; i < connections.length; i++) {
                if (connections[i].getAlias().equals(employee.getEmployeeId())) {
                    connectionId = connections[i].getConnectionId();
                    break;
                }
            }

            if (connectionId == "") {
                throw new ConnectionNotFoundException();
            } else {
                log.debug("Resending to connection id: {}", connectionId);
                this.sendCredentialOfferAgain(employee, connectionId);
            }
    }

    private void sendCredentialOfferAgain(EmployeeDTO employee, String connectionId) throws EmployeeAlreadyExistsException, CredentialForEmployeeAlreadyExistsException {

        this.issuedCredentialService.deleteIssuedCredential(employee.getEmployeeId());
        Optional<EmployeeDTO> findEmployee = getEmployeeById(employee.getEmployeeId());
        if (!findEmployee.isPresent()) {
            employee = this.createEmployee(employee);
        }
        CredentialOfferRequestDTO credentialOfferRequestDTO = this.prepareCredentialOfferRequestDTO(connectionId,
                    credentialDefinitionId, employee);
        this.acapyClient.sendCredentialOffer(apiKey, credentialOfferRequestDTO);
    }

    private String replaceEmailTemplateVariables(String textWithVariables, Employee employee) {
        return textWithVariables.replace("EMPLOYEE_ID", employee.getEmployeeId())
                .replace("FIRST_NAME", employee.getFirstName())
                .replace("LAST_NAME", employee.getLastName())
                .replace("EMAIL", employee.getEmail())
                .replace("FIRM_NAME", employee.getFirmName())
                .replace("FIRM_SUBJECT", employee.getFirmSubject())
                .replace("FIRM_STREET", employee.getFirmStreet())
                .replace("FIRM_POSTAL_CODE", employee.getFirmPostalCode())
                .replace("FIRM_CITY", employee.getFirmCity());
    }
}
