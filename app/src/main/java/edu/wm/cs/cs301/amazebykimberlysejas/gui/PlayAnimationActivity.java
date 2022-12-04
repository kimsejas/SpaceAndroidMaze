package edu.wm.cs.cs301.amazebykimberlysejas.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import edu.wm.cs.cs301.amazebykimberlysejas.R;
import edu.wm.cs.cs301.amazebykimberlysejas.generation.Floorplan;
import edu.wm.cs.cs301.amazebykimberlysejas.generation.Maze;

public class PlayAnimationActivity extends AppCompatActivity {
    private ToggleButton fullMaze;
    private ToggleButton showSolution;
    private ToggleButton walls;

    private ImageButton playPause;
    private boolean onPlay = false;

    private SeekBar speedBar;
    private TextView speedBarText;
    private Integer curSpeed;

    private Maze maze;

    Floorplan seenCells = new Floorplan(GeneratingActivity.maze.getWidth()+1,GeneratingActivity.maze.getHeight()+1) ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);

        fullMazeButton();
        showSolutionButton();
        wallsButton();
        zoomInClick();
        zoomOutClick();
        playPauseClick();
        toWinningClick();
        toLosingClick();
        speedBarSlider();

        //getting the maze
        maze = GeneratingActivity.maze;
        Log.v("play animation maze", "maze height: " + maze.getHeight() + " maze width: " + maze.getWidth());
        createGameScreenBitmap();




    }

    /**
    Toggle button for full maze on or off that displays Toast and Log.v messages
     */
    private void fullMazeButton(){
        fullMaze = (ToggleButton) findViewById(R.id.fullMazeB);
        fullMaze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayAnimationActivity.this, "Full Maze mode: "+ fullMaze.getText(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PlayAnimationActivity.this, "Show Solution mode: "+ showSolution.getText(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PlayAnimationActivity.this, "Walls Up: "+ walls.getText(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PlayAnimationActivity.this, "Zoomed in +", Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User zoomed in ");
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
                Toast.makeText(PlayAnimationActivity.this, "Zoomed out -", Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User zoomed out ");
            }
        });

    }

    /**
    Switches between playing and pausing animation by updating image
     */
    private void playPauseClick(){
        playPause = (ImageButton) findViewById(R.id.playPauseB);
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPlay){
                    //animation is happening and user wants to pause animation
                    playPause.setBackgroundResource(R.drawable.playbutton);
                    Toast.makeText(PlayAnimationActivity.this, "Animation paused", Toast.LENGTH_SHORT).show();
                    Log.v("buttonClicked", "User paused animation");
                    onPlay = false;

                }else{
                    //animation is paused and user wants to start animation
                    playPause.setBackgroundResource(R.drawable.pausebutton);
                    Toast.makeText(PlayAnimationActivity.this, "Animation started", Toast.LENGTH_SHORT).show();
                    Log.v("buttonClicked", "User started animation");
                    onPlay = true;
                }
            }
        });

    }

    /**
    Switches to WinningActivity when button is clicked
     */
    private void toWinningClick(){
        Button toWinning = findViewById(R.id.toWinningB);
        toWinning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayAnimationActivity.this, "To winning button clicked!", Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User clicked to winning button, switch to winning");
                Intent i = new Intent(PlayAnimationActivity.this, WinningActivity.class);
                i.putExtra("From", "Animation");
                startActivity(i);
            }
        });

    }

    /**
    Switches to LosingActivity when button is clicked
     */
    private void toLosingClick(){
        Button toLosing = findViewById(R.id.toLosingB);
        toLosing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayAnimationActivity.this, "To losing button clicked!", Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User clicked to losing button, switch to losing");
                Intent i = new Intent(PlayAnimationActivity.this, LosingActivity.class);
                i.putExtra("From", "Animation");
                startActivity(i);
            }
        });

    }


    /**
    Allows for the user to adjust speed of animation and updates text that displays current speed
     */
    private void speedBarSlider(){
        speedBarText = (TextView) findViewById(R.id.speedBarText);
        speedBar = (SeekBar) findViewById(R.id.speedBar);
        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int speed, boolean b) {
                curSpeed = speed;
                speedBarText.setText("Speed "+ speed +"x");
                Toast.makeText(PlayAnimationActivity.this, "Current speed:  " + speed + "x", Toast.LENGTH_SHORT).show();
                Log.v("speedBar", "Speed bar at "+ speed+ " speed" );

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     Draws the game screen by using a custom view maze panel
     */
    private void createGameScreenBitmap(){
        MazePanel panel = (MazePanel) findViewById(R.id.gameScreen2);
        if (panel == null) {
//            printWarning();
            return;
        }
        int walkStep = 0;
        int angle = 90;
        int px = maze.getStartingPosition()[0];
        int py = maze.getStartingPosition()[1];

//        int px2 = maze.getNeighborCloserToExit(px,py)[0];
//        int py2 = maze.getNeighborCloserToExit(px,py)[1];


        // draw the first person view and the map view if wanted
        FirstPersonView firstPersonView = new FirstPersonView(Constants.VIEW_WIDTH,
                Constants.VIEW_HEIGHT, Constants.MAP_UNIT,
                Constants.STEP_SIZE, seenCells, GeneratingActivity.maze.getRootnode()
        );

        firstPersonView.draw(panel, px, py, walkStep, angle,
                maze.getPercentageForDistanceToExit(px, py)) ;
        panel.commit();
    }






}