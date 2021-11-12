package com.kti.restaurant.model;

import com.kti.restaurant.dto.MenuItemDto;
import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.persistence.Table;

@Entity
@Table(name = "menu_item")
@SQLDelete(sql = "UPDATE menu_item SET deleted = true WHERE id=?")
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    public MenuItem(String name, String description, Boolean accepted, MenuItemType type, MenuItemCategory category,
                    Menu menu) {
        this.name = name;
        this.description = description;
        this.accepted = accepted;
        this.type = type;
        this.category = category;
        this.menu = menu;
    }

    public MenuItem() {

    }

    public MenuItem(MenuItemDto menuItemDto) {
        this.name = menuItemDto.getName();
        this.description = menuItemDto.getDescription();
        this.type = menuItemDto.getType();
        this.category = menuItemDto.getCategory();
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
}
