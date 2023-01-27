package com.yaskovich.hr.dto;

import com.yaskovich.hr.models.EmployeeBaseModel;
import com.yaskovich.hr.models.EmployeeFullModel;
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

    public List<EmployeeBaseModel> getAllEmployees() {
        return template.query(findAllEmployees, new EmployeeBaseModelRowMapper());
    }

    public List<EmployeeBaseModel> getEmployeesByDepartment(Long department) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("department", department);
        return template.query(findEmployeesByDepartment, sqlParameterSource, new EmployeeBaseModelRowMapper());
    }

    public Optional<EmployeeFullModel> getEmployeeById(Long id) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        List<EmployeeFullModel> results =
                template.query(findEmployeeById, sqlParameterSource, new EmployeeFullModelRowMapper());
        return Optional.ofNullable(DataAccessUtils.uniqueResult(results));
    }

    public boolean createEmployee(EmployeeFullModel model) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(employeeCreate, createSqlParameterSource(model), keyHolder);
        return true;
    }

    public boolean updateEmployee(EmployeeFullModel model) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(employeeUpdate, createSqlParameterSource(model), keyHolder);
        return true;
    }

    public Integer deleteEmployeeById(Long id) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        return template.update(employeeDelete, sqlParameterSource);
    }

    private SqlParameterSource createSqlParameterSource(EmployeeFullModel model) {
        Map<String, Object> sqlParameter = new HashMap();
        sqlParameter.put("id", model.getId());
        sqlParameter.put("firstName", model.getFirstName());
        sqlParameter.put("lastName", model.getLastName());
        sqlParameter.put("email", model.getEmail());
        sqlParameter.put("department", model.getDepartment());
        sqlParameter.put("position", model.getPosition());
        sqlParameter.put("salary", model.getSalary());
        sqlParameter.put("hiring", model.getHiring());
        sqlParameter.put("summary", model.getSummary());
        return new MapSqlParameterSource(sqlParameter);
    }

    private class EmployeeBaseModelRowMapper implements RowMapper<EmployeeBaseModel> {
        @Override
        public EmployeeBaseModel mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return EmployeeBaseModel.builder()
                    .id(resultSet.getLong("id"))
                    .firstName(resultSet.getString("first_name"))
                    .lastName(resultSet.getString("last_name"))
                    .salary(resultSet.getDouble("salary"))
                    .hiring(resultSet.getDate("hiring"))
                    .build();
        }
    }

    private class EmployeeFullModelRowMapper implements RowMapper<EmployeeFullModel> {
        @Override
        public EmployeeFullModel mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return EmployeeFullModel.builder()
                    .id(resultSet.getLong("id"))
                    .firstName(resultSet.getString("first_name"))
                    .lastName(resultSet.getString("last_name"))
                    .email(resultSet.getString("email"))
                    .department(resultSet.getInt("department"))
                    .position(resultSet.getString("position"))
                    .salary(resultSet.getDouble("salary"))
                    .hiring(resultSet.getDate("hiring"))
                    .summary(resultSet.getString("summary"))
                    .build();
        }
    }
}
