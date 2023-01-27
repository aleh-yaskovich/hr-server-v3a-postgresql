package com.yaskovich.hr.dto;

import com.yaskovich.hr.models.DepartmentBaseModel;
import com.yaskovich.hr.models.DepartmentFullModel;
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

    public List<DepartmentFullModel> getAllDepartments() {
        return template.query(findAllDepartments, new DepartmentFullModelRowMapper());
    }

    public Optional<DepartmentFullModel> getDepartmentById(Long id) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        List<DepartmentFullModel> results =
                template.query(findDepartmentById, sqlParameterSource, new DepartmentFullModelRowMapper());
        return Optional.ofNullable(DataAccessUtils.uniqueResult(results));
    }

    public boolean createDepartment(DepartmentBaseModel model) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(departmentCreate, createSqlParameterSource(model), keyHolder);
        return true;
    }

    public boolean updateDepartment(DepartmentBaseModel model) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(departmentUpdate, createSqlParameterSource(model), keyHolder);
        return true;
    }

    public Integer deleteDepartmentById(Long id) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        return template.update(departmentDelete, sqlParameterSource);
    }

    private SqlParameterSource createSqlParameterSource(DepartmentBaseModel model) {
        Map<String, Object> sqlParameter = new HashMap();
        sqlParameter.put("id", model.getId());
        sqlParameter.put("title", model.getTitle());
        return new MapSqlParameterSource(sqlParameter);
    }

    private class DepartmentFullModelRowMapper implements RowMapper<DepartmentFullModel> {
        @Override
        public DepartmentFullModel mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return DepartmentFullModel.builder()
                    .id(resultSet.getLong("id"))
                    .title(resultSet.getString("title"))
                    .numberOfEmployees(resultSet.getInt("number_of_employees"))
                    .avgSalary(resultSet.getDouble("avg_salary"))
                    .build();
        }
    }
}

