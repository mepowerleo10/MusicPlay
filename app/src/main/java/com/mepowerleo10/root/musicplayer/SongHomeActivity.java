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
    int cur_pos;
    Uri uri;
    MediaPlayer.TrackInfo mediaInfo;
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
        position = bundle.getInt("pos",0);
        cur_pos = bundle.getInt("cur_pos");

        uri = Uri.parse(songsList.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        if(bundle.getBoolean("is_playing")) {
            mediaPlayer.seekTo(cur_pos);
            mediaPlayer.start();
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
