package com.mepowerleo10.root.musicplayer;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.GestureDetector;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class SongManager {

    private final Context context;
    private  ContentResolver contentResolver;
    SongManager(Context context, ContentResolver contentResolver) {
        this.context = context;
        this.contentResolver = contentResolver;
    }
    public ArrayList<Song> getList() {
        ArrayList<Song> musicList = new ArrayList<>();
        /*for(MediaStore.Audio.Media oneFile ) {
            if(oneFile.isDirectory() && !oneFile.isHidden()) {
                musicList.addAll(getList(oneFile));
            } else {
                if(oneFile.getName().endsWith(".mp3")) {
                    Log.d("File: ", "Added "+oneFile.getName());
                    musicList.add(oneFile);
                }
            }
        }*/

        String artist, title, genre, path;
        long duration, id;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri,null,null, null, null);
        uri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
        Cursor genCursor = contentResolver.query(uri, null, null, null, null);
        int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
        int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
        int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        int pathColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        int durColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
        int genreColumn = genCursor.getColumnIndex(MediaStore.Audio.Genres.NAME);

        if(cursor == null)
            Toast.makeText(context, "Query failed!", Toast.LENGTH_LONG).show();
        else if(!cursor.moveToNext())
            Toast.makeText(context, "No media found in phone!", Toast.LENGTH_LONG).show();
        else {
            do {
                artist = cursor.getString(artistColumn);
                title = cursor.getString(titleColumn);
                path = cursor.getString(pathColumn);
                duration = cursor.getLong(durColumn);
                id = cursor.getLong(idColumn);
                //genre = genCursor.getString(genreColumn);
                Song song = new Song(id, artist, duration, title, path);
                musicList.add(song);
                //genCursor.moveToNext();
            }while (cursor.moveToNext());
        }
        return  musicList;
    }
}
