package de.uniwue.jpp.testing.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DayManager {

    public Optional<Integer> addOrder(Order order, int preferredSlot);

    public Optional<Integer> addOrder(Order order);

    public List<Order> getOrdersForSlot(int slot);

    public List<Order> getAllOrders();

    public double calculateEarnings();

    public static DayManager createSimpleDayManager(LocalDate date, int numberOfTimeSlots, int capacityPerSlot){

        return new DayManagerImpl(date, numberOfTimeSlots, capacityPerSlot);
    }
}
