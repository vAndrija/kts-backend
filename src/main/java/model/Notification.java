package model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "notification")
@SQLDelete(sql = "UPDATE notification SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Notification {
    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    private Boolean deleted = Boolean.FALSE;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bartender bartender;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cook cook;

    public Notification(String message, Order order, Bartender bartender, Cook cook) {
        this.message = message;
        this.order = order;
        this.bartender = bartender;
        this.cook = cook;
    }

    public Notification() {

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Bartender getBartender() {
        return bartender;
    }

    public void setBartender(Bartender bartender) {
        this.bartender = bartender;
    }

    public Cook getCook() {
        return cook;
    }

    public void setCook(Cook cook) {
        this.cook = cook;
    }
}
