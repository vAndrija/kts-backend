package model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "user")
@SQLDelete(sql = "UPDATE admin SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Admin extends User {

}
