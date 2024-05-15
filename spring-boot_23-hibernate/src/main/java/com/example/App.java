package com.example;

import com.example.model.Employee;
import com.example.utils.HibernateUtils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Iterator;
import java.util.List;

public class App {
    private static SessionFactory factory;

    public static void main(String[] args) {

        // get session factory
        factory = HibernateUtils.getSessionFactory();

        // create app object
        App app = new App();

        // Add few employee records in database
        Integer empID1 = app.addEmployee("David", "Bishop", 1000);
        Integer empID2 = app.addEmployee("Chris", "Ali", 5000);
        Integer empID3 = app.addEmployee("John", "Vector", 10000);

        // List down all the employees
        System.out.println("List down all the employees:");
        app.listEmployees();

        // Update employee's records
        app.updateEmployee(empID1, 5000);

        // Delete an employee from the database
        app.deleteEmployee(empID2);

        // List down new list of the employees
        app.listEmployees();
        System.out.println("List down new list of the employees:");
    }

    // Method to CREATE an employee in the database
    public Integer addEmployee(String firstName, String lastName, int salary) {
        Session session = factory.openSession();
        Transaction tx = null;
        Integer employeeID = null;
        try {
            tx = session.beginTransaction();
            Employee employee = new Employee(firstName, lastName, salary); // Corrected field names
            employeeID = (Integer) session.save(employee);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return employeeID;
    }

    // Method to READ all the employees

    public void listEmployees() {
        Session session = factory.openSession();
        try {

            List employees = session.createQuery("FROM Employee").list();
            for (Iterator iterator = employees.iterator(); iterator.hasNext(); ) {
                Employee employee = (Employee) iterator.next();
                System.out.print("First Name: " + employee.getFirstName());
                System.out.print("  Last Name: " + employee.getLastName());
                System.out.println("  Salary: " + employee.getSalary());
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // Method to UPDATE salary for an employee
    public void updateEmployee(Integer EmployeeID, int salary) {
        Session session = factory.openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Employee employee = (Employee) session.get(Employee.class, EmployeeID);
            employee.setSalary(salary);
            session.update(employee);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // Method to DELETE an employee from the records
    public void deleteEmployee(Integer EmployeeID) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Employee employee = (Employee) session.get(Employee.class, EmployeeID);
            session.delete(employee);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
