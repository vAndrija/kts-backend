package com.kti.restaurant.dto.menu;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class UpdateMenuDto {
    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    @NotNull(message = "Start date should not be null or empty")
    private LocalDateTime startDuration;

    @NotNull(message = "End date should not be null or empty")
    private LocalDateTime endDuration;

    @NotEmpty(message = "Id should not be null or empty")
    private Integer id;

    public UpdateMenuDto(String name, LocalDateTime startDuration, LocalDateTime endDuration, Integer id) {
        this.name = name;
        this.startDuration = startDuration;
        this.endDuration = endDuration;
        this.id = id;
    }

    public UpdateMenuDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartDuration() {
        return startDuration;
    }

    public void setStartDuration(LocalDateTime startDuration) {
        this.startDuration = startDuration;
    }

    public LocalDateTime getEndDuration() {
        return endDuration;
    }

    public void setEndDuration(LocalDateTime endDuration) {
        this.endDuration = endDuration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
