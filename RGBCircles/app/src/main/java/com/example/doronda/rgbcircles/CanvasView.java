package com.example.doronda.rgbcircles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by doronda on 01.12.2015.
 */
public class CanvasView extends View {

    private MainCircle mCircle;
    private Paint paint;


    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mCircle.getX(), mCircle.getY(), mCircle.getRadius(), paint);
    }
}
