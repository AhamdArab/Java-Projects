package de.uniwue.jpp.testing.util;

import org.junit.jupiter.api.function.Executable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestUtils {

    public static void assertThrowsWithMessage(Class<? extends Exception> expectedType, Executable executable, String exceptionMessage, String message){


        Exception thrown = assertThrows(expectedType, executable, message );

        if (!expectedType.isInstance(thrown)) {
            fail(message + " ==> Unexpected exception type thrown, expected: <" + expectedType.getName() + "> but was: <" + thrown.getClass().getName() + ">");
        }

        if (thrown.getMessage() == null) {
            fail(message + " ==> Exception was thrown but message was null");
        } else if (!thrown.getMessage().equals(exceptionMessage)) {
            fail(message + " ==> Incorrect exception message ==> expected: <" + exceptionMessage + "> but was: <" + thrown.getMessage() + ">");
        }
    }

    public static <T> void assertListsEqualInAnyOrder(List<T> expected, List<T> actual, String message){
        if (expected == null && actual == null) {

            return;
        }
        if (expected == null || actual == null) {
            fail(message);
        }
        HashMap<T, Integer> frequencyMapExpected = new HashMap<>();
        HashMap<T, Integer> frequencyMapActual = new HashMap<>();
        for (T item : expected) {
            frequencyMapExpected.put(item, frequencyMapExpected.getOrDefault(item, 0) + 1);
        }
        for (T item : actual) {
            frequencyMapActual.put(item, frequencyMapActual.getOrDefault(item, 0) + 1);
        }
        if (!frequencyMapExpected.equals(frequencyMapActual)) {
            fail(message);
        }
    }

    public static void assertListIsUnmodifiable(List<?> list, String message){

        if (list == null) {
            fail("List is null");
        }

        boolean isUnmodifiable = false;
        try {
            list.getClass().getMethod("add", Object.class);
            list.getClass().getMethod("remove", Object.class);
            try {
                list.add(null);
            } catch (UnsupportedOperationException e) {
                isUnmodifiable = true;
            } catch (Exception e) {
                fail(message + " Unexpected exception while testing add: " + e.getMessage());
            }
            try {
                list.remove(null);
            } catch (UnsupportedOperationException ignored) {
            } catch (Exception e) {
                fail(message + " Unexpected exception while testing remove: " + e.getMessage());
            }
        } catch (NoSuchMethodException e) {

            isUnmodifiable = true;
        }
        if (!isUnmodifiable) {
            fail(message);
        }
    }
}
