package com.yaskovich.hr.dto;

import com.yaskovich.hr.entity.EmployeeBase;
import com.yaskovich.hr.entity.EmployeeFull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:employee_dao.properties")
})
public class EmployeeDto {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmployeeDto.class);
    @Autowired
    private NamedParameterJdbcTemplate template;

    @Value("${employee.findAll}")
    private String findAllEmployees;
    @Value("${employee.findByDepartment}")
    private String findEmployeesByDepartment;
    @Value("${employee.findById}")
    private String findEmployeeById;
    @Value("${employee.create}")
    private String employeeCreate;
    @Value("${employee.update}")
    private String employeeUpdate;
    @Value("${employee.delete}")
    private String employeeDelete;
    @Value("${employee.checkEmail}")
    private String employeeCheckEmail;

    public List<EmployeeBase> getAllEmployees() {
        return template.query(findAllEmployees, new EmployeeBaseModelRowMapper());
    }

    public List<EmployeeBase> getEmployeesByDepartment(Long department) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("department", department);
        return template.query(findEmployeesByDepartment, sqlParameterSource, new EmployeeBaseModelRowMapper());
    }

    public Optional<EmployeeFull> getEmployeeById(Long id) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        List<EmployeeFull> results =
                template.query(findEmployeeById, sqlParameterSource, new EmployeeFullModelRowMapper());
        return Optional.ofNullable(DataAccessUtils.uniqueResult(results));
    }

    public boolean createEmployee(EmployeeFull model) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(employeeCreate, createSqlParameterSource(model), keyHolder);
        return true;
    }

    public boolean updateEmployee(EmployeeFull model) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(employeeUpdate, createSqlParameterSource(model), keyHolder);
        return true;
    }

    public Integer deleteEmployeeById(Long id) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        return template.update(employeeDelete, sqlParameterSource);
    }

    private void checkUniqueEmail(String email) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("email", email);
        Integer res = template.queryForObject(employeeCheckEmail, sqlParameterSource,  Integer.class);
        if(res > 0) {
            throw new RuntimeException("Employee with email "+email+" already exists");
        }
    }

    private SqlParameterSource createSqlParameterSource(EmployeeFull model) {
        Map<String, Object> sqlParameter = new HashMap();
        sqlParameter.put("id", model.getId());
        sqlParameter.put("firstName", model.getFirstName());
        sqlParameter.put("lastName", model.getLastName());
        sqlParameter.put("email", model.getEmail());
        sqlParameter.put("department", model.getDepartment());
        sqlParameter.put("position", model.getPosition());
        sqlParameter.put("salary", new Double(model.getSalary()));
        sqlParameter.put("hiring", model.getHiring());
        sqlParameter.put("summary", model.getSummary());
        return new MapSqlParameterSource(sqlParameter);
    }

    private class EmployeeBaseModelRowMapper implements RowMapper<EmployeeBase> {
        @Override
        public EmployeeBase mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return EmployeeBase.builder()
                    .id(resultSet.getLong("id"))
                    .firstName(resultSet.getString("first_name"))
                    .lastName(resultSet.getString("last_name"))
                    .salary(new DecimalFormat("#0.00").format(resultSet.getDouble("salary")))
                    .hiring(resultSet.getDate("hiring"))
                    .build();
        }
    }

    private class EmployeeFullModelRowMapper implements RowMapper<EmployeeFull> {
        @Override
        public EmployeeFull mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return EmployeeFull.builder()
                    .id(resultSet.getLong("id"))
                    .firstName(resultSet.getString("first_name"))
                    .lastName(resultSet.getString("last_name"))
                    .email(resultSet.getString("email"))
                    .department(resultSet.getInt("department"))
                    .position(resultSet.getString("position"))
                    .salary(new DecimalFormat("#0.00").format(resultSet.getDouble("salary")))
                    .hiring(resultSet.getDate("hiring"))
                    .summary(resultSet.getString("summary"))
                    .build();
        }
    }
}
