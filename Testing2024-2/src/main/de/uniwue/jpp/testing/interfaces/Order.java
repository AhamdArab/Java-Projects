package de.uniwue.jpp.testing.interfaces;

import java.util.List;

public interface Order {

    public double calculatePrice();

    public List<Dish> getDishes();

    public Guest getGuest();

    public static Order createOrder(Guest guest, List<Dish> dishes){

        return new OrderImpl(guest, dishes);
    }
}
