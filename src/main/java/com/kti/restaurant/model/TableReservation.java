package com.kti.restaurant.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "table_reservation")
@SQLDelete(sql = "UPDATE table_reservation SET deleted = true WHERE id=? and version=?")
@Where(clause = "deleted=false")
public class TableReservation {
    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    private Boolean deleted = Boolean.FALSE;

    private String name;

    private LocalDateTime durationStart;

    @ManyToOne(fetch = FetchType.LAZY)
    private RestaurantTable table;
 
    public TableReservation(String name, LocalDateTime durationStart, RestaurantTable table) {
        this.name = name;
        this.durationStart = durationStart;
        this.table = table;
    }

    public  TableReservation() {

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

    public RestaurantTable getTable() {
        return table;
    }

    public void setTable(RestaurantTable table) {
        this.table = table;
    }
}
