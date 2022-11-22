package edu.wm.cs.cs301.amazebykimberlysejas.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import edu.wm.cs.cs301.amazebykimberlysejas.R;

public class GeneratingActivity extends AppCompatActivity {
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


    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(GeneratingActivity.this, AMazeActivity.class);
        startActivity(i);
    }

    /*
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



    /*
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

    /*
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