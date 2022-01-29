package com.kti.restaurant.dto.menu;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class MenuDto {
    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    @NotNull(message = "Start date should not be null or empty")
    private LocalDateTime durationStart;

    @NotNull(message = "End date should not be null or empty")
    private LocalDateTime durationEnd;

    private Integer id;

    public MenuDto(String name, LocalDateTime durationStart, LocalDateTime durationEnd, Integer id) {
        this.name = name;
        this.durationStart = durationStart;
        this.durationEnd = durationEnd;
        this.id = id;
    }

    public MenuDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDurationStart() {
        return durationStart;
    }

    public void setDurationStart(LocalDateTime durationStart) {
        this.durationStart = durationStart;
    }

    public LocalDateTime getDurationEnd() {
        return durationEnd;
    }

    public void setDurationEnd(LocalDateTime durationEnd) {
        this.durationEnd = durationEnd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
