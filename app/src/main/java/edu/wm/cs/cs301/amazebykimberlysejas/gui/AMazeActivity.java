package edu.wm.cs.cs301.amazebykimberlysejas.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Random;

import edu.wm.cs.cs301.amazebykimberlysejas.R;

public class AMazeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button explore;
    private Button revisit;
    private Spinner spinner;

    private TextView planetSizeText;
    private SeekBar planetSizeBar;
    public int planetSizeNum;

    private CheckBox cratersChecked;

    private SharedPreferences mazePreferences;
    private SharedPreferences.Editor mazeEditor;
    private String planetType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amaze);
        mazePreferences = getSharedPreferences("mazePref",MODE_PRIVATE);
        exploreButtonOnClick();
        revisitButtonOnClick();
        spinnerPlanetSelection();
        skillBarSlider();
        craterSwitchCheckChange();

        //random seed



    }

    /*
    Switches to the GeneratingActivity when explore button is clicked and saves information about selected planet
     */
    private void exploreButtonOnClick(){
        Random rand = new Random();
        int seed = rand.nextInt();
        explore = findViewById(R.id.exploreB);
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AMazeActivity.this, "Explore button clicked! Going to new planet.", Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User clicked explore button");
                mazeEditor = mazePreferences.edit();
                mazeEditor.putString("planet", planetType);
                mazeEditor.putInt("size", planetSizeNum);
                mazeEditor.putBoolean("craters", cratersChecked.isChecked());
                mazeEditor.putInt("seed", seed);
                mazeEditor.commit();
//                Toast.makeText(AMazeActivity.this, "Information saved: "+ planetType + "," +planetSizeNum + "," + cratersChecked.isChecked() + "seed is " + seed, Toast.LENGTH_SHORT).show();
                Log.v("maze ", "New planet Chosen. Planet type: " + planetType + ", Planet Size: "+ planetSizeNum + ", Craters Checked: "+ cratersChecked.isChecked()+", Seed: "+ seed);
                Intent i = new Intent(AMazeActivity.this, GeneratingActivity.class);
                startActivity(i);
            }
        });
    }

    /*
    Switches to the GeneratingActivity when the user clicks on the revisit button by using an intent.
    Also, displays a toast message and outputs a Log.V indicating that the revisit button was clicked.
     */
    private void revisitButtonOnClick(){
        revisit = findViewById(R.id.revisitB);
        revisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lastPlanet = mazePreferences.getString("planet", "");
                int lastPlanetSize = mazePreferences.getInt("size", 0);
                boolean lastCratersChecked = mazePreferences.getBoolean("craters", false);
                int lastSeed = mazePreferences.getInt("seed", 0);
                Toast.makeText(AMazeActivity.this, "Revisit button clicked! Going to last planet." , Toast.LENGTH_SHORT).show();
                Log.v("buttonClicked", "User clicked revisit button");
                Log.v("maze", "Last planet chosen. Planet type: " + lastPlanet + ", Planet Size: "+ lastPlanetSize + ", Craters Checked: "+ lastCratersChecked +", Seed: "+ lastSeed);
                Intent i = new Intent(AMazeActivity.this, GeneratingActivity.class);
                startActivity(i);
            }
        });


    }
    /*
    Displays the drop down spinner selection for the user to select one of the planets (maze generation algorithms).
    Also, displays a toast message and outputs a Log.V indicating of the user's selection.
     */
    private void spinnerPlanetSelection(){
        spinner = findViewById(R.id.titleSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.algos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    /*
    Allows for the seekbar to progress from size to size indicating the planet size (maze skill level) the user chose.
    Changes the text displayed to the user of the current planet size.
    Also, displays a toast message and outputs a Log.V indicating the user's current planet size choice.
     */
    private void skillBarSlider(){
        planetSizeText = (TextView) findViewById(R.id.pSizeBarText);
        planetSizeBar = (SeekBar) findViewById(R.id.pSizeBar);
        planetSizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                planetSizeNum = progress;
                planetSizeText.setText("Planet Size:"+ progress);
                Toast.makeText(AMazeActivity.this, "Chosen Planet Size:  " + progress, Toast.LENGTH_SHORT).show();
                Log.v("skillBarProgress", "User chose a planet size of:  " + progress);

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    /*
    Creates a listener for the craters (rooms) checkbox that can change between craters mode on and off.
    A toast message and Log.V output will indicate the status of the switch and updates if changed.
     */
    private void craterSwitchCheckChange(){
        cratersChecked = findViewById(R.id.craterChecked);
        cratersChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cratersChecked.isChecked()){
                    cratersChecked.setChecked(true);
                    Toast.makeText(AMazeActivity.this, "Craters mode on", Toast.LENGTH_SHORT).show();
                    Log.v("switch", "User checked craters " );

                }
                else{
                    cratersChecked.setChecked(false);
                    Toast.makeText(AMazeActivity.this, "Craters mode off", Toast.LENGTH_SHORT).show();
                    Log.v("switch", "User unchecked craters " );

                }

            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        planetType = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(AMazeActivity.this, "Selected Planet: " + planetType, Toast.LENGTH_SHORT).show();
        Log.v("itemSelected", "User selected " + planetType + " planet");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }
}