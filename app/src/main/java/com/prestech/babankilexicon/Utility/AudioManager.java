package com.prestech.babankilexicon.Utility;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class AudioManager {

    private static MediaPlayer mediaPlayer = null;
    private MediaListener mediaListener;
    private Context context;

    public AudioManager(Context context){
        this.context = context;
    }
    public void playAudio(String audioFile){

        Log.d("PRESDEBUG", "Preparing to play audio for \""+audioFile+"\"");

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();

        try {
            AssetFileDescriptor asd = context.getAssets().openFd("Kejom_Audio/"+audioFile.trim()+".mp3");

            if(asd != null) {
                mediaPlayer.setOnPreparedListener(new MediaListener());
                mediaPlayer.setDataSource(asd.getFileDescriptor(), asd.getStartOffset(), asd.getLength());
                mediaPlayer.prepareAsync();
            }else{
                Log.d("PRESDEBUG", "Audio is not available for \""+audioFile+"\"");

            }

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
