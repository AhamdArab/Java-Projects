package de.uniwue.jpp.testing.enums;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestGuestType extends AbstractEnumTestClass {

    @Test
    public void testEnumValues(){
        Class<?> enumClass = getEnumClass();
        Object[] enumConstants = enumClass.getEnumConstants();
        assertEquals(3, enumConstants.length, "The values in the GuestType enum are not as expected");
        assertEquals("STUDENT", enumConstants[0].toString(), "The values in the GuestType enum are not as expected");
        assertEquals("STAFF", enumConstants[1].toString(), "The values in the GuestType enum are not as expected");
        assertEquals("GUEST", enumConstants[2].toString(), "The values in the GuestType enum are not as expected");

    }


    @Test
    public void testDiscountFactor() {
        Map<String, Double> expectedFactors = Map.of(
                "STUDENT", 0.6,
                "STAFF", 0.8,
                "GUEST", 1.0
        );

        expectedFactors.forEach((typeName, expectedFactor) -> {
            DiscountFactorProvider guestType = getGuestTypeConstant(typeName);
            assertNotNull(guestType, typeName + " should be a valid GuestType.");

            double actualFactor = guestType.getDiscountFactor();
            assertEquals(expectedFactor, actualFactor, 0.01,
                    "Incorrect value returned by getDiscountFactor() on " + typeName);
        });
    }
}
