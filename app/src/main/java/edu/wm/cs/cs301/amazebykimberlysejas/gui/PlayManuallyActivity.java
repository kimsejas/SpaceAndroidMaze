package edu.wm.cs.cs301.amazebykimberlysejas.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import edu.wm.cs.cs301.amazebykimberlysejas.generation.CardinalDirection;
import edu.wm.cs.cs301.amazebykimberlysejas.generation.Floorplan;
import edu.wm.cs.cs301.amazebykimberlysejas.generation.Maze;

public class PlayManuallyActivity extends AppCompatActivity {

    public static Context context;
    private ToggleButton map;
    private ToggleButton solution;
    private ToggleButton walls;
    private Maze maze;


    private int pathLength = 0;
    private FirstPersonView firstPersonView;
    private CompassRose cr;
    public static MazePanel panel;
    private Map mapView;

    public static boolean showMaze;
    private boolean showSolution;
    private boolean mapMode;
    int px, py ;
    CardinalDirection cd;
    Floorplan seenCells;
    boolean started;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);
        context = getApplicationContext();
        mapButton();
        showSolutionButton();
        wallsButton();
        zoomInClick();
        zoomOutClick();
        shortcutClicked();
        moveUp();
        jump();
        rotateLeft();
        rotateRight();
        setMaze();
        setPanel();
        start(panel);
    }

    /**
     * Provides the maze to play.
     */
    public void setMaze() {
        maze = GeneratingActivity.maze;
        Log.v("play manual maze", "maze height: " + maze.getHeight() + " maze width: " + maze.getWidth());
    }

    /**
     * Sets panel for game screen
     */
    public void setPanel(){
        panel = (MazePanel) findViewById(R.id.gameScreen);
    }


    /**
    Toggle button for map on or off that displays Toast and Log.v messages
     */
    private void mapButton(){
        map = (ToggleButton) findViewById(R.id.mapB);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayManuallyActivity.this, "Map mode: "+ map.getText(), Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User toggled map mode " +  map.getText());
                draw(cd.angle(), 0);
            }
        });
    }

    /**
    Toggle button for show solution on or off that displays Toast and Log.v messages
     */
    private void showSolutionButton(){
        solution = (ToggleButton) findViewById(R.id.solutionB);
        solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayManuallyActivity.this, "Show Solution mode: "+ solution.getText(), Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User toggled show solution mode " +  solution.getText());
                draw(cd.angle(), 0);
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
                showMaze = walls.isChecked();
                Toast.makeText(PlayManuallyActivity.this, "Walls Up: "+ walls.getText(), Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User toggled walls up mode " +  walls.getText());
                if (showMaze) {
                    draw(cd.angle(), 0);
                }else{
                    Log.v("walls up", " show maze : " + showMaze);
                    panel.addBackground(maze.getPercentageForDistanceToExit(px, py));
                    panel.commit();
                }

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
                Log.v("buttonClicked", "User zoomed in ");
                mapView.incrementMapScale();
                draw(cd.angle(), 0) ;
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
                Log.v("buttonClicked", "User zoomed out ");
                mapView.decrementMapScale();
                draw(cd.angle(), 0) ;
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
                finish();
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
                walk(1);
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
                int[] tmpDxDy = cd.getDxDyDirection();
                if (maze.isValidPosition(px + tmpDxDy[0], py + tmpDxDy[1])) {
                    setCurrentPosition(px + tmpDxDy[0], py + tmpDxDy[1]) ;
                    draw(cd.angle(), 0) ;
                }
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
                rotate(1);
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
                rotate(-1);
            }
        });

    }


    /////////////////////// Methods for Gameplay ////////////////////////////////
    /**
     * Start the actual game play by showing the playing screen.
     * If the panel is null, all drawing operations are skipped.
     * This mode of operation is useful for testing purposes,
     * i.e., a dryrun of the game without the graphics part.
     * @param panel is part of the UI and visible on the screen, needed for drawing
     */
    public void start(MazePanel panel) {
        assert null != maze : "StatePlaying.start: maze must exist!";

        started = true;
        // keep the reference to the panel for drawing
        PlayManuallyActivity.panel = panel;
        // adjust visibility settings to default values
        showMaze = true;
        showSolution = false;
        mapMode = false;

        // adjust internal state of maze model
        // init data structure for visible walls
        seenCells = new Floorplan(maze.getWidth() + 1, maze.getHeight() + 1);
        // set the current position and direction consistently with the viewing direction
        setPositionDirectionViewingDirection();

        if (panel != null) {
            startDrawer();
        } else {
            // else: dry-run without graphics, most likely for testing purposes
            Log.v("Error", "No panel for drawing during executing, dry-run game without graphics!");
        }
    }

    /**
     * Internal method to set the current position, the direction
     * and the viewing direction to values consistent with the
     * given maze.
     */
    private void setPositionDirectionViewingDirection() {
        int[] start = maze.getStartingPosition() ;
        setCurrentPosition(start[0],start[1]) ;
        cd = CardinalDirection.East;
    }

    protected void setCurrentPosition(int x, int y) {
        px = x ;
        py = y ;
    }

    /**
     * Initializes the drawer for the first person view
     * and the map view and then draws the initial screen
     * for this state.
     */
    protected void startDrawer() {
        cr = new CompassRose();
        cr.setPositionAndSize(Constants.VIEW_WIDTH/2,
                (int)(0.1*Constants.VIEW_HEIGHT),35);

        firstPersonView = new FirstPersonView(Constants.VIEW_WIDTH,
                Constants.VIEW_HEIGHT, Constants.MAP_UNIT,
                Constants.STEP_SIZE, seenCells, maze.getRootnode()) ;

        mapView = new Map(seenCells, 25, maze) ;
        // draw the initial screen for this state
        draw(cd.angle(), 0);
        drawHintIfNecessary();
    }


    /**
    Draws the game screen by using a custom view maze panel
     */
    private void draw(int angle, int walkStep) {
        if (panel == null) {
            Log.v("Error", "No panel for drawing during executing, dry-run game without graphics!");
            return;
        }
        // draw the first person view and the map view if wanted
        firstPersonView.draw(panel, px, py, walkStep, angle,
                maze.getPercentageForDistanceToExit(px, py)) ;
        if (isInMapMode()) {
            mapView.draw(panel, px, py, angle, walkStep,
                    isInShowMazeMode(),isInShowSolutionMode()) ;
        }
        // update the screen with the buffer graphics
        panel.commit() ;
    }

    /**
     * Draw a visual cue to help the user unless the
     * map is on display anyway.
     * This is the map if current position faces a dead end
     * otherwise it is a compass rose.
     */
    private void drawHintIfNecessary() {
        if (isInMapMode())
            return; // no need for help
        // in testing environments, there is sometimes no panel to draw on
        // or the panel is unable to deliver a graphics object
        // check this and quietly move on if drawing is impossible
        if (panel == null) {
            Log.v("Error", "No panel for drawing during executing, dry-run game without graphics!");
            return;
        }
        // if current position faces a dead end, show map with solution
        // for guidance
        if (maze.isFacingDeadEnd(px, py, cd)) {
//            System.out.println("Facing deadend, help by showing solution");
            mapView.draw(panel, px, py, cd.angle(), 0, true, true) ;
        }
        else {
            // draw compass rose
            cr.setCurrentDirection(cd);
            cr.paintComponent(panel);
        }
        panel.commit();
    }

    /**
     * Moves in the given direction with 4 intermediate steps,
     * updates the screen and the internal position
     * @param dir, only possible values are 1 (forward) and -1 (backward)
     */
    private synchronized void walk(int dir) {
        // check if there is a wall in the way
        if (!wayIsClear(dir))
            return;
        int walkStep = 0;
        // walkStep is a parameter of FirstPersonView.draw()
        // it is used there for scaling steps
        // so walkStep is implicitly used in slowedDownRedraw
        // which triggers the draw operation in
        // FirstPersonView and Map
        for (int step = 0; step != 4; step++) {
            walkStep += dir;
            slowedDownRedraw(cd.angle(), walkStep);
        }
        // update position to neighbor
        int[] tmpDxDy = cd.getDxDyDirection();
        setCurrentPosition(px + dir*tmpDxDy[0], py + dir*tmpDxDy[1]) ;
        logPosition(); // debugging
        drawHintIfNecessary();
    }

    /**
     * Performs a rotation with 4 intermediate views,
     * updates the screen and the internal direction
     * @param dir for current direction, values are either 1 or -1
     */
    private synchronized void rotate(int dir) {
        final int originalAngle = cd.angle();//angle;
        final int steps = 4;
        int angle = originalAngle; // just in case for loop is skipped
        for (int i = 0; i != steps; i++) {
            // add 1/4 of 90 degrees per step
            // if dir is -1 then subtract instead of addition
            angle = originalAngle + dir*(90*(i+1))/steps;
            angle = (angle+1800) % 360;
            // draw method is called and uses angle field for direction
            // information.
            slowedDownRedraw(angle, 0);
        }
        // update maze direction only after intermediate steps are done
        // because choice of direction values are more limited.
        cd = CardinalDirection.getDirection(angle);
        logPosition(); // debugging
        drawHintIfNecessary();
    }

    /**
     * Draws and waits. Used to obtain a smooth appearance for rotate and move operations
     */
    private void slowedDownRedraw(int angle, int walkStep) {
        Log.v("slowed down redraw", "Drawing intermediate figures: angle " + angle + ", walkStep " + walkStep);
        draw(angle, walkStep);
        try {
            Thread.sleep(25);
        } catch (Exception e) {
//             may happen if thread is interrupted
//             no reason to do anything about it, ignore exception
        }
    }


    /**
     * Determines if one can walk in the given direction
     * @param dir is the direction of interest, either 1 or -1
     * @return true if there is no wall in this direction, false otherwise
     */
    protected boolean wayIsClear(int dir) {
        switch (dir) {
            case 1: // forward
                return !maze.hasWall(px, py, cd);
            case -1: // backward
                return !maze.hasWall(px, py, cd.oppositeDirection());
            default:
                throw new RuntimeException("Unexpected direction value: " + dir);
        }
    }

    private void logPosition() {
        int[] tmpDxDy = cd.getDxDyDirection();
        Log.v("Current Pos", "x=" + px + ",y="+ py+ ",cd=" + cd);
        //LOGGER.fine("x="+px+",y="+py+",dx="+dx+",dy="+dy+",angle="+angle);
    	/*
        if (!deepdebug)
            return;
        dbg("x="+viewx/Constants.MAP_UNIT+" ("+
                viewx+") y="+viewy/Constants.MAP_UNIT+" ("+viewy+") ang="+
                angle+" dx="+dx+" dy="+dy+" "+viewdx+" "+viewdy);
                */
    }


    /////////////////////// Get Methods ////////////////////////////////
    protected int[] getCurrentPosition() {
        int[] result = new int[2];
        result[0] = px;
        result[1] = py;
        return result;
    }
    protected CardinalDirection getCurrentDirection() {
        return cd;
    }
    boolean isInMapMode() {
        mapMode = map.isChecked();
        return mapMode ;
    }
    boolean isInShowMazeMode() {
        return showMaze ;
    }
    boolean isInShowSolutionMode() {
        showSolution = solution.isChecked();
        return showSolution ;
    }
    public Maze getMaze() {
        return maze ;
    }



}