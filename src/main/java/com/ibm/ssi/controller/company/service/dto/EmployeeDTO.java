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

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ibm.ssi.controller.company.domain.Employee;
import com.ibm.ssi.controller.company.util.validation.CustomPattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class EmployeeDTO {

    @Id
    @NotNull
    @Size(max = 50)
    @CustomPattern(type = "id")
    private String employeeId;

    @Size(max = 50)
    @Field("firstName")
    @NotEmpty
    @CustomPattern
    private String firstName;

    @Size(max = 50)
    @Field("lastName")
    @NotEmpty
    @CustomPattern
    private String lastName;

    @Field("email")
    @Email
    @NotEmpty
    private String email;

    @Size(max = 100)
    @Field("firmName")
    @NotEmpty
    @CustomPattern
    private String firmName;

    @Size(max = 50)
    @Field("firmSubject")
    @CustomPattern
    private String firmSubject;

    @Size(max = 50)
    @Field("firmStreet")
    @NotEmpty
    @CustomPattern
    private String firmStreet;

    @Size(max = 50)
    @Field("firmPostalCode")
    @NotEmpty
    @CustomPattern
    private String firmPostalCode;

    @Size(max = 50)
    @Field("firmCity")
    @NotEmpty
    @CustomPattern
    private String firmCity;

    @Field("isEmailDownloaded")
    private Boolean isEmailDownloaded = false;

    @Field("isEmailInvitationSent")
    private Boolean isEmailInvitationSent = false;

    public EmployeeDTO() {
        // Need by Jackson
    }

    public EmployeeDTO(Employee employee) {
        this.employeeId = employee.getEmployeeId();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.email = employee.getEmail();
        this.firmName = employee.getFirmName();
        this.firmSubject = employee.getFirmSubject();
        this.firmStreet = employee.getFirmStreet();
        this.firmPostalCode = employee.getFirmPostalCode();
        this.firmCity = employee.getFirmCity();
        this.isEmailDownloaded = employee.getIsEmailDownloaded();
        this.isEmailInvitationSent = employee.getIsEmailInvitationSent();
    }

    public EmployeeDTO(@NotEmpty @Size(max = 50) @CustomPattern(type = "id") String employeeId,
            @Size(max = 50) @NotEmpty @CustomPattern String firstName,
            @Size(max = 50) @NotEmpty @CustomPattern String lastName,
            @Email @NotEmpty String email,
            @Size(max = 100) @NotEmpty @CustomPattern String firmName,
            @Size(max = 50) @CustomPattern String firmSubject,
            @Size(max = 50) @NotEmpty @CustomPattern String firmStreet,
            @Size(max = 50) @NotEmpty @CustomPattern String firmPostalCode,
            @Size(max = 50) @NotEmpty @CustomPattern String firmCity,
            Boolean isEmailDownloaded,
            Boolean isEmailInvitationSent) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.firmName = firmName;
        this.firmSubject = firmSubject;
        this.firmStreet = firmStreet;
        this.firmPostalCode = firmPostalCode;
        this.firmCity = firmCity;
        this.isEmailDownloaded = isEmailDownloaded;
        this.isEmailInvitationSent = isEmailInvitationSent;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String id) {
        this.employeeId = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getFirmSubject() {
        return firmSubject;
    }

    public void setFirmSubject(String firmSubject) {
        this.firmSubject = firmSubject;
    }

    public String getFirmStreet() {
        return firmStreet;
    }

    public void setFirmStreet(String firmStreet) {
        this.firmStreet = firmStreet;
    }

    public String getFirmPostalCode() {
        return firmPostalCode;
    }

    public void setFirmPostalCode(String firmPostalCode) {
        this.firmPostalCode = firmPostalCode;
    }

    public String getFirmCity() {
        return firmCity;
    }

    public void setFirmCity(String firmCity) {
        this.firmCity = firmCity;
    }

    public Boolean getIsEmailDownloaded() {
        return isEmailDownloaded;
    }

    public void setIsEmailDownloaded(Boolean isEmailDownloaded) {
        this.isEmailDownloaded = isEmailDownloaded;
    }

    public Boolean getIsEmailInvitationSent() {
        return isEmailInvitationSent;
    }

    public void setIsEmailInvitationSent(Boolean isEmailInvitationSent) {
        this.isEmailInvitationSent = isEmailInvitationSent;
    }

    @Override
    public String toString() {
        return "EmployeeDTO [firmSubject=" + firmSubject + 
            ", firmStreet=" + firmStreet +
            ", firmPostalCode=" + firmPostalCode +
            ", firmCity="+ firmCity +
            ", firmName=" + firmName +
            ", email=" + email +
            ", firstName=" + firstName +
            ", employeeId=" + employeeId +
            ", lastName=" + lastName +
            ", isEmailDownloaded="+ isEmailDownloaded +
            ", isEmailInvitationSent=" + isEmailInvitationSent +
            "]";
    }
}
