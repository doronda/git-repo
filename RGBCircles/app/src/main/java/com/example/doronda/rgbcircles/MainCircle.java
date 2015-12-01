package com.example.doronda.rgbcircles;

/**
 * Created by doronda on 01.12.2015.
 */
public class MainCircle extends SimpleCircle{

    public static final int RADIUS = 50;
    public static final int SPEED = 30;

    public MainCircle(int x, int y) {
        super(x, y, RADIUS);
    }


    public void moveCircleWhenTouchAt(int x1, int y1) {
        int dx = (x1-x)* SPEED /GameManager.getWidth();
        int dy = (y1-y)*SPEED/GameManager.getHeight();
        x+=dx;
        y+=dy;
    }
}
