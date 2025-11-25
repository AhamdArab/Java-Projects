package de.uniwue.jpp.testing.interfaces;

import de.uniwue.jpp.testing.enums.DishType;

public interface Dish {

    public String getName();

    public double getBasePrice();

    public DishType getDishType();

    public static Dish createDish(String name, double basePrice, DishType type){

        return new DishImpl(name, basePrice, type);
    }
}
