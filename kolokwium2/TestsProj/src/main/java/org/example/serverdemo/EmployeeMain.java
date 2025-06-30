package org.example.serverdemo;

public class EmployeeMain {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:employees.db";
        EmployeeSqliteDatabase db = new EmployeeSqliteDatabase(url);
        db.clear();
        db.addEmployee(new Employee(1, "Jan", 3000));
        db.addEmployee(new Employee(2, "Anna", 4000));
        db.addEmployee(new Employee(3, "Piotr", 3500));
        System.out.println("Pracownicy w bazie:");
        for (Employee e : db.getAllEmployees()) {
            System.out.println(e.getId() + ": " + e.getName() + ", " + e.getSalary());
        }
    }
}
