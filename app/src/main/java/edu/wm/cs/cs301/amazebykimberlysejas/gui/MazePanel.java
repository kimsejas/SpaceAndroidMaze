package edu.wm.cs.cs301.amazebykimberlysejas.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import edu.wm.cs.cs301.amazebykimberlysejas.R;

public class MazePanel extends androidx.appcompat.widget.AppCompatImageView {

    private Paint paint = new Paint();
    private  Bitmap bitmap;
    private Canvas canvas;


    public MazePanel(Context context) {
        super(context);
        init();

    }

    public MazePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public MazePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    /*
    Called in all constructors methods to initalize the same private bitmap,canvas, and other methods
     */
    public void init(){
        bitmap =  Bitmap.createBitmap(800,800, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        drawManualTestImage();
        drawAnimationTestImage();

    }



    @Override
    protected void onDraw(Canvas canvas){

        super.onDraw(canvas);
        //passes in the drawn on bitmap with a complete image and draws that complete image on the parameter canvas
        canvas.drawBitmap(bitmap, 0, 0, null);


    }

    @Override
    protected void onMeasure(int width, int height){
        setMeasuredDimension(800, 800);
    }


    /*
    Draws a static test image for the PlayManuallyActivity.
    Gray rectangle on top,black rectangle on bottom, and red circle in middle.
     */
    public void drawManualTestImage(){
        Paint black = new Paint();
        black.setColor(Color.BLACK);
        Paint gray = new Paint();
        gray.setColor(Color.GRAY);
        Paint red = new Paint();
        red.setColor(Color.RED);
        canvas.drawRect(0, 400, 800, 800, black);
        canvas.drawRect(0, 0, 800, 400, gray);
        canvas.drawCircle(400, 400, 80, red);
    }

    /*
    Draws a static test image for the PlayAnimationActivity.
    Gray rectangle on top,black rectangle on bottom, two polygons to represent walls
     */
    public void drawAnimationTestImage(){
        Paint black = new Paint();
        black.setColor(Color.BLACK);
        Paint gray = new Paint();
        gray.setColor(Color.GRAY);
        canvas.drawRect(0, 400, 800, 800, black);
        canvas.drawRect(0, 0, 800, 532, gray);

        Paint yellow = new Paint();
        yellow.setColor(Color.YELLOW);
        Path leftWallPath = new Path();
        leftWallPath.moveTo(0,0);
        leftWallPath.lineTo(200, 266);
        leftWallPath.lineTo(200,532);
        leftWallPath.lineTo(0,800);
        leftWallPath.lineTo(0,0);
        leftWallPath.close();
        canvas.drawPath(leftWallPath, yellow);

        Paint green = new Paint();
        green.setColor(Color.GREEN);
        Path rightWallPath = new Path();
        rightWallPath.moveTo(800,0);
        rightWallPath.lineTo(600, 266);
        rightWallPath.lineTo(600,532);
        rightWallPath.lineTo(800,800);
        rightWallPath.lineTo(800,0);
        rightWallPath.close();
        canvas.drawPath(rightWallPath, green);


    }

}
