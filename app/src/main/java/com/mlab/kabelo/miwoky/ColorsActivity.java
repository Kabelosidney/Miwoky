package com.mlab.kabelo.miwoky;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {


    //handles the playback of all sound files\
    private MediaPlayer mMediaPlayer;

    //handles audio focus when playing a sound file
    private AudioManager mAudioManager;

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {

                //pause playback and reset player to the start of the file . That way ,we can
                //play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

                //the audio_gain case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {

                //the audiofocus_loss case means we've lost audio focus and
                //stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    //This listener get triggered when the MediaPlayer has completed playing the audio
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            //Now that the sound file has finished playing , release the media player resources.
            releaseMediaPlayer();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Create  and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

      final   ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("black","kululli", R.drawable.black,R.raw.color_black));
        words.add(new Word("brown","takaakki", R.drawable.brown,R.raw.color_brown));
        words.add(new Word("dusty yellow","topiise", R.drawable.dustyyello,R.raw.color_dusty_yellow));
        words.add(new Word("gray","topoppi", R.drawable.gray,R.raw.color_gray));
        words.add(new Word("green","chokokki", R.drawable.green,R.raw.color_green));
        words.add(new Word("mustard yellow","chiwiite", R.drawable.mustard_yellow,R.raw.color_mustard_yellow));
        words.add(new Word("red","wetetti", R.drawable.red,R.raw.color_red));
        words.add(new Word("white","kelelli", R.drawable.white,R.raw.color_white));
        //words.add(new Word("","brown", R.drawable.brown));
        //words.add(new Word("white","wit", R.drawable.white));
        //words.add(new Word("maroon","maroen", R.drawable.maroon));
        //words.add(new Word("pink","pink", R.drawable.pink));
        //words.add(new Word("grey","gray", R.drawable.grey));
        //words.add(new Word("lavender","laventel", R.drawable.lavender));
        //words.add(new Word("navy blue","navy blou", R.drawable.navy));
        //words.add(new Word("apricot","appelkoos", R.drawable.apricot));
        //words.add(new Word("mustard","mosterd", R.drawable.mustard));
        //words.add(new Word("peach","peach", R.drawable.peach));
        //words.add(new Word("pear","peer", R.drawable.pear));
        //words.add(new Word("gold","goud", R.drawable.gold1));
        //words.add(new Word("silver","silwer", R.drawable.silver));
        //words.add(new Word("bronze","brons", R.drawable.bronze));
        //words.add(new Word("platinum","platinum", R.drawable.platinum));




        WordAdapter adapter = new WordAdapter(this, words);

        ListView listView = (ListView) findViewById(R.id.listview_Colors);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //release the media player if it currently exists because we are about to
                //play a different sound file
                releaseMediaPlayer();

                Word word = words.get(position);
                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback
                    mMediaPlayer = MediaPlayer.create(ColorsActivity.this, word.getmAudioResourceId());


                    mMediaPlayer.start();

                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        //When the activity is stopped ,releases the media player resources because we won't
        //be playing any more sounds.
        releaseMediaPlayer();
    }

    //clean up the media by releasing its resources.
    private void releaseMediaPlayer() {

        if (mMediaPlayer != null) {
            mMediaPlayer.release();

            mMediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            //unregisters the AudioFocusChangeListener so we don't gey anyone callbacks
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

}
