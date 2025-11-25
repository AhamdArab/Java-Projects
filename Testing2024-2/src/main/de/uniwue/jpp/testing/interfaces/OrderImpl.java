package de.uniwue.jpp.testing.interfaces;

import de.uniwue.jpp.testing.enums.DishType;

import java.util.*;

public class OrderImpl implements Order{

    Guest guest;
    List<Dish> dishes;

    public OrderImpl(Guest guest, List<Dish> dishes) {
        if (guest == null) {
            throw new IllegalArgumentException("Guest must not be null!");
        }
        if (dishes == null) {
            throw new IllegalArgumentException("Dish list must not be null!");
        }
        if (dishes.isEmpty()) {
            throw new IllegalArgumentException("Dish list must not be empty!");
        }
        this.guest = guest;
        this.dishes = new ArrayList<>(dishes);
    }

    @Override
    public List<Dish> getDishes() {
        return Collections.unmodifiableList(dishes);
    }

    @Override
    public Guest getGuest() {
        return guest;
    }

    @Override
    public double calculatePrice() {
        double sum = 0;
        List<DishType> list = new ArrayList<>();
        for (Dish dish:dishes) {
            sum+=dish.getBasePrice();
            list.add(dish.getDishType());
        }
        sum *= guest.getGuestType().getDiscountFactor();
        if (list.contains(DishType.MAIN_DISH) && list.contains(DishType.DESSERT) && list.contains(DishType.STARTER)) {
            sum *= 0.8;
        }

        return sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderImpl order = (OrderImpl) o;
        return guest.equals(order.guest) && new HashSet<>(dishes).equals(new HashSet<>(order.dishes));
    }

    @Override
    public int hashCode() {
        int dishesHashCode = dishes.stream().mapToInt(Dish::hashCode).sum();
        return Objects.hash(guest, dishesHashCode);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Order:\n");
        for (Dish dish : dishes) {
            builder.append("\t- ").append(dish.getName()).append("\n");
        }
        builder.append("\n").append("\tName: ").append(guest.getName()).append(" | Total price: ");

        builder.append(calculatePrice()).append("€");

        return builder.toString();
    }
}