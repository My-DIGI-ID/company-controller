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

package com.ibm.ssi.controller.company.client;

import com.ibm.ssi.controller.company.client.model.*;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@FeignClient(name = "ACAPYClient", url = "${ssibk.company.controller.agent.apiurl}")
public interface ACAPYClient {

    @RequestMapping(method = RequestMethod.POST, value = "/connections/create-invitation")
    InvitationResultDTO createInvitation(@RequestHeader("X-API-KEY") String apiKey, @RequestParam(value="alias") String alias, @RequestBody InvitationRequestDTO invitationRequestDTO);

    @RequestMapping(method = RequestMethod.POST, value = "/issue-credential/send-offer")
    void sendCredentialOffer(@RequestHeader("X-API-KEY") String apiKey, @RequestBody CredentialOfferRequestDTO credentialOfferRequestDTO);

    @RequestMapping(method = RequestMethod.GET, value = "/connections/{connection_id}")
    ConnectionRecordDTO getConnectionRecordById(@RequestHeader("X-API-KEY") String apiKey, @PathVariable("connection_id") String connectionId);

    @RequestMapping(method = RequestMethod.GET, value = "/connections")
    ConnectionsRecordDTO getConnectionsRecordByAlias(@RequestHeader("X-API-KEY") String apiKey, @RequestParam("alias") String alias);

    @RequestMapping(method = RequestMethod.GET, value="/issue-credential/records/{cred_ex_id}")
    IssueCredentialRecordDTO getIssueCredentialRecord(@RequestHeader("X-API-KEY") String apiKey, @PathVariable("cred_ex_id") String cred_ex_id);

    @RequestMapping(method = RequestMethod.DELETE, value="/issue-credential/records/{cred_ex_id}")
    IssueCredentialRecordDTO deleteIssueCredentialRecord(@RequestHeader("X-API-KEY") String apiKey, @PathVariable("cred_ex_id") String cred_ex_id);

    @RequestMapping(method = RequestMethod.POST, value="/revocation/revoke")
    void revokeCredential(@RequestHeader("X-API-KEY") String apiKey, @RequestBody RevocationRequestDTO revocationRequestDTO);

}
