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

package com.ibm.ssi.controller.company.service.mapper;

import com.ibm.ssi.controller.company.domain.IssuedCredential;
import com.ibm.ssi.controller.company.service.dto.IssuedCredentialDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class IssuedCredentialMapper {

    public List<IssuedCredentialDTO> issuedCredentialsToIssuedCredentialDTOs(List<IssuedCredential> issuedCredentials) {
        return issuedCredentials.stream()
            .filter(Objects::nonNull)
            .map(this::issuedCredentialToIssuedCredentialDTO)
            .collect(Collectors.toList());
    }

    public IssuedCredentialDTO issuedCredentialToIssuedCredentialDTO(IssuedCredential issuedCredential) {
        return new IssuedCredentialDTO(issuedCredential);
    }

    public List<IssuedCredential> issuedCredentialDTOsToIssuedCredential(List<IssuedCredentialDTO> issuedCredentialDTOs) {
        return issuedCredentialDTOs.stream()
            .filter(Objects::nonNull)
            .map(this::issuedCredentialDTOToIssuedCredential)
            .collect(Collectors.toList());
    }

    public IssuedCredential issuedCredentialDTOToIssuedCredential(IssuedCredentialDTO issuedCredentialDTO) {
        if (issuedCredentialDTO == null) {
            return null;
        } else {

            IssuedCredential issuedCredential = new IssuedCredential();
            issuedCredential.setId(issuedCredentialDTO.getId());
            issuedCredential.setIssuanceDate(issuedCredentialDTO.getIssuanceDate());

            return issuedCredential;
        }
    }

    public IssuedCredential issuedCredentialFrmId(String id) {
        if (id == null) {
            return null;
        }
        IssuedCredential issuedCredential = new IssuedCredential();
        issuedCredential.setId(id);
        return issuedCredential;
    }

}
