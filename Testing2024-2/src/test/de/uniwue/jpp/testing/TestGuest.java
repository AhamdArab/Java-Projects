package de.uniwue.jpp.testing;

import de.uniwue.jpp.testing.enums.GuestType;
import de.uniwue.jpp.testing.interfaces.Guest;
import de.uniwue.jpp.testing.util.AbstractTestClass;
import org.junit.jupiter.api.Test;

import static de.uniwue.jpp.testing.util.TestUtils.assertThrowsWithMessage;
import static org.junit.jupiter.api.Assertions.*;

public class TestGuest extends AbstractTestClass<Guest> {

    @Test
    public void testConstructorValidArguments() {
        try {
            construct("Ali", GuestType.STUDENT);
            construct("Bo", GuestType.STAFF);
            construct("lie", GuestType.GUEST);
            construct("Dina", GuestType.STUDENT);
            construct("Heva", GuestType.STAFF);

        } catch (Exception e) {
            fail("Testing valid arguments for Guest constructor");
        }
    }

    @Test
    public void testConstructorInvalidArguments() {
        assertThrowsWithMessage(IllegalArgumentException.class,
                () -> construct(null, GuestType.STUDENT),
                "Name must not be null!",
                "Testing invalid name argument for Guest constructor");

        assertThrowsWithMessage(IllegalArgumentException.class,
                () -> construct("", GuestType.GUEST),
                "Name must not be empty!",
                "Testing invalid name argument for Guest constructor");

        assertThrowsWithMessage(IllegalArgumentException.class,
                () -> construct("Ahmad", null),
                "GuestType must not be null!",
                "Testing invalid guestType argument for Guest constructor");
    }

    @Test
    public void testGetName() {
        Guest guest1 = construct("Alice", GuestType.STUDENT);
        Guest guest2 = construct("Bob", GuestType.STAFF);
        Guest guest3 = construct("Charlie", GuestType.GUEST);
        Guest guest4 = construct("Dana", GuestType.STUDENT);
        Guest guest5 = construct("Eve", GuestType.STAFF);

        assertEquals("Alice", guest1.getName(), "Incorrect name returned by getName() for Alice");
        assertEquals("Bob", guest2.getName(), "Incorrect name returned by getName() for Bob");
        assertEquals("Charlie", guest3.getName(), "Incorrect name returned by getName() for Charlie");
        assertEquals("Dana", guest4.getName(), "Incorrect name returned by getName() for Dana");
        assertEquals("Eve", guest5.getName(), "Incorrect name returned by getName() for Eve");

    }

    @Test
    public void testGetGuestType() {
        Guest guest1 = construct("Alice", GuestType.STUDENT);
        Guest guest2 = construct("Bob", GuestType.STAFF);
        Guest guest3 = construct("Charlie", GuestType.GUEST);
        Guest guest4 = construct("Dana", GuestType.STUDENT);
        Guest guest5 = construct("Eve", GuestType.STAFF);

        assertEquals(GuestType.STUDENT, guest1.getGuestType(), "Incorrect guestType returned by getGuestType() for Alice");
        assertEquals(GuestType.STAFF, guest2.getGuestType(), "Incorrect guestType returned by getGuestType() for Bob");
        assertEquals(GuestType.GUEST, guest3.getGuestType(), "Incorrect guestType returned by getGuestType() for Charlie");
        assertEquals(GuestType.STUDENT, guest4.getGuestType(), "Incorrect guestType returned by getGuestType() for Dana");
        assertEquals(GuestType.STAFF, guest5.getGuestType(), "Incorrect guestType returned by getGuestType() for Eve");
    }

    @Test
    public void testEquals() {
        Guest guest1 = construct("John", GuestType.STUDENT);
        Guest guest2 = construct("John", GuestType.STAFF);
        Guest guest3 = construct("Alice", GuestType.STUDENT);
        Guest guest4 = construct("Bob", GuestType.GUEST);
        Guest guest5 = construct("Bob", GuestType.STUDENT);
        Guest guest6 = construct("David", GuestType.GUEST);
        Guest guest7 = construct("Laura", GuestType.STUDENT);
        Guest guest8 = construct("Laura", GuestType.STAFF);
        Guest guest9 = construct("Emma", GuestType.GUEST);
        Guest guest10 = construct("Emma", GuestType.STUDENT);
        Guest guest11 = construct("William", GuestType.STAFF);
        Guest guest12 = construct("Olivia", GuestType.GUEST);

        assertEquals(guest1, guest2, "Two guests with identical names should be equal");
        assertEquals(guest4, guest5, "Two guests with identical names should be equal");
        assertNotEquals(guest1, guest3, "Two guests with different names should not be equal");
        assertNotEquals(guest2, guest6, "Two guests with different names should not be equal");
        assertEquals(guest7, guest8, "Two guests with identical names should be equal");
        assertEquals(guest9, guest10, "Two guests with identical names should be equal");
        assertNotEquals(guest3, guest11, "Two guests with different names should not be equal");
        assertNotEquals(guest5, guest12, "Two guests with different names should not be equal");
    }



