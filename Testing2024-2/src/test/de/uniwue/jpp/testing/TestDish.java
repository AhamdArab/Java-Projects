package test.de.uniwue.jpp.testing;

import de.uniwue.jpp.testing.enums.DishType;
import de.uniwue.jpp.testing.interfaces.Dish;
import de.uniwue.jpp.testing.util.AbstractTestClass;
import org.junit.jupiter.api.Test;

import static de.uniwue.jpp.testing.util.TestUtils.assertThrowsWithMessage;
import static org.junit.jupiter.api.Assertions.*;

public class TestDish extends AbstractTestClass<Dish> {

    @Test
    public void testConstructorValidArguments() {
        try {
            construct("Pizza", 3.0, DishType.OTHER);
            construct("Salat", 4.0, DishType.DESSERT);
            construct("Burger", 6.0, DishType.MAIN_DISH);
            construct("Apfel", 5.0, DishType.STARTER);
            construct("Tomate", 77.0, DishType.OTHER);
            construct("Oliven", 65.0, DishType.MAIN_DISH);
            construct("Mais", 34.0, DishType.STARTER);
            construct("Schoko", 31.0, DishType.OTHER);
            construct("Soup", 23.0, DishType.STARTER);

        }catch (Exception exception){
            throw new AssertionError("Testing valid arguments for Dish constructor");
        }
    }


        @Test
    public void testConstructorInvalidArguments() {
        assertThrowsWithMessage(IllegalArgumentException.class,
                () -> construct(null, 10.0, DishType.STARTER),
                "Name must not be null!",
                "Testing invalid name argument for Dish constructor");

        assertThrowsWithMessage(IllegalArgumentException.class,
                () -> construct("", 10.0, DishType.MAIN_DISH),
                "Name must not be empty!",
                "Testing invalid name argument for Dish constructor");

        assertThrowsWithMessage(IllegalArgumentException.class,
                () -> construct("Ahmad", -1.0, DishType.DESSERT),
                "BasePrice must not be zero or negative!",
                "Testing invalid basePrice argument for Dish constructor");

        assertThrowsWithMessage(IllegalArgumentException.class,
                () -> construct("Skalda", 0.0, DishType.MAIN_DISH),
                "BasePrice must not be zero or negative!",
                "Testing invalid basePrice argument for Dish constructor");

        assertThrowsWithMessage(IllegalArgumentException.class,
                () -> construct("ArabWalika", 10.0, null),
                "DishType must not be null!",
                "Testing invalid dishType argument for Dish constructor");
    }

    @Test
    public void testGetName(){
        String[] names = {"Bruschetta", "Ravioli", "Tiramisu", "Minestrone", "Cheese Platter", "Beef Burger", "Sushi Platter", "Chocolate Mousse", "Fruit Salad"};
        double[] prices = {13.5, 11.0, 7.5, 4.5, 10.5, 14.0, 12.5, 22.0, 5.5};
        DishType[] types = {DishType.MAIN_DISH, DishType.MAIN_DISH, DishType.STARTER, DishType.STARTER, DishType.MAIN_DISH, DishType.MAIN_DISH, DishType.MAIN_DISH, DishType.MAIN_DISH, DishType.DESSERT};

        for (int i = 0; i < names.length; i++) {

            Dish dish = construct(names[i], prices[i], types[i]);

            assertEquals(names[i], dish.getName(), "Incorrect name returned by getName() for " + names[i]);
        }
    }

    @Test
    public void testGetBasePrice(){
        String[] names = {"Bruschetta", "Ravioli", "Tiramisu", "Minestrone", "Cheese Platter", "Beef Burger", "Sushi Platter", "Chocolate Mousse", "Fruit Salad"};
        double[] prices = {13.5, 11.0, 7.5, 4.5, 10.5, 14.0, 12.5, 22.0, 5.5};
        DishType[] types = {DishType.MAIN_DISH, DishType.MAIN_DISH, DishType.STARTER, DishType.STARTER, DishType.MAIN_DISH, DishType.MAIN_DISH, DishType.MAIN_DISH, DishType.MAIN_DISH, DishType.DESSERT};

        for (int i = 0; i < names.length; i++) {

            Dish dish = construct(names[i], prices[i], types[i]);
            assertEquals(prices[i], dish.getBasePrice(), 0.01, "Incorrect basePrice returned by getBasePrice() for " + names[i]);
        }
    }

    @Test
    public void testGetDishType(){
        String[] names = {"Bruschetta", "Ravioli", "Tiramisu", "Minestrone", "Cheese Platter", "Beef Burger", "Sushi Platter", "Chocolate Mousse", "Fruit Salad"};
        DishType[] types = {DishType.STARTER, DishType.MAIN_DISH, DishType.DESSERT, DishType.STARTER, DishType.OTHER, DishType.MAIN_DISH, DishType.MAIN_DISH, DishType.DESSERT, DishType.OTHER};
        double price = 10.0;

        for (int i = 0; i < names.length; i++) {
            Dish dish = construct(names[i], price, types[i]);

            assertEquals(types[i], dish.getDishType(), "Incorrect dishType returned by getDishType() for " + names[i]);
        }
    }

