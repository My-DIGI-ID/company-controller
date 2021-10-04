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

import java.util.Date;

import com.ibm.ssi.controller.company.client.ACAPYClient;
import com.ibm.ssi.controller.company.client.model.ConnectionRecordDTO;
import com.ibm.ssi.controller.company.client.model.IssueCredentialRecordDTO;
import com.ibm.ssi.controller.company.domain.IssuedCredential;
import com.ibm.ssi.controller.company.service.EmployeeService;
import com.ibm.ssi.controller.company.service.IssuedCredentialService;
import com.ibm.ssi.controller.company.service.dto.WebhookConnectionDTO;
import com.ibm.ssi.controller.company.service.dto.WebhookIssueCredentialDTO;
import com.ibm.ssi.controller.company.service.exceptions.EmployeeNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Webhooks for ACAPY", description = "https://github.com/hyperledger/aries-cloudagent-python/blob/master/AdminAPI.md#administration-api-webhooks")
@RestController
@RequestMapping("/topic")
public class WebhookController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    ACAPYClient acapyClient;

    @Autowired
    IssuedCredentialService issuedCredentialService;

    @Value("${ssibk.company.controller.agent.apikey}")
    private String apiKey;

    private final Logger log = LoggerFactory.getLogger(WebhookController.class);

    @PostMapping("/connections")
    @Operation(security = @SecurityRequirement(name = "X-API-Key"))
    public ResponseEntity<Void> onConnectionsWebhook(@RequestBody WebhookConnectionDTO connectionDTO) {

        // connection is in state "response" as soon as a user has accepted it in his
        // wallet app
        if (connectionDTO.getState().equals("response")) {
            try {
                this.employeeService.sendCredentialOffer(connectionDTO.getAlias(), connectionDTO.getConnectionId());
            } catch (EmployeeNotFoundException e) {
                // The employee does not exists although we just received his connection response - this should never happen
                log.error("failed to send credential offer", e);
            }
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("issue_credential")
    @Operation(security = @SecurityRequirement(name = "X-API-Key"))
    @Transactional
    public ResponseEntity<Void> onIssueCredentialWebhook(@RequestBody WebhookIssueCredentialDTO webhookIssueCredentialDTO) {

        log.debug("Received Webhook Issue Credental with connection-id: {}", webhookIssueCredentialDTO.getConnectionId());

        if (webhookIssueCredentialDTO.getState().equals("credential_issued")){

            // Get Alias for Connection-ID:
            ConnectionRecordDTO connectionRecordDTO = this.acapyClient.getConnectionRecordById(apiKey, webhookIssueCredentialDTO.getConnectionId());

            IssueCredentialRecordDTO issueCredentialRecordDTO = this.acapyClient.getIssueCredentialRecord(apiKey, webhookIssueCredentialDTO.getCredentialExchangeId());

            IssuedCredential issuedCredential = new IssuedCredential();
            issuedCredential.setId(connectionRecordDTO.getAlias());
            issuedCredential.setRevocationRegistryId(issueCredentialRecordDTO.getRev_reg_id());
            issuedCredential.setCredentialRevocationId(issueCredentialRecordDTO.getRev_id());
            issuedCredential.setIssuanceDate(new Date());

            // create issued credential
            this.issuedCredentialService.createIssuedCredential(issuedCredential);

            // delete cred_ex_record on agent
            this.acapyClient.deleteIssueCredentialRecord(apiKey, webhookIssueCredentialDTO.getCredentialExchangeId());

            // Delete data set in employee database
            this.employeeService.deleteEmployeeById(connectionRecordDTO.getAlias());

        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/revocation_registry")
    @Operation(security = @SecurityRequirement(name = "X-API-Key"))
    public ResponseEntity<Void> onRevocationRegistryWebhook() {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/issuer_cred_rev")
    @Operation(security = @SecurityRequirement(name = "X-API-Key"))
    public ResponseEntity<Void> onIssuerCredentialRevocationWebhook() { return ResponseEntity.noContent().build(); }

}
