package com.imagezoom;

public class Easing implements IEasing {
    private double m56a(double d2, double d3, double d4, double d5) {
        d2 = (d2 / d5) - 1.0d;
        return ((((d2 * d2) * d2) + 1.0d) * d4) + d3;
    }

    @Override
    public double m40a(double d, double d2, double d3, double d4) {
        return m56a(d, d2, d3, d4);
    }
}
