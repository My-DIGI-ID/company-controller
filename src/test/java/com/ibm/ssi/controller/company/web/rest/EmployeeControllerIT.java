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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.List;
import java.util.function.Consumer;

import com.ibm.ssi.controller.company.CompanyControllerApp;
import com.ibm.ssi.controller.company.domain.Employee;
import com.ibm.ssi.controller.company.repository.EmployeeRepository;
import com.ibm.ssi.controller.company.security.AuthoritiesConstants;
import org.springframework.boot.test.context.SpringBootTest;
import com.ibm.ssi.controller.company.service.dto.EmployeeDTO;

import static org.hamcrest.Matchers.hasItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link EmployeeController} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@SpringBootTest(classes = CompanyControllerApp.class)

public class EmployeeControllerIT {

    private static final String DEFAULT_ID = "ID_1";
    private static final String ANOTHER_ID = "ID_2";
    private static final String DEFAULT_FIRSTNAME = "John";
    private static final String DEFAULT_LASTNAME = "Doe";
    private static final String DEFAULT_EMAIL = "John@Doe";
    private static final String UPDATED_EMAIL = "Jane@Doe";
    private static final String DEFAULT_FIRMNAME = "FIRM";
    private static final String DEFAULT_FIRMSUBJECT = "companyUnit";
    private static final String DEFAULT_FIRMSTREET = "street";
    private static final String DEFAULT_FIRMPOSTALCODE = "plz";
    private static final String DEFAULT_FIRMCITY = "city";

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    public Employee createEntity() {
        Employee employee = new Employee();
        employee.setEmployeeId(DEFAULT_ID);
        employee.setFirstName(DEFAULT_FIRSTNAME);
        employee.setLastName(DEFAULT_LASTNAME);
        employee.setEmail(DEFAULT_EMAIL);
        employee.setFirmName(DEFAULT_FIRMNAME);
        employee.setFirmSubject(DEFAULT_FIRMSUBJECT);
        employee.setFirmStreet(DEFAULT_FIRMSTREET);
        employee.setFirmPostalCode(DEFAULT_FIRMPOSTALCODE);
        employee.setFirmCity(DEFAULT_FIRMCITY);
        return employee;
    }

    @BeforeEach
    public void initTest() {
        employeeRepository.deleteAll();
        employee = this.createEntity();
    }

    @Test
    public void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // Create the employee
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(ANOTHER_ID);
        employeeDTO.setFirstName(DEFAULT_FIRSTNAME);
        employeeDTO.setLastName(DEFAULT_LASTNAME);
        employeeDTO.setEmail(DEFAULT_EMAIL);
        employeeDTO.setFirmName(DEFAULT_FIRMNAME);
        employeeDTO.setFirmSubject(DEFAULT_FIRMSUBJECT);
        employeeDTO.setFirmStreet(DEFAULT_FIRMSTREET);
        employeeDTO.setFirmPostalCode(DEFAULT_FIRMPOSTALCODE);
        employeeDTO.setFirmCity(DEFAULT_FIRMCITY);

