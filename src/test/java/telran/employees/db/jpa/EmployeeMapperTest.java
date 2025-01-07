package telran.employees.db.jpa;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import telran.employees.*;

public class EmployeeMapperTest {
    Employee employee = new Employee(1000, 10000, "QA");
    Employee manager = new Manager(1001, 12000, "QA", 1.6f);
    Employee wageEmployee = new WageEmployee(1002, 13000, "QA", 100, 50);
    Employee salesPerson = new SalesPerson(1004, 14000, "QA", 150, 60, 0.1f, 40);
    
    @Test
    void EmployeesMapperTest() {
        assertEmployees(employee);
        assertEmployees(manager);
        assertEmployees(wageEmployee);
        assertEmployees(salesPerson);
    }
    
    void assertEmployees(Employee empl) {
        EmployeeEntity employeeEntity = EmployeesMapper.toEmployeeEntityFromDto(empl); 
        Employee employeeRes = EmployeesMapper.toEmployeeDtoFromEntity(employeeEntity);
        assertEquals(employeeRes, empl);
    }
}
