package com.prestech.babankilexicon.Utility;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

public class AudioManager {

    private static MediaPlayer mediaPlayer = null;
    private Context context;
    private MediaListener mediaListener;

    public AudioManager(Context context) {
        this.context = context;
        mediaListener = new MediaListener();
    }

    public void playAudio(String audioFile) {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();

        try {
            AssetFileDescriptor asd = context.getAssets().openFd("Kejom_Audio/" + audioFile.trim() + ".mp3");

            mediaPlayer.setOnPreparedListener(mediaListener);
            mediaPlayer.setDataSource(asd.getFileDescriptor(), asd.getStartOffset(), asd.getLength());
            mediaPlayer.prepareAsync();
            asd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer = null;

    }//createMedia() Ends


    private class MediaListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();

            /**
             * resolved bug here with help from
             * https://stackoverflow.com/questions/49388645/getting-error-e-mediaplayer-error-1-19-when-trying-to-stop-and-play-sound-a
             */
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.release();
                }
            });

        }//onPrepared() Ends

    }

}
