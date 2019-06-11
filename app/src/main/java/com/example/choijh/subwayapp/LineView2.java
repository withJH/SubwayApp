package com.example.choijh.subwayapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class LineView2 extends View {
    Paint myPaint = new Paint();

    public LineView2(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        RectF rect = new RectF();
        RectF rect2 = new RectF();


        rect.set(2,1,32,31);
        canvas.drawArc(rect,0,360,true,myPaint);

        canvas.drawRect(8,28,26,93,myPaint);

        rect2.set(2,90,32,120);
        canvas.drawArc(rect2,0,360,true,myPaint);


    }
}
