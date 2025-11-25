package de.uniwue.jpp.testing.enums;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class AbstractEnumTestClass {
    private static final Map<String, Class<? extends Enum<?>>> clazzes = Map.of(
            "TestDishType", DishType.class,
            "TestGuestType", GuestType.class);
    
    protected Class<? extends Enum<?>> clazz;

    public void setClass(Class<? extends Enum<?>> clazz) {
        this.clazz = clazz;
    }

    protected Class<? extends Enum<?>> getEnumClass(){
        return getEnumClass(2);
    }

    private Class<? extends Enum<?>> getEnumClass(int hops){
        if(clazz != null) return clazz;

        String testClassName = getClassNameOfCallingClass(hops);
        if(!clazzes.containsKey(testClassName)) throw new IllegalStateException("'getEnumClass' was called from a test class, for which the class-Object is unknown!");
        return clazzes.get(testClassName);
    }

    protected DiscountFactorProvider getGuestTypeConstant(String name){
        Enum<?> enumConstant = List.of(getEnumClass(2).getEnumConstants()).stream()
                .filter(t -> t.name().equals(name))
                .findAny().orElseThrow((Supplier<RuntimeException>) () -> new IllegalStateException(getEnumClass(4).toString() + " has no constant " + name + "!"));

        try {
            return (DiscountFactorProvider) enumConstant;
        }catch (ClassCastException e){
            throw new IllegalStateException("'getGuestTypeConstant' was called from a test class, that does not test GuestType!");
        }
    }

    private String getClassNameOfCallingClass(int hops){
        String qualifiedName = Thread.currentThread().getStackTrace()[2+hops].getClassName();
        String[] parts = qualifiedName.split("\\.");
        return parts[parts.length-1];
    }

}
