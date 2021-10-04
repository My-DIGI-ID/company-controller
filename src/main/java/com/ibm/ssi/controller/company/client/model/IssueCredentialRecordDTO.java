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

package com.ibm.ssi.controller.company.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IssueCredentialRecordDTO {

    @JsonProperty("revocation_id")
    private String rev_id;

    @JsonProperty("credential_exchange_id")
    private String cred_ex_id;

    @JsonProperty("revoc_reg_id")
    private String rev_reg_id;

    @JsonProperty("created_at")
    private String createdAt;

    public IssueCredentialRecordDTO() {};

    public String getRev_id() {
        return rev_id;
    }

    public void setRev_id(String rev_id) {
        this.rev_id = rev_id;
    }

    public String getCred_ex_id() {
        return cred_ex_id;
    }

    public void setCred_ex_id(String cred_ex_id) {
        this.cred_ex_id = cred_ex_id;
    }

    public String getRev_reg_id() {
        return rev_reg_id;
    }

    public void setRev_reg_id(String rev_reg_id) {
        this.rev_reg_id = rev_reg_id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "IssueCredentialRecordDTO{" +
            "rev_id='" + rev_id + '\'' +
            ", cred_ex_id='" + cred_ex_id + '\'' +
            ", rev_reg_id='" + rev_reg_id + '\'' +
            ", createdAt='" + createdAt + '\'' +
            '}';
    }
}
