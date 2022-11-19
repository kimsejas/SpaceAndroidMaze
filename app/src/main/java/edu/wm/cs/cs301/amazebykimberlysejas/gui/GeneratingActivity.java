package edu.wm.cs.cs301.amazebykimberlysejas.gui;

import androidx.appcompat.app.AppCompatActivity;

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

    private RadioGroup radioGroup;
    private RadioButton radioButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generating);

//        mazeGenerationProgressBar();

//        radioGroup = findViewById(R.id.radioGroup);




    }

//    /*
//    Displays and runs the progress bar to show the user the maze generating progression. Creates a thread that runs in the background
//    of the activity to simulate the maze generation.
//     */
//    private void mazeGenerationProgressBar(){
//        mazeProgressBar = (ProgressBar) findViewById(R.id.mazeProgressBar);
//        mazeProgressBarText = (TextView) findViewById(R.id.mazeProgressBarText);
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(curProgress <= 100){
//                    curProgress +=1;
//                    android.os.SystemClock.sleep(500);
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mazeProgressBar.setProgress(curProgress);
//
//                        }
//                    });
//                }
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mazeProgressBarText.setText("Arrived!");
//                    }
//                });
//            }
//        }).start();
//
//    }
//
//    /*
//    Function called in generating xml to indicate which radio button from the radio group was selected.
//    Sets the radioId to the button that the user picked, displays a toast message, and outputs a Log.v message.
//     */
//    public void checkButton(View v){
//        int radioId = radioGroup.getCheckedRadioButtonId();
//        radioButton = findViewById(radioId);
//
//        Toast.makeText(GeneratingActivity.this, "Selected Rover Type: "+ radioButton.getText(), Toast.LENGTH_SHORT).show();
//        Log.v("buttonSelected", "User selected " + radioButton.getText()+ " rover type");
//
//    }
}