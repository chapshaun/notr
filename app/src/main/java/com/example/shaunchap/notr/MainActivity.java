package com.example.shaunchap.notr;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mListViewNotes;
    private EditText mTitle;
    private EditText mContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListViewNotes = (ListView) findViewById(R.id.notes_listview);
        mTitle = (EditText) findViewById(R.id.notes_title);
        mContent = (EditText) findViewById(R.id.notes_content);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.notr_title);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setDefault();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_main_create_note:
                Intent newNoteActivity = new Intent(this, NoteActivity.class);
                startActivity(newNoteActivity);
                break;
        }
        return true;
    }

    //getting all the notes in the array
    @Override
    protected void onResume() {
        super.onResume();
        mListViewNotes.setAdapter(null);

        ArrayList<NoteFile> notes = Options.getAllSavedNotes(this);

        if(notes == null || notes.size() == 0){
            Toast.makeText(this, "No saved notes", Toast.LENGTH_SHORT).show();
            createDefault();
            return;
        }else{
            NoteAdapter na = new NoteAdapter(this, R.layout.item_note, notes);
            mListViewNotes.setAdapter(na);

            mListViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String fileName = ((NoteFile)mListViewNotes.getItemAtPosition(position)).getmDate() + Options.FILE_EXTENSION;

                    Intent viewNoteIntent = new Intent(getApplicationContext(), NoteActivity.class);
                    viewNoteIntent.putExtra("NOTE_FILE", fileName);
                    startActivity(viewNoteIntent);
                }
            });
        }

    }

    //create a default note if there are no notes available
    public void createDefault(){
        NoteFile note;

        note = new NoteFile(System.currentTimeMillis(), "Welcome to Notr! This is a Title.",
                "This is the content of the Note. Date created is below.");

        Options.saveNote(this, note);
        Toast.makeText(this, "Default note created", Toast.LENGTH_SHORT).show();
        return;
    }

    //displaying default note
    public void setDefault(){
        mTitle.setText("Welcome to Notr! This is a Title.", EditText.BufferType.NORMAL);
        mContent.setText("This is the content of the Note. Date created is below.", EditText.BufferType.NORMAL);

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

