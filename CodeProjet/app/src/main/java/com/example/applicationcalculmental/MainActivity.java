package com.example.applicationcalculmental;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button newGameBtn;
    private Button scoresBtn;
    private Button settingBtn;
    private Button aboutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.setContentView(R.layout.home_activity);

        newGameBtn = (Button) findViewById(R.id.newgame);
        scoresBtn = (Button) findViewById(R.id.scores);
        settingBtn = (Button) findViewById(R.id.settings);
        aboutBtn = (Button) findViewById(R.id.about);

        newGameBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openActivityNewGame();
            }
        });

        scoresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityScores();
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivitySettings();
            }
        });

        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAbout();
            }
        });
    }

    public void openActivityNewGame() {
        startActivity(new Intent(this, NewGameActivity.class));
    }

    public void openActivityScores() {
        startActivity(new Intent(this, ScoresActivity.class));
    }

    public void openActivitySettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void openActivityAbout() { startActivity(new Intent(this, AboutActivity.class)); }
}