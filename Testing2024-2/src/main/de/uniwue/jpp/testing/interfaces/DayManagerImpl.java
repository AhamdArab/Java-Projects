package de.uniwue.jpp.testing.interfaces;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DayManagerImpl implements DayManager{
    LocalDate date;
    int numberOfTimeSlots;
    int capacityPerSlot;
    Map<Integer, List<Order>> slotOrders;
    Set<String> orderedGuests;

    public DayManagerImpl(LocalDate date, int numberOfTimeSlots, int capacityPerSlot) {
        if (date == null) {
            throw new IllegalArgumentException("Date must not be null!");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date must not be before current date!");
        }
        if (numberOfTimeSlots <= 0) {
            throw new IllegalArgumentException("NumberOfTimeSlots must not be zero or negative!");
        }
        if (capacityPerSlot <= 0) {
            throw new IllegalArgumentException("CapacityPerSlot must not be zero or negative!");
        }

        this.date = date;
        this.numberOfTimeSlots = numberOfTimeSlots;
        this.capacityPerSlot = capacityPerSlot;
        this.slotOrders = new HashMap<>();
        this.orderedGuests = new HashSet<>();
    }

    @Override
    public Optional<Integer> addOrder(Order order, int preferredSlot) {
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null!");
        }
        if (orderedGuests.contains(order.getGuest().getName())) {
            throw new IllegalArgumentException("Illegal order: This guest has already ordered!");
        }
        if (preferredSlot < 0 || preferredSlot > numberOfTimeSlots) {
            throw new IllegalArgumentException("Illegal slot index!");
        }

        List<Order> preferredSlotOrders = slotOrders.getOrDefault(preferredSlot, new ArrayList<>());
        if (preferredSlotOrders.size() < capacityPerSlot) {
            preferredSlotOrders.add(order);
            slotOrders.put(preferredSlot, preferredSlotOrders);
            orderedGuests.add(order.getGuest().getName());
            return Optional.of(preferredSlot);
        } else {
            for (int offset = 1; offset < numberOfTimeSlots; offset++) {
                int lowerSlot = preferredSlot - offset;
                int higherSlot = preferredSlot + offset;

                if (lowerSlot >= 0) {
                    List<Order> lowerSlotOrders = slotOrders.getOrDefault(lowerSlot, new ArrayList<>());
                    if (lowerSlotOrders.size() < capacityPerSlot) {
                        lowerSlotOrders.add(order);
                        slotOrders.put(lowerSlot, lowerSlotOrders);
                        orderedGuests.add(order.getGuest().getName());
                        return Optional.of(lowerSlot);
                    }
                }
                if (higherSlot < numberOfTimeSlots) {
                    List<Order> higherSlotOrders = slotOrders.getOrDefault(higherSlot, new ArrayList<>());
                    if (higherSlotOrders.size() < capacityPerSlot) {
                        higherSlotOrders.add(order);
                        slotOrders.put(higherSlot, higherSlotOrders);
                        orderedGuests.add(order.getGuest().getName());
                        return Optional.of(higherSlot);
                    }
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null!");
        }
        if (orderedGuests.contains(order.getGuest().getName())) {
            throw new IllegalArgumentException("Illegal order: This guest has already ordered!");
        }
        for (int slot = 0; slot < numberOfTimeSlots; slot++) {
            List<Order> slotOrdersList = slotOrders.getOrDefault(slot, new ArrayList<>());
            if (slotOrdersList.size() < capacityPerSlot) {
                slotOrdersList.add(order);
                slotOrders.put(slot, slotOrdersList);
                orderedGuests.add(order.getGuest().getName());
                return Optional.of(slot);
            }
        }
        return Optional.empty();
    }


    @Override
    public List<Order> getOrdersForSlot(int slot) {
        if (slot < 0 || slot >= numberOfTimeSlots) {
            throw new IllegalArgumentException("Illegal slot index!");
        }
        if (slotOrders.containsKey(slot))
            return Collections.unmodifiableList(slotOrders.get(slot));

        return Collections.emptyList();
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        for (List<Order> order: slotOrders.values()) {
            orders.addAll(order);

        }
        return Collections.unmodifiableList(orders);
    }

    @Override
    public double calculateEarnings() {
        return getAllOrders().stream().mapToDouble(Order::calculatePrice).sum();
    }

    @Override
    public String toString() {
        return String.format("SimpleDayManager (%s)", date.format(DateTimeFormatter.ofPattern("dd.MM.yy")));
    }

}
