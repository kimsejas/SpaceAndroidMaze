package edu.wm.cs.cs301.amazebykimberlysejas.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Arrays;
import java.util.Objects;

import edu.wm.cs.cs301.amazebykimberlysejas.R;
import edu.wm.cs.cs301.amazebykimberlysejas.generation.CardinalDirection;
import edu.wm.cs.cs301.amazebykimberlysejas.generation.Floorplan;
import edu.wm.cs.cs301.amazebykimberlysejas.generation.Maze;
import edu.wm.cs.cs301.amazebykimberlysejas.generation.ReliableRobot;
import edu.wm.cs.cs301.amazebykimberlysejas.generation.ReliableSensor;
import edu.wm.cs.cs301.amazebykimberlysejas.generation.UnreliableRobot;
import edu.wm.cs.cs301.amazebykimberlysejas.generation.UnreliableSensor;
import edu.wm.cs.cs301.amazebykimberlysejas.generation.WallFollower;
import edu.wm.cs.cs301.amazebykimberlysejas.generation.Wizard;

public class PlayAnimationActivity extends AppCompatActivity {
    private ToggleButton map;
    private ToggleButton solution;
    private ToggleButton walls;
    public static Maze maze;

    private ImageButton playPause;
    public boolean onPlay = true;

    private SeekBar speedBar;
    private TextView speedBarText;
    private long curSpeed = 1;

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

    RobotDriver robotDriver;
    Robot robot;
    DistanceSensor sensor;
    private Handler handler;
    boolean robotFailed = false;
    boolean unreliableRobot = false;
    String condition;

    private ProgressBar energyBar;
    private TextView energyText;

    private Object pauseLock;
    private ImageView forwardSensor;
    private ImageView rightSensor;
    private ImageView backwardSensor;
    private ImageView leftSensor;

