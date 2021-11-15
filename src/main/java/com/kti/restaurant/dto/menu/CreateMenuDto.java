package com.kti.restaurant.dto.menu;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateMenuDto {
    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    @NotNull(message = "Start date should not be null or empty")
    private LocalDateTime startDuration;

    @NotNull(message = "End date should not be null or empty")
    private LocalDateTime endDuration;

    public CreateMenuDto(String name, LocalDateTime startDuration, LocalDateTime endDuration) {
        this.name = name;
        this.startDuration = startDuration;
        this.endDuration = endDuration;
    }

    public CreateMenuDto() {

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
}
