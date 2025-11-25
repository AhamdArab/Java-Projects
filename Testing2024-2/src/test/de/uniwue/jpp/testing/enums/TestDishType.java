package de.uniwue.jpp.testing.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDishType extends AbstractEnumTestClass {

    @Test
    public void testEnumValues(){
        Class<?> enumClass = getEnumClass();
        Object[] enumConstants = enumClass.getEnumConstants();
        assertEquals(4, enumConstants.length, "The values in the DishType enum are not as expected");
        assertEquals("STARTER", enumConstants[0].toString(), "The values in the DishType enum are not as expected");
        assertEquals("MAIN_DISH", enumConstants[1].toString(), "The values in the DishType enum are not as expected");
        assertEquals("DESSERT", enumConstants[2].toString(), "The values in the DishType enum are not as expected");
        assertEquals("OTHER", enumConstants[3].toString(), "The values in the DishType enum are not as expected");

    }


}
