package com.example;

import javax.sql.DataSource;
import java.util.List;

public interface EmployeeDAO {
    public void setDataSource(DataSource ds);
    public List<Employee> listEmployees();
    public void create(String name, Integer id);
}
