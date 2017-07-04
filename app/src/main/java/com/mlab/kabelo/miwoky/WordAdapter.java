package com.mlab.kabelo.miwoky;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class WordAdapter extends ArrayAdapter<Word> {

    private static final String LOG_TAG = WordAdapter.class.getSimpleName();

    public WordAdapter(Activity context, ArrayList<Word> words) {
        super(context, 0,words);
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
            R.layout.list_item, parent, false);
        }

        Word currentWord = getItem(position);

        TextView englishTextView = (TextView) listItemView.findViewById(R.id.version_English);
        englishTextView.setText(currentWord.getVersionEnglish());


        TextView afrikaansTextView = (TextView) listItemView.findViewById(R.id.version_Afrikaans);
        afrikaansTextView.setText(currentWord.getVersionAfrikaans());


        ImageView iconView = (ImageView) listItemView.findViewById(R.id.list_item_icon);
        iconView.setImageResource(currentWord.getmImageResourceId());

        return listItemView;







    }
}
