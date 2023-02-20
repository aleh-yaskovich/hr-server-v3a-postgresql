package com.yaskovich.hr.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaskovich.hr.controller.model.BaseModel;
import com.yaskovich.hr.controller.model.EmployeeFullRequestModel;
import com.yaskovich.hr.entity.EmployeeBase;
import com.yaskovich.hr.entity.EmployeeFull;
import com.yaskovich.hr.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    private final String URL = "/employee";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;
    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    void shouldReturnListOfEmployees() throws Exception {
        List<EmployeeBase> employees = List.of(getEmployeeBase());
        when(employeeService.getAllEmployees()).thenReturn(employees);
        MockHttpServletResponse response = this.mockMvc.perform(get(URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        List<EmployeeBase> actual =
                objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
                });
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(employees.get(0).getId(), actual.get(0).getId());
        assertEquals(employees.get(0).getFirstName(), actual.get(0).getFirstName());
        assertEquals(employees.get(0).getLastName(), actual.get(0).getLastName());
        assertEquals(employees.get(0).getDepartment(), actual.get(0).getDepartment());
        assertEquals(employees.get(0).getSalary(), actual.get(0).getSalary());
        assertEquals(employees.get(0).getHiring().toString(), actual.get(0).getHiring().toString());
        verify(employeeService).getAllEmployees();
    }

    @Test
    void shouldReturnEmployeeById() throws Exception {
        EmployeeFull employee = getEmployeeFull();
        Long id = employee.getId();
        when(employeeService.getEmployeeById(any(Long.class))).thenReturn(employee);
        MockHttpServletResponse response = this.mockMvc.perform(get(URL + "/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        EmployeeFullRequestModel actual =
                objectMapper.readValue(response.getContentAsString(), EmployeeFullRequestModel.class);
        assertNotNull(actual);
        assertEquals(BaseModel.Status.SUCCESS, actual.getStatus());
        EmployeeFull actualEmployee = actual.getEmployeeFull();
        assertEquals(employee.getId(), actualEmployee.getId());
        assertEquals(employee.getFirstName(), actualEmployee.getFirstName());
        assertEquals(employee.getLastName(), actualEmployee.getLastName());
        assertEquals(employee.getDepartment(), actualEmployee.getDepartment());
        assertEquals(employee.getSalary(), actualEmployee.getSalary());
        assertEquals(employee.getHiring().toString(), actualEmployee.getHiring().toString());
        assertEquals(employee.getPosition(), actualEmployee.getPosition());
        assertEquals(employee.getEmail(), actualEmployee.getEmail());
        assertEquals(employee.getSummary(), actualEmployee.getSummary());
        verify(employeeService).getEmployeeById(id);
    }

    @Test
    void shouldReturnEmployeeByIdWithException() throws Exception {
        Long id = 1L;
        String message = "TEST";
        when(employeeService.getEmployeeById(any(Long.class))).thenThrow(new RuntimeException(message));
        MockHttpServletResponse response = this.mockMvc.perform(get(URL + "/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        EmployeeFullRequestModel actual =
                objectMapper.readValue(response.getContentAsString(), EmployeeFullRequestModel.class);
        assertNotNull(actual);
        assertEquals(BaseModel.Status.FAILURE, actual.getStatus());
        assertEquals(message, actual.getMessage());
        verify(employeeService).getEmployeeById(id);
    }

    @Test
    void shouldReturnBaseModelWhenEmployeeCreated() throws Exception {
        EmployeeFull employee = getEmployeeFull();
        employee.setId(null);
        String json = objectMapper.writeValueAsString(employee);
        MockHttpServletResponse response = this.mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        BaseModel actual = objectMapper.readValue(response.getContentAsString(), BaseModel.class);
        assertNotNull(actual);
        assertEquals(BaseModel.Status.SUCCESS, actual.getStatus());
        verify(employeeService).createEmployee(employee);
    }

    @Test
    void shouldReturnBaseModelWhenEmployeeCreatedWithException() throws Exception {
        String message = "TEST";
        EmployeeFull employee = getEmployeeFull();
        employee.setId(null);
        String json = objectMapper.writeValueAsString(employee);
        when(employeeService.createEmployee(any(EmployeeFull.class))).thenThrow(new RuntimeException(message));
        MockHttpServletResponse response = this.mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        BaseModel actual = objectMapper.readValue(response.getContentAsString(), BaseModel.class);
        assertNotNull(actual);
        assertEquals(BaseModel.Status.FAILURE, actual.getStatus());
        assertEquals(message, actual.getMessage());
        verify(employeeService).createEmployee(employee);
    }

    @Test
    void shouldReturnBaseModelWhenEmployeeUpdated() throws Exception {
        EmployeeFull employee = getEmployeeFull();
        String json = objectMapper.writeValueAsString(employee);
        MockHttpServletResponse response = this.mockMvc.perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        BaseModel actual = objectMapper.readValue(response.getContentAsString(), BaseModel.class);
        assertNotNull(actual);
        assertEquals(BaseModel.Status.SUCCESS, actual.getStatus());
        verify(employeeService).updateEmployee(employee);
    }

    @Test
    void shouldReturnBaseModelWhenEmployeeUpdatedWithException() throws Exception {
        String message = "TEST";
        EmployeeFull employee = getEmployeeFull();
        String json = objectMapper.writeValueAsString(employee);
        when(employeeService.updateEmployee(any(EmployeeFull.class))).thenThrow(new RuntimeException(message));
        MockHttpServletResponse response = this.mockMvc.perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        BaseModel actual = objectMapper.readValue(response.getContentAsString(), BaseModel.class);
        assertNotNull(actual);
        assertEquals(BaseModel.Status.FAILURE, actual.getStatus());
        assertEquals(message, actual.getMessage());
        verify(employeeService).updateEmployee(employee);
    }

    @Test
    void shouldReturnBaseModelWhenEmployeeDeleted() throws Exception {
        Long id = 1L;
        when(employeeService.deleteEmployeeById(any(Long.class))).thenReturn(1);
        MockHttpServletResponse response = this.mockMvc.perform(delete(URL + "/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        BaseModel actual = objectMapper.readValue(response.getContentAsString(), BaseModel.class);
        assertNotNull(actual);
        assertEquals(BaseModel.Status.SUCCESS, actual.getStatus());
        verify(employeeService).deleteEmployeeById(id);
    }

    @Test
    void shouldReturnBaseModelWhenEmployeeIsNotDeleted() throws Exception {
        Long id = 1L;
        when(employeeService.deleteEmployeeById(any(Long.class))).thenReturn(0);
        MockHttpServletResponse response = this.mockMvc.perform(delete(URL + "/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        BaseModel actual = objectMapper.readValue(response.getContentAsString(), BaseModel.class);
        assertNotNull(actual);
        assertEquals(BaseModel.Status.FAILURE, actual.getStatus());
        verify(employeeService).deleteEmployeeById(id);
    }

    private EmployeeBase getEmployeeBase() {
        return EmployeeBase.builder()
                .id(1L)
                .firstName("FIRST")
                .lastName("LAST")
                .department("DEPARTMENT")
                .salary("1000.00")
                .hiring(Date.valueOf(LocalDate.now()))
                .build();
    }

    private EmployeeFull getEmployeeFull() {
        EmployeeBase employeeBase = getEmployeeBase();
        return EmployeeFull.builder()
                .id(employeeBase.getId())
                .firstName(employeeBase.getFirstName())
                .lastName(employeeBase.getLastName())
                .department(employeeBase.getDepartment())
                .salary(employeeBase.getSalary())
                .hiring(employeeBase.getHiring())
                .position("POSITION")
                .email("aaa@aa.aa")
                .summary("")
                .build();
    }
}