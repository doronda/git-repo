package com.example.doronda.rgbcircles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by doronda on 01.12.2015.
 */
public class CanvasView extends View implements ICanvasView{

    private static int width;
    private static int height;
    private GameManager gManager;
    private  Paint paint;
    private  Canvas canvas;
    private Snackbar snack;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWidthAndHeght(context);
        initPaint();
        gManager = new GameManager(this, width, height);
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
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
       // gManager.onDraw();
        this.canvas = canvas;
        gManager.onDraw();

    }

    @Override
    public void drawCircle(SimpleCircle mainCircle) {
        paint.setColor(mainCircle.getColor());
        canvas.drawCircle(mainCircle.getX(), mainCircle.getY(), mainCircle.getRadius(), paint);
    }

    @Override
    public void redraw() {
        invalidate();
    }

    @Override
    public void showMessage(String text) {
         if(snack!=null) {snack.dismiss();}
        snack =  Snackbar.make(this,text,Snackbar.LENGTH_LONG);

        snack.show();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        if(event.getAction()==MotionEvent.ACTION_MOVE){
            gManager.onTouchEvent(x,y);
        }
        invalidate();
        return true;
    }
}