    boolean result = true;
    MediaPlayer player;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);

        mapButton();
        showSolutionButton();
        wallsButton();
        zoomInClick();
        zoomOutClick();
        speedBarSlider();
        setSensorDisplay();
        setMaze();
        setPanel();
        setRobot();
        energyBar();
        playPauseClick();
        start(panel);
        handler = new Handler(Looper.getMainLooper());
        if (unreliableRobot){
            startSensors((UnreliableRobot) robot, condition);
        }
        player = MediaPlayer.create(this, R.raw.interstellar);
        player.setLooping(true);
        player.start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        driverAnimation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
    }


    /**
     * Provides the maze to play.
     */
    public void setMaze() {
        maze = GeneratingActivity.maze;
        Log.v("play animation maze", "maze height: " + maze.getHeight() + " maze width: " + maze.getWidth());
    }
    /**
     * Sets panel for game screen
     */
    public void setPanel(){
        panel = (MazePanel) findViewById(R.id.gameScreen2);
    }


    /*
    Sets up the sensor display so that it can be updated if needed
     */
    public void setSensorDisplay(){
        forwardSensor = findViewById(R.id.forwardSensor);
        rightSensor = findViewById(R.id.rightSensor);
        backwardSensor = findViewById(R.id.backwardsensor);
        leftSensor = findViewById(R.id.leftSensor);
    }


    /**
     * Sets robot for game
     */
    public void setRobot(){
        Intent i = getIntent();
        String roverType = i.getStringExtra("Robot");
        condition = i.getStringExtra("Condition");
        if (Objects.equals(roverType, "Wallfollower")){
            if (Objects.equals(condition, "Premium")){
                robotDriver = new WallFollower(); //TODO change
                robot = new ReliableRobot();
                robot.setMaze(maze);
                sensor = new ReliableSensor(); //TODO change
                sensor.setMaze(maze);
                robotDriver.setMaze(maze);
                robotDriver.setRobot(robot);

            }else{
                robotDriver = new WallFollower();
                robot = new UnreliableRobot();
                robot.setMaze(maze);
                sensor = new UnreliableSensor();
                sensor.setMaze(maze);
                robotDriver.setMaze(maze);
                robotDriver.setRobot(robot);
                unreliableRobot = true;

            }
        }else if (Objects.equals(roverType, "Wizard")){
            robotDriver = new Wizard();
            robot = new ReliableRobot();
            robot.setMaze(maze);
            sensor = new ReliableSensor();
            sensor.setMaze(maze);
            robotDriver.setMaze(maze);
            robotDriver.setRobot(robot);
        }
    }

    /**
     * Private helper method for setting the robot to start the sensor's failure and repair process based on the condition chosen
     * @param condition (mediocre, soso, shaky)
     * @param robot unreliable robot
     */
    private void startSensors(UnreliableRobot robot, String condition){
//        boolean forward = true;
//        boolean right = true;
//        boolean backward = true;
//        boolean left = true;
        if (Objects.equals(condition, "Mediocre")){
//            right = false;
//            left = false;
            robot.startFailureAndRepairProcess(Robot.Direction.LEFT,4000,2000);
            try {
                Thread.sleep(1300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.startFailureAndRepairProcess(Robot.Direction.RIGHT,4000,2000);


        }
        else if (Objects.equals(condition, "Soso")){
//            forward = false;
//            backward = false;

            robot.startFailureAndRepairProcess(Robot.Direction.FORWARD,4000,2000);
            try {
                Thread.sleep(1300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.startFailureAndRepairProcess(Robot.Direction.BACKWARD,4000,2000);

        }else if (Objects.equals(condition, "Shaky")){
//            forward = false;
//            right = false;
//            backward = false;
//            left = false;

            robot.startFailureAndRepairProcess(Robot.Direction.LEFT,4000,2000);
            try {
                Thread.sleep(1300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.startFailureAndRepairProcess(Robot.Direction.RIGHT, 4000, 2000);

            try {
                Thread.sleep(1300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.startFailureAndRepairProcess(Robot.Direction.FORWARD, 4000, 2000);

            try {
                Thread.sleep(1300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.startFailureAndRepairProcess(Robot.Direction.BACKWARD, 4000, 2);
        }





//        if (left == false){
//            robot.startFailureAndRepairProcess(Robot.Direction.LEFT,4000,2000);
//
//        }
//        if (right == false) {
//            try {
//                Thread.sleep(1300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            robot.startFailureAndRepairProcess(Robot.Direction.RIGHT, 4000, 2000);
//
//        }
//        if (forward== false) {
//            try {
//                Thread.sleep(1300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            robot.startFailureAndRepairProcess(Robot.Direction.FORWARD, 4000, 2000);
//
//        }
//        if (backward == false) {
//            try {
//                Thread.sleep(1300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            robot.startFailureAndRepairProcess(Robot.Direction.BACKWARD, 4000, 2000);
//
//        }

    }



    /**
    Toggle button for map on or off that displays Toast and Log.v messages
     */
    private void mapButton(){
        map = (ToggleButton) findViewById(R.id.mapB);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(PlayAnimationActivity.this, "Map mode: "+ map.getText(), Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(PlayAnimationActivity.this, "Show Solution mode: "+ solution.getText(), Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(PlayAnimationActivity.this, "Walls Up: "+ walls.getText(), Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(PlayAnimationActivity.this, "Zoomed in +", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(PlayAnimationActivity.this, "Zoomed out -", Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User zoomed out ");
                mapView.decrementMapScale();
                draw(cd.angle(), 0) ;
            }
        });

    }

    /**
    Switches between playing and pausing animation by updating image button
     */
    private void playPauseClick(){
        playPause = (ImageButton) findViewById(R.id.playPauseB);
        playPause.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             if (onPlay) {
                 //animation is happening and user wants to pause animation
                 playPause.setBackgroundResource(R.drawable.playbutton);
//                 Toast.makeText(PlayAnimationActivity.this, "Animation paused", Toast.LENGTH_SHORT).show();
                 Log.v("buttonClicked", "User paused animation");
                 synchronized (pauseLock){
                     onPlay = false;

                 }

             } else {
                 //animation is paused and user wants to start animation
                 playPause.setBackgroundResource(R.drawable.pausebutton);
                 Log.v("buttonClicked", "User started animation");
                 synchronized (pauseLock){
                     onPlay = true;
                     pauseLock.notifyAll();

                 }

             }
         }
     });
    }


    private void energyBar(){
        energyBar = (ProgressBar) findViewById(R.id.energyBar);
        energyText = (TextView) findViewById(R.id.energyText);

    }



    /**
     * Begins robot driving to exit and update's maze panel (game screen)
     */
    private void driverAnimation(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                pauseLock = new Object();
                while(result){
                    try {
                        Thread.sleep(50*(10-curSpeed));
                        result = robotDriver.drive1Step2Exit();
                        cd = robot.getCurrentDirection();
                        px = robot.getCurrentPosition()[0];
                        py = robot.getCurrentPosition()[1];
                        logPosition();
                    } catch (Exception Exception) {
//                        robot crashed/died
                        robotFailed = true;
                        break;
                    }
                    synchronized (pauseLock){
                        while(!onPlay){
                            try {
                                pauseLock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (showMaze){
                                draw(cd.angle(), 0);
                            }
                            else{
                                panel.addBackground(maze.getPercentageForDistanceToExit(px, py));
                                panel.commit();
                            }
                            if (unreliableRobot){
                                updateSensorDisplay(leftSensor,robot.getSensorOperational(Robot.Direction.LEFT) );
                                updateSensorDisplay(rightSensor,robot.getSensorOperational(Robot.Direction.RIGHT) );
                                updateSensorDisplay(forwardSensor,robot.getSensorOperational(Robot.Direction.FORWARD) );
                                updateSensorDisplay(backwardSensor,robot.getSensorOperational(Robot.Direction.BACKWARD) );
                            }
                            energyBar.setProgress((int)robot.getBatteryLevel());
                            energyText.setText("Remaining Energy: " + robot.getBatteryLevel());
                        }
                    });
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (unreliableRobot){
                            robot.stopFailureAndRepairProcess(Robot.Direction.LEFT);
                            robot.stopFailureAndRepairProcess(Robot.Direction.RIGHT);
                            robot.stopFailureAndRepairProcess(Robot.Direction.FORWARD);
                            robot.stopFailureAndRepairProcess(Robot.Direction.BACKWARD);
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (robotFailed){
                            Intent i = new Intent(PlayAnimationActivity.this, LosingActivity.class);
                            i.putExtra("From", "Animation");
                            i.putExtra("Pathlength", robot.getOdometerReading());
                            i.putExtra("Battery used", 3500 - robot.getBatteryLevel());
                            startActivity(i);
                            finish();
                        }else{
                            Log.v("Game won", "Robot reached exit! Switch to winning");
                            panel.addBackground(1);
                            panel.commit();
                            Intent i = new Intent(PlayAnimationActivity.this, WinningActivity.class);
                            i.putExtra("From", "Animation");
                            i.putExtra("Pathlength", robot.getOdometerReading()+1);
                            i.putExtra("Battery used", 3500 - robot.getBatteryLevel());
                            startActivity(i);
                            finish();
                        }
                    }
                });

            }
        }).start();

    }

    /**
     * Private helper function to update the sensor display during gameplay. Only used if an unreliable robot is used.
     * @param operationalState the current state of the sensor
     * @param sensorView the sensor imageview that will be updated
     */
    private void updateSensorDisplay(ImageView sensorView, boolean operationalState){
        if (operationalState){
            sensorView.setImageResource(R.drawable.workingsensor);
        }else{
            sensorView.setImageResource(R.drawable.brokensensor);
        }

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
                curSpeed = (long)  speed ;
                speedBarText.setText("Speed "+ speed +"x");
//                Toast.makeText(PlayAnimationActivity.this, "Current speed:  " + speed + "x", Toast.LENGTH_SHORT).show();
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
        PlayAnimationActivity.panel = panel;
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
        robot.setCurrentPosition(start[0], start[1]);
        cd = CardinalDirection.East;
        robot.setCurrentDirection(cd);
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
        else{
            if (maze.isFacingDeadEnd(px, py, cd)) {
//            System.out.println("Facing deadend, help by showing solution");
                mapView.draw(panel, px, py, cd.angle(), 0, true, true) ;
            }
            else {
                // draw compass rose
                cr.setCurrentDirection(cd);
                cr.paintComponent(panel);
            }
        }
        // update the screen with the buffer graphics
        panel.commit() ;
    }

    /**
     * Checks if the given position is outside the maze
     * @param x coordinate of position
     * @param y coordinate of position
     * @return true if position is outside, false otherwise
     */
    private boolean isOutside(int x, int y) {
        return !maze.isValidPosition(x, y) ;
    }

    /**
     * Switches the controller to the final screen
     * @param pathLength gives the length of the path
     */
    public void switchFromPlayingToFinal(int pathLength) {
        Log.v("Game end", "Switching from playing to winning screen");
//        Toast.makeText(PlayManuallyActivity.this, "Game ended. You win!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(PlayAnimationActivity.this, WinningActivity.class);
        i.putExtra("From", "Animation");
        i.putExtra("Pathlength", robot.getOdometerReading());
        startActivity(i);
        finish();
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
        pathLength+=1;
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
     * 1 is left, -1 is right
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
        robot.setCurrentDirection(cd);
        logPosition(); // debugging
        drawHintIfNecessary();
    }

    /**
     * Draws and waits. Used to obtain a smooth appearance for rotate and move operations
     */
    public void slowedDownRedraw(int angle, int walkStep) {
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
    }


    /////////////////////// Get Methods ////////////////////////////////
    public int[] getCurrentPosition() {
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