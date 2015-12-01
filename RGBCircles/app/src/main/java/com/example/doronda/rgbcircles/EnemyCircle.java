package com.example.doronda.rgbcircles;

import java.util.Random;

/**
 * Created by doronda on 01.12.2015.
 */
public class EnemyCircle extends SimpleCircle {

    public static final int MIN_RADIUS = 10;
    public static final int MAX_RADIUS = 100;

    public EnemyCircle(int x, int y, int radius) {
        super(x, y, radius);
    }

    public static EnemyCircle getRandomCircle() {
        Random rand = new Random();
        int x = rand.nextInt(GameManager.getWidth());
        int y = rand.nextInt(GameManager.getHeight());
        int radius = MIN_RADIUS + rand.nextInt(MAX_RADIUS-MIN_RADIUS);
        EnemyCircle circle = new EnemyCircle(x,y,radius);

        return circle;
    }
}
