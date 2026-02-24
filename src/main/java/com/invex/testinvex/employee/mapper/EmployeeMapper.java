package com.invex.testinvex.employee.mapper;

import com.invex.testinvex.employee.dto.EmployeeRequestDto;
import com.invex.testinvex.employee.dto.EmployeeResponseDto;
import com.invex.testinvex.employee.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public Employee toEntity(EmployeeRequestDto dto) {
        if (dto == null) return null;

        return Employee.builder()
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .lastName(dto.getLastName())
                .secondLastName(dto.getSecondLastName())
                .age(dto.getAge())
                .gender(dto.getGender())
                .birthDate(dto.getBirthDate())
                .position(dto.getPosition())
                .active(dto.getActive())
                .build();
    }

    public EmployeeResponseDto toResponseDto(Employee entity) {
        if (entity == null) return null;

        return EmployeeResponseDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .middleName(entity.getMiddleName())
                .lastName(entity.getLastName())
                .secondLastName(entity.getSecondLastName())
                .age(entity.getAge())
                .gender(entity.getGender())
                .birthDate(entity.getBirthDate())
                .position(entity.getPosition())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public void updateEntityFromDto(EmployeeRequestDto dto, Employee entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setMiddleName(dto.getMiddleName());
        entity.setLastName(dto.getLastName());
        entity.setSecondLastName(dto.getSecondLastName());
        entity.setAge(dto.getAge());
        entity.setGender(dto.getGender());
        entity.setBirthDate(dto.getBirthDate());
        entity.setPosition(dto.getPosition());
        entity.setActive(dto.getActive());
    }
}
