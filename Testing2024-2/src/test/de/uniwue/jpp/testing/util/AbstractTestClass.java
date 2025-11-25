package de.uniwue.jpp.testing.util;

import de.uniwue.jpp.testing.enums.DishType;
import de.uniwue.jpp.testing.enums.GuestType;
import de.uniwue.jpp.testing.interfaces.DayManager;
import de.uniwue.jpp.testing.interfaces.Dish;
import de.uniwue.jpp.testing.interfaces.Guest;
import de.uniwue.jpp.testing.interfaces.Order;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.*;

public class AbstractTestClass<T> {
    private static final Map<String, MyRunnable> methods;
    private static final Map<String, List<Class<?>>> args;
    static {
        Map<String, MyRunnable> temp = new HashMap<>();
        try {
            temp.put("TestDish", createForMethod(Dish.class.getMethod("createDish", String.class, double.class, DishType.class)));
            temp.put("TestGuest", createForMethod(Guest.class.getMethod("createGuest", String.class, GuestType.class)));
            temp.put("TestOrder", createForMethod(Order.class.getMethod("createOrder", Guest.class, List.class)));
            temp.put("TestSimpleDayManager", createForMethod(DayManager.class.getMethod("createSimpleDayManager", LocalDate.class, int.class, int.class)));
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("A factory-Method was not found. This may happen, when a given method-signature was altered!");
        }

        methods = Collections.unmodifiableMap(temp);

        args = Map.of(
                "TestDish", List.of(String.class, Double.class, DishType.class),
                "TestGuest", List.of(String.class, GuestType.class),
                "TestOrder", List.of(Guest.class, List.class),
                "TestSimpleDayManager", List.of(LocalDate.class, Integer.class, Integer.class));
    }

    private static MyRunnable createForMethod(Method m){
        return params -> {
            try {
                return m.invoke(null, params);
            } catch (IllegalAccessException | ClassCastException e) {
                throw new IllegalStateException("Illegal state reached. This may happen, when a given method-signature was altered!");
            } catch (InvocationTargetException e) {
                throw (RuntimeException) e.getCause();
            }
        };
    }

    protected MyRunnable factoryMethod;

    public void setFactoryMethod(MyRunnable method){
        this.factoryMethod = method;
    }

    protected T construct(Object... params)  {
        MyRunnable factoryMethod = Objects.requireNonNullElse(this.factoryMethod, getCorrespondingFactoryMethod());

        if(!typesMatch(getCorrespondingArgs(),params)) throw new IllegalArgumentException("'construct' was called with non matching parameters");

        return (T) factoryMethod.run(params);
    }

    private MyRunnable getCorrespondingFactoryMethod(){
        String testClassName = getClassNameOfCallingClass(2);

        if(!methods.containsKey(testClassName)) throw new IllegalStateException("'construct' was called from a test class, for which the factory-Method is unknown!");
        return methods.get(testClassName);
    }

    private List<Class<?>> getCorrespondingArgs(){
        String testClassName = getClassNameOfCallingClass(2);

        if(!args.containsKey(testClassName)) throw new IllegalStateException("'construct' was called from a test class, for which no args are known");
        return args.get(testClassName);
    }


    private String getClassNameOfCallingClass(int hops){
        String qualifiedName = Thread.currentThread().getStackTrace()[2+hops].getClassName();
        String[] parts = qualifiedName.split("\\.");
        return parts[parts.length-1];
    }

    public interface MyRunnable{
        public Object run(Object... params);
    }

    private boolean typesMatch(List<Class<?>> expected, Object... actual){
        if(expected.size() != actual.length) return false;

        for (int i = 0; i < expected.size(); i++) {
            if(actual[i] != null && !expected.get(i).isAssignableFrom(actual[i].getClass())) return false;
        }
        return true;
    }
}
