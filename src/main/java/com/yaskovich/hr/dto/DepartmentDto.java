package com.yaskovich.hr.dto;

import com.yaskovich.hr.entity.DepartmentBase;
import com.yaskovich.hr.entity.DepartmentFull;
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
        @PropertySource("classpath:department_dao.properties")
})
public class DepartmentDto {

    private final static Logger LOGGER = LoggerFactory.getLogger(DepartmentDto.class);
    @Autowired
    private NamedParameterJdbcTemplate template;

    @Value("${department.findAll}")
    private String findAllDepartments;
    @Value("${department.findById}")
    private String findDepartmentById;
    @Value("${department.create}")
    private String departmentCreate;
    @Value("${department.update}")
    private String departmentUpdate;
    @Value("${department.delete}")
    private String departmentDelete;

    public List<DepartmentFull> getAllDepartments() {
        return template.query(findAllDepartments, new DepartmentFullModelRowMapper());
    }

    public Optional<DepartmentFull> getDepartmentById(Long id) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        List<DepartmentFull> results =
                template.query(findDepartmentById, sqlParameterSource, new DepartmentFullModelRowMapper());
        return Optional.ofNullable(DataAccessUtils.uniqueResult(results));
    }

    public boolean createDepartment(DepartmentBase model) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(departmentCreate, createSqlParameterSource(model), keyHolder);
        return true;
    }

    public boolean updateDepartment(DepartmentBase model) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(departmentUpdate, createSqlParameterSource(model), keyHolder);
        return true;
    }

    public Integer deleteDepartmentById(Long id) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        return template.update(departmentDelete, sqlParameterSource);
    }

    private SqlParameterSource createSqlParameterSource(DepartmentBase model) {
        Map<String, Object> sqlParameter = new HashMap();
        sqlParameter.put("id", model.getId());
        sqlParameter.put("title", model.getTitle().toUpperCase());
        return new MapSqlParameterSource(sqlParameter);
    }

    private class DepartmentFullModelRowMapper implements RowMapper<DepartmentFull> {
        @Override
        public DepartmentFull mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return DepartmentFull.builder()
                    .id(resultSet.getLong("id"))
                    .title(resultSet.getString("title"))
                    .numberOfEmployees(resultSet.getInt("number_of_employees"))
                    .avgSalary(new DecimalFormat("#0.00").format(resultSet.getDouble("avg_salary")))
                    .build();
        }
    }
}

