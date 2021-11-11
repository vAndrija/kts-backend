package com.kti.restaurant.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "waiter")
@PrimaryKeyJoinColumn(name = "user")
@SQLDelete(sql = "UPDATE waiter SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Waiter extends User {

}
