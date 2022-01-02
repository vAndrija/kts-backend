package com.kti.restaurant.model.enums;

public enum OrderItemStatus {
    ORDERED("Poručeno"),
    PREPARATION("U pripremi"),
    PREPARED("Pripremljeno"),
    SERVED("Servirano") ;

    private String type;

    OrderItemStatus(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static OrderItemStatus findType(String type){
        if(OrderItemStatus.ORDERED.getType().toLowerCase().contains(type.toLowerCase())){
            return OrderItemStatus.ORDERED;
        }else if(OrderItemStatus.PREPARATION.getType().toLowerCase().contains(type.toLowerCase())){
            return OrderItemStatus.PREPARATION;
        }else if(OrderItemStatus.PREPARED.getType().toLowerCase().contains(type.toLowerCase())){
            return OrderItemStatus.PREPARED;
        }else if(OrderItemStatus.SERVED.getType().toLowerCase().contains(type.toLowerCase())){
            return OrderItemStatus.SERVED;
        }
        return null;
    }
}
