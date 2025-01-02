package telran.employees.db.jpa;

import org.json.JSONObject;
import jakarta.persistence.*;
import telran.employees.*;

@Entity
public class WageEmployeeEntity extends EmployeeEntity {
    private int wage;
    private int hours;

    @Override
    protected void fromEmployeeDto(Employee empl) {
        super.fromEmployeeDto(empl);
        wage = ((WageEmployee) empl).getWage();
        hours = ((WageEmployee) empl).getHours();
    }

    @Override
    protected void toJsonObject(JSONObject jsonObject) {
        super.toJsonObject(jsonObject);
        jsonObject.put("wage", wage);
        jsonObject.put("hours", hours);
    }
}
