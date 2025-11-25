package de.uniwue.jpp.testing;

import de.uniwue.jpp.testing.enums.DishType;
import de.uniwue.jpp.testing.enums.GuestType;
import de.uniwue.jpp.testing.interfaces.*;
import de.uniwue.jpp.testing.util.AbstractTestClass;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.stream.Collectors;

import static de.uniwue.jpp.testing.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestOrder extends AbstractTestClass<Order> {

    @Test
    public void testConstructorValidArguments() {
        try {
            Guest guest1 = mock(Guest.class);
            when(guest1.getName()).thenReturn("Guest_1");
            when(guest1.getGuestType()).thenReturn(GuestType.STUDENT);

            Guest guest2 = mock(Guest.class);
            when(guest2.getName()).thenReturn("Guest_2");
            when(guest2.getGuestType()).thenReturn(GuestType.STAFF);

            Guest guest3 = mock(Guest.class);
            when(guest3.getName()).thenReturn("Guest_3");
            when(guest3.getGuestType()).thenReturn(GuestType.GUEST);

            Guest guest4 = mock(Guest.class);
            when(guest4.getName()).thenReturn("Guest_4");
            when(guest4.getGuestType()).thenReturn(GuestType.STUDENT);

            Guest guest5 = mock(Guest.class);
            when(guest5.getName()).thenReturn("Guest_5");
            when(guest5.getGuestType()).thenReturn(GuestType.STAFF);

            Dish mainDish = mock(Dish.class);
            when(mainDish.getDishType()).thenReturn(DishType.MAIN_DISH);
            when(mainDish.getName()).thenReturn("Obst");
            when(mainDish.getBasePrice()).thenReturn(3.5);

            Dish starter = mock(Dish.class);
            when(starter.getDishType()).thenReturn(DishType.STARTER);
            when(starter.getName()).thenReturn("Pommes");
            when(starter.getBasePrice()).thenReturn(3.0);

            Dish dessert = mock(Dish.class);
            when(dessert.getDishType()).thenReturn(DishType.DESSERT);
            when(dessert.getName()).thenReturn("Burger");
            when(dessert.getBasePrice()).thenReturn(7.5);

            Dish other = mock(Dish.class);
            when(other.getDishType()).thenReturn(DishType.OTHER);
            when(other.getName()).thenReturn("Pizza");
            when(other.getBasePrice()).thenReturn(9.5);

            List<Dish> dish = List.of(mainDish, dessert);
            List<Dish> dish2 = List.of(starter);
            List<Dish> dish3 = List.of(mainDish, starter, dessert);
            List<Dish> dish4 = List.of(mainDish, starter, dessert, other);
            List<Dish> dish5 = List.of(mainDish, other);

            Order order1 = construct(guest1, dish);
            Order order2 = construct(guest2, dish2);
            Order order3 = construct(guest3, dish3);
            Order order4 = construct(guest4, dish4);
            Order order5 = construct(guest5, dish5);

        } catch (Exception e) {
            fail("Testing valid arguments for Order constructor");
        }
    }

    @Test
    public void testConstructorInvalidArguments() {
        List<Dish> validDishes = List.of(mock(Dish.class));
        List<Dish> dishList = new ArrayList<>();
        assertThrowsWithMessage(IllegalArgumentException.class, () -> construct(null, validDishes),
                "Guest must not be null!",
                "Testing invalid guest argument for Order constructor");

        Guest validGuest = Mockito.mock(Guest.class);
        assertThrowsWithMessage(IllegalArgumentException.class, () -> construct(validGuest, null),
                "Dish list must not be null!",
                "Testing invalid dishes argument for Order constructor");

        assertThrowsWithMessage(IllegalArgumentException.class, () -> construct(validGuest, dishList),
                "Dish list must not be empty!",
                "Testing invalid dishes argument for Order constructor");


    }

    @Test
    public void testGetDishes() {
        Guest guest1 = mock(Guest.class);

        Dish mainDish = mock(Dish.class);
        when(mainDish.getDishType()).thenReturn(DishType.MAIN_DISH);
        when(mainDish.getName()).thenReturn("Obst");
        when(mainDish.getBasePrice()).thenReturn(1.5);

        Dish starter = mock(Dish.class);
        when(starter.getDishType()).thenReturn(DishType.STARTER);
        when(starter.getName()).thenReturn("Pommes");
        when(starter.getBasePrice()).thenReturn(3.0);

        Dish dessert = mock(Dish.class);
        when(dessert.getDishType()).thenReturn(DishType.DESSERT);
        when(dessert.getName()).thenReturn("Burger");
        when(dessert.getBasePrice()).thenReturn(7.5);

        Dish other = mock(Dish.class);
        when(other.getDishType()).thenReturn(DishType.OTHER);
        when(other.getName()).thenReturn("Salat");
        when(other.getBasePrice()).thenReturn(12.0);


        List<Dish> dish1 = Arrays.asList(mainDish, starter);
        List<Dish> dish2 = Arrays.asList(dessert, other);
        List<Dish> dish3 = Arrays.asList(starter, mainDish, dessert);
        List<Dish> dish4 = Arrays.asList(other);
        List<Dish> dish5 = Arrays.asList(dessert, other, starter);

        Order order1 = construct(guest1, dish1);
        Order order2 = construct(guest1, dish2);
        Order order3 = construct(guest1, dish3);
        Order order4 = construct(guest1, dish4);
        Order order5 = construct(guest1, dish5);

        assertListsEqualInAnyOrder(dish1, order1.getDishes(), "Incorrect list returned by getDishes()");
        assertListsEqualInAnyOrder(dish2, order2.getDishes(), "Incorrect list returned by getDishes()");
        assertListsEqualInAnyOrder(dish3, order3.getDishes(), "Incorrect list returned by getDishes()");
        assertListsEqualInAnyOrder(dish4, order4.getDishes(), "Incorrect list returned by getDishes()");
        assertListsEqualInAnyOrder(dish5, order5.getDishes(), "Incorrect list returned by getDishes()");

        assertListIsUnmodifiable(order1.getDishes(), "List returned by getDishes() is modifiable");
        assertListIsUnmodifiable(order2.getDishes(), "List returned by getDishes() is modifiable");
        assertListIsUnmodifiable(order3.getDishes(), "List returned by getDishes() is modifiable");
        assertListIsUnmodifiable(order4.getDishes(), "List returned by getDishes() is modifiable");
        assertListIsUnmodifiable(order5.getDishes(), "List returned by getDishes() is modifiable");

    }


    @Test
    public void testGetGuest() {

        Guest guest1 = mock(Guest.class);
        when(guest1.getName()).thenReturn("Ahmad");
        when(guest1.getGuestType()).thenReturn(GuestType.STUDENT);

        Guest guest2 = mock(Guest.class);
        when(guest2.getName()).thenReturn("Yousef");
        when(guest2.getGuestType()).thenReturn(GuestType.STAFF);

        Guest guest3 = mock(Guest.class);
        when(guest3.getName()).thenReturn("Abd");
        when(guest3.getGuestType()).thenReturn(GuestType.GUEST);

        Guest guest4 = mock(Guest.class);
        when(guest4.getName()).thenReturn("Bischr");
        when(guest4.getGuestType()).thenReturn(GuestType.STUDENT);

        Guest guest5 = mock(Guest.class);
        when(guest5.getName()).thenReturn("Baraa");
        when(guest5.getGuestType()).thenReturn(GuestType.STAFF);

        Dish dish = mock(Dish.class);

        Order order1 = construct(guest1, Collections.singletonList(dish));
        Order order2 = construct(guest2, Collections.singletonList(dish));
        Order order3 = construct(guest3, Collections.singletonList(dish));
        Order order4 = construct(guest4, Collections.singletonList(dish));
        Order order5 = construct(guest5, Collections.singletonList(dish));


        assertEquals(guest1, order1.getGuest(), "Incorrect guest returned by getGuest() for the first order.");
        assertEquals(guest2, order2.getGuest(), "Incorrect guest returned by getGuest() for the second order.");
        assertEquals(guest3, order3.getGuest(), "Incorrect guest returned by getGuest() for the third order.");
        assertEquals(guest4, order4.getGuest(), "Incorrect guest returned by getGuest() for the fourth order.");
        assertEquals(guest5, order5.getGuest(), "Incorrect guest returned by getGuest() for the fifth order.");
    }

    @Test
    public void testCalculatePrice() {

        Dish mainDish = mock(Dish.class);
        when(mainDish.getName()).thenReturn("Pommes");
        when(mainDish.getDishType()).thenReturn(DishType.MAIN_DISH);
        when(mainDish.getBasePrice()).thenReturn(10.0);

        Dish starter = mock(Dish.class);
        when(starter.getName()).thenReturn("Burger");
        when(starter.getDishType()).thenReturn(DishType.STARTER);
        when(starter.getBasePrice()).thenReturn(5.0);

        Dish dessert = mock(Dish.class);
        when(dessert.getName()).thenReturn("Lasania");
        when(dessert.getDishType()).thenReturn(DishType.DESSERT);
        when(dessert.getBasePrice()).thenReturn(7.0);

        Dish other = mock(Dish.class);
        when(other.getName()).thenReturn("Gurken");
        when(other.getDishType()).thenReturn(DishType.OTHER);
        when(other.getBasePrice()).thenReturn(3.0);

        Dish other2 = mock(Dish.class);
        when(other2.getName()).thenReturn("Tomaten");
        when(other2.getDishType()).thenReturn(DishType.OTHER);
        when(other2.getBasePrice()).thenReturn(6.0);

        Guest[] guests = new Guest[5];
        double[] discountFactors = new double[] {0.6, 0.8, 1.0, 0.6, 0.8};
        GuestType[] guestTypes = new GuestType[] {GuestType.STUDENT, GuestType.STAFF, GuestType.GUEST, GuestType.STUDENT, GuestType.STAFF};
        String[] strings= new String[]{"Ahmad", "Skalda", "Arab", "Walika", "AhmadSkalda"};

        for (int i = 0; i < guests.length; i++) {
            guests[i] = mock(Guest.class);
            when(guests[i].getGuestType()).thenReturn(guestTypes[i]);
            when(guests[i].getDiscountFactor()).thenReturn(discountFactors[i]);
            when(guests[i].getName()).thenReturn(strings[i]);
        }

        List<Dish>[] dishCombinations = new List[]{
                Arrays.asList(mainDish, starter),
                Arrays.asList(dessert, other2),
                Arrays.asList(starter, dessert, other),
                Arrays.asList(mainDish, other),
                Arrays.asList(mainDish, starter, dessert)
        };

        double[] expectedPrices = new double[dishCombinations.length];
        for (int i = 0; i < dishCombinations.length; i++) {
            double sum = dishCombinations[i].stream().mapToDouble(Dish::getBasePrice).sum();
            sum *= guests[i].getDiscountFactor();
            if (dishCombinations[i].containsAll(Arrays.asList(mainDish, starter, dessert))) {
                sum *= 0.8;
            }
            expectedPrices[i] = sum;
        }

        for (int i = 0; i < dishCombinations.length; i++) {
            Order order = construct(guests[i], dishCombinations[i]);
            double actualPrice = order.calculatePrice();
            assertEquals(expectedPrices[i], actualPrice, 0.001,
                    String.format("Created order containing these dishes:%n%n%s%n%nGuestType was %s%nIncorrect price returned by calculatePrice(). Expected: %.2f, Actual: %.2f",
                            dishCombinations[i].stream().map(dish -> dish.getDishType() + ", " + String.format("%.1f€", dish.getBasePrice())).collect(Collectors.joining("\n")),
                            guests[i].getGuestType(),
                            expectedPrices[i],
                            actualPrice));
        }

    }


    @Test
    public void testEquals() {
        Guest guest1 = Mockito.mock(Guest.class);
        GuestType guestType1 = GuestType.GUEST;
        when(guest1.getGuestType()).thenReturn(guestType1);

        Guest guest2 = Mockito.mock(Guest.class);
        GuestType guestType2 = GuestType.STUDENT;
        when(guest2.getGuestType()).thenReturn(guestType2);

        Dish dish1 = Mockito.mock(Dish.class);
        Dish dish2 = Mockito.mock(Dish.class);
        Dish dish3 = Mockito.mock(Dish.class);
        when(dish1.getBasePrice()).thenReturn(10.0);
        when(dish2.getBasePrice()).thenReturn(15.0);
        when(dish3.getBasePrice()).thenReturn(20.0);

        Order order1 = construct(guest1, List.of(dish1, dish2));
        Order order2 = construct(guest1, List.of(dish1, dish2));
        assertEquals(order1, order2, "Two orders with identical guests and dishes should be equal");

        Order order3 = construct(guest1, List.of(dish2, dish1));
        assertEquals(order1, order3, "Two orders with identical dishes (which may be out of order...) should be equal");

        Order order4 = construct(guest2, List.of(dish1, dish2));
        assertNotEquals(order1, order4, "Two orders with different guests should not be equal");

        Order order5 = construct(guest1, List.of(dish1, dish3));
        assertNotEquals(order1, order5, "Two orders containing different dishes should not be equal");

        Order order6 = construct(guest2, List.of(dish3));
        assertNotEquals(order1, order6, "Two orders with different guests and dishes should not be equal");


        Order order7 = construct(guest2, List.of(dish2, dish3));
        assertNotEquals(order1, order7, "Two orders with different guests and dishes should not be equal");
    }

    @Test
    public void testHashCode() {
        Guest guest = Mockito.mock(Guest.class);
        Dish dish1 = Mockito.mock(Dish.class);
        Dish dish2 = Mockito.mock(Dish.class);
        Mockito.when(dish1.getName()).thenReturn("Pizza");
        Mockito.when(dish2.getName()).thenReturn("Soup");

        List<Dish> dishesOrdered1 = Arrays.asList(dish1, dish2);
        List<Dish> dishesOrdered2 = Arrays.asList(dish2, dish1);

        Order order1 = construct(guest, dishesOrdered1);
        Order order2 = construct(guest, dishesOrdered1);
        assertEquals(order1.hashCode(), order2.hashCode(), "Two Orders with identical guests and dishes should have the same hash code");

        Order order3 = construct(guest, dishesOrdered2);
        assertEquals(order1.hashCode(), order3.hashCode(), "Two Orders with identical dishes (which may be out of order...) should have the same hash code");

        for (int i = 0; i < 4; i++) {
            List<Dish> newOrderDishes = Arrays.asList(dish1, dish2);
            Order newOrder = construct(guest, newOrderDishes);
            assertEquals(order1.hashCode(), newOrder.hashCode(), "Two Orders with identical guests and dishes should have the same hash code on repetition " + (i+1));
        }
    }

    @Test
    public void testToString() {
        Dish mainDish = mock(Dish.class);
        when(mainDish.getDishType()).thenReturn(DishType.MAIN_DISH);
        when(mainDish.getName()).thenReturn("Pasta");
        when(mainDish.getBasePrice()).thenReturn(8.0);

        Dish starter = mock(Dish.class);
        when(starter.getName()).thenReturn("Burger");
        when(starter.getDishType()).thenReturn(DishType.STARTER);
        when(starter.getBasePrice()).thenReturn(5.0);

        Dish dessert = mock(Dish.class);
        when(dessert.getName()).thenReturn("Lasania");
        when(dessert.getDishType()).thenReturn(DishType.DESSERT);
        when(dessert.getBasePrice()).thenReturn(7.0);

        Dish other = mock(Dish.class);
        when(other.getName()).thenReturn("Gurken");
        when(other.getDishType()).thenReturn(DishType.OTHER);
        when(other.getBasePrice()).thenReturn(3.0);

        Dish other2 = mock(Dish.class);
        when(other2.getName()).thenReturn("Tomaten");
        when(other2.getDishType()).thenReturn(DishType.OTHER);
        when(other2.getBasePrice()).thenReturn(6.0);


        Guest guest1 = mock(Guest.class);
        when(guest1.getName()).thenReturn("Ahmad");
        when(guest1.getGuestType()).thenReturn(GuestType.STUDENT);

        Guest guest2 = mock(Guest.class);
        when(guest2.getName()).thenReturn("Ali");
        when(guest2.getGuestType()).thenReturn(GuestType.STAFF);

        Guest guest3 = mock(Guest.class);
        when(guest3.getName()).thenReturn("Skalda");
        when(guest3.getGuestType()).thenReturn(GuestType.GUEST);

        Guest guest4 = mock(Guest.class);
        when(guest4.getName()).thenReturn("Diana");
        when(guest4.getGuestType()).thenReturn(GuestType.STUDENT);

        Guest guest5 = mock(Guest.class);
        when(guest5.getName()).thenReturn("Arab");
        when(guest5.getGuestType()).thenReturn(GuestType.STAFF);

        Order order1 = construct(guest1, Arrays.asList(mainDish, dessert));
        Order order2 = construct(guest2, Arrays.asList(mainDish, dessert, other));
        Order order3 = construct(guest3, List.of(mainDish));
        Order order4 = construct(guest4, Arrays.asList(mainDish, dessert, starter));
        Order order5 = construct(guest5, Arrays.asList(mainDish, starter));

        assertEquals("Order:\n" +
                "\t- " + mainDish.getName() +"\n" +
                "\t- " + dessert.getName() +"\n" +
                "\n" +
                "\tName: " + guest1.getName( )+ " | Total price: " + order1.calculatePrice() + "€", order1.toString(), "Incorrect string representation returned by toString()");
        assertEquals("Order:\n" +
                "\t- " + mainDish.getName() +"\n" +
                "\t- " + dessert.getName() +"\n" +
                "\t- " + other.getName() +"\n" +
                "\n" +
                "\tName: " + guest2.getName( )+ " | Total price: " + order2.calculatePrice() + "€", order2.toString(), "Incorrect string representation returned by toString()");
        assertEquals("Order:\n" +
                "\t- " + mainDish.getName() +"\n" +
                "\n" +
                "\tName: " + guest3.getName( )+ " | Total price: " + order3.calculatePrice() + "€", order3.toString(), "Incorrect string representation returned by toString()");
        assertEquals("Order:\n" +
                "\t- " + mainDish.getName() +"\n" +
                "\t- " + dessert.getName() +"\n" +
                "\t- " + starter.getName() +"\n" +
                "\n" +
                "\tName: " + guest4.getName( )+ " | Total price: " + order4.calculatePrice() + "€", order4.toString(), "Incorrect string representation returned by toString()");
        assertEquals("Order:\n" +
                "\t- " + mainDish.getName() +"\n" +
                "\t- " + starter.getName() +"\n" +
                "\n" +
                "\tName: " + guest5.getName( )+ " | Total price: " + order5.calculatePrice() + "€", order5.toString(), "Incorrect string representation returned by toString()");
    }

}
