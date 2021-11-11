package com.kti.restaurant.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "bartender")
@PrimaryKeyJoinColumn(name = "users")
@SQLDelete(sql = "UPDATE bartender SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Bartender extends User {
    private Boolean priority;

    public Bartender(Long version, Integer id, String lastName, String name, String phoneNumber, String emailAddress,
                     String password, String accountNumber, Boolean priority) {
        super(lastName, name, phoneNumber, emailAddress, password, accountNumber);
        this.priority = priority;
    }

    public Bartender() {

    }

    public Boolean getPriority() {
        return priority;
    }

    public void setPriority(Boolean priority) {
        this.priority = priority;
    }
}
