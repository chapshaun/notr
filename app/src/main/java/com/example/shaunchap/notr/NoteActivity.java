package com.example.shaunchap.notr;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NoteActivity extends AppCompatActivity {

    private EditText mTitle;
    private EditText mContent;
    private String mNoteFileName;
    private NoteFile mLoadedNote;
    private static final String PATH = "/storage/emulated/0/Notr/";
    private ShareActionProvider mShareActionProvider;

    DatabaseHelper mDatabaseHelper;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_main);
        } else {
            setContentView(R.layout.activity_note);
        }

        mTitle = (EditText) findViewById(R.id.notes_title);
        mContent = (EditText) findViewById(R.id.notes_content);
        imageView = (ImageView) findViewById(R.id.image_view);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.notr_title);

        mDatabaseHelper = new DatabaseHelper(this);

        mNoteFileName = getIntent().getStringExtra("NOTE_FILE");
        if(mNoteFileName != null && !mNoteFileName.isEmpty()){
            mLoadedNote = Options.getNoteByName(this, mNoteFileName);

            if(mLoadedNote != null){
                mTitle.setText(mLoadedNote.getmTitle());
                mContent.setText(mLoadedNote.getmContent());
            }
        }

        //Asking permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }
    }

    //for exporting txt file to phone
    private void saveTxtFile(String title, String content){
        String fileName = title + ".txt";

        //create
        File file = new File(PATH, fileName);

        //write
        try{
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
            Toast.makeText(this, "File exported!", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_new, menu);

        return true;
    }

    //switches from the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            //sharing switch - check to see if title or content is empty first
            case R.id.action_note_share:
                if(mTitle.getText().toString().trim().isEmpty() || mContent.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, "Title and Notes can't be blank",
                            Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,  mTitle.getText().toString() + "\n\n"
                            + mContent.getText().toString());
                    sendIntent.setType("text/plain");

                    startActivity(sendIntent);
                    break;
                }

            //save switch
            case R.id.action_note_save:
                saveNote();
                break;

            //delete switch
            case R.id.action_note_delete:
                deleteNote();
                break;

            case R.id.action_note_camera:
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //only worked with marshmellow
//                File file = getFile();
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

                startActivityForResult(cameraIntent, 1);

                break;

            case R.id.action_note_export:
                saveNote();

                if(!mTitle.getText().toString().equals("") && !mContent.getText().toString().equals("")){
                    saveTxtFile(mTitle.getText().toString(), mContent.getText().toString());
                }

                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }

        //Only worked with marshmellow
//        imageView.setImageDrawable(Drawable.createFromPath(PATH + mTitle.getText().toString() + ".jpg"));
    }

    //method of getting picture from saved file path
    private File getFile(){
        File folder = new File(PATH);

        if(!folder.exists()){
            folder.mkdir();
        }

        File image_file = new File(folder,mTitle.getText().toString() + ".jpg");

        return image_file;
    }

    @Override
    //if the back button is pressed
    public void onBackPressed() {
        if(mTitle.getText().toString().trim().isEmpty() || mContent.getText().toString().trim().isEmpty()){
            finish();
        }else{
            saveNote();
        }
    }

    //method for saving notes
    private void saveNote(){
        NoteFile note;
        String process;

        if(mTitle.getText().toString().trim().isEmpty() || mContent.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Title and Content can't be blank", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mLoadedNote == null){ //new note
            note = new NoteFile(System.currentTimeMillis(), mTitle.getText().toString(),
                    mContent.getText().toString());
            process = "saved";

            //Database entry:
            long dateEntry = System.currentTimeMillis();
            String titleEntry = mTitle.getText().toString();
            String contentEntry = mContent.getText().toString();
            AddData(dateEntry, titleEntry, contentEntry, process);

        }else{ //already saved note
            note = new NoteFile(mLoadedNote.getmDate(), mTitle.getText().toString(),
                    mContent.getText().toString());
            process = "updated";

            //Database entry:
            long dateEntry = mLoadedNote.getmDate();
            String titleEntry = mTitle.getText().toString();
            String contentEntry = mContent.getText().toString();
            AddData(dateEntry, titleEntry, contentEntry, process);
        }


        if(Options.saveNote(this, note)){
//            Toast.makeText(this, "Note " + process, Toast.LENGTH_SHORT).show();
        }else{
//            Toast.makeText(this, "Note not " + process, Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    //adding method for database
    public void AddData(long date, String title, String content, String actionWord){
        boolean insertData = mDatabaseHelper.addData(date, title, content);

        if(insertData){
            Toast.makeText(this, "Note " + actionWord + " in Database", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Note not " + actionWord + " in Database", Toast.LENGTH_SHORT).show();
        }
    }

    //deleting method
    private void deleteNote() {
        if(mLoadedNote == null){
            finish();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setMessage("You are about to delete " + mTitle.getText().toString() + " are you sure?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Options.deleteNote(getApplicationContext()
                                    , mLoadedNote.getmDate() + Options.FILE_EXTENSION);
                            Toast.makeText(getApplicationContext()
                                    , mTitle.getText().toString() + " was deleted.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .setCancelable(false);
            dialog.show();
        }
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
