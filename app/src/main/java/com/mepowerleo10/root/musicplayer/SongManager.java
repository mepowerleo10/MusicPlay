package com.mepowerleo10.root.musicplayer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class SongManager {

    //SDCard path
    final String MEDIA_PATH = new String("/sdcard/");
    private ArrayList<HashMap<String, String>> songsList =
            new ArrayList<HashMap<String, String>>();

    //Constructor
    public SongManager() {

    }


    /*
    * Function to read all mp3 files from sdcard
    * and store the details in ArrayList
    * */

    public ArrayList<HashMap<String, String>> getSongsList() {

        File home = new File(MEDIA_PATH);

        if(home.listFiles(new FileExtensionFilter()).length > 0) {
            for(File file : home.listFiles(new FileExtensionFilter())) {
                HashMap<String, String> song = new HashMap<String, String>();
                song.put("songTitle", file.getName().substring(0,(file.getName().length() - 4)));
                song.put("songPath", file.getPath());

                //adding the song fetched to SongList list-hashmap
                songsList.add(song);
            }
            return songsList;
        }

        return songsList;
    }

    /*
    * Class to filter files having .mp3 extension
    * */
    class FileExtensionFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }
}
