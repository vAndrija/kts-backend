package com.kti.restaurant.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "restaurant_table")
@SQLDelete(sql = "UPDATE restaurant_table SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class RestaurantTable {
    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    private Boolean deleted = Boolean.FALSE;

    private Integer tableNumber;

    private Integer capacity;

    private Integer xCoordinate;

    private Integer yCoordinate;

    public RestaurantTable(Boolean deleted, Integer tableNumber, Integer capacity, Integer xCoordinate, Integer yCoordinate) {
        this.deleted = deleted;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public RestaurantTable() {

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

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(Integer xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Integer getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(Integer yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
}
