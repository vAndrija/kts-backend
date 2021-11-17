package com.kti.restaurant.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "cook")
@PrimaryKeyJoinColumn(name = "users")
public class Cook extends User {
    private Boolean priority;

    public Cook() {
    }

    public Cook(String lastName, String name, String phoneNumber, String emailAddress, String accountNumber, Boolean priority,String password) {
        super(lastName, name, phoneNumber, emailAddress,password, accountNumber);
        this.priority = priority;
    }

    public Cook(Integer id, String name, String lastName, String accountNumber, String phoneNumber, Boolean priority) {
        super(id, name, lastName, accountNumber, phoneNumber);
        this.priority = priority;
    }

    public Boolean getPriority() {
        return priority;
    }

    public void setPriority(Boolean priority) {
        this.priority = priority;
    }
}
