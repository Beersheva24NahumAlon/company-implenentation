package telran.employees.db.jpa;

import java.util.HashMap;

import telran.employees.CompanyTest;
import telran.employees.db.CompanyDBImpl;
import telran.employees.db.CompanyRepository;

public class CompanyJpaTest extends CompanyTest {
    @Override
    protected void setCompany() {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "create");
        CompanyRepository repository = new CompanyRepositoryJpaImpl(new EmployeesTestPersistenceUnitInfo(), properties);
        company = new CompanyDBImpl(repository);
        super.setCompany();
    }   

}
