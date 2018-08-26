package com.mepowerleo10.root.musicplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 *TODO:Set the songs image as the default imageView in this screen
 */

public class SongHomeActivity extends AppCompatActivity {

    ImageButton play_pause;
    int num = 0;
    public  void onPlayPause(View view) {

        play_pause = findViewById(R.id.button_play);
        if(num % 2 == 0) {
            play_pause.setImageResource(R.drawable.ic_pause);
        }
        else
            play_pause.setImageResource(R.drawable.ic_play);
        num++;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_home);
    }
}
