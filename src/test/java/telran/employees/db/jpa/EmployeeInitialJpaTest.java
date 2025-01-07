package telran.employees.db.jpa;

import java.util.HashMap;
import org.junit.jupiter.api.Test;

import telran.employees.Company;
import telran.employees.db.CompanyDBImpl;
import telran.employees.db.CompanyRepository;
import telran.employees.db.jpa.config.EmployeesPersistenceUnitInfo;

public class EmployeeInitialJpaTest {
    @Test
    void getAllEmployeesTest() {
        HashMap<String, Object> hibernateProperties = new HashMap<>();
        hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
        CompanyRepository repository = new CompanyRepositoryJpaImpl(new EmployeesPersistenceUnitInfo(), hibernateProperties);
        Company company = new CompanyDBImpl(repository);
        company.forEach(System.out::println);
    }
}
