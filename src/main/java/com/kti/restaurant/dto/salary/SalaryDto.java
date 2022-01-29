package com.kti.restaurant.dto.salary;

import java.time.LocalDate;

public class SalaryDto {
	private Integer id;
	
	private Double value;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private String name;
	
	private String lastName;
	
	private String email;
	
	public SalaryDto() {
		
	}
	
	public SalaryDto(Integer id, Double value, LocalDate startDate, LocalDate endDate, String name, String lastName, String email) {
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.value = value;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	
	

}
