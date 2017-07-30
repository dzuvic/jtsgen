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


import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

import static dz.jtsgen.processor.util.ToDo.todo;

/**
 * Simple Either type. Right centered.
 *
 * @param <L>  type of the left value
 * @param <R>  type of he right value
 */
interface Either<L, R> extends Iterable<R> {

    boolean isRight();

    boolean isLeft();

    static <L, R>  Either<L, R> left(L l) {
        return new Left<>(l);
    }

    static <L, R>  Either<L, R> right(R r) {
        return new Right<>(r);
    }

    R value();

    L leftValue();


    @Override
    default void forEach(Consumer<? super R> action) {
        Objects.requireNonNull(action, "action MUST NOT be null");
        for (R t : this) {
            action.accept(t);
        }
    }

    @Override
    default Spliterator<R> spliterator() {
        return todo("not needed");
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
        return this.isLeft() ? new EmptyIterator() : new EitherIterator<>(value());
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
