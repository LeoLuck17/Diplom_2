package model;

public class Ingredient {
    public String ingredientType;
    public String name;
    public String price;

    public Ingredient(String ingredientType, String name, String price) {
        this.ingredientType = ingredientType;
        this.name = name;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return ingredientType;
    }
}
