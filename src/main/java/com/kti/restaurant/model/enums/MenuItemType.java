package com.kti.restaurant.model.enums;

public enum MenuItemType {
    DRINK("Pice"),
    FOOD("Hrana");

    private String type;

    MenuItemType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public static MenuItemType findType(String type){
        if(MenuItemType.DRINK.getType().toLowerCase().contains(type.toLowerCase())){
            return MenuItemType.DRINK;
        }else if(MenuItemType.FOOD.getType().toLowerCase().contains(type.toLowerCase())){
            return MenuItemType.FOOD;
        }
        return null;
    }
}
