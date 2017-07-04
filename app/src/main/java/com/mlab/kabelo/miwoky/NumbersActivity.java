package com.mlab.kabelo.miwoky;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.AdapterView.*;

public class
NumbersActivity extends AppCompatActivity {


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
        setContentView(R.layout.activity_numbers);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Create  and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        //Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("one", "lutti", R.drawable.one, R.raw.number_one));
        words.add(new Word("two", "otliko", R.drawable.two, R.raw.number_two));
        words.add(new Word("three", "tolookosu", R.drawable.three, R.raw.number_three));
        words.add(new Word("four", "oyyisa", R.drawable.four, R.raw.number_four));
        words.add(new Word("five", "massokka", R.drawable.five, R.raw.number_five));
        words.add(new Word("six", "temmokka", R.drawable.six, R.raw.number_six));
        words.add(new Word("seven", "kenekaku", R.drawable.seven, R.raw.number_seven));
        words.add(new Word("eight", "kawinta", R.drawable.eight, R.raw.number_eight));
        words.add(new Word("nine", "wo'e", R.drawable.nine, R.raw.number_nine));
        words.add(new Word("ten", "na'aacha", R.drawable.ten, R.raw.number_ten));
        //words.add(new Word("thirteen","dertien", R.drawable.thirteen13));
        //words.add(new Word("fourteen","veertien", R.drawable.fourteen14));
        //words.add(new Word("fifteen","vyftien", R.drawable.fifteen15));
        //words.add(new Word("sixteen","sestien", R.drawable.sixteen16));
        //words.add(new Word("seventeen","sewentien", R.drawable.seventeen17));
        //words.add(new Word("eighteen","agtien", R.drawable.eighteen18));
        //words.add(new Word("nineteen","negentien", R.drawable.nineteen));
        //words.add(new Word("twenty","twintig", R.drawable.twenty20));


        //creates an {@link wordAdapter},whose data source is a list of {@link words.
        WordAdapter adapter = new WordAdapter(this, words);
        //wordlist lay out .xml
        ListView listView = (ListView) findViewById(R.id.listview_Number);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
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
                    mMediaPlayer = MediaPlayer.create(NumbersActivity.this, word.getmAudioResourceId());


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
