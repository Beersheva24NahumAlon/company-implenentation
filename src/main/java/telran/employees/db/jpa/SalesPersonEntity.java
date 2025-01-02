package telran.employees.db.jpa;

import org.json.JSONObject;
import jakarta.persistence.*;
import telran.employees.*;

@Entity
public class SalesPersonEntity extends WageEmployeeEntity {
    private float percent;
    private long sales;

    @Override
    protected void fromEmployeeDto(Employee empl) {
        super.fromEmployeeDto(empl);
        percent = ((SalesPerson) empl).getPercent();
        sales = ((SalesPerson) empl).getSales();
    }

    @Override
    protected void toJsonObject(JSONObject jsonObject) {
        super.toJsonObject(jsonObject);
        jsonObject.put("percent", percent);
        jsonObject.put("sales", sales);
    }
}
