package com.example.doronda.rgbcircles;

/**
 * Created by doronda on 01.12.2015.
 */
public class MainCircle {

    public static final int RADIUS = 50;
    public static final int SPEED = 30;
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


    public void moveCircleWhenTouchAt(int x1, int y1) {
        int dx = (x1-x)* SPEED /GameManager.getWidth();
        int dy = (y1-y)*SPEED/GameManager.getHeight();
        x+=dx;
        y+=dy;
    }
}
