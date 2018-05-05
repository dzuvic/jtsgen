/*
 * Copyright (c) 2017 Dragan Zuvic
 *
 * This file is part of jtsgen.
 *
 * jtsgen is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jtsgen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jtsgen.  If not, see http://www.gnu.org/licenses/
 *
 */

package dz.jtsgen.processor.util;


import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static dz.jtsgen.processor.util.ToDo.todoEx;

/**
 * Simple Either type. Right centered.
 *
 * @param <L>  type of the left value
 * @param <R>  type of he right value
 */
public interface Either<L, R> extends Iterable<R> {

    /**
     * @return true if right value
     */
    boolean isRight();

    /**
     * @return true if left value
     */
    boolean isLeft();

    static <L, R>  Either<L, R> left(L l) {
        return new Left<>(l);
    }

    static <L, R>  Either<L, R> right(R r) {
        return new Right<>(r);
    }

    R value();

    L leftValue();


    default void ifPresent(Consumer<? super R> action) {
        Objects.requireNonNull(action, "action MUST NOT be null");
        for (R t : this) {
            action.accept(t);
        }
    }

    @Override
    default Spliterator<R> spliterator() {
        throw  todoEx("not needed");
    }

    default <U> U fold(Function<? super L, ? extends U> f_left, Function<? super R, ? extends U> f_right) {
        Objects.requireNonNull(f_left, "f_left is null");
        Objects.requireNonNull(f_right, "f_right is null");
        if (isRight()) {
            return f_right.apply(value());
        } else {
            return f_left.apply(leftValue());
        }
    }

    @Override
    default Iterator<R> iterator() {
        return this.isLeft() ? new EmptyIterator<>() : new EitherIterator<R>(value());
    }


    /**
     * map only the right side
     *
     * @param f the function
     * @param <T> the type
     * @return mapped either
     */
    @SuppressWarnings("unchecked")
    default <T> Either<L, T> map(Function<? super R, ? extends T> f) {
        Objects.requireNonNull(f, "function must not be null");
        if (isRight()) {
            return Either.right(f.apply(value()));
        } else {
            return (Either<L, T>) this;
        }
    }

    /**
     * flatmap like in Haskel. Either is a box...
     * @param f  function from R to U (maps only right side)
     * @param <T> the type to convert
     * @return a mapped either or the left side
     */
    @SuppressWarnings("unchecked")
    default <T> Either<L, T> flatMap(Function<? super R, ? extends Either<L, ? extends T>> f) {
        Objects.requireNonNull(f, "mapper is null");
        if (isRight()) {
            return (Either<L, T>) f.apply(value());
        } else {
            return (Either<L, T>) this;
        }
    }

    default Either<L, R> check(L l, Predicate<? super R> p) {
        Objects.requireNonNull(p, "predicate must not be null");
        Objects.requireNonNull(l, "possible left value must not be null");
        return (isLeft() || (p.test(value()))) ? this : Either.left(l);
    }

    /**
     * either return the left value if the predicate does not hold or null
     * used in testing, where the left value is extracted because of an error
     * @param l the left value to return if predicate fails
     * @param p the predicate checking the right value
     * @return the left value or null if predicate holds
     */

    default L checkOrLeft(L l, Predicate<? super R> p) {
        Either<L, R> result = check(l,p);
        return result.isLeft() ? result.leftValue() : null ;
    }

    default R orElse(R other) {
        return this.isRight() ? value() : other;
    }


    default R orElseGet(Supplier<? extends R> other) {
        return this.isRight() ? value() : other.get();
    }

    default <X extends Throwable> R orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (this.isRight()) {
            return value();
        } else {
            throw exceptionSupplier.get();
        }
    }

    /**
     * @return left value or null
     */
    default L leftOrNull() {
       return isLeft() ? this.leftValue() : null;
    }

    /**
     * @return right value or null
     */
    default R rightOrNull() {
       return isRight() ? this.value() : null;
    }

    /**
     * @return return optional od right value if set or optional.empty
     */
    default Optional<R> toOptional() {
        return Optional.ofNullable(rightOrNull());
    }

}

/**
 * The Left Value
 */
final class Left<L, R> implements Either<L, R>  {

    private final L value;

    Left(L l) {
        Objects.requireNonNull(l, "must not be null");
        this.value = l;
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public R value() {
        throw new IllegalStateException("should never be called.");
    }

    @Override
    public L leftValue() {
        return this.value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Left{");
        sb.append("value=").append(value);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Left)) return false;
        Left<?, ?> left = (Left<?, ?>) o;
        return Objects.equals(value, left.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

/**
 * The Right value
 */
final class Right<L, R>  implements Either<L, R>  {

    private final R value;

    Right(R r) {
        Objects.requireNonNull(r, "must not be null");
        this.value = r;
    }

    @Override
    public boolean isRight() {
        return true;
    }

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public R value() {
        return this.value;
    }

    @Override
    public L leftValue() {
        throw new IllegalStateException("Right has not Left Value");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Right{");
        sb.append("value=").append(value);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Right)) return false;
        Right<?, ?> right = (Right<?, ?>) o;
        return Objects.equals(value, right.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

/**
 * An empty iterator
 */
final class EmptyIterator<T> implements Iterator<T> {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        throw new NoSuchElementException("There is no Element to iterate");
    }
}

/**
 * a simple iterator
 */
final class EitherIterator<T> implements Iterator<T> {
    private T[] values;
    private int currentIdx=0;

    @SafeVarargs
    EitherIterator(T ... values) {
        this.values = values;
    }

    @Override
    public boolean hasNext() {
        return currentIdx < values.length;
    }

    @Override
    public T next() {
        T value=values[currentIdx];
        currentIdx = currentIdx+1;
        return value;
    }
}
