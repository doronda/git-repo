package com.example.doronda.rgbcircles;

import android.graphics.Color;

/**
 * Created by doronda on 01.12.2015.
 */
public class MainCircle extends SimpleCircle{

    public static final int RADIUS = 50;
    public static final int SPEED = 30;
    public static final int BLUE = Color.BLUE;

    public MainCircle(int x, int y) {
        super(x, y, RADIUS);
        setColor(BLUE);
    }


    public void moveCircleWhenTouchAt(int x1, int y1) {
        int dx = (x1-x)* SPEED /GameManager.getWidth();
        int dy = (y1-y)*SPEED/GameManager.getHeight();
        x+=dx;
        y+=dy;
    }

    public void initRadius() {
        radius = RADIUS;
    }

    public void grow(EnemyCircle circle) {
        radius =(int) Math.sqrt(Math.pow(radius, 2 ) + Math.pow(circle.radius, 2));
    }
}
