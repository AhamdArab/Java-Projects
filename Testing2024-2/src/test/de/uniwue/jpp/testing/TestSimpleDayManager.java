package de.uniwue.jpp.testing;

import de.uniwue.jpp.testing.enums.DishType;
import de.uniwue.jpp.testing.enums.GuestType;
import de.uniwue.jpp.testing.interfaces.DayManager;
import de.uniwue.jpp.testing.interfaces.Dish;
import de.uniwue.jpp.testing.interfaces.Guest;
import de.uniwue.jpp.testing.interfaces.Order;
import de.uniwue.jpp.testing.util.AbstractTestClass;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static de.uniwue.jpp.testing.util.TestUtils.assertListIsUnmodifiable;
import static de.uniwue.jpp.testing.util.TestUtils.assertThrowsWithMessage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestSimpleDayManager extends AbstractTestClass<DayManager> {

    @Test
    public void testConstructorValidArguments() {
        assertDoesNotThrow(() -> construct(LocalDate.now(), 5, 10), "Testing valid arguments for DayManager constructor");
        assertDoesNotThrow(() -> construct(LocalDate.now().plusDays(1), 3, 15), "Testing valid arguments for DayManager constructor");
        assertDoesNotThrow(() -> construct(LocalDate.now().plusDays(2), 4, 20), "Testing valid arguments for DayManager constructor");
        assertDoesNotThrow(() -> construct(LocalDate.now().plusDays(3), 6, 5), "Testing valid arguments for DayManager constructor");
        assertDoesNotThrow(() -> construct(LocalDate.now().plusDays(4), 2, 25), "Testing valid arguments for DayManager constructor");

    }

    @Test
    public void testConstructorInvalidArguments() {
        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> construct(null, 3, 10),
                "Date must not be null!",
                "Testing invalid date argument for DayManager constructor"
        );

        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> construct(LocalDate.now().minusDays(1), 3, 10),
                "Date must not be before current date!",
                "Testing invalid date argument for DayManager constructor"
        );

        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> construct(LocalDate.now().plusDays(1), 0, 10),
                "NumberOfTimeSlots must not be zero or negative!",
                "Testing invalid numberOfTimeSlots argument for DayManager constructor"
        );

        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> construct(LocalDate.now().plusDays(1), -1, 10),
                "NumberOfTimeSlots must not be zero or negative!",
                "Testing invalid numberOfTimeSlots argument for DayManager constructor"
        );

        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> construct(LocalDate.now().plusDays(1), 3, -1),
                "CapacityPerSlot must not be zero or negative!",
                "Testing invalid capacityPerSlot argument for DayManager constructor"
        );

        assertThrowsWithMessage(
                IllegalArgumentException.class,
                () -> construct(LocalDate.now().plusDays(1), 3, 0),
                "CapacityPerSlot must not be zero or negative!",
                "Testing invalid capacityPerSlot argument for DayManager constructor"
        );
    }

    @Test
    public void testAddOrderInvalidArguments() {
        DayManager manager = construct(LocalDate.of(2024, 4, 1), 5, 10);
        Guest guest = mock(Guest.class);
        when(guest.getName()).thenReturn("Ahmad");
        when(guest.getGuestType()).thenReturn(GuestType.STUDENT);
        Dish dish1 = mock(Dish.class);
        when(dish1.getName()).thenReturn("Spaghetti Bolognese");
        when(dish1.getBasePrice()).thenReturn(7.5);
        when(dish1.getDishType()).thenReturn(DishType.MAIN_DISH);
        Dish dish2 = mock(Dish.class);
        when(dish2.getName()).thenReturn("Vanilla Ice Cream");
        when(dish2.getBasePrice()).thenReturn(3.0);
        when(dish2.getDishType()).thenReturn(DishType.DESSERT);
        List<Dish> dishes = Arrays.asList(dish1, dish2);
        Order validOrder = mock(Order.class);
        when(validOrder.getGuest()).thenReturn(guest);
        when(validOrder.getDishes()).thenReturn(dishes);



        assertThrowsWithMessage(IllegalArgumentException.class, () -> manager.addOrder(null, 1),
                "Order must not be null!", "Testing invalid order argument for addOrder()");

        assertThrowsWithMessage(IllegalArgumentException.class, () -> manager.addOrder(null),
                "Order must not be null!", "Testing invalid order argument for addOrder()");

        assertThrowsWithMessage(IllegalArgumentException.class, () -> manager.addOrder(validOrder, -1),
                "Illegal slot index!", "Testing invalid slot argument for addOrder()");

        assertThrowsWithMessage(IllegalArgumentException.class, () -> manager.addOrder(validOrder, 5),
                "Illegal slot index!", "Testing invalid slot argument for addOrder()");

        assertThrowsWithMessage(IllegalArgumentException.class, () -> manager.addOrder(validOrder, 6),
                "Illegal slot index!", "Testing invalid slot argument for addOrder()");

        manager.addOrder(validOrder, 1);
        assertThrowsWithMessage(IllegalArgumentException.class, () -> manager.addOrder(validOrder, 1), "Illegal order: This guest has already ordered!", "Testing invalid order argument for addOrder()");


    }


    @Test
    public void testAddOrderWithSlotArgument() {
        int[][] scenarios = {{1, 1}, {1, 5}, {3, 2}, {5, 4}};
        for (int[] scenario : scenarios) {
            DayManager dayManager = construct(LocalDate.now(), scenario[0], scenario[1]);
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Executing addOrder sequence on a DayManager with ")
                    .append(scenario[0]).append(" time slots and ")
                    .append(scenario[1]).append(" capacity per slot:\n");
            Map<Integer, List<Order>> slotOrders = new HashMap<>();
            for (int i = 0; i < scenario[0]; i++) {
                slotOrders.put(i, new ArrayList<>());
            }
            List<Order> orders = new ArrayList<>();
            for (int i = 0; i < scenario[0] * scenario[1]; i++) {
                Order order = mock(Order.class);
                orders.add(order);
            }
            for (int i = 0; i < scenario[0] * scenario[1]; i++) {
                int preferredSlot = i < scenario[0] ? i : scenario[0] / 2;
                Optional<Integer> allocatedSlot = dayManager.addOrder(orders.get(i), preferredSlot);
                errorMessage.append("adding Order with preferred slot ").append(preferredSlot).append("\n");
                int actualSlot = 0;
                if (slotOrders.get(preferredSlot).size() < scenario[1]) {
                    slotOrders.get(preferredSlot).add(orders.get(i));
                    actualSlot = preferredSlot;
                } else {
                    for (int offset = 1; offset < scenario[0]; offset++) {
                        if (preferredSlot - offset >= 0 && slotOrders.get(preferredSlot - offset).size() < scenario[1]) {
                            slotOrders.get(preferredSlot - offset).add(orders.get(i));
                            actualSlot = preferredSlot - offset;
                            break;
                        }
                        if (preferredSlot + offset < scenario[0] && slotOrders.get(preferredSlot + offset).size() < scenario[1]) {
                            slotOrders.get(preferredSlot + offset).add(orders.get(i));
                            actualSlot = preferredSlot + offset;
                            break;
                        }
                    }
                }if (allocatedSlot.isPresent()){
                assertFalse(actualSlot != allocatedSlot.get(), errorMessage + "Wrong return value");
                }
            }
            errorMessage.append("adding Order\n");
            assertFalse(dayManager.addOrder(mock(Order.class)).isPresent(), errorMessage + "Wrong return value");
        }
    }


    @Test
    public void testAddOrderWithoutSlotArgument() {
        int[][] scenarios = {{1, 1}, {3, 2}};
        for (int[] scenario : scenarios) {
            DayManager dayManager = construct(LocalDate.of(2024, 4, 1), scenario[0], scenario[1]);
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Executing addOrder sequence on a DayManager with ")
                    .append(scenario[0]).append(" time slots and ")
                    .append(scenario[1]).append(" capacity per slot:\n");
            for (int i = 0; i < scenario[0] * scenario[1]; i++) {
                Order mockOrder = mock(Order.class);
                Optional<Integer> optionalSlot = dayManager.addOrder(mockOrder);
                errorMessage.append("adding Order\n");
                assertTrue(optionalSlot.isPresent(), errorMessage + "Wrong return value");
                int slot = optionalSlot.get();
                assertTrue(slot >= 0 && slot < scenario[0], errorMessage + "Wrong return value");
            }
            Order mockOrder = mock(Order.class);
            Optional<Integer> optionalSlot = dayManager.addOrder(mockOrder);
            errorMessage.append("adding Order\n");
            assertFalse(optionalSlot.isPresent(), errorMessage + "Wrong return value");
        }
    }




    @Test
    public void testGetOrdersForSlot() {
        int[][] scenarios = {{1, 1}, {1, 5}, {3, 2}, {5, 4}, {1, 1}, {3, 2}};
        for (int[] scenario : scenarios) {
            DayManager dayManager = construct(LocalDate.now(), scenario[0], scenario[1]);
            StringBuilder errorMessage = new StringBuilder("Executing addOrder sequence on a DayManager with " + scenario[0] +
                    " time slots and " + scenario[1] + " capacity per slot:\n");
            HashMap<Integer, List<Order>> slotOrders = new HashMap<>();
            for (int i = 0; i < scenario[0]; i++) {
                slotOrders.put(i, new ArrayList<>());
            }
            List<Order> orders = new ArrayList<>();
            for (int i = 0; i < scenario[0] * scenario[1]; i++) {
                Order order = mock(Order.class);
                orders.add(order);
            }
            for (int i = 0; i < scenario[0] * scenario[1]; i++) {
                int preferredSlot = 0;
                dayManager.addOrder(orders.get(i), preferredSlot);
                errorMessage.append("adding Order with preferred slot ").append(preferredSlot).append("\n");
                if (slotOrders.get(preferredSlot).size() < scenario[1]) {
                    slotOrders.get(preferredSlot).add(orders.get(i));
                } else {
                    for (int j = 1; j < scenario[0]; j++) {
                        if (slotOrders.get(preferredSlot + j).size() < scenario[1]) {
                            slotOrders.get(preferredSlot + j).add(orders.get(i));
                            break;
                        }
                    }
                }for (int j = 0; j < scenario[0]; j++) {
                    assertFalse(dayManager.getOrdersForSlot(j) == null || !(dayManager.getOrdersForSlot(j)
                                    .containsAll(slotOrders.get(j))),
                            errorMessage + "Incorrect list returned by getOrdersForSlot() for slot " + j);
                    assertListIsUnmodifiable(dayManager.getOrdersForSlot(j), "List returned by getOrdersForSlot() is modifiable");
                }
            }
            errorMessage.append("adding Order\n");
            assertFalse(dayManager.addOrder(mock(Order.class)).isPresent(), errorMessage + "Wrong return value");
        }
    }


    @Test
    public void testGetAllOrders() {
        int[][] scenarios = {{1, 1}, {1, 5}, {3, 2}, {5, 4}, {1, 1}, {3, 2}};
        for (int[] scenario: scenarios){
            DayManager dayManager = construct(LocalDate.now(), scenario[0], scenario[1]);
            StringBuilder errorMessage = new StringBuilder("Executing addOrder sequence on a DayManager with "
                    + scenario[0] + " time slots and " + scenario[1] + " capacity per slot:\n");
            HashMap<Integer,List<Order>> listHashMap = new HashMap<>();
            for (int i = 0; i < scenario[0]; i++) {
                listHashMap.put(i, new ArrayList<>());
            }
            List<Order> orders = new ArrayList<>();
            for (int i = 0; i < scenario[0] * scenario[1]; i++) {
                Order order = mock(Order.class);
                orders.add(order);
            }
            for (int i = 0; i < scenario[0] * scenario[1]; i++){
                int preferredSlot = i < scenario[0] ? i : scenario[0] / 2;
                dayManager.addOrder(orders.get(i),preferredSlot);
                errorMessage.append("adding Order with preferred slot ").append(preferredSlot).append("\n");
                if (listHashMap.get(preferredSlot).size() < scenario[1]){
                    listHashMap.get(preferredSlot).add(orders.get(i));
                }else {
                    for (int j = 1; j < scenario[0]; j++) {
                        if (preferredSlot - j >= 0 && listHashMap.get(preferredSlot - j).size() < scenario[1]){
                            listHashMap.get(preferredSlot - j).add(orders.get(i));
                            break;
                        }
                        if (preferredSlot + j < scenario[0] && listHashMap.get(preferredSlot + j).size() < scenario[1]){
                            listHashMap.get(preferredSlot + j).add(orders.get(i));
                            break;
                        }
                    }
                }
            }
            List<Order> expectedOrders = new ArrayList<>();
            for (List<Order> i : listHashMap.values()){
                expectedOrders.addAll(i);
            }
            assertTrue(dayManager.getAllOrders().containsAll(expectedOrders), errorMessage + "Incorrect list returned by getAllOrders()");
            assertListIsUnmodifiable(dayManager.getAllOrders(),"List returned by getAllOrders() is modifiable");
            errorMessage.append("adding Order\n");
            assertFalse(dayManager.addOrder(mock(Order.class)).isPresent(), errorMessage + "Wrong return value");
        }
    }


    @Test
    public void testCalculateEarnings() {
        LocalDate testDate = LocalDate.now().plusDays(1);
        DayManager dayManager = construct(testDate, 5, 10);
        assertEquals(0, dayManager.calculateEarnings(), 0.001, "Initial earnings should be zero.");

        double[] prices = {10.0, 20.0, 30.0, 40.0, 50.0};
        double totalExpected = 0;
        for (int i = 0; i < prices.length; i++) {
            Order mockOrder = mock(Order.class);
            Mockito.when(mockOrder.calculatePrice()).thenReturn(prices[i]);
            dayManager.addOrder(mockOrder);
            totalExpected += prices[i];
            assertEquals(totalExpected, dayManager.calculateEarnings(), 0.001, "Earnings after adding order " + (i + 1) + " should be correct.");
        }
    }

    @Test
    public void testToString() {
        LocalDate[] testDates = {
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 12, 31),
                LocalDate.of(2024, 2, 29),
                LocalDate.of(2024, 7, 4),
                LocalDate.of(2025, 10, 15)
        };

        for (LocalDate testDate : testDates) {
            DayManager dayManager = construct(testDate, 3, 10);
            String expectedOutput = String.format("SimpleDayManager (%s)", testDate.format(DateTimeFormatter.ofPattern("dd.MM.yy")));
            assertEquals(expectedOutput, dayManager.toString(), "Incorrect string representation returned by toString()");
        }

    }
}
