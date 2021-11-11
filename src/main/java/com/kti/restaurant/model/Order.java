package com.kti.restaurant.model;

import com.kti.restaurant.model.enums.OrderStatus;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order")
@SQLDelete(sql = "UPDATE order SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Order {
    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    private Boolean deleted = Boolean.FALSE;

    private OrderStatus status;

    private LocalDateTime dateOfOrder;

    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    private RestaurantTable table;

    @ManyToOne(fetch = FetchType.LAZY)
    private Waiter waiter;

    public Order(OrderStatus status, LocalDateTime dateOfOrder, Double price, RestaurantTable table, Waiter waiter) {
        this.status = status;
        this.dateOfOrder = dateOfOrder;
        this.price = price;
        this.table = table;
        this.waiter = waiter;
    }

    public Order() {

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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(LocalDateTime dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public RestaurantTable getTable() {
        return table;
    }

    public void setTable(RestaurantTable table) {
        this.table = table;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }
}
