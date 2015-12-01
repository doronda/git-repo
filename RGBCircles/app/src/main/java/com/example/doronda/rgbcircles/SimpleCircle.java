package com.example.doronda.rgbcircles;

/**
 * Created by doronda on 01.12.2015.
 */
public class SimpleCircle {

    protected   int x;
    protected  int y;

    public void setColor(int color) {
        this.color = color;
    }

    private int color;
    protected int radius;

    public SimpleCircle(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getColor() {
          return color;
    }
}
