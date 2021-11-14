package com.kti.restaurant.dto.menu;

import java.time.LocalDateTime;

public class CreateMenuDto {
    private String name;
    private LocalDateTime startDuration;
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
