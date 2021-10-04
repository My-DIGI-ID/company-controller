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

package com.ibm.ssi.controller.company.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

public class IssuedCredential extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("cred_rev_id")
    private String credentialRevocationId;

    @Field("rev_reg_id")
    private String revocationRegistryId;

    @Field("issuance_date")
    private Date issuanceDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCredentialRevocationId() {
        return credentialRevocationId;
    }

    public void setCredentialRevocationId(String credentialRevocationId) {
        this.credentialRevocationId = credentialRevocationId;
    }

    public String getRevocationRegistryId() {
        return revocationRegistryId;
    }

    public void setRevocationRegistryId(String revocationRegistryId) {
        this.revocationRegistryId = revocationRegistryId;
    }

    public Date getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(Date issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    @Override
    public String toString() {
        return "IssuedCredential [credentialRevocationId=" + credentialRevocationId + ", id=" + id + ", issuanceDate="
                + issuanceDate + ", revocationRegistryId=" + revocationRegistryId + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((credentialRevocationId == null) ? 0 : credentialRevocationId.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((issuanceDate == null) ? 0 : issuanceDate.hashCode());
        result = prime * result + ((revocationRegistryId == null) ? 0 : revocationRegistryId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IssuedCredential other = (IssuedCredential) obj;
        if (credentialRevocationId == null) {
            if (other.credentialRevocationId != null)
                return false;
        } else if (!credentialRevocationId.equals(other.credentialRevocationId))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (issuanceDate == null) {
            if (other.issuanceDate != null)
                return false;
        } else if (!issuanceDate.equals(other.issuanceDate))
            return false;
        if (revocationRegistryId == null) {
            if (other.revocationRegistryId != null)
                return false;
        } else if (!revocationRegistryId.equals(other.revocationRegistryId))
            return false;
        return true;
    }
}
