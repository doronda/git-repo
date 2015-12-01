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

    private GameManager gManager;



    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gManager = new GameManager();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        gManager.onDraw(canvas);

    }
}
