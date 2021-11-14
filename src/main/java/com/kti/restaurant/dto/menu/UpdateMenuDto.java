package com.kti.restaurant.dto.menu;

import java.time.LocalDateTime;

public class UpdateMenuDto {
    private String name;
    private LocalDateTime startDuration;
    private LocalDateTime endDuration;
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
