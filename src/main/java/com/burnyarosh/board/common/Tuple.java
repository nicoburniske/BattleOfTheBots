package com.burnyarosh.board.common;

import java.util.Objects;

/**
 * A generic tuple class to help manage return values
 *
 * @param <Key>
 * @param <Value>
 */
public class Tuple<Key, Value> {
    protected Key left;
    protected Value right;

    public Tuple(Key left, Value right) {
        this.left = left;
        this.right = right;
    }

    /**
     * @return The left side of the tuple
     */
    public Key getLeft() {
        return left;
    }

    public void setLeft(Key left) {
        this.left = left;
    }

    public void setRight(Value right) {
        this.right = right;
    }

    /**
     * @return The right side of the tuple
     */
    public Value getRight() {
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> that = (Tuple<?, ?>) o;
        return left.equals(that.left) &&
                right.equals(that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", this.left.toString(), this.right.toString());
    }
}

