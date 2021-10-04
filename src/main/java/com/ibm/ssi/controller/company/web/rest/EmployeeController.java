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

package com.ibm.ssi.controller.company.web.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.ibm.ssi.controller.company.client.ACAPYClient;
import com.ibm.ssi.controller.company.service.EmployeeService;
import com.ibm.ssi.controller.company.service.IssuedCredentialService;
import com.ibm.ssi.controller.company.service.QRCodeGeneratorService;
import com.ibm.ssi.controller.company.service.dto.EmployeeDTO;
import com.ibm.ssi.controller.company.service.dto.InvitationResponseDTO;
import com.ibm.ssi.controller.company.service.exceptions.ConnectionNotFoundException;
import com.ibm.ssi.controller.company.service.exceptions.CredentialForEmployeeAlreadyExistsException;
import com.ibm.ssi.controller.company.service.exceptions.EMailGenerationException;
import com.ibm.ssi.controller.company.service.exceptions.EmployeeAlreadyExistsException;
import com.ibm.ssi.controller.company.service.exceptions.EmployeeNotFoundException;
import com.ibm.ssi.controller.company.service.exceptions.IssuedCredentialNotFoundException;
import com.ibm.ssi.controller.company.service.exceptions.QRCodeGenerationException;
import com.ibm.ssi.controller.company.util.validation.ValidatableList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Employees", description = "CRUD for Employees operations")
@RestController
@RequestMapping("/api")
public class EmployeeController {

    private final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    EmployeeService employeeService;

    @Autowired
    QRCodeGeneratorService qrCodeGeneratorService;

    @Autowired
    IssuedCredentialService issuedCredentialService;

    @Autowired
    ACAPYClient acapyClient;

    @Value("${ssibk.company.controller.agent.apikey}")
    private String apiKey;