    @Test
    public void testEquals(){
        Dish dish1 = construct("Pizza", 10.0, DishType.MAIN_DISH);
        Dish dish2 = construct("Pizza", 10.0, DishType.MAIN_DISH);
        Dish dish3 = construct("Pizza", 9.0, DishType.STARTER);
        Dish dish4 = construct("Pasta", 10.0, DishType.MAIN_DISH);
        Dish dish5 = construct("Salad", 5.0, DishType.STARTER);
        Dish dish6 = construct("Soup", 3.5, DishType.STARTER);
        Dish dish7 = construct("Ice Cream", 2.0, DishType.DESSERT);
        Dish dish8 = construct("Steak", 15.0, DishType.MAIN_DISH);
        Dish dish9 = construct("Fish", 12.0, DishType.MAIN_DISH);
        Dish dish10 = construct("Cheese Plate", 8.0, DishType.DESSERT);
        Dish dish11 = construct("Bread", 1.5, DishType.OTHER);
        Dish dish12 = construct("Fruit Salad", 6.0, DishType.DESSERT);

        assertEquals(dish1, dish2, "Two dishes with identical names should be equal");
        assertEquals(dish1, dish3, "Two dishes with identical names should be equal");


        assertNotEquals(dish1, dish4, "Two dishes with different names should not be equal");
        assertNotEquals(dish2, dish5, "Two dishes with different names should not be equal");

    }

    @Test
    public void testHashCode() {

        Dish[] dishes = {
                construct("Pizza", 12.0, DishType.MAIN_DISH),
                construct("Pizza", 1.0, DishType.STARTER),
                construct("Salad", 10.0, DishType.STARTER),
                construct("Salad", 11.0, DishType.OTHER),
                construct("Cake", 6.0, DishType.DESSERT),
                construct("Cake", 7.0, DishType.MAIN_DISH),
                construct("Soup", 8.0, DishType.STARTER),
                construct("Soup", 2.0, DishType.DESSERT),
                construct("Steak", 20.0, DishType.MAIN_DISH),
                construct("Steak", 21.0, DishType.DESSERT),
                construct("Ice Cream", 5.0, DishType.DESSERT),
                construct("Ice Cream", 3.0, DishType.OTHER),
                construct("Sushi", 15.0, DishType.MAIN_DISH),
                construct("Sushi", 14.0, DishType.OTHER),
                construct("Coffee", 9.0, DishType.OTHER),
                construct("Coffee", 4.0, DishType.DESSERT)
        };

        for (int i = 0; i < dishes.length; i += 2) {
            assertEquals(dishes[i].hashCode(), dishes[i+1].hashCode(), "Two dishes with identical names should have the same hash code");
        }
    }

    @Test
    public void testToString(){
        Dish dish1 = construct("Pizza", 10.0, DishType.MAIN_DISH);
        String expected1 = "Pizza | Baseprice: 10.0€ | Type: MAIN_DISH";
        assertEquals(expected1, dish1.toString(), "Incorrect string representation returned by toString() for Pizza");

        Dish dish2 = construct("Salad", 5.0, DishType.STARTER);
        String expected2 = "Salad | Baseprice: 5.0€ | Type: STARTER";
        assertEquals(expected2, dish2.toString(), "Incorrect string representation returned by toString() for Salad");

        Dish dish3 = construct("Ice Cream", 2.0, DishType.DESSERT);
        String expected3 = "Ice Cream | Baseprice: 2.0€ | Type: DESSERT";
        assertEquals(expected3, dish3.toString(), "Incorrect string representation returned by toString() for Ice Cream");

        Dish dish4 = construct("Bread", 1.5, DishType.OTHER);
        String expected4 = "Bread | Baseprice: 1.5€ | Type: OTHER";
        assertEquals(expected4, dish4.toString(), "Incorrect string representation returned by toString() for Bread");

        Dish dish5 = construct("Steak", 20.0, DishType.MAIN_DISH);
        String expected5 = "Steak | Baseprice: 20.0€ | Type: MAIN_DISH";
        assertEquals(expected5, dish5.toString(), "Incorrect string representation returned by toString() for Steak");

        Dish dish6 = construct("Soup", 7.5, DishType.STARTER);
        String expected6 = "Soup | Baseprice: 7.5€ | Type: STARTER";
        assertEquals(expected6, dish6.toString(), "Incorrect string representation returned by toString() for Soup");

        Dish dish7 = construct("Cheesecake", 5.5, DishType.DESSERT);
        String expected7 = "Cheesecake | Baseprice: 5.5€ | Type: DESSERT";
        assertEquals(expected7, dish7.toString(), "Incorrect string representation returned by toString() for Cheesecake");

        Dish dish8 = construct("French Fries", 3.0, DishType.OTHER);
        String expected8 = "French Fries | Baseprice: 3.0€ | Type: OTHER";
        assertEquals(expected8, dish8.toString(), "Incorrect string representation returned by toString() for French Fries");

        Dish dish9 = construct("Risotto", 12.0, DishType.MAIN_DISH);
        String expected9 = "Risotto | Baseprice: 12.0€ | Type: MAIN_DISH";
        assertEquals(expected9, dish9.toString(), "Incorrect string representation returned by toString() for Risotto");
    }

}
