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

package com.ibm.ssi.controller.company.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import com.ibm.ssi.controller.company.domain.Employee;
import com.ibm.ssi.controller.company.repository.EmployeeRepository;
import com.ibm.ssi.controller.company.repository.IssuedCredentialRepository;
import com.ibm.ssi.controller.company.service.dto.EmployeeDTO;
import com.ibm.ssi.controller.company.service.exceptions.CredentialForEmployeeAlreadyExistsException;
import com.ibm.ssi.controller.company.service.exceptions.EmployeeAlreadyExistsException;
import com.ibm.ssi.controller.company.service.mapper.EmployeeMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest()
public class EmployeeServiceImplT {

  private static final String DEFAULT_ID = "ID_1";
  private static final String DEFAULT_FIRSTNAME = "John";
  private static final String DEFAULT_LASTNAME = "Doe";
  private static final String DEFAULT_EMAIL = "John@Doe";
  private static final String DEFAULT_FIRMNAME = "FIRM";
  private static final String DEFAULT_FIRMSUBJECT = "companyUnit";
  private static final String DEFAULT_FIRMSTREET = "street";
  private static final String DEFAULT_FIRMPOSTALCODE = "plz";
  private static final String DEFAULT_FIRMCITY = "city";

  @Autowired
  private EmployeeServiceImpl employeeServiceImpl;

  @Autowired
  EmployeeMapper employeeMapper;

  @MockBean
  private EmployeeRepository employeeRepository;

  @MockBean
  private IssuedCredentialRepository issuedCredentialRepository;

  @Test()
  void createEmployeeForExistingEmployee() {
    EmployeeDTO employeeDTO = this.createEmployeeDTO();

    Mockito.when(employeeRepository.existsById(employeeDTO.getEmployeeId()))
      .thenReturn(true);

      assertThrows(EmployeeAlreadyExistsException.class, new Executable() {
        @Override
        public void execute() throws Throwable {
          employeeServiceImpl.createEmployee(employeeDTO);
        }
      });
  }

  @Test()
  void createEmployeeForExistingIssuedCredential() {
    EmployeeDTO employeeDTO = this.createEmployeeDTO();

    Mockito.when(employeeRepository.existsById(employeeDTO.getEmployeeId()))
      .thenReturn(false);
    Mockito.when(issuedCredentialRepository.existsById(employeeDTO.getEmployeeId()))
      .thenReturn(true);

    assertThrows(CredentialForEmployeeAlreadyExistsException.class, new Executable() {
      @Override
      public void execute() throws Throwable {
        employeeServiceImpl.createEmployee(employeeDTO);
      }
    });
  }

  @Test()
  void createEmployee() {
    EmployeeDTO employeeDTO = this.createEmployeeDTO();
    Employee employee = employeeMapper.employeeDTOToEmployee(employeeDTO);

    Mockito.when(employeeRepository.existsById(employeeDTO.getEmployeeId()))
      .thenReturn(false);
    Mockito.when(issuedCredentialRepository.existsById(employeeDTO.getEmployeeId()))
      .thenReturn(false);
    Mockito.when(employeeRepository.insert(employee))
      .thenReturn(employee);

    EmployeeDTO result = new EmployeeDTO();
    try {
      result = employeeServiceImpl.createEmployee(employeeDTO);
    } catch(Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }

    assertEquals(employeeDTO, result);
  }

  private EmployeeDTO createEmployeeDTO() {
    // Create the employee
    EmployeeDTO employeeDTO = new EmployeeDTO();
    employeeDTO.setEmployeeId(DEFAULT_ID);
    employeeDTO.setFirstName(DEFAULT_FIRSTNAME);
    employeeDTO.setLastName(DEFAULT_LASTNAME);
    employeeDTO.setEmail(DEFAULT_EMAIL);
    employeeDTO.setFirmName(DEFAULT_FIRMNAME);
    employeeDTO.setFirmSubject(DEFAULT_FIRMSUBJECT);
    employeeDTO.setFirmStreet(DEFAULT_FIRMSTREET);
    employeeDTO.setFirmPostalCode(DEFAULT_FIRMPOSTALCODE);
    employeeDTO.setFirmCity(DEFAULT_FIRMCITY);

    return employeeDTO;
  }
}
