package com.mepowerleo10.root.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 *TODO:Set the songs image as the default imageView in this screen
 */

public class SongHomeActivity extends AppCompatActivity
implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener,View.OnClickListener {

    private ImageButton play_pause;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private SeekBar songProgressBar;
    private TextView songTitleLabel;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    //Media player
    private  MediaPlayer mediaPlayer;

    int position;
    //Handler to update UI timer & progress bar
    private ArrayList<File> songsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_home);

        //Player buttons initialization
        play_pause = findViewById(R.id.button_play);
        nextButton = findViewById(R.id.button_next);
        prevButton = findViewById(R.id.button_prev);
        songProgressBar = findViewById(R.id.seekBar2);
        songTitleLabel = findViewById(R.id.textView_title);
        songCurrentDurationLabel = findViewById(R.id.textView_timeElapsed);
        songTotalDurationLabel = findViewById(R.id.textView_songLength);

        //MediaPlayer initialization
        mediaPlayer = new MediaPlayer();

        //Listeners
        songProgressBar.setOnSeekBarChangeListener(this);
        mediaPlayer.setOnCompletionListener(this);

        //Getting & bundling together all of the extras bound from MainActivity.java
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songsList = (ArrayList) bundle.getParcelableArrayList("songList");
        mediaPlayer = bundle.getParcelable("media");
        position = bundle.getInt("pos",0);

        Uri uri = Uri.parse(songsList.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        if(mediaPlayer.isPlaying()) {
            play_pause.setImageResource(R.drawable.ic_pause);
        } else {
            play_pause.setImageResource(R.drawable.ic_play);
        }
    }


    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public  void onPlayPause() {
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            play_pause.setImageResource(R.drawable.ic_pause);
        }
        else {
            mediaPlayer.pause();
            play_pause.setImageResource(R.drawable.ic_play);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.button_play:
                onPlayPause();
                break;

            case R.id.button_next:
                Uri uri = Uri.parse(songsList.get(position + 1).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                break;

            case R.id.button_prev:
                uri = Uri.parse(songsList.get(position - 1).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                break;
        }
    }
}
