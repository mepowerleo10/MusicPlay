package com.mepowerleo10.root.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    public ArrayList<File> songsList = new ArrayList<>();


    ListView playlist;
    ImageButton play_pause;
    String[] musicList;
    int pos = 0;
    MediaPlayer mediaPlayer;
    Uri uri;

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
    }
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

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //fetch all song from the sdcard
        SongManager manager = new SongManager();
        songsList = manager.getList(Environment.getExternalStorageDirectory());
        musicList = new String [songsList.size()];
        for(int i = 0; i < songsList.size(); i++) {

            //customize the songs for the playlist-view
            musicList[i] = songsList.get(i).getName().toString().replace(".mp3","");
        }

        //Populate the listview
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.playlist, R.id.song_view, musicList);
        playlist = findViewById(R.id.play_list);
        playlist.setAdapter(adapter);

        //Listening to item on listview click-event
        play_pause = findViewById(R.id.button_play);
        playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;

                //Parse the item's location to a URI
                uri = Uri.parse(songsList.get(position).toString());
                if(mediaPlayer == null) {
                    //The player has come from a destroyed process
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    mediaPlayer.start();
                    play_pause.setImageResource(R.drawable.ic_pause);
                } else if(!mediaPlayer.isPlaying()) {
                    //The player was paused
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    mediaPlayer.start();
                    play_pause.setImageResource(R.drawable.ic_pause);
                } else {
                    //Another song on the listview has been selected, stop the previous one
                    mediaPlayer.stop();
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    mediaPlayer.start();
                }
            }
        });

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     *clicking the playing song's name on this screen leads to the SongHomeActivity
     */
    public void onClickSong(View view) {
        //Bundling together all information from this context
        startActivity(new Intent(getApplicationContext(),SongHomeActivity.class)
                    .putExtra("pos", pos).putExtra("songList", songsList));
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
                Uri uri = Uri.parse(songsList.get(pos + 1).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                break;

            case R.id.button_prev:
                uri = Uri.parse(songsList.get(pos - 1).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                break;
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
