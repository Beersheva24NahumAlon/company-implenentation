package telran.employees;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.lang.IllegalStateException;
import telran.io.Persistable;
import java.io.*;

public class CompanyImpl implements Company, Persistable {
    private TreeMap<Long, Employee> employees = new TreeMap<>();
    private HashMap<String, List<Employee>> employeesDepartment = new HashMap<>();
    private TreeMap<Float, List<Manager>> managers = new TreeMap<>();
    private boolean stateChanged = false;
    private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static Lock readLock = readWriteLock.readLock();
    private static Lock writeLock = readWriteLock.writeLock();

    public class IteratorCompany implements Iterator<Employee>{
        Iterator<Employee> it = employees.values().iterator();
        Employee iteratedObj;

        @Override
        public boolean hasNext() {
            try {
                readLock.lock();
                return it.hasNext();
            } finally {
                readLock.unlock();
            }   
        }

        @Override
        public Employee next() {
            try {
                readLock.lock();
                iteratedObj = it.next();
                return iteratedObj;
            } finally {
                readLock.unlock();
            }   
        }

        @Override
        public void remove() {
            try {
                writeLock.lock();
                it.remove();
                removeEmployeeFromDepartment(iteratedObj);
                removeManagerFromManagers(iteratedObj);
                stateChanged = true;
            } finally {
                writeLock.unlock();
            }
        }
    }

    @Override
    public Iterator<Employee> iterator() {
        return new IteratorCompany();
    }

    @Override
    public void addEmployee(Employee empl) {
        Long id = empl.getId();
        if (getEmployee(id) != null) {
            throw new IllegalStateException("The employee with this id is already exist in th compamy");
        }
        try {
            writeLock.lock();
            employees.put(id, empl);
            addEmployeeToDepartment(empl);
            addEmployeeToManagers(empl);
            stateChanged = true;
        } finally {
            writeLock.unlock();
        }
    }

    @SuppressWarnings("unused")
    private void addEmployeeToDepartment(Employee empl) {
        employeesDepartment.computeIfAbsent(empl.getDepartment(), k -> new LinkedList<>()).add(empl);
    }

    @SuppressWarnings("unused")
    private void addEmployeeToManagers(Employee empl) {
        if (empl instanceof Manager) {
            Manager manager = (Manager) empl;
            managers.computeIfAbsent(manager.getFactor(), k -> new LinkedList<>()).add(manager);
        }
    }

    @Override
    public Employee getEmployee(long id) {
        try {
            readLock.lock();
            return employees.get(id);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Employee removeEmployee(long id) {
        Employee empl = employees.get(id);
        if (empl == null) {
            throw new NoSuchElementException("The employee with this id is not exist in the compamy");
        }
        try {
            writeLock.lock();
            removeEmployeeFromDepartment(empl);
            removeManagerFromManagers(empl);
            stateChanged = true;
            return employees.remove(id);
        } finally {
            writeLock.unlock();
        }
    }

    private void removeEmployeeFromDepartment(Employee empl) {
        removeFromMapsOfLists(employeesDepartment, empl, empl.getDepartment());
    }

    private void removeManagerFromManagers(Employee empl) {
        if (empl instanceof Manager) {
            Manager manager = (Manager) empl;
            removeFromMapsOfLists(managers, manager, manager.getFactor());
        }
    }

    private <K, E extends Employee> void removeFromMapsOfLists(Map<K, List<E>> map, Employee empl, K key) {
        List<E> list = map.get(key);
        list.remove(empl);
        if (list.isEmpty()) {
            map.remove(key);
        }
    }

    @Override
    public int getDepartmentBudget(String department) { 
        try {
            readLock.lock();
            int budget = 0;
            List<Employee> listEmployees = employeesDepartment.get(department);
            if (listEmployees != null) {
                budget = listEmployees.stream().mapToInt(e -> e.computeSalary()).sum();
            }
            return budget;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public String[] getDepartments() {
        try {
            readLock.lock();
            return employeesDepartment.keySet().stream().sorted().toArray(String[]::new);
        } finally {
            readLock.unlock();
        }   
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        try {
            readLock.lock();
            Entry<Float, List<Manager>> mostFactorEntry = managers.lastEntry();
            Manager[] res = {};
            if (mostFactorEntry != null) {
                res = mostFactorEntry.getValue().stream().toArray(Manager[]::new);
            }
            return res;
        } finally {
            readLock.unlock();
        }

    }

    @Override
    public boolean saveToFile(String fileName) {
        boolean res = false;
        if (stateChanged) {
            try (PrintWriter writer = new PrintWriter(fileName)) {
                forEach(writer::println);
                stateChanged = false;
                res = true;        
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return res;
    }

    @Override
    public void restoreFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.lines().forEach(l -> addEmployee(Employee.getEmployeeFromJSON(l)));
        } catch (FileNotFoundException e) { 
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
