package com.mepowerleo10.root.musicplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 *TODO:Set the songs image as the default imageView in this screen
 */

public class SongHomeActivity extends AppCompatActivity {

    int num = 0;
    public  void onPlayPause(View view) {
        if(num % 2 == 0)
            view.setBackgroundResource(R.drawable.ic_pause);
        else
            view.setBackgroundResource(R.drawable.ic_play);
        num++;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_home);
    }
}
