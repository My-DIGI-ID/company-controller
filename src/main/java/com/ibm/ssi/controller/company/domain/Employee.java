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

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@org.springframework.data.mongodb.core.mapping.Document(collection = "employees")
public class Employee extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    private String employeeId;

    @Size(max = 50)
    @Field("firstName")
    private String firstName;

    @Size(max = 50)
    @Field("lastName")
    private String lastName;

    @Field("email")
    @Email
    private String email;

    @Size(max = 100)
    @Field("firmName")
    private String firmName;

    @Size(max = 50)
    @Field("firmSubject")
    private String firmSubject;

    @Size(max = 50)
    @Field("firmStreet")
    private String firmStreet;

    @Size(max = 50)
    @Field("firmPostalCode")
    private String firmPostalCode;

    @Size(max = 50)
    @Field("firmCity")
    private String firmCity;

    @Field("isEmailDownloaded")
    private Boolean isEmailDownloaded = false;

    @Field("isEmailInvitationSent")
    private Boolean isEmailInvitationSent = false;

    public Employee() {
    }

    public Employee(String id, 
            @Size(max = 50) String firstName, 
            @Size(max = 50) String lastName,
            @Email String email,
            @Size(max = 100) String firmName,
            @Size(max = 50) String firmSubject,
            @Size(max = 50) String firmStreet,
            @Size(max = 50) String firmPostalCode,
            @Size(max = 50) String firmCity, 
            Boolean isEmailDownloaded,
            Boolean isEmailInvitationSent) {
        this.employeeId = id;
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

    public static long getSerialversionuid() {
        return serialVersionUID;
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
        return "Employee [firmName=" + firmName +
            ", firmSubject=" + firmSubject +
            ", firmPostalCode=" + firmPostalCode +
            ", firmStreet=" + firmStreet +
            ", firmCity=" + firmCity +
            ", email=" + email +
            ", firstName=" + firstName +
            ", id=" + employeeId +
            ", lastName=" + lastName +
            ", isEmailDownloaded="+ isEmailDownloaded +
            ", isEmailInvitationSent=" + isEmailInvitationSent +
            "]";
    }
}
