package com.example.doronda.rgbcircles;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by doronda on 01.12.2015.
 */
public class GameManager {

    public static final int ENEMY_COUNT = 10;
    private CanvasView canvasView;
    private static int width;
    private static int height;
    private MainCircle mCircle;
    private ArrayList<EnemyCircle> enemyCircles;

    public GameManager(CanvasView c, int w, int h) {
        canvasView = c;
        width = w;
        height = h;
        initMainCircle();
        initEnemyCircles();
    }

    private void initEnemyCircles() {

        enemyCircles = new ArrayList<EnemyCircle>();
        for(int i =0; i< ENEMY_COUNT; i++){
            EnemyCircle circle;
            circle = EnemyCircle.getRandomCircle();
            enemyCircles.add(circle);
        }
    }


    private void initMainCircle() {
        mCircle = new MainCircle(width/2, height/2);
    }

    public void onDraw() {

        canvasView.drawCircle(mCircle);
        for(EnemyCircle circle : enemyCircles){
            canvasView.drawCircle(circle);
        }

    }

    public void onTouchEvent(int x, int y) {

        mCircle.moveCircleWhenTouchAt(x, y);
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}
