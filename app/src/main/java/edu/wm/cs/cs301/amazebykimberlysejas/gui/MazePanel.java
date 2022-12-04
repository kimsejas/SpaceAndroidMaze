package edu.wm.cs.cs301.amazebykimberlysejas.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import edu.wm.cs.cs301.amazebykimberlysejas.R;
import edu.wm.cs.cs301.amazebykimberlysejas.generation.Floorplan;

public class MazePanel extends View implements P7PanelF22 {

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

    /**
    Called in all constructors methods to initalize the same private bitmap,canvas, and other methods
     */
    public void init(){
        bitmap =  Bitmap.createBitmap(800,800, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }



    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //passes in the drawn on bitmap with a complete image and draws that complete image on the parameter canvas
        if (canvas == null){
            Log.v("missing canvas", "Can't get graphics object to draw on, mitigate this by skipping draw operation");
        }else{
            canvas.drawBitmap(bitmap, 0, 0, null);
        }

    }

    @Override
    protected void onMeasure(int width, int height){
        setMeasuredDimension(800, 800);
    }


//    /**
//    Draws a static test image for the PlayManuallyActivity.
//    Gray rectangle on top,black rectangle on bottom, and red circle in middle.
//     */
//    public void drawManualTestImage(){
//        Paint black = new Paint();
//        black.setColor(Color.BLACK);
//        Paint gray = new Paint();
//        gray.setColor(Color.GRAY);
//        Paint red = new Paint();
//        red.setColor(Color.RED);
//        canvas.drawRect(0, 400, 800, 800, black);
//        canvas.drawRect(0, 0, 800, 400, gray);
//        canvas.drawCircle(400, 400, 80, red);
//        float percentToExit = GeneratingActivity.maze.getPercentageForDistanceToExit()
//        addBackground(0);
//        setColor(Color.GREEN);
//        addFilledOval(400, 400, 80*2, 80*2);
//
//    }

//    /**
//    Draws a static test image for the PlayAnimationActivity.
//    Gray rectangle on top,black rectangle on bottom, two polygons to represent walls
//     */
//    public void drawAnimationTestImage(){
//        Paint black = new Paint();
//        black.setColor(Color.BLACK);
//        Paint gray = new Paint();
//        gray.setColor(Color.GRAY);
//        canvas.drawRect(0, 400, 800, 800, black);
//        canvas.drawRect(0, 0, 800, 532, gray);
//
//        addBackground(0);
//        setColor(Color.BLUE);
//        addFilledOval(400, 400, 80*2, 80*2);
//
//
//        Paint yellow = new Paint();
//        yellow.setColor(Color.YELLOW);
//        Path leftWallPath = new Path();
//        leftWallPath.moveTo(0,0);
//        leftWallPath.lineTo(200, 266);
//        leftWallPath.lineTo(200,532);
//        leftWallPath.lineTo(0,800);
//        leftWallPath.lineTo(0,0);
//        leftWallPath.close();
//        canvas.drawPath(leftWallPath, yellow);
//
//        setColor(Color.YELLOW);
//        addFilledPolygon(new int[]{0, 200, 200, 0}, new int[] {0, 200, 400, 800, }, 4);
//
//        Paint green = new Paint();
//        green.setColor(Color.GREEN);
//        Path rightWallPath = new Path();
//        rightWallPath.moveTo(800,0);
//        rightWallPath.lineTo(600, 266);
//        rightWallPath.lineTo(600,532);
//        rightWallPath.lineTo(800,800);
//        rightWallPath.lineTo(800,0);
//        rightWallPath.close();
//        canvas.drawPath(rightWallPath, green);
//
//        setColor(Color.GREEN);
//        addFilledPolygon(new int[]{800, 600, 600, 800}, new int[] {0, 200, 400, 800, }, 4);
//
//        setColor(Color.RED);
//        addPolygon(new int[]{800, 600, 600, 800}, new int[] {0, 200, 400, 800, }, 4);
//
//
//        //test add arc
//        setColor(Color.WHITE);
//        addArc(200, 200, 400, 200, 0, 360 );
//
//        setColor(Color.RED);
//        addMarker(400,200, "N");
//
//    }



    @Override
    public void commit() {
        invalidate();
        requestLayout();
    }

    @Override
    public boolean isOperational() {
        if (canvas != null) {
            return true;
        }
        return false;
    }

    @Override
    public void setColor(int argb) {
        paint.setColor(argb);

    }

    @Override
    public int getColor() {
        return paint.getColor();
    }


    @Override
    public void addBackground(float percentToExit) {

//        setColor(ColorTheme.getColor(ColorTheme.MazeColors.BACKGROUND_TOP,percentToExit).toArgb());

//        Bitmap fillBMPTop= BitmapFactory.decodeResource(getResources(), R.drawable.galaxy);
//        BitmapShader fillBMPshaderTop = new BitmapShader(fillBMPTop, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setShader(fillBMPshaderTop);

        //black on bottom, gray on top
        setColor(Color.GRAY);
        addFilledRectangle(0, 0, 800, 400);

//        Bitmap fillBMPB = BitmapFactory.decodeResource(getResources(), R.drawable.planettexture);
//        BitmapShader fillBMPshader = new BitmapShader(fillBMPB, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setShader(fillBMPshader);

//        setColor(ColorTheme.getColor(ColorTheme.MazeColors.BACKGROUND_BOTTOM,percentToExit).toArgb());
        setColor(Color.BLACK);
        addFilledRectangle(0, 400, 800, 400);
    }

    @Override
    public void addFilledRectangle(int x, int y, int width, int height) {
        paint.setStyle(Paint.Style.FILL);
        int right = x + width;
        int bottom = y + height;
        canvas.drawRect(x, y, right, bottom, paint);
    }

    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints) {

//        Bitmap fillBMP = BitmapFactory.decodeResource(getResources(), R.drawable.concrete);
//        BitmapShader fillBMPshader = new BitmapShader(fillBMP, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setShader(fillBMPshader);
        paint.setStyle(Paint.Style.FILL);
        float moveToX = xPoints[0];
        float moveToY = yPoints[0];
        Path path = new Path();
        path.moveTo(moveToX, moveToY);
        for (int i = 0; i < nPoints; i++){
            int x = xPoints[i];
            int y = yPoints[i];
            path.lineTo(x,y);
        }
        path.close();
        canvas.drawPath(path, paint);
    }

    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        float moveToX = xPoints[0];
        float moveToY = yPoints[0];
        Path path = new Path();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
        path.moveTo(moveToX, moveToY);
        for (int i = 1; i < nPoints; i++){
            int x = xPoints[i];
            int y = yPoints[i];
            path.lineTo(x,y);
        }
        canvas.drawPath(path, paint);
    }

    @Override
    public void addLine(int startX, int startY, int endX, int endY) {
        canvas.drawLine(startX,startY, endX , endY, paint);
    }

    @Override
    public void addFilledOval(int x, int y, int width, int height) {
        paint.setStyle(Paint.Style.FILL);
        int right = x + width;
        int bottom = y + height;
        canvas.drawOval(x,y,right,bottom,paint);


    }

    @Override
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        float right = (float) x + (float) width;
        float bottom = (float) y + (float) height;
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc((float) x, (float) y, right,bottom, (float) startAngle, (float) arcAngle, true, paint  );
    }

    @Override
    public void addMarker(float x, float y, String str) {
        paint.setTextSize(15);
        paint.setStrokeWidth(1);
        canvas.drawText(str, x-5, y+5, paint);
    }

    @Override
    public void setRenderingHint(P7RenderingHints hintKey, P7RenderingHints hintValue) {

    }
}
