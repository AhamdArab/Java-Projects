package de.uniwue.jpp.testing.util;

import de.uniwue.jpp.testing.enums.DishType;
import de.uniwue.jpp.testing.enums.GuestType;
import de.uniwue.jpp.testing.interfaces.Dish;
import de.uniwue.jpp.testing.interfaces.Guest;
import de.uniwue.jpp.testing.interfaces.Order;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataGenerator {

    public static List<String> createGuestNames(int amount) {
        List<String> guestNames = new ArrayList<>();
        if (amount <= 0) {
            return guestNames;
        }

        for (int i = 1; i <= amount; i++) {
            guestNames.add("Guest_" + i);
        }

        return Collections.unmodifiableList(guestNames);
    }

    public static List<GuestType> createGuestTypes(int staff, int student, int guest) {
        if (staff < 0 || student < 0 || guest < 0) return new ArrayList<>();
        List<GuestType> types = new ArrayList<>();
        for (int i = 0; i < staff; i++)
            types.add(GuestType.STAFF);
        for (int i = 0; i < student; i++)
            types.add(GuestType.STUDENT);
        for (int i = 0; i < guest; i++)
            types.add(GuestType.GUEST);
        return Collections.unmodifiableList(types);
    }

    public static List<String> createDishNames(int amount) {
        List<String> dishNames = new ArrayList<>();
        if (amount <= 0) {
            return dishNames;
        }
        for (int i = 1; i <= amount; i++) {
            dishNames.add("Dish_" + i);
        }
        return Collections.unmodifiableList(dishNames);
    }

    public static List<Double> createBasePrices(int amount) {
        List<Double> basePrices = new ArrayList<>();
        if (amount <= 0) {
            return basePrices;
        }

        for (int i = 1; i <= amount; i++) {
            basePrices.add(i * 0.5);
        }
        return Collections.unmodifiableList(basePrices);
    }

    public static List<DishType> createDishTypes(int main, int starter, int dessert, int other) {
        List<DishType> types = new ArrayList<>();
        if (main < 0 || starter < 0 || dessert < 0 || other < 0)
            return types;

        for (int i = 0; i < main; i++)
            types.add(DishType.MAIN_DISH);
        for (int i = 0; i < starter; i++)
            types.add(DishType.STARTER);
        for (int i = 0; i < dessert; i++)
            types.add(DishType.DESSERT);
        for (int i = 0; i < other; i++)
            types.add(DishType.OTHER);
        return Collections.unmodifiableList(types);
    }

    public static List<Guest> createGuestMocks(int staff, int student, int guest) {
        List<Guest> guests = new ArrayList<>();

        if (staff < 0 || student < 0 || guest < 0) {
            return guests;
        }

        int totalGuests = staff + student + guest;
        for (int i = 1; i <= totalGuests; i++) {
            Guest guestMock = mock(Guest.class);
            when(guestMock.getName()).thenReturn("Guest_" + i);

            if (i <= staff) {
                when(guestMock.getGuestType()).thenReturn(GuestType.STAFF);
            } else if (i <= staff + student) {
                when(guestMock.getGuestType()).thenReturn(GuestType.STUDENT);
            } else {
                when(guestMock.getGuestType()).thenReturn(GuestType.GUEST);
            }

            guests.add(guestMock);
        }
        return Collections.unmodifiableList(guests);
    }

    public static List<Dish> createDishMocks(int main, int starter, int dessert, int other) {
        List<Dish> dishes = new ArrayList<>();


        if (main < 0 || starter < 0 || dessert < 0 || other < 0) {
            return dishes;
        }

        List<Double> prices = createBasePrices(main + starter + dessert + other);

        int totalDishes = main + starter + dessert + other;
        for (int i = 0; i < totalDishes; i++) {
            Dish dishMock = mock(Dish.class);
            when(dishMock.getName()).thenReturn("Dish_" + (i + 1));
            when(dishMock.getBasePrice()).thenReturn(prices.get(i));


            if (i < main) {
                when(dishMock.getDishType()).thenReturn(DishType.MAIN_DISH);
            } else if (i < main + starter) {
                when(dishMock.getDishType()).thenReturn(DishType.STARTER);
            } else if (i < main + starter + dessert) {
                when(dishMock.getDishType()).thenReturn(DishType.DESSERT);
            } else {
                when(dishMock.getDishType()).thenReturn(DishType.OTHER);
            }

            dishes.add(dishMock);
        }
        return Collections.unmodifiableList(dishes);
    }

    public static List<List<Dish>> createDishSets(int[][] data){
        List<List<Dish>> dishSets = new ArrayList<>();
        if (data.length == 0) {
            return dishSets;
        }

        for (int[] set : data) {
            if (set.length < 4) {
                throw new IllegalArgumentException("Inner array too small!");
            }

            List<Dish> dishSet = new ArrayList<>();
            int totalDishes = set[0] + set[1] + set[2] + set[3];
            List<Double> prices = createBasePrices(totalDishes);

            int counter = 0;
            for (int i = 0; i < set[0]; i++, counter++) {
                Dish dishMock = mock(Dish.class);
                when(dishMock.getName()).thenReturn("Dish_" + (counter + 1));
                when(dishMock.getBasePrice()).thenReturn(prices.get(counter));
                when(dishMock.getDishType()).thenReturn(DishType.MAIN_DISH);
                dishSet.add(dishMock);
            }
            for (int i = 0; i < set[1]; i++, counter++) {
                Dish dishMock = mock(Dish.class);
                when(dishMock.getName()).thenReturn("Dish_" + (counter + 1));
                when(dishMock.getBasePrice()).thenReturn(prices.get(counter));
                when(dishMock.getDishType()).thenReturn(DishType.STARTER);
                dishSet.add(dishMock);
            }
            for (int i = 0; i < set[2]; i++, counter++) {
                Dish dishMock = mock(Dish.class);
                when(dishMock.getName()).thenReturn("Dish_" + (counter + 1));
                when(dishMock.getBasePrice()).thenReturn(prices.get(counter));
                when(dishMock.getDishType()).thenReturn(DishType.DESSERT);
                dishSet.add(dishMock);
            }
            for (int i = 0; i < set[3]; i++, counter++) {
                Dish dishMock = mock(Dish.class);
                when(dishMock.getName()).thenReturn("Dish_" + (counter + 1));
                when(dishMock.getBasePrice()).thenReturn(prices.get(counter));
                when(dishMock.getDishType()).thenReturn(DishType.OTHER);
                dishSet.add(dishMock);
            }

            dishSets.add(dishSet);
        }

        return Collections.unmodifiableList(dishSets);
    }

    public static Order createOrderMock(int index){
        if (index <= 0) {
            return null;
        }

        Guest mockGuest = mock(Guest.class);
        when(mockGuest.getName()).thenReturn("Guest_" + index);
        when(mockGuest.getGuestType()).thenReturn(GuestType.GUEST);

        List<Dish> dishes = new ArrayList<>();
        for (int i = 1; i <= index; i++) {
            Dish mockDish = mock(Dish.class);
            when(mockDish.getName()).thenReturn("Dish_" + i);
            when(mockDish.getBasePrice()).thenReturn(0.5 * i);
            when(mockDish.getDishType()).thenReturn(DishType.MAIN_DISH);
            dishes.add(mockDish);
        }

        Order mockOrder = mock(Order.class);
        when(mockOrder.getGuest()).thenReturn(mockGuest);
        when(mockOrder.getDishes()).thenReturn(dishes);

        when(mockOrder.calculatePrice()).thenAnswer(invocation -> {
            double price = dishes.stream()
                    .mapToDouble(Dish::getBasePrice)
                    .sum();
            double discountFactor = 1.0;
            return price * discountFactor;
        });

        return mockOrder;
    }

}
