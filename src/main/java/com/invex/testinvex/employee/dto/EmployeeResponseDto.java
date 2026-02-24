package com.invex.testinvex.employee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.invex.testinvex.employee.utils.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "EmployeeResponseDto", description = "Response payload for employee operations")
public class EmployeeResponseDto {
    @Schema(description = "Employee ID", example = "1")
    private Long id;
    @Schema(description = "Employee first name", example = "Rodolfo")
    private String firstName;
    @Schema(description = "Employee middle name", example = "Gabriel")
    private String middleName;
    @Schema(description = "Employee last name", example = "Juarez")
    private String lastName;
    @Schema(description = "Employee second last name", example = "Perez")
    private String secondLastName;
    @Schema(description = "Employee age", example = "29")
    private Integer age;
    @Schema(description = "Employee gender", example = "M")
    private String gender;
    @Schema(description = "Birth date", example = "15-01-1996")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;
    @Schema(description = "Employee position", example = "Backend Java Developer")
    private String position;
    @Schema(description = "Employee active flag", example = "true")
    private Boolean active;
    @Schema(description = "Record creation timestamp", example = "2026-02-23 13:56:43")
    @JsonFormat(pattern = Constants.DATETIME_PATTERN)
    private LocalDateTime createdAt;
}
