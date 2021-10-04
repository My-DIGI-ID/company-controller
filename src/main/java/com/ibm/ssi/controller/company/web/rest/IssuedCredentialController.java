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
import java.util.Optional;

import javax.validation.Valid;

import com.ibm.ssi.controller.company.service.IssuedCredentialService;
import com.ibm.ssi.controller.company.service.dto.EmployeeDTO;
import com.ibm.ssi.controller.company.service.dto.IssuedCredentialDTO;
import com.ibm.ssi.controller.company.service.exceptions.ConnectionNotFoundException;
import com.ibm.ssi.controller.company.service.exceptions.CredentialForEmployeeAlreadyExistsException;
import com.ibm.ssi.controller.company.service.exceptions.EmployeeAlreadyExistsException;
import com.ibm.ssi.controller.company.service.exceptions.EmployeeNotFoundException;
import com.ibm.ssi.controller.company.service.exceptions.IssuedCredentialNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Issued Credentials", description = "CRUD for Issued Credentials operations")
@RestController
@RequestMapping("/api")
public class IssuedCredentialController {

    private final Logger log = LoggerFactory.getLogger(IssuedCredentialController.class);

    @Autowired
    IssuedCredentialService issuedCredentialService;

    /**
     * {@code GET  /issued-credential} : get all issued-credential stored in the databse.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of issued credentials in body
     */
    @GetMapping("/issued-credential")
    @Operation
    @SecurityRequirement(name="X-API-Key")
    @SecurityRequirement(name="bearerAuth")
    public List<IssuedCredentialDTO> getAllIssuedCredentials() throws URISyntaxException {

        log.debug("REST request to get all issued credentials");

        return this.issuedCredentialService.getAllIssuedCredential();
    }

    /**
     * {@code GET  /issued-credential/{id}} : get an issued credential with specific id.
     *
     * @param id the id of the issued credential to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the IssuedCredential, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/issued-credential/{id}")
    @Operation
    @SecurityRequirement(name="X-API-Key")
    @SecurityRequirement(name="bearerAuth")
    public ResponseEntity<IssuedCredentialDTO> getIssuedCredentialById(@PathVariable String id) {

        log.debug("REST request to get issued credential by ID: {}", id);

        Optional<IssuedCredentialDTO> issuedCredential = this.issuedCredentialService.getIssuedCredentialById(id);

        return ResponseUtil.wrapOrNotFound(issuedCredential);
    }

    @PostMapping("/issued-credential/re-issue-credential")
    @Operation
    @SecurityRequirement(name = "X-API-Key")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> reIssueCredential(@RequestBody @Valid EmployeeDTO employeeDTO) throws URISyntaxException {

        log.debug("REST request to resend issued credential for employee id: {}", employeeDTO.getEmployeeId());

        try {
            this.issuedCredentialService.reIssueCredential(employeeDTO);
        } catch (EmployeeAlreadyExistsException | CredentialForEmployeeAlreadyExistsException | EmployeeNotFoundException |
            ConnectionNotFoundException | IssuedCredentialNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return ResponseEntity.noContent().build();
    }

    /**
     * {@code DELETE  /issued-credential/{id}} : delete the issued credential with "id" .
     *
     * @param id the id of the Issued Credential to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/issued-credential/{id}")
    @Operation
    @SecurityRequirement(name="X-API-Key")
    @SecurityRequirement(name="bearerAuth")
    public ResponseEntity<Void> deleteIssuedCredentialById(@PathVariable String id, @RequestParam(name = "Revoke credential", required = false, defaultValue = "true") Boolean revoke) {

        log.debug("REST request to delete issued credential by ID: {}", id);

        this.issuedCredentialService.deleteIssuedCredential(id);

        return ResponseEntity.noContent().build();
    }
}
