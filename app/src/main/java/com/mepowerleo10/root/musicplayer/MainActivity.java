package com.mepowerleo10.root.musicplayer;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    public ArrayList<File> songsList = new ArrayList<File>();


    ImageButton play_pause;
    String[] musicList;
    int position = 0;
    MediaPlayer mediaPlayer;
    Uri uri;
    Intent intent;
    Bundle bundle;
    MediaMetadataRetriever mediaInfo;
    TextView artist, song_label;
    /*AnimationDrawable animationDrawable;
    ConstraintLayout super_layout;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        song_label = findViewById(R.id.textView_title);
        play_pause = findViewById(R.id.button_play);
        //fetch all song from the sdcard
        SongManager manager = new SongManager();
        songsList = manager.getList(Environment.getExternalStorageDirectory());
        musicList = new String [songsList.size()];
        for(int i = 0; i < songsList.size(); i++) {

            //customize the songs for the playlist-view
            musicList[i] = songsList.get(i).getName().replace(".mp3","");
        }

        uri = Uri.parse(songsList.get(position).getPath());
        /*super_layout = findViewById(R.id.super_layout);
        animationDrawable = (AnimationDrawable) super_layout.getBackground();
            animationDrawable.setEnterFadeDuration(4000);
            animationDrawable.setExitFadeDuration(4000);
            animationDrawable.start();*/

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //The music listView
        RecyclerView playlist = findViewById(R.id.play_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        playlist.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new MyAdapter(this, this, songsList);
        playlist.setAdapter(adapter);

        song_label.setSelected(true);


    }


    /*
     *clicking the playing song's name on this screen leads to the SongHomeActivity
     **/
    public void onClickSong(View view) {
        //Bundling together all information from this context
        intent = new Intent(getApplicationContext(),SongHomeActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("musicList", songsList);
        intent.putExtra("cur_positon", mediaPlayer.getCurrentPosition());
        mediaPlayer.release();
        mediaPlayer = null;
        startActivity(intent);
    }

    public  void onPlayPause() {
        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
            play_pause.setImageResource(R.drawable.ic_pause);
        }
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            play_pause.setImageResource(R.drawable.ic_pause);
        }
        else {
            mediaPlayer.pause();
            play_pause.setImageResource(R.drawable.ic_play);
        }
        song_label.setText(songsList.get(position).getName());
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
                song_label.setText(songsList.get(position).getName());
                mediaPlayer.start();
                break;

            case R.id.button_prev:
                mediaPlayer.reset();
                if(position == 0)
                    position = songsList.size() - 1;
                else
                    position -= 1;
                uri = Uri.parse(songsList.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                song_label.setText(songsList.get(position).getName());
                mediaPlayer.start();
                break;
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult ", "ResCode: "+resultCode +", ReqCode: "+requestCode);
        if(requestCode == 619 && requestCode == 0) {
            uri = Uri.parse(songsList.get(data.getIntExtra("position",0)).getPath());
            Log.d("Main: ", "Received "+uri.toString());
            mediaPlayer.create(this,uri);
            mediaPlayer.seekTo(data.getIntExtra("cur_position",0));
            mediaPlayer.start();
        }
    }


    /**
     * On the event of clicking the login button from the side menu
     * One should be taken to the settings activity
     */
/*    public void onClickLogin(MenuItem item) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }*/
}
