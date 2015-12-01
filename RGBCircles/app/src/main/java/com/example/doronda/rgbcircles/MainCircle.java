package com.example.doronda.rgbcircles;

/**
 * Created by doronda on 01.12.2015.
 */
public class MainCircle {

    public static final int RADIUS = 50;
    private  int x;
    private  int y;
    private int radius;

    public MainCircle(int x, int y) {
        this.x = x;
        this.y = y;
        this.radius = RADIUS;
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



}
