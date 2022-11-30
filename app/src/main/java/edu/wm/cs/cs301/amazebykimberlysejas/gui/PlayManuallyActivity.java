package edu.wm.cs.cs301.amazebykimberlysejas.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import edu.wm.cs.cs301.amazebykimberlysejas.R;
import edu.wm.cs.cs301.amazebykimberlysejas.generation.Maze;

public class PlayManuallyActivity extends AppCompatActivity {
    private ToggleButton fullMaze;
    private ToggleButton showSolution;
    private ToggleButton walls;
    private Maze maze;

    private int pathLength = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);

        fullMazeButton();
        showSolutionButton();
        wallsButton();
        zoomInClick();
        zoomOutClick();
        shortcutClicked();
        moveUp();
        jump();
        rotateLeft();
        rotateRight();
        createGameScreenBitmap();


        //getting the maze
        maze = GeneratingActivity.maze;
        Log.v("play manual maze", "maze height: " + maze.getHeight() + " maze width: " + maze.getWidth());


    }

    /**
    Toggle button for full maze on or off that displays Toast and Log.v messages
     */
    private void fullMazeButton(){
        fullMaze = (ToggleButton) findViewById(R.id.fullMazeB);
        fullMaze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayManuallyActivity.this, "Full Maze mode: "+ fullMaze.getText(), Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User toggled full maze mode " +  fullMaze.getText());
            }
        });
    }

    /**
    Toggle button for show solution on or off that displays Toast and Log.v messages
     */
    private void showSolutionButton(){
        showSolution = (ToggleButton) findViewById(R.id.solutionB);
        showSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayManuallyActivity.this, "Show Solution mode: "+ showSolution.getText(), Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User toggled show solution mode " +  showSolution.getText());
            }
        });
    }

    /**
    Toggle button for full maze on or off that displays Toast and Log.v messages
     */
    private void wallsButton(){
        walls = (ToggleButton) findViewById(R.id.wallsB);
        walls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayManuallyActivity.this, "Walls Up: "+ walls.getText(), Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User toggled walls up mode " +  walls.getText());
            }
        });
    }

    /**
    Zooms in to the maze view when image button is clicked
     */
    private void zoomInClick(){
        ImageButton zoomIn = (ImageButton) findViewById(R.id.zoomInB);
        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayManuallyActivity.this, "Zoomed in +", Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User zoomed in " +  walls.getText());
            }
        });

    }

    /**
    Zooms out of the maze view when image button is clicked
     */
    private void zoomOutClick(){
        ImageButton zoomOut = (ImageButton) findViewById(R.id.zoomOutB);
        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayManuallyActivity.this, "Zoomed out -", Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User zoomed out " +  walls.getText());
            }
        });

    }

    /**
    Switches to WinningActivity when user clicks on shortcut button
     */
    private void shortcutClicked(){
        Button shortcut = findViewById(R.id.shortcutB);
        shortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayManuallyActivity.this, "Shortcut button clicked!", Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User clicked shortcut button, switch to winning");
                Intent i = new Intent(PlayManuallyActivity.this, WinningActivity.class);
                i.putExtra("From", "Manual");
                startActivity(i);
            }
        });
    }

    /**
    Moves user forward when up arrow image button is clicked. Adds one to path length.
     */
    private void moveUp(){
        ImageButton upArrow = (ImageButton) findViewById(R.id.arrowUpB);
        upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathLength+=1;
                Toast.makeText(PlayManuallyActivity.this, "Moved up!", Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User clicked arrow up button, moved forwardPath length is: " + pathLength);
            }
        });
    }

    /**
    Handles user jump when jump image button is clicked. Adds one to path length.
     */
    private void jump(){
        ImageButton jumpButton = (ImageButton) findViewById(R.id.jumpB);
        jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathLength+=1;
                Toast.makeText(PlayManuallyActivity.this, "Jumped!", Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User clicked jumped button. Path length is: "+ pathLength);
            }
        });
    }

    /**
    Rotates user left when left arrow image button is clicked.
     */
    private void rotateLeft(){
        ImageButton leftArrow = (ImageButton) findViewById(R.id.arrowLeftB);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayManuallyActivity.this, "Rotated left!", Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User clicked left arrow button, rotated left");
            }
        });

    }

    /**
   Rotates user right when right arrow image button is clicked.
    */
    private void rotateRight(){
        ImageButton rightArrow = (ImageButton) findViewById(R.id.arrowRightB);
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayManuallyActivity.this, "Rotated right!", Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User clicked right arrow button, rotated right");
            }
        });

    }


    /**
    Draws the game screen by using a custom view maze panel
     */
    private void createGameScreenBitmap(){
        MazePanel gameScreen = (MazePanel) findViewById(R.id.gameScreen);
        gameScreen.drawManualTestImage();

    }



}