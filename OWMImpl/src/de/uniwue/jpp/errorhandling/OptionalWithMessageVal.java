package de.uniwue.jpp.errorhandling;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class OptionalWithMessageVal<T, OptionalWithMessageMsg> implements OptionalWithMessage<T>{

    private final T value;

    public OptionalWithMessageVal(T value) {
        this.value = value;
    }

    @Override
    public boolean isPresent() {
         return value != null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    @Override
    public T orElse(T def) {
       if (value!=null){
           return value;
       }
       return def;
    }

    @Override
    public T orElseGet(Supplier<? extends T> supplier) {
        if (supplier!=null){
            return value;
        }
        return supplier.get();
    }

    @Override
    public String getMessage() {
        throw new NoSuchElementException("No value present");
    }

    @Override
    public <S> OptionalWithMessage<S> map(Function<T, S> f) {
        Objects.requireNonNull(f);
        if (value != null) {
            S result = f.apply(value);
            if (result == null) {
                throw new NullPointerException("Function must not return null.");
            }
            return OptionalWithMessage.of(result);
        } else {
            return OptionalWithMessage.ofMsg(getMessage());
        }
    }

    @Override
    public <S> OptionalWithMessage<S> flatMap(Function<T, OptionalWithMessage<S>> f) {
        if (f == null) {
            throw new NullPointerException("Function must not be null");
        }
        OptionalWithMessage<S> newValue = f.apply(value);
        if (newValue == null) {
            throw new NullPointerException("Mapped value must not be null");
        }
        return newValue;
    }

    @Override
    public Optional<String> consume(Consumer<T> c) {
        if (this.value != null) {
            c.accept(this.value);
            return Optional.empty();
        } else {
            return Optional.ofNullable(this.getMessage());
        }
    }
    @Override
    public Optional<String> tryToConsume(Function<T, Optional<String>> c) {
        Objects.requireNonNull(c);

        if (!this.isPresent()) {
            return Optional.empty();
        }

        return c.apply(this.get());

    }
}
