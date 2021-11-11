package model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "manager")
@PrimaryKeyJoinColumn(name = "user")
@SQLDelete(sql = "UPDATE manager SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Manager extends User {

}
