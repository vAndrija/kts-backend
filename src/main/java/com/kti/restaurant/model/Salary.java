package com.kti.restaurant.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "salary")
@SQLDelete(sql = "UPDATE salary SET deleted = true WHERE id=? AND version=?")
@Where(clause = "deleted=false")
public class Salary {
    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    private Double value;

    private LocalDate startDate;

    private LocalDate endDate;
    //Stavila sam EAGER jer uvijek kad nam bude trebala plata treba ce nam i korisnik vezan za tu platu
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    private Boolean deleted = Boolean.FALSE;

    public Salary(Double value, LocalDate startDate, LocalDate endDate) {
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Salary(Double value, LocalDate startDate, LocalDate endDate, User user) {
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
    }

    public Salary() {

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
