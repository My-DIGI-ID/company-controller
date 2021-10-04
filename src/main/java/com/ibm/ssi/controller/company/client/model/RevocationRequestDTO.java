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

public class RevocationRequestDTO {

    @JsonProperty("cred_rev_id")
    private String credRevId;

    @JsonProperty("publish")
    private Boolean publish;

    @JsonProperty("rev_reg_id")
    private String revRegId;

    public RevocationRequestDTO() {};

    public void setCredRevId(String credRevId) {
        this.credRevId = credRevId;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }

    public void setRevRegId(String revRegId) {
        this.revRegId = revRegId;
    }

}
