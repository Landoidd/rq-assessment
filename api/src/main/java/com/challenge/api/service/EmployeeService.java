package com.challenge.api.service;

import com.challenge.api.model.Employee;
import com.challenge.api.model.GetAllEmployeesResponse;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private static final int PAGE_SIZE = 30;
    private final Map<UUID, Employee> employees = new HashMap<>();

    public EmployeeService() {
    }

    public GetAllEmployeesResponse getAllEmployees(String cursor) {
        try {
            List<Employee> sortedEmployees = employees.values().stream()
                    .sorted(Comparator.comparing(
                            Employee::getContractHireDate, Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing(Employee::getUuid))
                    .collect(Collectors.toList());

            List<Employee> result;
            String nextCursor = null;
            String previousCursor = null;

            if (cursor == null || cursor.isEmpty()) {
                result = sortedEmployees.stream()
                        .limit(PAGE_SIZE + 1)
                        .collect(Collectors.toList());

                boolean hasMore = result.size() > PAGE_SIZE;
                if (hasMore) {
                    result = result.subList(0, PAGE_SIZE);
                }

                if (hasMore && !result.isEmpty()) {
                    UUID lastUuid = result.get(result.size() - 1).getUuid();
                    nextCursor = "next:" + lastUuid.toString();
                }

                return new GetAllEmployeesResponse(result, nextCursor, previousCursor);
            }

            String[] cursorParts = cursor.split(":");
            if (cursorParts.length != 2) {
                throw new RuntimeException("Invalid cursor: " + cursor);
            }

            String direction = cursorParts[0];
            UUID cursorUuid;
            try {
                cursorUuid = UUID.fromString(cursorParts[1]);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid cursor UUID: " + cursorParts[1]);
            }

            Employee cursorEmployee = employees.get(cursorUuid);
            int cursorIndex = cursorEmployee != null ? sortedEmployees.indexOf(cursorEmployee) : -1;

            if (cursorIndex == -1) {
                result = sortedEmployees.stream()
                        .limit(PAGE_SIZE + 1)
                        .collect(Collectors.toList());

                boolean hasMore = result.size() > PAGE_SIZE;
                if (hasMore) {
                    result = result.subList(0, PAGE_SIZE);
                }

                if (hasMore && !result.isEmpty()) {
                    UUID lastUuid = result.get(result.size() - 1).getUuid();
                    nextCursor = "next:" + lastUuid.toString();
                }

                return new GetAllEmployeesResponse(result, nextCursor, null);
            }

            if ("next".equals(direction)) {
                int startIndex = cursorIndex + 1;
                if (startIndex >= sortedEmployees.size()) {
                    return new GetAllEmployeesResponse(new ArrayList<>(), null, null);
                }

                int endIndex = Math.min(startIndex + PAGE_SIZE + 1, sortedEmployees.size());
                result = sortedEmployees.subList(startIndex, endIndex);

                boolean hasMore = result.size() > PAGE_SIZE;
                if (hasMore) {
                    result = result.subList(0, PAGE_SIZE);
                }

                if (hasMore && !result.isEmpty()) {
                    UUID lastUuid = result.get(result.size() - 1).getUuid();
                    nextCursor = "next:" + lastUuid.toString();
                }

                if (startIndex > 0 && !result.isEmpty()) {
                    UUID firstUuid = result.get(0).getUuid();
                    previousCursor = "previous:" + firstUuid.toString();
                }

            } else if ("previous".equals(direction)) {
                int endIndex = cursorIndex;
                if (endIndex <= 0) {
                    return new GetAllEmployeesResponse(new ArrayList<>(), null, null);
                }

                int startIndex = Math.max(0, endIndex - PAGE_SIZE);
                result = sortedEmployees.subList(startIndex, endIndex);

                if (!result.isEmpty()) {
                    UUID lastUuid = result.get(result.size() - 1).getUuid();
                    nextCursor = "next:" + lastUuid.toString();
                }

                if (startIndex > 0 && !result.isEmpty()) {
                    UUID firstUuid = result.get(0).getUuid();
                    previousCursor = "previous:" + firstUuid.toString();
                }

            } else {
                throw new RuntimeException("Invalid cursor direction: " + direction);
            }

            return new GetAllEmployeesResponse(result, nextCursor, previousCursor);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all employees: " + e.getMessage());
        }
    }

    public Employee getEmployeeByUuid(UUID uuid) {
        try {
            Employee employee = employees.get(uuid);
            if (employee == null) {
                throw new RuntimeException("Employee not found with UUID: " + uuid);
            }
            return employee;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get employee: " + e.getMessage());
        }
    }

    public Employee createEmployee(Employee employee) {
        try {
            employees.put(UUID.randomUUID(), employee);
            return employee;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create employee: " + e.getMessage());
        }
    }
}
