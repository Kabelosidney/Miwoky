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

public class FamilyMembersActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_family_members);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Create  and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

      final   ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("daughter","tune", R.drawable.daughter_m,R.raw.family_daughter));
        words.add(new Word("father","epe", R.drawable.father_m,R.raw.family_father));
        words.add(new Word("grandfather","paaptsa", R.drawable.grandfather_m,R.raw.family_grandfather));
        words.add(new Word("grandmother","ama", R.drawable.grandmother_m,R.raw.family_grandmother));
        words.add(new Word("mother","eta", R.drawable.mother_m,R.raw.family_mother));
        words.add(new Word("older brother","taachi", R.drawable.olderbrother_m,R.raw.family_older_brother));
        words.add(new Word("older sister","tete", R.drawable.oldersister_m,R.raw.family_older_sister));
        words.add(new Word("son","angsi", R.drawable.son_m,R.raw.family_son));
        words.add(new Word("younger brother","chalitti", R.drawable.yongerbrother_m,R.raw.family_younger_brother));
        words.add(new Word("younger sister","kolliti", R.drawable.youngersister_m,R.raw.family_younger_sister));
        //words.add(new Word("","", R.drawable.boy));
        //words.add(new Word("son","seun", R.drawable.boy));
        //words.add(new Word("daughter","dogter", R.drawable.niece));
        //words.add(new Word("sister-in-law","skoonsuster", R.drawable.sister));
        //words.add(new Word("brother-in-law","swaer", R.drawable.brother));
        //words.add(new Word("girl","meisie", R.drawable.niece));
       // words.add(new Word("boy","seuntjie", R.drawable.boy));
        //words.add(new Word("baby","baba", R.drawable.baby));







        WordAdapter adapter = new WordAdapter(this, words);

        ListView listView = (ListView) findViewById(R.id.listview_Family);

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
                    mMediaPlayer = MediaPlayer.create(FamilyMembersActivity.this, word.getmAudioResourceId());


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
