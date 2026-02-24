package com.invex.testinvex.employee;

import com.invex.testinvex.employee.dto.EmployeeRequestDto;
import com.invex.testinvex.employee.dto.EmployeeResponseDto;
import com.invex.testinvex.employee.entity.Employee;
import com.invex.testinvex.employee.exception.ResourceNotFoundException;
import com.invex.testinvex.employee.mapper.EmployeeMapper;
import com.invex.testinvex.employee.repository.EmployeeRepository;
import com.invex.testinvex.employee.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeRequestDto requestDto;
    private EmployeeResponseDto responseDto;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
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
                .createdAt(LocalDateTime.now())
                .build();

        requestDto = EmployeeRequestDto.builder()
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

        responseDto = EmployeeResponseDto.builder()
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
                .createdAt(employee.getCreatedAt())
                .build();
    }

    @Test
    void findById_ShouldReturnEmployee_WhenExists() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toResponseDto(employee)).thenReturn(responseDto);

        EmployeeResponseDto result = employeeService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Rodolfo", result.getFirstName());
        verify(employeeRepository).findById(1L);
        verify(employeeMapper).toResponseDto(employee);
    }

    @Test
    void findById_ShouldThrowException_WhenNotExists() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.findById(999L)
        );

        assertEquals("Employee not found with id: 999", ex.getMessage());
        verify(employeeRepository).findById(999L);
        verifyNoInteractions(employeeMapper);
    }

    @Test
    void create_ShouldSaveAndReturnEmployee() {
        when(employeeMapper.toEntity(requestDto)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toResponseDto(employee)).thenReturn(responseDto);

        EmployeeResponseDto result = employeeService.create(requestDto);

        assertNotNull(result);
        assertEquals("Rodolfo", result.getFirstName());
        verify(employeeMapper).toEntity(requestDto);
        verify(employeeRepository).save(employee);
        verify(employeeMapper).toResponseDto(employee);
    }

    @Test
    void findAll_ShouldReturnList() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));
        when(employeeMapper.toResponseDto(employee)).thenReturn(responseDto);

        List<EmployeeResponseDto> result = employeeService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Rodolfo", result.get(0).getFirstName());
        verify(employeeRepository).findAll();
    }

    @Test
    void searchByName_ShouldReturnMatchingEmployees() {
        when(employeeRepository.findByFirstNameContainingIgnoreCase("rod"))
                .thenReturn(List.of(employee));
        when(employeeMapper.toResponseDto(employee)).thenReturn(responseDto);

        List<EmployeeResponseDto> result = employeeService.searchByName("rod");

        assertEquals(1, result.size());
        assertEquals("Rodolfo", result.get(0).getFirstName());
        verify(employeeRepository).findByFirstNameContainingIgnoreCase("rod");
    }

    @Test
    void update_ShouldUpdateEmployee_WhenExists() {
        Employee updatedEntity = Employee.builder()
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
                .createdAt(employee.getCreatedAt())
                .build();

        EmployeeResponseDto updatedResponse = EmployeeResponseDto.builder()
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
                .createdAt(employee.getCreatedAt())
                .build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(updatedEntity);
        when(employeeMapper.toResponseDto(updatedEntity)).thenReturn(updatedResponse);

        EmployeeRequestDto updateRequest = EmployeeRequestDto.builder()
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

        EmployeeResponseDto result = employeeService.update(1L, updateRequest);

        assertNotNull(result);
        assertEquals("Gabo", result.getMiddleName());
        assertEquals(30, result.getAge());
        assertEquals("Technical Lead", result.getPosition());

        verify(employeeRepository).findById(1L);
        verify(employeeMapper).updateEntityFromDto(updateRequest, employee);
        verify(employeeRepository).save(employee);
        verify(employeeMapper).toResponseDto(updatedEntity);
    }

    @Test
    void update_ShouldThrowException_WhenEmployeeNotExists() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.update(999L, requestDto));

        verify(employeeRepository).findById(999L);
        verify(employeeMapper, never()).updateEntityFromDto(any(), any());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void deleteById_ShouldDelete_WhenEmployeeExists() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.deleteById(1L);

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).delete(employee);
    }

    @Test
    void deleteById_ShouldThrowException_WhenEmployeeNotExists() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteById(999L));

        verify(employeeRepository).findById(999L);
        verify(employeeRepository, never()).delete(any());
    }

    @Test
    void createAll_ShouldSaveAndReturnList() {
        when(employeeMapper.toEntity(requestDto)).thenReturn(employee);
        when(employeeRepository.saveAll(anyList())).thenReturn(List.of(employee));
        when(employeeMapper.toResponseDto(employee)).thenReturn(responseDto);

        List<EmployeeResponseDto> result = employeeService.createAll(List.of(requestDto));

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(employeeMapper).toEntity(requestDto);
        verify(employeeRepository).saveAll(anyList());
        verify(employeeMapper).toResponseDto(employee);
    }

    @Test
    void searchByName_ShouldReturnEmptyList_WhenNoMatches() {
        when(employeeRepository.findByFirstNameContainingIgnoreCase("zzz"))
                .thenReturn(Collections.emptyList());

        List<EmployeeResponseDto> result = employeeService.searchByName("zzz");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(employeeRepository).findByFirstNameContainingIgnoreCase("zzz");
        verify(employeeMapper, never()).toResponseDto(any());
    }
}
