package org.longmetal.util;

/** A simple class for grouping two values together into an object */
public class Dual<Type> {
    private Type a;
    private Type b;

    /**
     * Constructor
     *
     * @param a
     * @param b
     */
    public Dual(Type a, Type b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Getter for a
     *
     * @return the value of a
     */
    public Type geta() {
        return a;
    }

    /**
     * Getter for b
     *
     * @return the value of b
     */
    public Type getb() {
        return b;
    }

    /**
     * Setter for a
     *
     * @param a the value to store to a
     */
    public void seta(Type a) {
        this.a = a;
    }

    /**
     * Setter for b
     *
     * @param b the value to store to b
     */
    public void setb(Type b) {
        this.b = b;
    }
}