    /**
     * {@code GET /employee/{employeeId}/create-invitation : return an invitation for a
     * specific employee id.
     *
     * @param id the id of the employee.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the InvitationResultDTO, or with status {@code 404 (Not Found)}.
     * @throws MalformedURLException
     */
    @PostMapping("/employee/{employeeId}/create-invitation")
    @Operation
    @SecurityRequirement(name = "X-API-Key")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<InvitationResponseDTO> createInvitation(@PathVariable String employeeId)
            throws MalformedURLException {
        log.debug("REST request to create invitation for employee ID : {}", employeeId);

        String url;
        try {
            url = employeeService.createEmployeeInvitation(employeeId);
            InvitationResponseDTO invitationResponse = new InvitationResponseDTO();
            invitationResponse.setUrl(url);
            return new ResponseEntity<InvitationResponseDTO>(invitationResponse, HttpStatus.OK);
        } catch (EmployeeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping(path = "/employee/{employeeId}/create-invitation/qr", produces = MediaType.IMAGE_PNG_VALUE)
    @Operation
    @SecurityRequirement(name = "X-API-Key")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<InputStreamResource> createInvitationbyQR(@PathVariable String employeeId) throws IOException {

        try {
            byte[] qrCode = this.employeeService.createEmployeeInvitationAsQRCode(employeeId);
            InputStream is = new ByteArrayInputStream(qrCode);

            return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .contentLength(qrCode.length)
                .body(new InputStreamResource(is));
        } catch (EmployeeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (QRCodeGenerationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(path = "/employee/{employeeId}/create-invitation/e-mail", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation
    @SecurityRequirement(name = "X-API-Key")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(type = "file")))
    public ResponseEntity<InputStreamResource> createInvitationAsEMail(@PathVariable String employeeId) throws IOException {

        try {
            byte[] email = this.employeeService.createEmployeeInvitationAsEmail(employeeId);
            InputStream is = new ByteArrayInputStream(email);

            return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(email.length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + employeeId + "-invitation.eml\"")
                .body(new InputStreamResource(is));
        } catch (EmployeeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EMailGenerationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * {@code POST  /employee} : Create a new Employee.
     *
     * @body employeeDTO
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new EmployeeDTO, or with status {@code 400 (Bad Request)} if
     *         something goes wrong.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employee")
    @Operation
    @SecurityRequirement(name = "X-API-Key")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) throws URISyntaxException {
        log.debug("REST request to create a new Employee : {}", employeeDTO);

        try {
            EmployeeDTO updatedEmployee = this.employeeService.createEmployee(employeeDTO);

            return new ResponseEntity<EmployeeDTO>(updatedEmployee, new HttpHeaders(), HttpStatus.OK);
        } catch (EmployeeAlreadyExistsException | CredentialForEmployeeAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * {@code POST  /employees} : Create new Employees.
     *
     * @body employeesDTO
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new ValidList<EmployeeDTO>, or with status {@code 400 (Bad Request)} if
     *         something goes wrong.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employees")
    @Operation
    @SecurityRequirement(name = "X-API-Key")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ValidatableList<EmployeeDTO>> createEmployees(@RequestBody @Valid ValidatableList<EmployeeDTO> employeesDTO) throws URISyntaxException {
        log.debug("REST request to create new Employees : {}", employeesDTO);

        ValidatableList<EmployeeDTO> createdEmployees = new ValidatableList<EmployeeDTO>();

        employeesDTO.forEach((employeeDTO) -> {
            try {
                EmployeeDTO createdEmployee = this.employeeService.createEmployee(employeeDTO);

                createdEmployees.add(createdEmployee);
            } catch (EmployeeAlreadyExistsException | CredentialForEmployeeAlreadyExistsException e) {
                createdEmployees.forEach((createdEmployeeDTO) -> {
                    this.employeeService.deleteEmployeeById(createdEmployeeDTO.getEmployeeId());
                });

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        });

        return new ResponseEntity<ValidatableList<EmployeeDTO>>(createdEmployees, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * {@code PUT /employee{id} : update an employee with specific id.
     *
     * @body employeeDTO
     *
     * @return the {@link ResponseEntity} with status {@code 200 (Updated)} and with
     *         body the new EmployeeDTO, or with status {@code 400 (Bad Request)} if
     *         something goes wrong.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employee")
    @Operation
    @SecurityRequirement(name = "X-API-Key")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<EmployeeDTO> updateEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        log.debug("REST request to update an Employee : {}", employeeDTO);

        try {
            EmployeeDTO updatedEmployee = this.employeeService.updateEmployee(employeeDTO);

            return new ResponseEntity<EmployeeDTO>(updatedEmployee, new HttpHeaders(), HttpStatus.OK);
        } catch (EmployeeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * {@code GET  /employee} : get all employees stored in the databse.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employees in body
     */
    @GetMapping("/employee")
    @Operation
    @SecurityRequirement(name="X-API-Key")
    @SecurityRequirement(name="bearerAuth")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() throws URISyntaxException {
        log.debug("REST request to get all employees");

        return ResponseEntity.ok(this.employeeService.getAllEmployees());
    }

    /**
     * {@code GET  /employee/{employeeId}} : get an employee with specific id.
     *
     * @param employeeId the id of the employee to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the EmployeeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employee/{employeeId}")
    @Operation
    @SecurityRequirement(name="X-API-Key")
    @SecurityRequirement(name="bearerAuth")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable String employeeId) {
        log.debug("REST request to get employee by ID: {}", employeeId);

        Optional<EmployeeDTO> employeeDTO = this.employeeService.getEmployeeById(employeeId);

        return ResponseUtil.wrapOrNotFound(employeeDTO);
    }

    /**
     * {@code DELETE  /employee/{employeeId}} : delete the employee with "id" .
     *
     * @param employeeId the id of the Employee to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employee/{employeeId}")
    @Operation
    @SecurityRequirement(name="X-API-Key")
    @SecurityRequirement(name="bearerAuth")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable String employeeId, @RequestParam(name = "Revoke credential", required = false, defaultValue = "true") Boolean revoke) {
        log.debug("REST request to delete employee by ID: {}", employeeId);

        employeeService.deleteEmployeeById(employeeId);

        return ResponseEntity.noContent().build();
    }
}
