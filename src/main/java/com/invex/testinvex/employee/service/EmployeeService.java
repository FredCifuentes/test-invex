package com.invex.testinvex.employee.service;



import com.invex.testinvex.employee.dto.EmployeeRequestDto;
import com.invex.testinvex.employee.dto.EmployeeResponseDto;

import java.util.List;

public interface EmployeeService {
    List<EmployeeResponseDto> findAll();
    EmployeeResponseDto findById(Long id);
    EmployeeResponseDto create(EmployeeRequestDto request);
    List<EmployeeResponseDto> createAll(List<EmployeeRequestDto> requests);
    EmployeeResponseDto update(Long id, EmployeeRequestDto request);
    void deleteById(Long id);
    List<EmployeeResponseDto> searchByName(String name);
}
