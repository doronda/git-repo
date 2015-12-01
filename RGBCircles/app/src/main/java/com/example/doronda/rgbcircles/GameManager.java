package com.example.doronda.rgbcircles;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by doronda on 01.12.2015.
 */
public class GameManager {

    private CanvasView canvasView;
    private int width;
    private int height;
    private MainCircle mCircle;
    private Paint paint;

    public GameManager(CanvasView c, int w, int h) {
        canvasView = c;
        width = w;
        height = h;
        initMainCircle();
        initPaint();

    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    private void initMainCircle() {
        mCircle = new MainCircle(width/2, height/2);
    }

    public void onDraw(Canvas canvas) {

        canvas.drawCircle(mCircle.getX(), mCircle.getY(), mCircle.getRadius(), paint);

    }
}
