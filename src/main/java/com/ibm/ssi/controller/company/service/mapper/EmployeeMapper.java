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

import com.ibm.ssi.controller.company.service.dto.EmployeeDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ibm.ssi.controller.company.domain.Employee;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link Employee} and its DTO called {@link EmployeeDTO}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */

@Service
public class EmployeeMapper {

    @Autowired
    AddressMapper addressMapper;

    public List<EmployeeDTO> employeesToEmployeeDTOs(List<Employee> employees) {
        return employees.stream()
            .filter(Objects::nonNull)
            .map(this::employeeToEmployeeDTO)
            .collect(Collectors.toList());
    }

    public EmployeeDTO employeeToEmployeeDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(employee.getEmployeeId());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setFirmName(employee.getFirmName());
        employeeDTO.setFirmSubject(employee.getFirmSubject());
        employeeDTO.setFirmStreet(employee.getFirmStreet());
        employeeDTO.setFirmPostalCode(employee.getFirmPostalCode());
        employeeDTO.setFirmCity(employee.getFirmCity());
        employeeDTO.setIsEmailDownloaded(employee.getIsEmailDownloaded());
        employeeDTO.setIsEmailInvitationSent(employee.getIsEmailInvitationSent());

        return employeeDTO;
    }

    public List<Employee> employeeDTOsToEmployees(List<EmployeeDTO> employeeDTOs) {
        return employeeDTOs.stream()
            .filter(Objects::nonNull)
            .map(this::employeeDTOToEmployee)
            .collect(Collectors.toList());
    }

    public Employee employeeDTOToEmployee(EmployeeDTO employeeDTO) {
        if (employeeDTO == null) {
            return null;
        }

        Employee employee = new Employee();
        employee.setEmployeeId(employeeDTO.getEmployeeId());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setFirmName(employeeDTO.getFirmName());
        employee.setFirmSubject(employeeDTO.getFirmSubject());
        employee.setFirmStreet(employeeDTO.getFirmStreet());
        employee.setFirmPostalCode(employeeDTO.getFirmPostalCode());
        employee.setFirmCity(employeeDTO.getFirmCity());
        employee.setIsEmailDownloaded(employeeDTO.getIsEmailDownloaded());
        employee.setIsEmailInvitationSent(employeeDTO.getIsEmailInvitationSent());

        return employee;
    }

    public Employee employeeFromId(String id) {
        if (id == null) {
            return null;
        }

        Employee employee = new Employee();
        employee.setEmployeeId(id);
        
        return employee;
    }
}
