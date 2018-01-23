package com.example.shaunchap.notr;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by shaunchap on 12/7/17.
 */
//seriable saves the state
public class NoteFile implements Serializable {
    private String mTitle;
    private String mContent;
    private long mDate;

    public NoteFile(long mDate, String mTitle, String mContent) {
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mDate = mDate;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public void setmDate(long mDate) {
        this.mDate = mDate;
    }

    public long getmDate() {
        return mDate;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public String getDateAsFormatted(Context context){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss"
                , context.getResources().getConfiguration().locale);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(mDate));
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
