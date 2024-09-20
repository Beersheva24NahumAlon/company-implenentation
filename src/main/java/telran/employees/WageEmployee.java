package telran.employees;

public class WageEmployee extends Employee{
    private int wage;
    private int hours;

    public WageEmployee(long id, int basicSalary, String department, int wage, int hours) {
        super(id, basicSalary, department);
        this.hours = hours;
        this.wage = wage;
    }

    public int getHours() {
        return hours;
    }

    public int getWage() {
        return wage;
    }

    @Override
    public int computeSalary() {
        return super.computeSalary() + wage * hours;
    }
}
