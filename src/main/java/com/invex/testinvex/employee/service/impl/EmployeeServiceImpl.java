package com.invex.testinvex.employee.service.impl;



import com.invex.testinvex.employee.dto.EmployeeRequestDto;
import com.invex.testinvex.employee.dto.EmployeeResponseDto;
import com.invex.testinvex.employee.entity.Employee;
import com.invex.testinvex.employee.exception.ResourceNotFoundException;
import com.invex.testinvex.employee.mapper.EmployeeMapper;
import com.invex.testinvex.employee.repository.EmployeeRepository;
import com.invex.testinvex.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;


    @Override
    public List<EmployeeResponseDto> findAll() {


        log.debug("START Service.findAll - Fetching employees from repository");

        List<EmployeeResponseDto> result = employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toResponseDto)
                .toList();

        log.debug("END Service.findAll - Retrieved {} employees", result.size());
        return result;


    }

    @Override
    public EmployeeResponseDto findById(Long id) {
        Employee employee = getEmployeeOrThrow(id);
        return employeeMapper.toResponseDto(employee);
    }

    @Override
    public EmployeeResponseDto create(EmployeeRequestDto request) {
        Employee employee = employeeMapper.toEntity(request);
        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toResponseDto(saved);
    }

    @Override
    public List<EmployeeResponseDto> createAll(List<EmployeeRequestDto> requests) {
        List<Employee> employees = requests.stream()
                .map(employeeMapper::toEntity)
                .collect(Collectors.toList());

        return employeeRepository.saveAll(employees).stream()
                .map(employeeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponseDto update(Long id, EmployeeRequestDto request) {
        Employee existing = getEmployeeOrThrow(id);
        employeeMapper.updateEntityFromDto(request, existing);
        Employee updated = employeeRepository.save(existing);
        return employeeMapper.toResponseDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        Employee employee = getEmployeeOrThrow(id);
        employeeRepository.delete(employee);
    }

    @Override
    public List<EmployeeResponseDto> searchByName(String name) {
        return employeeRepository.findByFirstNameContainingIgnoreCase(name)
                .stream()
                .map(employeeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    private Employee getEmployeeOrThrow(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }
}
