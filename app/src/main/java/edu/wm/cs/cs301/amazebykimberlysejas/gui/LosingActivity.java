package edu.wm.cs.cs301.amazebykimberlysejas.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edu.wm.cs.cs301.amazebykimberlysejas.R;

public class LosingActivity extends AppCompatActivity {
    private TextView length;
    private TextView shortestLength;
    private TextView energyConsumed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losing);
        pathLengthDisplayed();
        energyDisplayed();
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(LosingActivity.this, AMazeActivity.class);
        startActivity(i);
    }

    /**
    Customizes the text views related to path length to gameplay
     */
    private void pathLengthDisplayed(){
        Intent i = getIntent();
        String prev = i.getStringExtra("From");
        int pathlength = i.getIntExtra("Pathlength", 0);

        length = (TextView) findViewById(R.id.length);
        shortestLength = (TextView) findViewById(R.id.shortestlength);


        if (prev.equals("Animation")){
            length.setText("Path length taken: " + pathlength);
            shortestLength.setText("Initial distance to exit: " + GeneratingActivity.maze.getDistanceToExit(GeneratingActivity.maze.getStartingPosition()[0],GeneratingActivity.maze.getStartingPosition()[1]));
        }else if (prev.equals("Manual")){
            length.setText("Path length taken: " + pathlength);
            shortestLength.setText("Initial distance to exit: " + GeneratingActivity.maze.getDistanceToExit(GeneratingActivity.maze.getStartingPosition()[0],GeneratingActivity.maze.getStartingPosition()[1]));
        }

    }

    /**
    Customizes the text views related to energy consumption to gameplay
     */
    private void energyDisplayed(){
        energyConsumed =  (TextView) findViewById(R.id.energyConsumed);
        Intent i = getIntent();
        String prev = i.getStringExtra("From");

        if (prev.equals("Animation")){
            float energyUsed = i.getFloatExtra("Battery used", 3500);
            energyConsumed.setText("Total Energy Used: " + energyUsed);
        }else if (prev.equals("Manual")){
            energyConsumed.setVisibility(View.INVISIBLE);
        }
    }
}