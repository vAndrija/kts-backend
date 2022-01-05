package com.kti.restaurant.dto.salary;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class CreateSalaryDto {
    @DecimalMin(value = "0.01", message = "Salary should not be negative value.")
    @NotNull(message = "Value should not be null")
    private Double value;

    @NotNull(message = "Start date of salary should not be null")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotEmpty(message = "User email should not be null or empty")
    private String userEmail;

    public CreateSalaryDto() {

    }

    public CreateSalaryDto(Double value, LocalDate startDate, LocalDate endDate, String userEmail) {
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userEmail = userEmail;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
