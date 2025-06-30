package org.example.serverdemo;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDatabase {
    private List<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    public void clear() {
        employees.clear();
    }
}
