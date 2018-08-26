package com.mepowerleo10.root.musicplayer;

public class Utilities {
    /*
    * Functions to convert milliseconds time to
    * Timer format
    * Min:Sec
    * */

    public final String millisecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondString = "";

        //Converting the total duration to time
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int)((milliseconds % (1000*60*60)) % (1000*60) /1000);
        if(minutes > 0) {
            finalTimerString = minutes + ":";
        }

        if(seconds < 10) {
            secondString = "0" + seconds;
        } else {
            secondString = "" + seconds;
        }
        finalTimerString = finalTimerString + secondString;
        return  finalTimerString;
    }

    /*
    * Function to get Progress percentage
    * @param currentDuration
    * @param totalDuration
    * */
    public int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;
        long currentSeconds = (int)(currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        //calculating percentage
        percentage = (((double)currentSeconds)/totalSeconds)*100;

        return percentage.intValue();
    }

    /*
    * Function to change progress to timer
    * @param progress -
    * @param totalDuration
    * returns current duration in milliseconds
    * */
    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int)(totalDuration / 1000);
        currentDuration = (int)((((double)progress)/100)*totalDuration);

        //return current duration in milliseconds
        return  currentDuration * 1000;
    }
}
