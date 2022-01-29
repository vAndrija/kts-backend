package com.kti.restaurant.dto.tablereservation;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class TableReservationDto {
	
	private Integer id;
	
    @NotNull(message = "Name should not be null or empty")
    private String name;

    @NotNull(message = "Start date should not be null or empty")
    private LocalDateTime durationStart;
    
    @NotNull(message = "End date should not be null or empty")
    private LocalDateTime durationEnd;

    @NotNull(message = "Table id should not be null")
    private Integer tableId;

    public TableReservationDto() {

    }

    public TableReservationDto(String name, Integer tableId, LocalDateTime durationStart, LocalDateTime durationEnd) {
        this.name = name;
        this.tableId = tableId;
        this.durationStart = durationStart;
        this.durationEnd = durationEnd;
    }
    
    public TableReservationDto(Integer id, String name, Integer tableId, LocalDateTime durationStart, LocalDateTime durationEnd) {
        this.name = name;
        this.tableId = tableId;
        this.durationStart = durationStart;
        this.durationEnd = durationEnd;
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
    
    public LocalDateTime getDurationEnd() {
        return durationEnd;
    }

    public void setDurationEnd(LocalDateTime durationEnd) {
        this.durationEnd = durationEnd;
    }
}
