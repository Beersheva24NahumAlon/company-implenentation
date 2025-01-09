package telran.employees.db;

import java.util.*;
import telran.employees.*;

public class CompanyDBImpl implements Company {
    private CompanyRepository repository;

    public CompanyDBImpl(CompanyRepository repository) {
        this.repository = repository;
    }

    private class CompanyDBIterator implements Iterator<Employee>{
        Iterator<Employee> it = new ArrayList<>(repository.getAllEmployees()).iterator();
        Employee prev;

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Employee next() {
            prev = it.next();
            return prev;
        }

        @Override
        public void remove() {
            it.remove();
            removeEmployee(prev.getId());
        }

    }

    @Override
    public Iterator<Employee> iterator() {
        return new CompanyDBIterator();
    }

    @Override
    public void addEmployee(Employee empl) {
        repository.insertEmployee(empl);
    }

    @Override
    public Employee getEmployee(long id) {
        return repository.findEmployee(id);
    }

    @Override
    public Employee removeEmployee(long id) {
        return repository.removeEmployee(id);
    }

    @Override
    public int getDepartmentBudget(String department) {
        List<Employee> employees = repository.getEmployeesByDepartment(department);
        return employees.stream().mapToInt(Employee::computeSalary).sum();
    }

    @Override
    public String[] getDepartments() {
        List<String> listDepartments = repository.findDepartments();
        return listDepartments.toArray(String[]::new);
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        List<Manager> employees = repository.findManagersWithMaxFactor();
        return employees.toArray(Manager[]::new);
    }
}
