package com.prestech.babankilexicon.Utility;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

public class AudioManager {

    private static MediaPlayer mediaPlayer = null;
    private MediaListener mediaListener;
    private Context context;

    public AudioManager(Context context){
        this.context = context;
    }
    public void playAudio(String audioFile){

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();

        try {
            AssetFileDescriptor asd = context.getAssets().openFd("Kejom_Audio/"+audioFile.trim()+".mp3");

            mediaPlayer.setOnPreparedListener(new MediaListener());
            mediaPlayer.setDataSource(asd.getFileDescriptor(), asd.getStartOffset(), asd.getLength());
            mediaPlayer.prepareAsync();

        } catch (IOException e ) {
            e.printStackTrace();
        }


    }//createMedia() Ends


    private class MediaListener implements MediaPlayer.OnPreparedListener{

        @Override
        public void onPrepared(MediaPlayer mp) {
                mp.start();
        }//onPrepared() Ends

    }

}
