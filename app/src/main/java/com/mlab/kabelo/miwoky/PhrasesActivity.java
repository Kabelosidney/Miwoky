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

public class PhrasesActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_phrases);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Create  and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

      final   ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("are you coming","eenes'aa?", R.drawable.lets_talk,R.raw.phrase_are_you_coming));
        words.add(new Word("come here","enni'nem", R.drawable.lets_talk,R.raw.phrase_come_here));
        words.add(new Word("how are you feeling","michekses", R.drawable.lets_talk,R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I'm coming","eenem", R.drawable.lets_talk,R.raw.phrase_im_coming));
        words.add(new Word("I'm feeling good","kuchi achit", R.drawable.lets_talk,R.raw.phrase_im_feeling_good));
        words.add(new Word("let's go","yoowutis", R.drawable.lets_talk,R.raw.phrase_lets_go));
        words.add(new Word("my name is___","oyaaset___", R.drawable.lets_talk,R.raw.phrase_my_name_is));
        words.add(new Word("where are you going?","minto wuksus", R.drawable.lets_talk,R.raw.phrase_where_are_you_going));
        words.add(new Word("yes I'm coming","hee'eenem", R.drawable.lets_talk,R.raw.phrase_yes_im_coming));
      words.add(new Word("what is your name?","tinne oyaase'ne", R.drawable.lets_talk,R.raw.phrase_what_is_your_name));
       // words.add(new Word("I'm sad","ek is hartseer", R.drawable.talk));
       // words.add(new Word("who are you?","wie is jy", R.drawable.talk));
        //words.add(new Word("good luck","sterkte", R.drawable.talk));
        //words.add(new Word("speak to me ","praat met my", R.drawable.talk));
        //words.add(new Word("please","asseblief", R.drawable.talk));
        //words.add(new Word("leave me alone","los my uit", R.drawable.talk));
        //words.add(new Word("","dertien", R.drawable.talk));
        //words.add(new Word("i'm sorry","Ek is jammer", R.drawable.talk));
        //words.add(new Word("you are lying","jy lieg", R.drawable.talk));
        //words.add(new Word("happy birthday","gelukkige verjaarsdag", R.drawable.talk));
        //words.add(new Word("where do you stay","waar bly jy", R.drawable.talk));
        //words.add(new Word("how old are you","hoe oud is jy", R.drawable.talk));
        //words.add(new Word("see you tomorrow","sien jou more", R.drawable.talk));
        //words.add(new Word("what is your name","wat is jou naam", R.drawable.talk));



        WordAdapter adapter = new WordAdapter(this, words);

        ListView listView = (ListView) findViewById(R.id.listview_Phraises);

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
                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, word.getmAudioResourceId());


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
