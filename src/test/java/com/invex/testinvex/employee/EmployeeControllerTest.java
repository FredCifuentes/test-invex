package com.invex.testinvex.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invex.testinvex.employee.controller.EmployeeController;
import com.invex.testinvex.employee.dto.EmployeeRequestDto;
import com.invex.testinvex.employee.dto.EmployeeResponseDto;
import com.invex.testinvex.employee.exception.ResourceNotFoundException;
import com.invex.testinvex.employee.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import com.invex.testinvex.employee.controller.GlobalExceptionHandler;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = EmployeeController.class)
@Import(GlobalExceptionHandler.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    private EmployeeResponseDto buildResponseDto() {
        return EmployeeResponseDto.builder()
                .id(1L)
                .firstName("Rodolfo")
                .middleName("Gabriel")
                .lastName("Juarez")
                .secondLastName("Perez")
                .age(29)
                .gender("M")
                .birthDate(LocalDate.of(1996, 1, 15))
                .position("Backend Java Developer")
                .active(true)
                .createdAt(LocalDateTime.of(2026, 2, 23, 13, 56, 43))
                .build();
    }

    private EmployeeRequestDto buildRequestDto() {
        return EmployeeRequestDto.builder()
                .firstName("Rodolfo")
                .middleName("Gabriel")
                .lastName("Juarez")
                .secondLastName("Perez")
                .age(29)
                .gender("M")
                .birthDate(LocalDate.of(1996, 1, 15))
                .position("Backend Java Developer")
                .active(true)
                .build();
    }

    @Test
    @DisplayName("GET /api/employees should return 200 and list")
    void getAll_ShouldReturnOk() throws Exception {
        when(employeeService.findAll()).thenReturn(List.of(buildResponseDto()));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Rodolfo"))
                .andExpect(jsonPath("$[0].middleName").value("Gabriel"))
                .andExpect(jsonPath("$[0].birthDate").value("15-01-1996"));
    }

    @Test
    @DisplayName("GET /api/employees/{id} should return 200 when exists")
    void getById_ShouldReturnOk_WhenExists() throws Exception {
        when(employeeService.findById(1L)).thenReturn(buildResponseDto());

        mockMvc.perform(get("/api/employees/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Rodolfo"))
                .andExpect(jsonPath("$.middleName").value("Gabriel"))
                .andExpect(jsonPath("$.lastName").value("Juarez"))
                .andExpect(jsonPath("$.secondLastName").value("Perez"))
                .andExpect(jsonPath("$.age").value(29))
                .andExpect(jsonPath("$.gender").value("M"))
                .andExpect(jsonPath("$.birthDate").value("15-01-1996"))
                .andExpect(jsonPath("$.position").value("Backend Java Developer"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("GET /api/employees/{id} should return 404 when not found")
    void getById_ShouldReturnNotFound_WhenNotExists() throws Exception {
        when(employeeService.findById(999L))
                .thenThrow(new ResourceNotFoundException("Employee not found with id: 999"));

        mockMvc.perform(get("/api/employees/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Employee not found with id: 999"))
                .andExpect(jsonPath("$.path").value("/api/employees/999"));
    }

    @Test
    @DisplayName("GET /api/employees/search should return 200 and filtered list")
    void searchByName_ShouldReturnOk() throws Exception {
        when(employeeService.searchByName("rod")).thenReturn(List.of(buildResponseDto()));

        mockMvc.perform(get("/api/employees/search").param("name", "rod"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("Rodolfo"));
    }

    @Test
    @DisplayName("POST /api/employees should return 201 when request is valid")
    void create_ShouldReturnCreated_WhenValidRequest() throws Exception {
        EmployeeRequestDto request = buildRequestDto();
        EmployeeResponseDto response = buildResponseDto();

        when(employeeService.create(any(EmployeeRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Rodolfo"))
                .andExpect(jsonPath("$.middleName").value("Gabriel"))
                .andExpect(jsonPath("$.birthDate").value("15-01-1996"));
    }

    @Test
    @DisplayName("POST /api/employees should return 400 when validation fails")
    void create_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        String invalidJson = """
                {
                  "firstName": "",
                  "middleName": "Gabriel",
                  "lastName": "Juarez",
                  "secondLastName": "Perez",
                  "age": 29,
                  "gender": "M",
                  "birthDate": "15-01-1996",
                  "position": "Backend Java Developer",
                  "active": true
                }
                """;

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.path").value("/api/employees"))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    @DisplayName("POST /api/employees should return 400 when birthDate format is invalid")
    void create_ShouldReturnBadRequest_WhenInvalidDateFormat() throws Exception {
        String invalidDateJson = """
                {
                  "firstName": "Rodolfo",
                  "middleName": "Gabriel",
                  "lastName": "Juarez",
                  "secondLastName": "Perez",
                  "age": 29,
                  "gender": "M",
                  "birthDate": "1996-01-15",
                  "position": "Backend Java Developer",
                  "active": true
                }
                """;

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidDateJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/employees"));

    }

    @Test
    @DisplayName("PUT /api/employees/{id} should return 200 when request is valid")
    void update_ShouldReturnOk_WhenValidRequest() throws Exception {
        EmployeeRequestDto request = EmployeeRequestDto.builder()
                .firstName("Rodolfo")
                .middleName("Gabo")
                .lastName("Juarez")
                .secondLastName("Perez")
                .age(30)
                .gender("M")
                .birthDate(LocalDate.of(1996, 1, 15))
                .position("Technical Lead")
                .active(true)
                .build();

        EmployeeResponseDto response = EmployeeResponseDto.builder()
                .id(1L)
                .firstName("Rodolfo")
                .middleName("Gabo")
                .lastName("Juarez")
                .secondLastName("Perez")
                .age(30)
                .gender("M")
                .birthDate(LocalDate.of(1996, 1, 15))
                .position("Technical Lead")
                .active(true)
                .createdAt(LocalDateTime.of(2026, 2, 23, 13, 56, 43))
                .build();

        when(employeeService.update(eq(1L), any(EmployeeRequestDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.middleName").value("Gabo"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.position").value("Technical Lead"));
    }

    @Test
    @DisplayName("PUT /api/employees/{id} should return 404 when employee does not exist")
    void update_ShouldReturnNotFound_WhenEmployeeNotExists() throws Exception {
        EmployeeRequestDto request = buildRequestDto();

        when(employeeService.update(eq(999L), any(EmployeeRequestDto.class)))
                .thenThrow(new ResourceNotFoundException("Employee not found with id: 999"));

        mockMvc.perform(put("/api/employees/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Employee not found with id: 999"))
                .andExpect(jsonPath("$.path").value("/api/employees/999"));
    }

    @Test
    @DisplayName("DELETE /api/employees/{id} should return 204 when employee exists")
    void delete_ShouldReturnNoContent_WhenExists() throws Exception {
        doNothing().when(employeeService).deleteById(1L);

        mockMvc.perform(delete("/api/employees/{id}", 1L))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("DELETE /api/employees/{id} should return 404 when employee does not exist")
    void delete_ShouldReturnNotFound_WhenNotExists() throws Exception {
        doThrow(new ResourceNotFoundException("Employee not found with id: 999"))
                .when(employeeService).deleteById(999L);

        mockMvc.perform(delete("/api/employees/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Employee not found with id: 999"))
                .andExpect(jsonPath("$.path").value("/api/employees/999"));
    }
}