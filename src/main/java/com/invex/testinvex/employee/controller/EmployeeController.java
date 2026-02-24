package com.invex.testinvex.employee.controller;


import com.invex.testinvex.employee.dto.EmployeeRequestDto;
import com.invex.testinvex.employee.dto.EmployeeResponseDto;
import com.invex.testinvex.employee.exception.ApiErrorResponse;
import com.invex.testinvex.employee.service.EmployeeService;
import com.invex.testinvex.employee.utils.Constants;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.validation.Valid;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(Constants.BASE_EMPLOYEES_PATH)
@RequiredArgsConstructor
@Tag(name = "Employees", description = "Operations for employee management")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(summary = "Get all employees", description = "Returns all registered employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constants.CODE_STATUS_200, description = "Employees retrieved successfully")
    })
    @GetMapping
    public List<EmployeeResponseDto> getAll() {
        long start = System.currentTimeMillis();
        log.info("START Controller.getAll - Request received for GET /api/employees");
        List<EmployeeResponseDto> employees = employeeService.findAll();
        long duration = System.currentTimeMillis() - start;
        log.info("END Controller.getAll - {} employees returned in {} ms", employees.size(), duration);

        return employees;

    }


    @Operation(summary = "Get employee by ID", description = "Returns one employee by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = Constants.CODE_STATUS_200,
                    description = "Employee retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = Constants.CODE_STATUS_404,
                    description = "Employee not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public EmployeeResponseDto getById(
            @Parameter(description = "Employee ID", example = "1")
            @PathVariable Long id) {
        long start = System.currentTimeMillis();
        log.info("START Controller.getById - Request received for GET /api/employees/{id}");

        EmployeeResponseDto employee = employeeService.findById(id);
        long duration = System.currentTimeMillis() - start;
        log.info("END Controller.getById - {} employee returned in {} ms", employee.toString(), duration);

        return employee;
    }

    @Operation(summary = "Create employee", description = "Creates a new employee")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = Constants.CODE_STATUS_201,
                    description = "Employee created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = Constants.CODE_STATUS_400,
                    description = "Validation error / malformed request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponseDto create(@Valid @RequestBody EmployeeRequestDto request) {
        long start = System.currentTimeMillis();
        log.info("START Controller.create - Request received for POST /api/employees");

        EmployeeResponseDto employee = employeeService.create(request);
        long duration = System.currentTimeMillis() - start;
        log.info("END Controller.create - {} employee returned in {} ms", employee.toString(), duration);

        return employee;
    }

    @Operation(summary = "Create employees in batch", description = "Creates multiple employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constants.CODE_STATUS_201, description = "Employees created successfully"),
            @ApiResponse(responseCode = Constants.CODE_STATUS_400, description = "Validation error / malformed request")
    })
    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    public List<EmployeeResponseDto> createBatch(@Valid @RequestBody List<EmployeeRequestDto> requests) {
        return employeeService.createAll(requests);
    }

    @Operation(summary = "Update employee", description = "Updates an existing employee by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = Constants.CODE_STATUS_200,
                    description = "Employee updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = Constants.CODE_STATUS_400,
                    description = "Validation error / malformed request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = Constants.CODE_STATUS_404,
                    description = "Employee not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public EmployeeResponseDto update(@PathVariable Long id,
                                      @Valid @RequestBody EmployeeRequestDto request) {

        long start = System.currentTimeMillis();
        log.info("START Controller.update - Request received for PUT /api/employees/{id}");

        EmployeeResponseDto employee = employeeService.update(id, request);
        long duration = System.currentTimeMillis() - start;
        log.info("END Controller.update - {} employee returned in {} ms", employee.toString(), duration);

        return employee;

    }

    @Operation(summary = "Search employees by name", description = "Searches employees by first name (contains, ignore case)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constants.CODE_STATUS_200, description = "Search completed successfully")
    })
    @GetMapping("/search")
    public List<EmployeeResponseDto> searchByName(@RequestParam String name) {

        long start = System.currentTimeMillis();
        log.info("START Controller.searchByName - Request received for PUT /api/employees/{id}");

        List<EmployeeResponseDto> employees = employeeService.searchByName(name);
        long duration = System.currentTimeMillis() - start;
        log.info("END Controller.searchByName - {} employees returned in {} ms", employees.size(), duration);

        return employees;
    }

    @Operation(summary = "Delete employee", description = "Deletes an employee by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = Constants.CODE_STATUS_204,
                    description = "Employee deleted successfully",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = Constants.CODE_STATUS_404,
                    description = "Employee not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        long start = System.currentTimeMillis();
        log.info("START Controller.delete - Request received for DELETE /api/employees/{id}");

        employeeService.deleteById(id);
        long duration = System.currentTimeMillis() - start;
        log.info("END Controller.delete - {} employee returned in {} ms", id, duration);


    }
}
