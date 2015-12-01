package com.example.doronda.rgbcircles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by doronda on 01.12.2015.
 */
public class CanvasView extends View {

    private static int width;
    private static int height;
    private GameManager gManager;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWidthAndHeght(context);
        gManager = new GameManager(this, width, height);
    }

    private void initWidthAndHeght(Context context) {

        WindowManager wManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        width = point.x;
        height = point.y;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        gManager.onDraw(canvas);

    }
}
