package de.uniwue.jpp.errorhandling;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class OptionalWithMessageMsg<T> implements OptionalWithMessage<T>{

    private final String message;

    public OptionalWithMessageMsg(String message) {
        this.message = message;
    }
    @Override
    public boolean isPresent() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return  true;
    }

    @Override
    public T get() {
        throw new NoSuchElementException("No value present");
    }

    @Override
    public T orElse(T def) {
        return def;
    }

    @Override
    public T orElseGet(Supplier<? extends T> supplier) {
        return supplier.get();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public <S> OptionalWithMessage<S> map(Function<T, S> f) {
        Objects.requireNonNull(f);
        if (message != null) {
            return OptionalWithMessage.ofMsg(message);
        } else {
            T value=get();
            S newValue = f.apply(value);
            if (newValue == null) {
                throw new NullPointerException("Function must not return null.");
            } else {
                return OptionalWithMessage.of(newValue);
            }
        }
    }

    @Override
    public <S> OptionalWithMessage<S> flatMap(Function<T, OptionalWithMessage<S>> f) {
        return OptionalWithMessage.ofMsg(message);
    }

    @Override
    public Optional<String> consume(Consumer<T> c) {
        return Optional.ofNullable(this.getMessage());
    }

    @Override
    public Optional<String> tryToConsume(Function<T, Optional<String>> c) {
        return Optional.ofNullable(message);
    }
}


