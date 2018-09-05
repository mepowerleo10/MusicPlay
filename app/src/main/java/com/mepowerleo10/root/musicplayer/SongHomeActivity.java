package com.mepowerleo10.root.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

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
    private SeekBar seekBar;
    private TextView songTitleLabel, songArtistLabel;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    public Handler handler;
    public Runnable runnable;

    //Media player
    public static  MediaPlayer mediaPlayer;
    int position;
    Uri uri;
    private ArrayList<Song> songsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_home);


        //Player buttons initialization
        play_pause = findViewById(R.id.button_play);
        nextButton = findViewById(R.id.button_next);
        prevButton = findViewById(R.id.button_prev);
        seekBar = findViewById(R.id.seekBar2);
        songTitleLabel = findViewById(R.id.textView_title);
        songArtistLabel = findViewById(R.id.textView_artist);
        songCurrentDurationLabel = findViewById(R.id.textView_timeElapsed);
        songTotalDurationLabel = findViewById(R.id.textView_songLength);

        position = getIntent().getIntExtra("position", 0);
        songsList = new SongManager(this, getContentResolver()).getList();

        //MediaPlayer initialization
        uri = Uri.parse(songsList.get(position).getPath());
        mediaPlayer = MediaPlayer.create(this, uri);
        play_pause.setImageResource(R.drawable.ic_pause);
        songArtistLabel.setText(songsList.get(position).getArtist());
        songTitleLabel.setText(songsList.get(position).getTitle());
        songTotalDurationLabel.setText(String.valueOf(songsList.get(position).getDuration() / 1000 * 60 * 60));
        mediaPlayer.start();

        handler = new Handler();

        //Listeners
        seekBar = findViewById(R.id.seekBar2);
        setSeekBar();
        mediaPlayer.setOnCompletionListener(this);


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


    public void setSeekBar() {
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                playCyle();
            }
        });
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                    mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void playCyle() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());

        if(mediaPlayer.isPlaying()){
            runnable = new Runnable() {
                @Override
                public void run() {
                    playCyle();
                    mediaPlayer.start();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
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
        handler.removeCallbacks(runnable);
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
                songArtistLabel.setText(songsList.get(position).getArtist());
                songTitleLabel.setText(songsList.get(position).getTitle());
                songTotalDurationLabel.setText( String.valueOf(songsList.get(position).getDuration() / 1000 * 60 * 60));
                uri = Uri.parse(songsList.get(position).getPath());
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
                songArtistLabel.setText(songsList.get(position).getArtist());
                songTitleLabel.setText(songsList.get(position).getTitle());
                songTotalDurationLabel.setText(String.valueOf(songsList.get(position).getDuration() / 1.667 * 0.000001));
                uri = Uri.parse(songsList.get(position).getPath());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
