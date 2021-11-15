package com.kti.restaurant.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "users")
@SQLDelete(sql = "UPDATE admin SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Admin extends User {

    public Admin(String lastName, String name, String phoneNumber, String emailAddress, String accountNumber) {
        super(lastName, name, phoneNumber, emailAddress, accountNumber);
    }

    public Admin(Integer id, String name, String lastName, String accountNumber, String phoneNumber){
        super(id,name,lastName,accountNumber,phoneNumber);
    }

    public Admin() {
        super();
    }
}
