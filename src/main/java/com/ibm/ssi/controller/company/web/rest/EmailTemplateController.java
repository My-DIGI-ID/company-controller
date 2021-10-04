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

import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import com.ibm.ssi.controller.company.service.EmailTemplateService;
import com.ibm.ssi.controller.company.service.dto.EmailTemplateDTO;
import com.ibm.ssi.controller.company.service.exceptions.EmailTemplateNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "EmailTemplate", description = "CRUD for Email Template operations")
@RestController
@RequestMapping("/api")
public class EmailTemplateController {

    private final Logger log = LoggerFactory.getLogger(EmailTemplateController.class);

    @Autowired
    EmailTemplateService emailTemplateService;

    /**
     * {@code POST  /email/update-template} : Update an Email Template.
     *
     * @body emailTemplateDTO
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new EmailTemplateDTO, or with status {@code 400 (Bad Request)} if
     *         something goes wrong.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("email/update-template")
    @Operation
    @SecurityRequirement(name = "X-API-Key")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<EmailTemplateDTO> updateEmailTemplate(@Valid @RequestBody EmailTemplateDTO emailTemplateDTO) throws URISyntaxException {
        log.debug("REST request to update invitation email template");
        
        try {
            EmailTemplateDTO updatedTemplateDTO = this.emailTemplateService.updateEmailTemplate(emailTemplateDTO);

            return new ResponseEntity<EmailTemplateDTO>(updatedTemplateDTO, HttpStatus.OK);
        } catch (EmailTemplateNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    /**
     * {@code GET  /emailTemplate} : get all employees stored in the databse.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employees in body
     */
    @GetMapping("email/get-template")
    @Operation
    @SecurityRequirement(name="X-API-Key")
    @SecurityRequirement(name="bearerAuth")
    public ResponseEntity<EmailTemplateDTO> getEmailTemplate() throws URISyntaxException {
        log.debug("REST request to get the invitation email template");

        return ResponseEntity.ok(this.emailTemplateService.getEmailTemplate());
    }
}