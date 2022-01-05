package com.kti.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.persistence.Table;

@Entity
@Table(name = "menu_item")
@SQLDelete(sql = "UPDATE menu_item SET deleted = true WHERE id=? AND version = ?")
@Where(clause = "deleted=false")
public class MenuItem {
    @Version
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    private String name;

    private String description;

    private Boolean accepted = Boolean.FALSE;

    private MenuItemType type;

    private MenuItemCategory category;

    private Boolean deleted = Boolean.FALSE;

    private Integer preparationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Menu menu;

    public MenuItem(String name, String description, Boolean accepted, MenuItemType type, MenuItemCategory category,
                    Menu menu, Integer preparationTime) {
        this.name = name;
        this.description = description;
        this.accepted = accepted;
        this.type = type;
        this.category = category;
        this.menu = menu;
        this.preparationTime = preparationTime;
    }

    public MenuItem() {

    }

    public MenuItem(String name, String description, MenuItemCategory category, MenuItemType type, Integer preparationTime) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.type = type;
        this.preparationTime = preparationTime;
    }
    public MenuItem(String name, String description, MenuItemCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }
    public MenuItem(String name, String description) {
        this.name = name;
        this.description = description;
        this.accepted = true;
    }

    public MenuItem(Integer id, String name, String description, Boolean accepted, MenuItemType type, MenuItemCategory category,
                    Menu menu, Integer preparationTime) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.type = type;
        this.id = id;
        this.accepted = accepted;
        this.menu = menu;
        this.preparationTime = preparationTime;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public MenuItemType getType() {
        return type;
    }

    public void setType(MenuItemType type) {
        this.type = type;
    }

    public MenuItemCategory getCategory() {
        return category;
    }

    public void setCategory(MenuItemCategory category) {
        this.category = category;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Integer getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
    }
}
