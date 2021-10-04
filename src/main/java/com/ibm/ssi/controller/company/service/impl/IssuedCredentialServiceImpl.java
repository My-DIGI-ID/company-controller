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

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ibm.ssi.controller.company.client.ACAPYClient;
import com.ibm.ssi.controller.company.client.model.RevocationRequestDTO;
import com.ibm.ssi.controller.company.domain.IssuedCredential;
import com.ibm.ssi.controller.company.repository.IssuedCredentialRepository;
import com.ibm.ssi.controller.company.service.EmployeeService;
import com.ibm.ssi.controller.company.service.IssuedCredentialService;
import com.ibm.ssi.controller.company.service.dto.IssuedCredentialDTO;
import com.ibm.ssi.controller.company.service.mapper.IssuedCredentialMapper;

import com.ibm.ssi.controller.company.service.dto.EmployeeDTO;
import com.ibm.ssi.controller.company.service.exceptions.ConnectionNotFoundException;
import com.ibm.ssi.controller.company.service.exceptions.CredentialForEmployeeAlreadyExistsException;
import com.ibm.ssi.controller.company.service.exceptions.EmployeeAlreadyExistsException;
import com.ibm.ssi.controller.company.service.exceptions.EmployeeNotFoundException;
import com.ibm.ssi.controller.company.service.exceptions.IssuedCredentialNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IssuedCredentialServiceImpl implements IssuedCredentialService {

    private final Logger log = LoggerFactory.getLogger(IssuedCredentialServiceImpl.class);

    @Autowired
    IssuedCredentialMapper issuedCredentialMapper;

    @Autowired
    ACAPYClient acapyClient;

    @Value("${ssibk.company.controller.agent.apikey}")
    private String apiKey;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    IssuedCredentialRepository issuedCredentialRepository;

    @Override
    public IssuedCredential createIssuedCredential(IssuedCredential issuedCredential) {

        log.debug("Create New IssuedCredential: {}", issuedCredential);

        issuedCredentialRepository.save(issuedCredential);

        return null;
    }

    @Override
    public void reIssueCredential(EmployeeDTO employeeDTO) throws EmployeeAlreadyExistsException, CredentialForEmployeeAlreadyExistsException, EmployeeNotFoundException, ConnectionNotFoundException, IssuedCredentialNotFoundException {

        log.debug("Resend issued credential to employee with id {}", employeeDTO.getEmployeeId());

        this.deleteIssuedCredential(employeeDTO.getEmployeeId());
        this.employeeService.createEmployee(employeeDTO);
        this.employeeService.resendCredentialOffer(employeeDTO);
    }

    @Override
    public List<IssuedCredentialDTO> getAllIssuedCredential() {

        log.debug("Get All Issued Credential");

        return this.issuedCredentialRepository.findAll().stream().map(this.issuedCredentialMapper::issuedCredentialToIssuedCredentialDTO)
            .collect(Collectors.toCollection(LinkedList::new));

    }

    @Override
    public void deleteIssuedCredential(String id) {

        log.debug("Delete issued credential with ID: {}", id);

        Optional<IssuedCredential> existingIssuedCredential = this.issuedCredentialRepository.findById(id);

        if(existingIssuedCredential.isPresent()) {

            RevocationRequestDTO revocationRequestDTO = new RevocationRequestDTO();
            revocationRequestDTO.setCredRevId(existingIssuedCredential.get().getCredentialRevocationId());
            revocationRequestDTO.setRevRegId(existingIssuedCredential.get().getRevocationRegistryId());
            revocationRequestDTO.setPublish(Boolean.TRUE);

            log.debug("Revoke the credential");
            this.acapyClient.revokeCredential(apiKey, revocationRequestDTO);

            log.debug("Delete the credential in the mongodb");
            this.issuedCredentialRepository.deleteById(id);

        }

    }

    @Override
    public Optional<IssuedCredentialDTO> getIssuedCredentialById(String id) {
        log.debug("Get Issued Credential by Id {}", id);

        return this.issuedCredentialRepository.findById(id).map(this.issuedCredentialMapper::issuedCredentialToIssuedCredentialDTO);

    }

    @Override
    public boolean existsById(String id) {
        log.debug("Issued Credential exists for Id {}", id);

        return this.issuedCredentialRepository.existsById(id);
    }
}
