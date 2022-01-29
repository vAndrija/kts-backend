package com.kti.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "price_item")
@SQLDelete(sql = "UPDATE price_item SET deleted = true WHERE id=? AND version = ?")
@Where(clause = "deleted=false")
public class PriceItem {
    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    private Boolean deleted = Boolean.FALSE;

    private Double value;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MenuItem menuItem;

    private Boolean isCurrent;

    private Double preparationValue;

    public PriceItem(Double value, LocalDate startDate, LocalDate endDate, MenuItem menuItem, Boolean isCurrent,
                     Double preparationValue) {
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.menuItem = menuItem;
        this.isCurrent = isCurrent;
        this.preparationValue = preparationValue;
    }

    public PriceItem(Double value, LocalDate startDate, LocalDate endDate, MenuItem menuItem, Boolean isCurrent,
                     Integer id, Double preparationValue) {
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.menuItem = menuItem;
        this.isCurrent = isCurrent;
        this.id = id;
        this.preparationValue = preparationValue;
    }

    public PriceItem() {

    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public Boolean getCurrent() {
        return isCurrent;
    }

    public void setCurrent(Boolean current) {
        isCurrent = current;
    }

    public Double getPreparationValue() {
        return preparationValue;
    }

    public void setPreparationValue(Double preparationValue) {
        this.preparationValue = preparationValue;
    }
}
