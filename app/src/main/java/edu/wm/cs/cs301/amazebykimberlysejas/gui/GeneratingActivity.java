package edu.wm.cs.cs301.amazebykimberlysejas.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.util.Objects;

import edu.wm.cs.cs301.amazebykimberlysejas.R;
import edu.wm.cs.cs301.amazebykimberlysejas.generation.DefaultOrder;
import edu.wm.cs.cs301.amazebykimberlysejas.generation.Maze;
import edu.wm.cs.cs301.amazebykimberlysejas.generation.MazeFactory;
import edu.wm.cs.cs301.amazebykimberlysejas.generation.Order;

public class GeneratingActivity extends AppCompatActivity {
    public static Maze maze;
    private ProgressBar mazeProgressBar;
    private int curProgress = 0;
    private TextView mazeProgressBarText;
    private Handler mHandler = new Handler();

    public RadioGroup roverTypeGroup;

    private RadioGroup conditionGroup;

    private Boolean threadFinished = false;
    private Boolean playManuallySelected = false;
    private Boolean playAnimationSelected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generating);

        mazeGenerationProgressBar();

        roverTypeGroup = findViewById(R.id.radioGroup);

        conditionGroup = findViewById(R.id.conditionGroup);

        mazeConfiguration();


    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(GeneratingActivity.this, AMazeActivity.class);
        startActivity(i);
    }

    /**
    Displays and runs the progress bar to show the user the maze generating progression. Creates a thread that runs in the background
    of the activity to simulate the maze generation.
    When progress bar is finished it will display messages to either remind the user to select
    buttons or that the game will begin soon.
    Switches to either PlayAnimationActivity or PlayManuallyActivity if certain conditions are met.
     */
    private void mazeGenerationProgressBar() {
        mazeProgressBar = (ProgressBar) findViewById(R.id.mazeProgressBar);
        mazeProgressBarText = (TextView) findViewById(R.id.mazeProgressBarText);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (curProgress <= 100) {
                    curProgress += 1;
                    android.os.SystemClock.sleep(200);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mazeProgressBar.setProgress(curProgress);

                        }
                    });
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        threadFinished = true;
                        mazeProgressBarText.setText("Arrived!");
                        if (!playAnimationSelected & !playManuallySelected){
                            Toast.makeText(GeneratingActivity.this, "Please select Rover type", Toast.LENGTH_SHORT).show();
                        }
                        else if (playManuallySelected){
                            Toast.makeText(GeneratingActivity.this, "Rover is ready. Game will begin soon!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(GeneratingActivity.this, PlayManuallyActivity.class);
                            startActivity(i);
                        }
                        else if(playAnimationSelected){
                            if (conditionGroup.getCheckedRadioButtonId()  == -1){
                                Toast.makeText(GeneratingActivity.this, "Please select Rover condition", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(GeneratingActivity.this, "Rover is ready. Game will begin soon!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(GeneratingActivity.this, PlayAnimationActivity.class);
                                startActivity(i);
                            }
                        }
                    }
                });

            }
        }).start();


    }


    /**
     * Creating maze configuration????
     */
    private void mazeConfiguration(){
        Intent i = getIntent();
        int builderSkillLevel = i.getIntExtra("size", 0);
        boolean builderRooms = i.getBooleanExtra("craters", false);
        int builderSeed = i.getIntExtra("seed", 13);
        String builderType = i.getStringExtra("planet");
        Order.Builder builder = Order.Builder.DFS;
        if (Objects.equals(builderType, "PRIM")){
            builder = Order.Builder.Prim;
        }else if (Objects.equals(builderType, "BORUVKA")){
            builder = Order.Builder.Boruvka;
        }
        MazeFactory mazeFactory = new MazeFactory();
        DefaultOrder order = new DefaultOrder(builderSkillLevel, builder, builderRooms, builderSeed);
        mazeFactory.order(order);
        mazeFactory.waitTillDelivered();
        maze = order.getMaze();
        Log.v("config ", "Planet type: " + builder + ", Planet Size: "+ builderSkillLevel + ", Craters Checked: "+ builderRooms+", Seed: "+ builderSeed);

        //testing if maze was properly made
        Log.v("generating maze", "maze height: " + maze.getHeight() + " maze width: " + maze.getWidth());

    }



    /**
    Function for selecting rover type called in generating xml to indicate which radio button from the radio group was selected.
    Sets the radioId to the button that the user picked, displays a toast message, and outputs a Log.v message.
    Keeps track of the type of rover selected (manual or automatic).
    Switches to either PlayAnimationActivity or PlayManuallyActivity if maze generation is complete and other conditions are met.
     */
    public void checkButton(View v) {
        int radioId = roverTypeGroup.getCheckedRadioButtonId();
        RadioButton roverTypeButton = findViewById(radioId);
        Toast.makeText(GeneratingActivity.this, "Selected Rover Type: " + roverTypeButton.getText(), Toast.LENGTH_SHORT).show();
        Log.v("buttonSelected", "User selected " + roverTypeButton.getText() + " rover type");

        if (roverTypeButton.getId() == R.id.wizardB || roverTypeButton.getId() == R.id.wallfollowerB){
            playAnimationSelected = true;
            playManuallySelected = false;

        }
        else if(roverTypeButton.getId() == R.id.manualB){
            playManuallySelected = true;
            playAnimationSelected = false;
        }

        if (threadFinished){
            if (playManuallySelected){
                Intent i = new Intent(GeneratingActivity.this, PlayManuallyActivity.class);
                startActivity(i);
            } else if (playAnimationSelected){
                if (conditionGroup.getCheckedRadioButtonId()  == -1){
                    Toast.makeText(GeneratingActivity.this, "Please select Rover condition", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(GeneratingActivity.this, "Rover is ready. Game will begin soon!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(GeneratingActivity.this, PlayAnimationActivity.class);
                    startActivity(i);
                }
            }
        }

    }

    /**
    Function for selecting rover condition called in generating xml to indicate which radio button from the radio group was selected.
    Sets the radioId to the button that the user picked, displays a toast message,and outputs a Log.v message.
    Switches to PlayAnimationActivity if maze generation is complete and an animation driver was selected.
     */
    public void checkConditionButton(View v){
        int radioId = conditionGroup.getCheckedRadioButtonId();
        RadioButton conditionButton = findViewById(radioId);

        Toast.makeText(GeneratingActivity.this, "Selected Rover Condition: " + conditionButton.getText(), Toast.LENGTH_SHORT).show();
        Log.v("buttonSelected", "User selected " + conditionButton.getText() + " rover condition");

        if (threadFinished){
            if (playAnimationSelected){
                Toast.makeText(GeneratingActivity.this, "Rover is ready. Game will begin soon!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(GeneratingActivity.this, PlayAnimationActivity.class);
                startActivity(i);
            }
        }


    }




}