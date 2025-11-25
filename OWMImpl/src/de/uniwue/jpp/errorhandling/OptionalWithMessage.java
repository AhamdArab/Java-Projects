package de.uniwue.jpp.errorhandling;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface OptionalWithMessage<T> {

    boolean isPresent();
    boolean isEmpty();
    T get();
    T orElse(T def);
    T orElseGet(Supplier<? extends T> supplier);
    String getMessage();
    <S> OptionalWithMessage<S> map(Function<T, S> f);
    <S> OptionalWithMessage<S> flatMap(Function<T, OptionalWithMessage<S>> f);
    Optional<String> consume(Consumer<T> c);
    Optional<String> tryToConsume(Function<T, Optional<String>> c);

    static <T> OptionalWithMessage<T> of(T val) {
        if (val == null) {
            throw new NullPointerException("Value must not be null");
        }
        return new OptionalWithMessageVal<>(val);
    }

    static <T> OptionalWithMessage<T> ofMsg(String msg) {
        if (msg == null) {
            throw new NullPointerException("Message must not be null");
        }
        return new OptionalWithMessageMsg<>(msg);
    }

    static <T> OptionalWithMessage<T> ofNullable(T val, String msg) {
        if (msg == null) {
            throw new NullPointerException("Message must not be null");
        }
        return val != null ? new OptionalWithMessageVal<>(val) : new OptionalWithMessageMsg<>(msg);
    }

    static <T> OptionalWithMessage<T> ofOptional(Optional<T> opt, String msg) {
        if (opt == null || msg == null) {
            throw new NullPointerException("Optional and message must not be null");
        }
        return opt.isPresent() ? new OptionalWithMessageVal<>(opt.get()) : new OptionalWithMessageMsg<>(msg);
    }

    static <T> OptionalWithMessage<List<T>> sequence(List<OptionalWithMessage<T>> list) {
        List<T> values = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        boolean allValuesPresent = true;

        for (OptionalWithMessage<T> opt : list) {
            if (opt.isPresent()) {
                values.add(opt.get());
            } else {
                errorMessages.add(opt.getMessage());
                allValuesPresent = false;
            }
        }

        if (!allValuesPresent) {
            String errorMessage = String.join(System.lineSeparator(), errorMessages);
            return new OptionalWithMessageMsg<>(errorMessage);
        } else {
            return of(values);
        }
    }
}
