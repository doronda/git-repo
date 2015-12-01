package com.example.doronda.rgbcircles;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by doronda on 01.12.2015.
 */
public class GameManager {

    private CanvasView canvasView;
    private static int width;
    private static int height;
    private MainCircle mCircle;

    public GameManager(CanvasView c, int w, int h) {
        canvasView = c;
        width = w;
        height = h;
        initMainCircle();
    }



    private void initMainCircle() {
        mCircle = new MainCircle(width/2, height/2);
    }

    public void onDraw() {

        canvasView.drawCircle(mCircle);

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
