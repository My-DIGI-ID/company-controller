
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

package com.ibm.ssi.controller.company.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class WebhookIssueCredentialDTO {

    @JsonProperty("credential_exchange_id")
    private String credentialExchangeId;

    @JsonProperty("connection_id")
    private String connectionId;

    private String state;

    @JsonProperty("credential_definition_id")
    private String credentialDefinitionId;

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCredentialExchangeId() {
        return credentialExchangeId;
    }

    public void setCredentialExchangeId(String credentialExchangeId) {
        this.credentialExchangeId = credentialExchangeId;
    }

    public String getCredentialDefinitionId() {
        return credentialDefinitionId;
    }

    public void setCredentialDefinitionId(String credentialDefinitionId) {
        this.credentialDefinitionId = credentialDefinitionId;
    }

    @Override
    public String toString() {
        return "WebhookIssueCredentialDTO{" +
            "credentialExchangeId='" + credentialExchangeId + '\'' +
            ", connectionId='" + connectionId + '\'' +
            ", state='" + state + '\'' +
            ", credentialDefinitionId='" + credentialDefinitionId + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebhookIssueCredentialDTO that = (WebhookIssueCredentialDTO) o;
        return Objects.equals(credentialExchangeId, that.credentialExchangeId) && Objects.equals(connectionId, that.connectionId) && Objects.equals(state, that.state) && Objects.equals(credentialDefinitionId, that.credentialDefinitionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(credentialExchangeId, connectionId, state, credentialDefinitionId);
    }
}
