package com.kti.restaurant.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "manager")
@PrimaryKeyJoinColumn(name = "users")
public class Manager extends User {

    public Manager(String lastName, String name, String phoneNumber, String emailAddress, String accountNumber) {
        super(lastName, name, phoneNumber, emailAddress,accountNumber);
    }

    public Manager(String name, String lastName, String accountNumber, String phoneNumber){
        super(name,lastName,accountNumber,phoneNumber);
    }

    public Manager() {
        super();
    }
}
