package telran.employees.db.jpa;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import jakarta.persistence.*;
import jakarta.persistence.spi.*;
import telran.employees.*;
import telran.employees.db.CompanyRepository;

public class CompanyRepositoryJpaImpl implements CompanyRepository {
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public CompanyRepositoryJpaImpl(PersistenceUnitInfo persistenceUnitInfo,
            HashMap<String, Object> properties) {
        try {
            String provaderName = persistenceUnitInfo.getPersistenceProviderClassName();
            Class<PersistenceProvider> clazz = (Class<PersistenceProvider>) Class.forName(provaderName);
            Constructor<PersistenceProvider> constructor = clazz.getConstructor();
            PersistenceProvider provider = constructor.newInstance();
            EntityManagerFactory enf = provider.createContainerEntityManagerFactory(persistenceUnitInfo, properties);
            em = enf.createEntityManager();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        TypedQuery<EmployeeEntity> query = em.createQuery("select empl from EmployeeEntity empl",
                EmployeeEntity.class);
        return toEmployeeList(query.getResultList());
    }

    private List<Employee> toEmployeeList(List<EmployeeEntity> resultList) {
        return resultList.stream().map(EmployeesMapper::toEmployeeDtoFromEntity).toList();
    }

    @Override
    public void insertEmployee(Employee empl) {
        var transaction = em.getTransaction();
        try {
            transaction.begin();
            var employeeEntity = em.find(EmployeeEntity.class, empl.getId());
            if (employeeEntity != null) {
                throw new IllegalStateException();
            }
            EmployeeEntity entity = EmployeesMapper.toEmployeeEntityFromDto(empl);
            em.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

    }

    @Override
    public Employee findEmployee(long id) {
        EmployeeEntity entity = em.find(EmployeeEntity.class, id);
        return entity == null ? null : EmployeesMapper.toEmployeeDtoFromEntity(entity);
    }

    @Override
    public Employee removeEmployee(long id) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            EmployeeEntity entity = em.find(EmployeeEntity.class, id);
            if (entity == null) {
                throw new NoSuchElementException("Employee doesn't exist");
            }
            em.remove(entity);
            transaction.commit();
            return EmployeesMapper.toEmployeeDtoFromEntity(entity);
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public List<Employee> getEmployeesByDepartment(String department) {
        TypedQuery<EmployeeEntity> query = em.createQuery("select empl from EmployeeEntity empl where department=?1", 
            EmployeeEntity.class);
        query.setParameter(1, department);
        return toEmployeeList(query.getResultList());
    }

    @Override
    public List<String> findDepartments() {
        TypedQuery<String> query = em.createQuery("select distinct department from EmployeeEntity", 
                String.class);
        return query.getResultList();
    }

}
