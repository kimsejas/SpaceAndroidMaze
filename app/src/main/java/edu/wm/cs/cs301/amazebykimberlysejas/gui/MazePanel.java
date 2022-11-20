package edu.wm.cs.cs301.amazebykimberlysejas.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class MazePanel extends View {

    private Paint paint = new Paint();
    private Bitmap bitmap;
    private Canvas canvas = new Canvas(bitmap);






    public MazePanel(Context context) {
        this(context, null);
    }

    public MazePanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MazePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
//        canvas.setBitmap(bitmap);
//        setBackgroundColor(Color.BLACK);
    }


    @Override
    protected void onDraw(Canvas canvas){
//        super.onDraw(canvas);
//        //create bitmap and draaw on it
//        // draw out that entire bitmap
//
////        canvas.drawBitmap(bitmap, getMatrix(), paint);
////        Rect rectangle = new Rect(0,0,100,100);
////        canvas.drawBitmap(bitmap, null, rectangle, null);
//        paint.setColor(Color.RED);
//        canvas.drawCircle(getWidth()/2, getHeight()/2, 60, paint);
//        canvas.drawBitmap(bitmap, new Matrix(), null);
    }

}
