package telran.employees;

import java.util.*;
import java.util.Map.Entry;
import java.lang.IllegalStateException;

public class CompanyImpl implements Company {
    private TreeMap<Long, Employee> employees = new TreeMap<>();
    private HashMap<String, List<Employee>> employeesDepartment = new HashMap<>();
    private TreeMap<Float, List<Manager>> managers = new TreeMap<>();

    public class IteratorCompany implements Iterator<Employee>{
        Iterator<Employee> it = employees.values().iterator();
        Employee iteratedObj;

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Employee next() {
            iteratedObj = it.next();
            return iteratedObj;
        }

        @Override
        public void remove() {
            it.remove();
            removeEmployee(iteratedObj);
        }
    }

    @Override
    public Iterator<Employee> iterator() {
        return new IteratorCompany();
    }

    @Override
    public void addEmployee(Employee empl) {
        if (getEmployee(empl.getId()) != null) {
            throw new IllegalStateException();
        }
        employees.put(empl.getId(), empl);
        
        String department = empl.getDepartment();
        List<Employee> listEmployees = employeesDepartment.getOrDefault(department, new LinkedList<>());
        listEmployees.add(empl);
        employeesDepartment.put(empl.getDepartment(), listEmployees);

        if (empl instanceof Manager) {
            Manager manager = (Manager) empl;
            Float factor = manager.getFactor();
            List<Manager> listManagers = managers.getOrDefault(factor, new LinkedList<>());
            listManagers.add(manager);
            managers.put(factor, listManagers);
        }
    }

    @Override
    public Employee getEmployee(long id) {
        return employees.get(id);
    }

    @Override
    public Employee removeEmployee(long id) {
        if (getEmployee(id) == null) {
            throw new NoSuchElementException();
        }
        Employee empl = employees.get(id);
        return removeEmployee(empl);
    }

    private Employee removeEmployee(Employee empl) {
        String department = empl.getDepartment();
        List<Employee> listEmployees = employeesDepartment.get(department);
        listEmployees.remove(empl);
        if (listEmployees.isEmpty()) {
            employeesDepartment.remove(department);
        }
        if (empl instanceof Manager) {
            Manager manager = (Manager) empl;
            Float factor = manager.getFactor();
            List<Manager> listManagers = managers.get(factor);
            listManagers.remove(manager);
            if (listManagers.isEmpty()) {
                managers.remove(factor);
            }
        }
        return employees.remove(empl.getId());
    }

    @Override
    public int getDepartmentBudget(String department) {
        int budget = 0;
        List<Employee> listEmployees = employeesDepartment.get(department);
        if (listEmployees != null) {
            budget = listEmployees.stream().mapToInt(e -> e.computeSalary()).sum();
        }
        return budget;
    }

    @Override
    public String[] getDepartments() {
        return employeesDepartment.keySet().stream().sorted().toArray(String[]::new);
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        Entry<Float, List<Manager>> mostFactorEntry = managers.lastEntry();
        Manager[] res = {};
        if (mostFactorEntry != null) {
            res = mostFactorEntry.getValue().stream().toArray(Manager[]::new);
        }
        return res;
    }
}
