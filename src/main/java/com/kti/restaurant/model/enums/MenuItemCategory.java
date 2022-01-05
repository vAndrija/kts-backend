package com.kti.restaurant.model.enums;

public enum MenuItemCategory {
    SOUP("Supa"),
    BREAKFAST("Doručak"),
    APPETIZER("Predjelo"),
    MAIN_COURSE("Glavno jelo"),
    DESSERT("Dezert"),
    COCKTAIL("Koktel"),
    HOT_DRINK("Topli napitak"),
    NON_ALCOHOLIC("Bezalkoholno piće");

    private String category;

    MenuItemCategory(String category){
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public static MenuItemCategory findCategory(String category){
        if(MenuItemCategory.NON_ALCOHOLIC.getCategory().toLowerCase().contains(category.toLowerCase())){
            return MenuItemCategory.NON_ALCOHOLIC;
        }else if(MenuItemCategory.APPETIZER.getCategory().toLowerCase().contains(category.toLowerCase())){
            return MenuItemCategory.APPETIZER;
        }else if(MenuItemCategory.BREAKFAST.getCategory().toLowerCase().contains(category.toLowerCase())) {
            return MenuItemCategory.BREAKFAST;
        }else if(MenuItemCategory.COCKTAIL.getCategory().toLowerCase().contains(category.toLowerCase())){
            return MenuItemCategory.COCKTAIL;
        }else if(MenuItemCategory.DESSERT.getCategory().toLowerCase().contains(category.toLowerCase())){
            return MenuItemCategory.DESSERT;
        }else if(MenuItemCategory.HOT_DRINK.getCategory().toLowerCase().contains(category.toLowerCase())){
            return MenuItemCategory.HOT_DRINK;
        }else if(MenuItemCategory.MAIN_COURSE.getCategory().toLowerCase().contains(category.toLowerCase())){
            return MenuItemCategory.MAIN_COURSE;
        }else if(MenuItemCategory.SOUP.getCategory().toLowerCase().contains(category.toLowerCase())){
            return MenuItemCategory.SOUP;
        }
        return null;
    }
}


