package com.example.doronda.rgbcircles;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by doronda on 01.12.2015.
 */
public class EnemyCircle extends SimpleCircle {

    public static final int MIN_RADIUS = 10;
    public static final int MAX_RADIUS = 100;
    public static final int RED = Color.RED;
    public static final int GREEN = Color.GREEN;
    public static final int SPEED = 10;
    private int dx;
    private int dy;

    public EnemyCircle(int x, int y, int radius, int dx, int dy) {
        super(x, y, radius);
        this.dx = dx;
        this.dy = dy;
    }

    public static EnemyCircle getRandomCircle() {
        Random rand = new Random();
        int x = rand.nextInt(GameManager.getWidth());
        int y = rand.nextInt(GameManager.getHeight());
        int dx = 1 + rand.nextInt(SPEED);
        int dy = 1 + rand.nextInt(SPEED);
        int radius = MIN_RADIUS + rand.nextInt(MAX_RADIUS-MIN_RADIUS);
        EnemyCircle circle = new EnemyCircle(x,y,radius, dx, dy);
        circle.setColor(RED);
        return circle;
    }

    public void setEnemyOrFood(MainCircle mCircle) {
        if(isSmallerThan(mCircle)){
            setColor(GREEN);
        } else {
            setColor(RED);
        }

    }

    public boolean isSmallerThan(SimpleCircle sCircle) {

        if(radius < sCircle.radius) return true;
        else return false;
    }

    public void moveOneStep() {
        x+= dx;
        y+= dy;
        checkBounds();
    }

    private void checkBounds() {
        if(x > GameManager.getWidth() || x < 0){
            dx *= -1;
        }
        if(y > GameManager.getHeight() || y < 0){
            dy *= -1;
        }
    }
}
