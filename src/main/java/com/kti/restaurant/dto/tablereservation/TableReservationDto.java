package com.kti.restaurant.dto.tablereservation;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class TableReservationDto {
	
	private Integer id;
	
    @NotNull(message = "Name should not be null or empty")
    private String name;

    @NotNull(message = "Start date should not be null or empty")
    private LocalDateTime durationStart;

    @NotNull(message = "Table id should not be null")
    private Integer tableId;

    public TableReservationDto() {

    }

    public TableReservationDto(String name, Integer tableId, LocalDateTime durationStart) {
        this.name = name;
        this.tableId = tableId;
        this.durationStart = durationStart;
    }
    
    public TableReservationDto(Integer id, String name, Integer tableId, LocalDateTime durationStart) {
        this.name = name;
        this.tableId = tableId;
        this.durationStart = durationStart;
        this.id = id;
    }

    public Integer getId() {
    	return id;
    }
    
    public void setId(Integer id) {
    	this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public LocalDateTime getDurationStart() {
        return durationStart;
    }

    public void setDurationStart(LocalDateTime durationStart) {
        this.durationStart = durationStart;
    }
}
