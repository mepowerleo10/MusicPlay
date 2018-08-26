package com.mepowerleo10.root.musicplayer;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class SongManager {

    public ArrayList<File> getList(File root) {
        ArrayList<File> musicList = new ArrayList<File>();
        File[] files = root.listFiles();
        for(File oneFile : files) {
            if(oneFile.isDirectory() && !oneFile.isHidden()) {
                musicList.addAll(getList(oneFile));
            } else {
                if(oneFile.getName().endsWith(".mp3")) {
                    Log.d("File: ", "Added "+oneFile.getName());
                    musicList.add(oneFile);
                }
            }
        }
        return  musicList;
    }
}
