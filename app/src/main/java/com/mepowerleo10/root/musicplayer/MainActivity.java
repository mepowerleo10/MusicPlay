package com.mepowerleo10.root.musicplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    public ArrayList<Song>  songsList = new ArrayList<>();
    User user;
    DataBaseHelper dataBaseHelper;


    TextView nameField, emailField;
    ImageButton play_pause;
    int position = 0;
    public static MediaPlayer mediaPlayer;
    Uri uri;
    Intent intent;
    TextView song_label;
 //   ContentResolver contentResolver = getContentResolver();

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
        dataBaseHelper = new DataBaseHelper(this);
        nameField = findViewById(R.id.nameField);
        emailField = findViewById(R.id.emailField);


        Bundle bundle = getIntent().getExtras();
        user = new User();
        if(bundle != null) {
            String email = bundle.getString("email");
            user = dataBaseHelper.getUser(email);
            Log.d("Email", email);
            Log.d("User", user.getUname());
            Log.d("User", user.getEmail());

            NavigationView navigationView = findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            nameField = headerView.findViewById(R.id.nameField);
            emailField = headerView.findViewById(R.id.emailField);
            nameField.setText("I am " + user.getUname());
            emailField.setText(user.getEmail());
        }

        //Initializing the music list
        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        if(isStoragePermissionGranted()) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if(cursor == null) {
                Toast.makeText(this, "Query failed",Toast.LENGTH_LONG).show();
            } else if(!cursor.moveToFirst()) {
                Toast.makeText(this, "Found no media", Toast.LENGTH_LONG).show();
            } else {
                SongManager manager = new SongManager(this, getContentResolver());
                songsList = manager.getList();
            }
        }


        //The navigation Drawer menu
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //The music listView
        RecyclerView playlist = findViewById(R.id.play_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        playlist.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new MyAdapter(this, this, songsList, getContentResolver());
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
        intent.putExtra("cur_position", mediaPlayer.getCurrentPosition());
        mediaPlayer.release();
        mediaPlayer = null;

        startActivity(intent);
    }

    public  void onPlayPause() {

        uri = Uri.parse(songsList.get(position).getPath());
        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, uri);
            song_label.setText(songsList.get(position).getTitle());
            mediaPlayer.start();
            play_pause.setImageResource(R.drawable.ic_pause);
        }
        if(!mediaPlayer.isPlaying()) {
            song_label.setText(songsList.get(position).getTitle());
            mediaPlayer.start();
            play_pause.setImageResource(R.drawable.ic_pause);
        }
        else {
            song_label.setText(songsList.get(position).getTitle());
            mediaPlayer.pause();
            play_pause.setImageResource(R.drawable.ic_play);
        }
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
                Uri uri = Uri.parse(songsList.get(position).getPath());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                song_label.setText(songsList.get(position).getTitle());
                mediaPlayer.start();
                break;

            case R.id.button_prev:
                mediaPlayer.reset();
                if(position == 0)
                    position = songsList.size() - 1;
                else
                    position -= 1;
                uri = Uri.parse(songsList.get(position).getPath());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                song_label.setText(songsList.get(position).getTitle());
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
        } else if( id == R.id.login) {
            /*if(mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }*/
            startActivity(new Intent(this,LoginActivity.class));
        } else if(id == R.id.about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if(id == R.id.exit) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "This is the best app for Music playback" +
                    "\n https://play.google.com/store/apps/details?id=com.mepowerleo10.root.musicplayerhl=en");
            sharingIntent.setPackage("com.whatsapp");
            startActivity(sharingIntent);

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    /**
     * On the event of clicking the login button from the side menu
     * One should be taken to the settings activity
     */

    public  boolean isStoragePermissionGranted() {
        String TAG = "Permission";
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
}
