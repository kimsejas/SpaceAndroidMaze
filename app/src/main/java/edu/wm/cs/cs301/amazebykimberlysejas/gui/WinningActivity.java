package edu.wm.cs.cs301.amazebykimberlysejas.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edu.wm.cs.cs301.amazebykimberlysejas.R;

public class WinningActivity extends AppCompatActivity {

    private TextView length;
    private TextView shortestLength;
    private TextView energyConsumed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);
        energyDisplayed();
        pathLengthDisplayed();
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(WinningActivity.this, AMazeActivity.class);
        startActivity(i);
    }



    /*
    Customizes the text views related to path length to gameplay
     */
    private void pathLengthDisplayed(){
        Intent i = getIntent();
        String prev = i.getStringExtra("From");

        length = (TextView) findViewById(R.id.length);
        shortestLength = (TextView) findViewById(R.id.shortestlength);


        if (prev.equals("Animation")){
            length.setText("Path length taken: 70");
            shortestLength.setText("Shortest path length possible: 70");
        }else if (prev.equals("Manual")){
            length.setText("Path length taken: 100");
            shortestLength.setText("Shortest path length possible: 70");
        }

    }

    /*
      Customizes the text views related to energy consumption to gameplay
   */
    private void energyDisplayed(){
        energyConsumed =  (TextView) findViewById(R.id.energyConsumed);
        Intent i = getIntent();
        String prev = i.getStringExtra("From");

        if (prev.equals("Animation")){
            energyConsumed.setText("Total Energy Consumed: 1,500");
        }else if (prev.equals("Manual")){
            energyConsumed.setVisibility(View.INVISIBLE);
        }
    }
}