package de.uniwue.jpp.javalgebra.mengen;

import de.uniwue.jpp.javalgebra.Menge;

import java.util.Optional;
import java.util.stream.Stream;

public class LeereMenge<T> implements Menge<T> {

    private Stream<T> element;
    @Override
    public Stream<T> getElements() {

        return Stream.empty();
    }
   /* public boolean contains(T element){

        return false;
    }
    public Optional<Integer> getSize(){

        return Optional.of(0);
    }
    public boolean isEmpty(){

        return true;
    }
    public String asString(int maxDisplay){
        if (maxDisplay<0)
            throw new IllegalArgumentException("");
        if (maxDisplay==0)
            throw new IllegalArgumentException("");

        return "[]";
    }
   public String asString(){

        return "[]";
   }*/
}
