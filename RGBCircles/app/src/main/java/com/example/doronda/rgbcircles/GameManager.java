package com.example.doronda.rgbcircles;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by doronda on 01.12.2015.
 */
public class GameManager {

    private MainCircle mCircle;
    private Paint paint;

    public GameManager() {

        initMainCircle();
        initPaint();

    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    private void initMainCircle() {
        mCircle = new MainCircle(200,500);
    }

    public void onDraw(Canvas canvas) {

        canvas.drawCircle(mCircle.getX(), mCircle.getY(), mCircle.getRadius(), paint);

    }
}
