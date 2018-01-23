package com.example.shaunchap.notr;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shaunchap on 12/9/17.
 */

public class NoteAdapter extends ArrayAdapter<NoteFile>{


    public NoteAdapter(@NonNull Context context, int resource, @NonNull ArrayList<NoteFile> notes) {
        super(context, resource, notes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_note, null);
        }

        NoteFile note = getItem(position);

        if(note != null) {
            TextView title = (TextView) convertView.findViewById(R.id.list_title_note);
            TextView date = (TextView) convertView.findViewById(R.id.list_date_note);
            TextView content = (TextView) convertView.findViewById(R.id.list_content_note);

            title.setText(note.getmTitle());
            date.setText(note.getDateAsFormatted(getContext()));

            int maxChar = 50;
            int lineBreaks =  1 * (note.getmContent().indexOf('\n'));

            if(note.getmContent().length() > maxChar || lineBreaks < maxChar){
                if(lineBreaks < maxChar){
                    maxChar = lineBreaks;
                }
                if(maxChar > 0){
                    content.setText(note.getmContent().substring(0, maxChar) + "\n......");
                }else{
                    content.setText(note.getmContent());
                }
            }else{
                content.setText(note.getmContent());
            }
        }
        return convertView;
    }
}

// RESOURCES: Giving credit, for where I found some code that helped me.

// Saving txt files: https://www.youtube.com/watch?v=BnYruBLqdmM
// Launching camera intent: https://www.youtube.com/watch?v=je9bdkdNQqg
// SQLite DB Insertion: https://www.youtube.com/watch?v=T0ClYrJukPA
// Saving Notes as objects: https://github.com/EMBEDONIX/
// https://stackoverflow.com/questions/3674933/find-out-if-android-device-is-portrait-or-landscape-for-normal-usage
// https://stackoverflow.com/questions/10539268/making-two-linearlayouts-have-50-of-the-screen-each-without-using-layout-weight/10539489
// https://developer.android.com/training/sharing/shareaction.html
// https://developer.android.com/guide/topics/media/camera.html