        restEmployeeMockMvc.perform(post("/api/employee").contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(employeeDTO))).andExpect(status().isOk());

           // Validate the employee in the database
           assertPersistedEmployees(employees -> {
            assertThat(employees).hasSize(databaseSizeBeforeCreate + 1);
            Employee testemployee = employees.get(employees.size() - 1);
            assertThat(testemployee.getEmployeeId()).isEqualTo(ANOTHER_ID);
            assertThat(testemployee.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
            assertThat(testemployee.getLastName()).isEqualTo(DEFAULT_LASTNAME);
            assertThat(testemployee.getEmail()).isEqualTo(DEFAULT_EMAIL);
            assertThat(testemployee.getFirmName()).isEqualTo(DEFAULT_FIRMNAME);
            assertThat(testemployee.getFirmSubject()).isEqualTo(DEFAULT_FIRMSUBJECT);
            assertThat(testemployee.getFirmStreet()).isEqualTo(DEFAULT_FIRMSTREET);
            assertThat(testemployee.getFirmPostalCode()).isEqualTo(DEFAULT_FIRMPOSTALCODE);
            assertThat(testemployee.getFirmCity()).isEqualTo(DEFAULT_FIRMCITY);
        });
    }

    @Test
    public void createEmployeeWithInvalidEmail() throws Exception {

        // Create the employee
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(ANOTHER_ID);
        employeeDTO.setFirstName(DEFAULT_FIRSTNAME);
        employeeDTO.setLastName(DEFAULT_LASTNAME);
        employeeDTO.setEmail("");
        employeeDTO.setFirmName(DEFAULT_FIRMNAME);
        employeeDTO.setFirmSubject(DEFAULT_FIRMSUBJECT);
        employeeDTO.setFirmStreet(DEFAULT_FIRMSTREET);
        employeeDTO.setFirmPostalCode(DEFAULT_FIRMPOSTALCODE);
        employeeDTO.setFirmCity(DEFAULT_FIRMCITY);

        restEmployeeMockMvc.perform(post("/api/employee").contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(employeeDTO))).andExpect(status().isBadRequest());
    }

    @Test
    public void createEmployeeWithAnotherInvalidEmail() throws Exception {

        // Create the employee
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(ANOTHER_ID);
        employeeDTO.setFirstName(DEFAULT_FIRSTNAME);
        employeeDTO.setLastName(DEFAULT_LASTNAME);
        employeeDTO.setEmail("email@");
        employeeDTO.setFirmName(DEFAULT_FIRMNAME);
        employeeDTO.setFirmSubject(DEFAULT_FIRMSUBJECT);
        employeeDTO.setFirmStreet(DEFAULT_FIRMSTREET);
        employeeDTO.setFirmPostalCode(DEFAULT_FIRMPOSTALCODE);
        employeeDTO.setFirmCity(DEFAULT_FIRMCITY);

        restEmployeeMockMvc.perform(post("/api/employee").contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(employeeDTO))).andExpect(status().isBadRequest());
    }

    @Test
    public void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.save(employee);

        // Get all the employees
        restEmployeeMockMvc.perform(get("/api/employee").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_ID)))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRSTNAME)))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LASTNAME)))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
                .andExpect(jsonPath("$.[*].firmName").value(hasItem(DEFAULT_FIRMNAME)));
    }

    @Test
    public void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.save(employee);

        // Get the employee
        restEmployeeMockMvc.perform(get("/api/employee/{employeeId}", employee.getEmployeeId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.employeeId").value(DEFAULT_ID))
                .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRSTNAME))
                .andExpect(jsonPath("$.lastName").value(DEFAULT_LASTNAME))
                .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
                .andExpect(jsonPath("$.firmName").value(DEFAULT_FIRMNAME))
                .andExpect(jsonPath("$.firmSubject").value(DEFAULT_FIRMSUBJECT))
                .andExpect(jsonPath("$.firmStreet").value(DEFAULT_FIRMSTREET))
                .andExpect(jsonPath("$.firmPostalCode").value(DEFAULT_FIRMPOSTALCODE))
                .andExpect(jsonPath("$.firmCity").value(DEFAULT_FIRMCITY));
    }

    @Test
    public void updateEmployee() throws Exception {
        // Initialize the database
        employeeRepository.save(employee);
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getEmployeeId()).get();

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(updatedEmployee.getEmployeeId());
        employeeDTO.setFirstName(DEFAULT_FIRSTNAME);
        employeeDTO.setLastName(DEFAULT_LASTNAME);
        employeeDTO.setEmail(UPDATED_EMAIL);
        employeeDTO.setFirmName(DEFAULT_FIRMNAME);
        employeeDTO.setFirmSubject(DEFAULT_FIRMSUBJECT);
        employeeDTO.setFirmStreet(DEFAULT_FIRMSTREET);
        employeeDTO.setFirmPostalCode(DEFAULT_FIRMPOSTALCODE);
        employeeDTO.setFirmCity(DEFAULT_FIRMCITY);

        restEmployeeMockMvc.perform(put("/api/employee")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isOk());

        // Validate the employees in the database
        assertPersistedEmployees(employees -> {
            assertThat(employees).hasSize(databaseSizeBeforeUpdate);
            Employee testEmployee = employees.get(employees.size() - 1);
            assertThat(testEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
        });
    }

    @Test
    public void updateEmployeeWithInvalidEmail() throws Exception {
        // Initialize the database
        employeeRepository.save(employee);

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getEmployeeId()).get();

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(updatedEmployee.getEmployeeId());
        employeeDTO.setFirstName(DEFAULT_FIRSTNAME);
        employeeDTO.setLastName(DEFAULT_LASTNAME);
        employeeDTO.setEmail(null);
        employeeDTO.setFirmName(DEFAULT_FIRMNAME);
        employeeDTO.setFirmSubject(DEFAULT_FIRMSUBJECT);
        employeeDTO.setFirmStreet(DEFAULT_FIRMSTREET);
        employeeDTO.setFirmPostalCode(DEFAULT_FIRMPOSTALCODE);
        employeeDTO.setFirmCity(DEFAULT_FIRMCITY);

        restEmployeeMockMvc.perform(put("/api/employee")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteEmployee() throws Exception {
        // Initialize the database
        employeeRepository.save(employee);
        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Delete the employee
        restEmployeeMockMvc.perform(delete("/api/employee/{employeeId}", employee.getEmployeeId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        assertPersistedEmployees(employees -> assertThat(employees).hasSize(databaseSizeBeforeDelete - 1));
    }

    private void assertPersistedEmployees(Consumer<List<Employee>> employeeAssertion) {
        employeeAssertion.accept(employeeRepository.findAll());
    }
}
