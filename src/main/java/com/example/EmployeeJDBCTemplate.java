package com.example;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class EmployeeJDBCTemplate implements EmployeeDAO{
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Employee> listEmployees(){
        String SQL = "SELECT node.id, node.name, (COUNT(parent.name) - 1) AS depth\n" +
                "FROM employee AS node,\n" +
                "        employee AS parent\n" +
                "WHERE node.lft BETWEEN parent.lft AND parent.rgt\n" +
                "GROUP BY node.id\n" +
                "ORDER BY node.lft;";
        List<Employee> employees = jdbcTemplate.query(SQL, new EmployeeMapper());
        return employees;
    }

    public void create(String name, Integer id){
        String SQL = "UPDATE employee AS e, (SELECT @myLeft := lft FROM employee WHERE id = '" + id + "') AS n SET e.rgt = e.rgt + 2 WHERE e.rgt > @myLeft;";
        jdbcTemplate.update(SQL);
        String SQL2 = "UPDATE employee AS e, (SELECT @myLeft := lft FROM employee WHERE id = '" + id + "') AS n SET e.lft = e.lft + 2 WHERE e.lft > @myLeft;";
        jdbcTemplate.update(SQL2);
        String SQL3 = "SELECT @myLeft := lft FROM employee WHERE id = '" + id + "'";
        Integer myLeft = jdbcTemplate.queryForObject(SQL3, Integer.class);
        String SQL4 = "INSERT INTO employee(name, lft, rgt) VALUES('" + name + "', " + myLeft + " +1, " + myLeft + " +2);";
        jdbcTemplate.update(SQL4);

        return;
    }
}
