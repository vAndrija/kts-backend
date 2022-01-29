package com.kti.restaurant.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "menu")
@SQLDelete(sql = "UPDATE menu SET deleted = true WHERE id=? AND version = ?")
@Where(clause = "deleted=false")
public class Menu {
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

    private LocalDateTime durationEnd;

    public Menu(String name, LocalDateTime durationStart, LocalDateTime durationEnd) {
        this.name = name;
        this.durationStart = durationStart;
        this.durationEnd = durationEnd;
    }

    public Menu(String name, LocalDateTime durationStart, LocalDateTime durationEnd, Integer id) {
        this.name = name;
        this.durationStart = durationStart;
        this.durationEnd = durationEnd;
        this.id = id;
    }

    public Menu() {

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
        return this.name;
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

    public LocalDateTime getDurationEnd() {
        return durationEnd;
    }

    public void setDurationEnd(LocalDateTime durationEnd) {
        this.durationEnd = durationEnd;
    }
}
