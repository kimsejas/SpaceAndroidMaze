package edu.wm.cs.cs301.amazebykimberlysejas.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import edu.wm.cs.cs301.amazebykimberlysejas.R;

public class PlayAnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);

    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(PlayAnimationActivity.this, AMazeActivity.class);
        startActivity(i);
    }


}