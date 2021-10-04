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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ibm.ssi.controller.company.util.validation.CustomPattern;

import org.springframework.data.mongodb.core.mapping.Field;

public class AddressDTO {

    @Size(max = 50)
    @Field("company_unit")
    @CustomPattern
    private String companyUnit;

    @Size(max = 50)
    @NotNull
    @CustomPattern
    private String street;

    @Size(max = 50)
    @NotNull
    @CustomPattern
    private String plz;

    @Size(max = 50)
    @NotNull
    @CustomPattern
    private String city;

    public AddressDTO() {
        // Empty needed by Jackson
    }

    public AddressDTO(@Size(max = 50) String companyUnit, @Size(max = 50) String street, @Size(max = 50) String plz,
            @Size(max = 50) String city) {
        this.companyUnit = companyUnit;
        this.street = street;
        this.plz = plz;
        this.city = city;
    }

    public String getCompanyUnit() {
        return companyUnit;
    }

    public void setCompanyUnit(String companyUnit) {
        this.companyUnit = companyUnit;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "AddressDTO [city=" + city + ", companyUnit=" + companyUnit + ", plz=" + plz + ", street=" + street
                + "]";
    }
}
