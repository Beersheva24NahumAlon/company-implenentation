package telran.employees.db;

import java.util.List;
import telran.employees.Employee;

public interface CompanyRepository {

    List<Employee> getAllEmployees();

    void insertEmployee(Employee empl);

    Employee findEmployee(long id);

    Employee removeEmployee(long id);

    List<Employee> getEmployeesByDepartment(String department);

    List<String> findDepartments();

}
