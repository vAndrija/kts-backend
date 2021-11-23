package com.kti.restaurant.dto.report;

import java.time.LocalDate;

public class PeriodDto {
    private LocalDate startDate;
    private LocalDate endDate;

    public PeriodDto() {

    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
