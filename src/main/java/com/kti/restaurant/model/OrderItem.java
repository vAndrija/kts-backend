package com.kti.restaurant.model;

import com.kti.restaurant.model.enums.OrderItemStatus;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@SQLDelete(sql = "UPDATE order_item SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class OrderItem {
    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    private Boolean deleted = Boolean.FALSE;

    private Integer quantity;

    private String note;

    private OrderItemStatus status;

    private Integer priority;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuItem menuItem;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bartender bartender;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cook cook;

    public OrderItem(Integer quantity, String note, OrderItemStatus status, Integer priority, Order order, MenuItem menuItem, Bartender bartender, Cook cook) {
        this.quantity = quantity;
        this.note = note;
        this.status = status;
        this.priority = priority;
        this.order = order;
        this.menuItem = menuItem;
        this.bartender = bartender;
        this.cook = cook;
    }

    public OrderItem() {

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public OrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
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
