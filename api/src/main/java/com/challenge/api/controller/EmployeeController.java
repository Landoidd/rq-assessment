package com.challenge.api.controller;

import com.challenge.api.model.Employee;
import com.challenge.api.model.GetAllEmployeesResponse;
import com.challenge.api.security.JwtUtil;
import com.challenge.api.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Fill in the missing aspects of this Spring Web REST Controller. Don't forget
 * to add a Service layer.
 */
@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    /**
     * @implNote Need not be concerned with an actual persistence layer. Generate mock Employee models as necessary.
     * @return One or more Employees.
     */
    @GetMapping
    public GetAllEmployeesResponse getAllEmployees(String cursor,
            HttpServletRequest request) {
        try {
            JwtUtil.verifyTokenFromRequest(request);
            return employeeService.getAllEmployees(cursor);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
        }
    }

    /**
     * @implNote Need not be concerned with an actual persistence layer. Generate mock Employee model as necessary.
     * @param uuid Employee UUID
     * @return Requested Employee if exists
     */
    @GetMapping("/{uuid}")
    public Employee getEmployeeByUuid(UUID uuid, HttpServletRequest request) {
        try {
            JwtUtil.verifyTokenFromRequest(request);
            return employeeService.getEmployeeByUuid(uuid);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
        }
    }

    /**
     * @implNote Need not be concerned with an actual persistence layer.
     * @param requestBody hint!
     * @return Newly created Employee
     */
    @PostMapping
    public Employee createEmployee(Employee requestBody, HttpServletRequest request) {
        try {
            JwtUtil.verifyTokenFromRequest(request);
            return employeeService.createEmployee(requestBody);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
        }
    }
}
