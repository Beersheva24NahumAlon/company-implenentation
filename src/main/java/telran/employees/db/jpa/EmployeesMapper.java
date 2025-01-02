package telran.employees.db.jpa;

import org.json.JSONObject;

import telran.employees.Employee;

public class EmployeesMapper {
    private static final String PACKAGE = "telran.employees.";
    private static final String PACKAGE_ENTITY = PACKAGE + "db.jpa.";
    private static final String CLASS_NAME = "className";
    private static final String ENTITY = "Entity";

    public static Employee toEmployeeDtoFromEntity(EmployeeEntity entity) {
        String dtoEntityClassName = entity.getClass().getSimpleName();
        String dtoClassName = PACKAGE + dtoEntityClassName.replaceAll("Entity", "");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CLASS_NAME, dtoClassName);
        entity.toJsonObject(jsonObject);
        return Employee.getEmployeeFromJSON(jsonObject.toString());
    }

    @SuppressWarnings("unchecked")
    public static EmployeeEntity toEmployeeEntityFromDto(Employee employee) {
        String dtoClassName = employee.getClass().getSimpleName();
        String dtoEntityClassName = PACKAGE_ENTITY + dtoClassName + ENTITY;
        try {
            Class<EmployeeEntity> clazz = (Class<EmployeeEntity>) Class.forName(dtoEntityClassName);
            EmployeeEntity entity = clazz.getConstructor().newInstance();
            entity.fromEmployeeDto(employee);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
