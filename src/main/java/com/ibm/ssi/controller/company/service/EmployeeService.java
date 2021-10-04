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

package com.ibm.ssi.controller.company.service;

import java.util.List;
import java.util.Optional;

import com.ibm.ssi.controller.company.service.dto.EmployeeDTO;
import com.ibm.ssi.controller.company.service.exceptions.ConnectionNotFoundException;
import com.ibm.ssi.controller.company.service.exceptions.CredentialForEmployeeAlreadyExistsException;
import com.ibm.ssi.controller.company.service.exceptions.EMailGenerationException;
import com.ibm.ssi.controller.company.service.exceptions.EmployeeAlreadyExistsException;
import com.ibm.ssi.controller.company.service.exceptions.EmployeeNotFoundException;
import com.ibm.ssi.controller.company.service.exceptions.IssuedCredentialNotFoundException;
import com.ibm.ssi.controller.company.service.exceptions.QRCodeGenerationException;

public interface EmployeeService {

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO) throws EmployeeAlreadyExistsException, CredentialForEmployeeAlreadyExistsException;

    EmployeeDTO updateEmployee(EmployeeDTO employeeDTO) throws EmployeeNotFoundException;

    String createEmployeeInvitation(String id) throws EmployeeNotFoundException;

    byte[] createEmployeeInvitationAsEmail(String id) throws EmployeeNotFoundException, EMailGenerationException;

    byte[] createEmployeeInvitationAsQRCode(String id) throws EmployeeNotFoundException, QRCodeGenerationException;

    List<EmployeeDTO> getAllEmployees();

    Optional<EmployeeDTO> getEmployeeById(String id);

    void deleteEmployeeById(String id);

    void sendCredentialOffer(String id, String connectionId) throws EmployeeNotFoundException;

    void resendCredentialOffer(EmployeeDTO employee) throws EmployeeNotFoundException, ConnectionNotFoundException, IssuedCredentialNotFoundException, EmployeeAlreadyExistsException, CredentialForEmployeeAlreadyExistsException;

}
