package com.yaskovich.hr.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaskovich.hr.controller.model.BaseModel;
import com.yaskovich.hr.controller.model.DepartmentRequestModel;
import com.yaskovich.hr.entity.Department;
import com.yaskovich.hr.entity.DepartmentBase;
import com.yaskovich.hr.entity.DepartmentFull;
import com.yaskovich.hr.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    private final String URL = "/department";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DepartmentService departmentService;
    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    void shouldReturnListOfDepartments() throws Exception {
        DepartmentFull department = DepartmentFull.builder()
                .id(1L)
                .title("TITLE")
                .numberOfEmployees(0)
                .avgSalary("0.00")
                .build();
        List<DepartmentFull> departments = List.of(department);
        when(departmentService.getAllDepartments()).thenReturn(departments);
        MockHttpServletResponse response = this.mockMvc.perform(get(URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        List<DepartmentFull> actual =
                objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
                });
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(department, actual.get(0));
        verify(departmentService).getAllDepartments();
    }

    @Test
    void shouldReturnDepartmentById() throws Exception {
        Long id = 1L;
        Department department = Department.builder()
                .id(1L)
                .title("TITLE")
                .numberOfEmployees(0)
                .avgSalary("0.00")
                .employees(new ArrayList<>())
                .build();
        when(departmentService.getDepartmentById(any(Long.class))).thenReturn(department);
        MockHttpServletResponse response = this.mockMvc.perform(get(URL + "/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        DepartmentRequestModel actual =
                objectMapper.readValue(response.getContentAsString(), DepartmentRequestModel.class);
        assertNotNull(actual);
        assertEquals(BaseModel.Status.SUCCESS, actual.getStatus());
        assertEquals(department, actual.getDepartment());
        verify(departmentService).getDepartmentById(id);
    }

    @Test
    void shouldReturnDepartmentByIdWithException() throws Exception {
        Long id = 1L;
        String message = "TEST";
        when(departmentService.getDepartmentById(any(Long.class))).thenThrow(new RuntimeException(message));
        MockHttpServletResponse response = this.mockMvc.perform(get(URL + "/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        DepartmentRequestModel actual =
                objectMapper.readValue(response.getContentAsString(), DepartmentRequestModel.class);
        assertNotNull(actual);
        assertEquals(BaseModel.Status.FAILURE, actual.getStatus());
        assertEquals(message, actual.getMessage());
        verify(departmentService).getDepartmentById(id);
    }

    @Test
    void shouldReturnBaseModelWhenDepartmentCreated() throws Exception {
        DepartmentBase department = new DepartmentBase(null, "TITLE");
        String json = objectMapper.writeValueAsString(department);
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
        verify(departmentService).createDepartment(department);
    }

    @Test
    void shouldReturnBaseModelWhenDepartmentCreatedWithException() throws Exception {
        String message = "TEST";
        DepartmentBase department = new DepartmentBase(null, "TITLE");
        String json = objectMapper.writeValueAsString(department);
        when(departmentService.createDepartment(any(DepartmentBase.class))).thenThrow(new RuntimeException(message));
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
        verify(departmentService).createDepartment(department);
    }

    @Test
    void shouldReturnBaseModelWhenDepartmentUpdated() throws Exception {
        DepartmentBase department = new DepartmentBase(1L, "TITLE");
        String json = objectMapper.writeValueAsString(department);
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
        verify(departmentService).updateDepartment(department);
    }

    @Test
    void shouldReturnBaseModelWhenDepartmentUpdatedWithException() throws Exception {
        String message = "TEST";
        DepartmentBase department = new DepartmentBase(1L, "TITLE");
        String json = objectMapper.writeValueAsString(department);
        when(departmentService.updateDepartment(any(DepartmentBase.class))).thenThrow(new RuntimeException(message));
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
        verify(departmentService).updateDepartment(department);
    }

    @Test
    void shouldReturnBaseModelWhenEmployeeDeleted() throws Exception {
        Long id = 1L;
        when(departmentService.deleteDepartmentById(any(Long.class))).thenReturn(1);
        MockHttpServletResponse response = this.mockMvc.perform(delete(URL + "/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        BaseModel actual = objectMapper.readValue(response.getContentAsString(), BaseModel.class);
        assertNotNull(actual);
        assertEquals(BaseModel.Status.SUCCESS, actual.getStatus());
        verify(departmentService).deleteDepartmentById(id);
    }

    @Test
    void shouldReturnBaseModelWhenEmployeeIsNotDeleted() throws Exception {
        Long id = 1L;
        when(departmentService.deleteDepartmentById(any(Long.class))).thenReturn(0);
        MockHttpServletResponse response = this.mockMvc.perform(delete(URL + "/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        BaseModel actual = objectMapper.readValue(response.getContentAsString(), BaseModel.class);
        assertNotNull(actual);
        assertEquals(BaseModel.Status.FAILURE, actual.getStatus());
        verify(departmentService).deleteDepartmentById(id);
    }
}