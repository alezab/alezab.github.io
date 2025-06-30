package org.example.serverdemo;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeDatabaseTest {
    private EmployeeDatabase db;

    @BeforeAll
    void setupAll() {
        db = new EmployeeDatabase();
        db.addEmployee(new Employee(1, "Jan", 3000));
        db.addEmployee(new Employee(2, "Anna", 4000));
    }

    @AfterAll
    void tearDownAll() {
        db.clear();
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = db.getAllEmployees();
        assertEquals(2, employees.size());
    }

    @ParameterizedTest
    @CsvSource({
        "1,Jan,3000",
        "2,Anna,4000"
    })
    void testEmployeeData(int id, String name, double salary) {
        Employee emp = db.getAllEmployees().stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
        assertNotNull(emp);
        assertEquals(name, emp.getName());
        assertEquals(salary, emp.getSalary());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/org/example/serverdemo/employees.csv", numLinesToSkip = 0)
    void testEmployeeFromCsv(int id, String name, double salary) {
        assertTrue(id > 0);
        assertNotNull(name);
        assertTrue(salary > 0);
    }

    static Stream<Arguments> employeeFromSqlite() {
        EmployeeSqliteDatabase db = new EmployeeSqliteDatabase("jdbc:sqlite:employees.db");
        return db.getAllEmployees().stream()
                .map(e -> Arguments.of(e.getId(), e.getName(), e.getSalary()));
    }

    @ParameterizedTest
    @MethodSource("employeeFromSqlite")
    void testEmployeeFromSqlite(int id, String name, double salary) {
        assertTrue(id > 0);
        assertNotNull(name);
        assertTrue(salary > 0);
    }
}
