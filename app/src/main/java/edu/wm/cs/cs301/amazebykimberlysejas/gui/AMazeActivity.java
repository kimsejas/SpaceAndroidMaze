package edu.wm.cs.cs301.amazebykimberlysejas.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import edu.wm.cs.cs301.amazebykimberlysejas.R;

public class AMazeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button explore;
    private Button revisit;
    private Spinner spinner;

    private TextView skillBarText;
    private SeekBar skillBar;
    public int skillBarNum;

    private Switch craterSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amaze);

        exploreButtonOnClick();
        revisitButtonOnClick();
        spinnerPlanetSelection();
        skillBarSlider();
        craterSwitchCheckChange();
    }

    /*
    Switches to the GeneratingActivity when the user clicks on the explore button by using an intent.
    Also, displays a toast message and outputs a Log.V indicating that the explore button was clicked.
     */
    private void exploreButtonOnClick(){
        explore = findViewById(R.id.exploreB);
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AMazeActivity.this, "Explore button clicked!", Toast.LENGTH_LONG).show();
                Log.v("buttonClicked", "User clicked explore button");
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
                Toast.makeText(AMazeActivity.this, "Revisit button clicked!", Toast.LENGTH_LONG).show();
                Log.v("buttonClicked", "User clicked revisit button");
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
        skillBarText = (TextView) findViewById(R.id.pSizeBarText);
        skillBar = (SeekBar) findViewById(R.id.pSizeBar);
        skillBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                skillBarNum = progress;
                skillBarText.setText("Planet Size:"+ progress);
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
    Creates a listener for the craters (rooms) switch that can change between craters mode on and off.
    A toast message and Log.V output will indicate the status of the switch and updates if changed.
     */
    private void craterSwitchCheckChange(){
        craterSwitch = findViewById(R.id.craterSwitch);

        craterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(AMazeActivity.this, "Craters mode on", Toast.LENGTH_SHORT).show();
                    Log.v("switch", "User checked craters " );

                }else{
                    Toast.makeText(AMazeActivity.this, "Craters mode off", Toast.LENGTH_SHORT).show();
                    Log.v("switch", "User unchecked craters " );
                }

            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(AMazeActivity.this, "Selected Planet: " + text, Toast.LENGTH_SHORT).show();
        Log.v("itemSelected", "User selected " + text + " planet");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }
}