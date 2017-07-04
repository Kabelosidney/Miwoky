package com.mlab.kabelo.miwoky;



public class Word {

    private String mVersionEnglish;

    private String getmVersionAfrikaans;

    private int mImageResourceId;
    private int mAudioResourceId;

    public Word(String vEnglish, String vAfrikaans, int imageResourceId, int audioResourceId)

    {
        mVersionEnglish = vEnglish;
        getmVersionAfrikaans = vAfrikaans;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResourceId;
    }

    public String getVersionEnglish()
    { return mVersionEnglish;}

    public String getVersionAfrikaans()
    { return getmVersionAfrikaans;}

    public int getmImageResourceId()
    { return mImageResourceId;}

    public int getmAudioResourceId()
    {return mAudioResourceId;}


}
