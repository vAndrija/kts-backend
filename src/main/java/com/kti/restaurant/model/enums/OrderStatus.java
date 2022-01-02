package com.kti.restaurant.model.enums;

public enum OrderStatus {
    ORDERED("Poručeno"),
    PAID("Plaćeno"),
    FINISHED("Završeno");
    private String type;

    OrderStatus(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static OrderStatus findType(String type){
        if(OrderStatus.ORDERED.getType().toLowerCase().contains(type.toLowerCase())){
            return OrderStatus.ORDERED;
        }else if(OrderStatus.PAID.getType().toLowerCase().contains(type.toLowerCase())){
            return OrderStatus.PAID;
        }else if(OrderStatus.FINISHED.getType().toLowerCase().contains(type.toLowerCase())){
            return OrderStatus.FINISHED;
        }
        return null;
    }

}
