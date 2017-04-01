package com.example;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class EmployeeMapper implements RowMapper{
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException{
        Employee employee = new Employee();
        employee.setName(rs.getString("name"));
        employee.setDepth(rs.getInt("depth"));
        return employee;
    }
}
