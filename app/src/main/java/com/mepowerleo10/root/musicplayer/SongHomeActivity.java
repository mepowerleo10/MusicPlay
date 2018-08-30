package com.mepowerleo10.root.musicplayer;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *TODO:Set the songs image as the default imageView in this screen
 */

public class SongHomeActivity extends AppCompatActivity
implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener,View.OnClickListener {

    //Buttons
    private ImageButton play_pause;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private SeekBar songProgressBar;
    private TextView songTitleLabel;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    public Handler handler;

    //Media player
    public static  MediaPlayer mediaPlayer;
    int position;
    Uri uri;
    Bundle bundle;
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

        bundle = getIntent().getExtras();
        position = bundle.getInt("position");
        songsList = (ArrayList) bundle.getParcelableArrayList("musicList");

        //MediaPlayer initialization
        uri = Uri.parse(songsList.get(position).getPath());
        mediaPlayer = MediaPlayer.create(this,uri);
        play_pause.setImageResource(R.drawable.ic_pause);
        mediaPlayer.start();

        handler = new Handler();

        //Listeners
        songProgressBar = findViewById(R.id.seekBar2);
        songProgressBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.setOnCompletionListener(this);
        SongHomeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    songProgressBar.setProgress(mediaPlayer.getCurrentPosition() / 1000);
                }
                handler.postDelayed(this, 1000);
            }
        });

        songProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


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


    /*
    * The function sets the images of the play/pause button
    * */
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
        Intent intent = new Intent();
        intent.putExtra("position", position);
        intent.putExtra("cur_position", mediaPlayer.getCurrentPosition());
        mediaPlayer.release();
        mediaPlayer = null;
        setResult(RESULT_OK, intent);
        finish();
    }

    /*
    * @param v(View)
    * */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.button_play:
                onPlayPause();
                break;

            case R.id.button_next:
                mediaPlayer.reset();
                position = (position + 1) % songsList.size();
                Uri uri = Uri.parse(songsList.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                break;

            case R.id.button_prev:
                mediaPlayer.reset();
                if(position == 0)
                    position = songsList.size() - 1;
                else
                    position -= 1;
//                pos = (pos - 1) % songsList.size();
                uri = Uri.parse(songsList.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                break;
        }
    }

}
