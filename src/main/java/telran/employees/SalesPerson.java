package telran.employees;

public class SalesPerson extends WageEmployee {
    private float percent;
    private long sales;

    public SalesPerson(long id, int basicSalary, String department, int wage, int hours, float percent, long sales) {
        super(id, basicSalary, department, wage, hours);
        this.percent = percent;
        this.sales = sales;
    }

    public float getPercent() {
        return percent;
    }

    public long getSales() {
        return sales;
    }

    @Override
    public int computeSalary() {
        return super.computeSalary() + (int) (sales * percent / 100);
    }

}
