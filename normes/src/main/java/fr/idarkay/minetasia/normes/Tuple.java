package fr.idarkay.minetasia.normes;

/**
 * File <b>Tuple</b> located on fr.idarkay.minetasia.normes
 * Tuple is a part of MinetasiaCore.
 * <p>
 * Copyright (c) 2020 MinetasiaCore.
 * <p>
 *
 * @author Alois. B. (IDarKay),
 * Created the 20/01/2020 at 14:34
 */
public class Tuple<A, B> {
    private A a;
    private B b;

    public Tuple(final A a, final B b) {
        this.a = a;
        this.b = b;
    }

    public A a() {
        return this.a;
    }

    public B b() {
        return this.b;
    }

    @Override
    public String toString()
    {
        return "Tuple{" + "a=" + a + ", b=" + b + '}';
    }
}