    @Test
    public void testHashCode() {
        Guest guest1 = construct("Alice", GuestType.STUDENT);
        Guest guest2 = construct("Alice", GuestType.STAFF);
        Guest guest3 = construct("Bob", GuestType.GUEST);
        Guest guest4 = construct("Bob", GuestType.STUDENT);
        Guest guest5 = construct("Charlie", GuestType.STAFF);
        Guest guest6 = construct("Charlie", GuestType.GUEST);
        Guest guest7 = construct("Dana", GuestType.STUDENT);
        Guest guest8 = construct("Dana", GuestType.STAFF);
        Guest guest9 = construct("Eve", GuestType.GUEST);
        Guest guest10 = construct("Eve", GuestType.STUDENT);


        assertEquals(guest1.hashCode(), guest2.hashCode(), "Two guests with identical names should have the same hash code");
        assertEquals(guest3.hashCode(), guest4.hashCode(), "Two guests with identical names should have the same hash code");
        assertEquals(guest5.hashCode(), guest6.hashCode(), "Two guests with identical names should have the same hash code");
        assertEquals(guest7.hashCode(), guest8.hashCode(), "Two guests with identical names should have the same hash code");
        assertEquals(guest9.hashCode(), guest10.hashCode(), "Two guests with identical names should have the same hash code");
    }

    @Test
    public void testToString() {
        Guest guest1 = construct("Alice", GuestType.STUDENT);
        Guest guest2 = construct("Bob", GuestType.STAFF);
        Guest guest3 = construct("Charlie", GuestType.GUEST);
        Guest guest4 = construct("Dana", GuestType.STUDENT);
        Guest guest5 = construct("Eve", GuestType.STAFF);
        Guest guest6 = construct("Frank", GuestType.GUEST);
        Guest guest7 = construct("Gina", GuestType.STUDENT);
        Guest guest8 = construct("Harry", GuestType.STAFF);
        Guest guest9 = construct("Ivy", GuestType.GUEST);
        Guest guest10 = construct("Jack", GuestType.STUDENT);
        Guest guest11 = construct("Kathy", GuestType.STAFF);
        Guest guest12 = construct("Liam", GuestType.GUEST);

        assertEquals(guest1.getName() + " (STUDENT)", guest1.toString(), "Incorrect string representation returned by toString()");
        assertEquals(guest2.getName() + " (STAFF)", guest2.toString(), "Incorrect string representation returned by toString()");
        assertEquals(guest3.getName() + " (GUEST)", guest3.toString(), "Incorrect string representation returned by toString()");
        assertEquals(guest4.getName() + " (STUDENT)", guest4.toString(), "Incorrect string representation returned by toString()");
        assertEquals(guest5.getName() + " (STAFF)", guest5.toString(), "Incorrect string representation returned by toString()");
        assertEquals(guest6.getName() + " (GUEST)", guest6.toString(), "Incorrect string representation returned by toString()");
        assertEquals(guest7.getName() + " (STUDENT)", guest7.toString(), "Incorrect string representation returned by toString()");
        assertEquals(guest8.getName() + " (STAFF)", guest8.toString(), "Incorrect string representation returned by toString()");
        assertEquals(guest9.getName() + " (GUEST)", guest9.toString(), "Incorrect string representation returned by toString()");
        assertEquals(guest10.getName() + " (STUDENT)", guest10.toString(), "Incorrect string representation returned by toString()");
        assertEquals(guest11.getName() + " (STAFF)", guest11.toString(), "Incorrect string representation returned by toString()");
        assertEquals(guest12.getName() + " (GUEST)", guest12.toString(), "Incorrect string representation returned by toString()");
    }

}
