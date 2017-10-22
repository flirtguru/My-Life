package com.example.hp.mylife;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by hp on 14-Oct-17.
 */

public class AddEvent extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_LOADER = 0;


    private Uri mCurrentUri;


    private EditText msubEditText;


    private EditText mdescripEditText;

    private Spinner mGenreSpinner;
    private int mGenre = LifeContracts.LifeEntry.GENRE_UNKNOWN;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editor);
        View.OnTouchListener mTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mHasChanged = true;
                return false;
            }
        };


        Intent intent = getIntent();
        mCurrentUri = intent.getData();
        if (mCurrentUri == null) {

            setTitle("AddEvent");


            invalidateOptionsMenu();
        } else {

            setTitle("Incident");


            getLoaderManager().initLoader(EXISTING_LOADER, null, null);
        }
        msubEditText = (EditText) findViewById(R.id.subject);
        mdescripEditText = (EditText) findViewById(R.id.descrip);
        mGenreSpinner = (Spinner) findViewById(R.id.spinner_genre);


        msubEditText.setOnTouchListener(mTouchListener);
        mdescripEditText.setOnTouchListener(mTouchListener);

        mGenreSpinner.setOnTouchListener(mTouchListener);

        setupSpinner();
    }

    private void setupSpinner() {


        ArrayAdapter genreSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);


        genreSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);


        mGenreSpinner.setAdapter(genreSpinnerAdapter);


        mGenreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("very good")) {
                        mGenre = LifeContracts.LifeEntry.GENRE_VERY_GOOD;
                    } else if (selection.equals("bad")) {
                        mGenre = LifeContracts.LifeEntry.GENRE_BAD_;
                    } else {
                        mGenre = LifeContracts.LifeEntry.GENRE_AVERAGE_;
                    }
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGenre = LifeContracts.LifeEntry.GENRE_UNKNOWN;
            }
        });
    }

    private int mGenre = LifeContracts.LifeEntry.GENRE_UNKNOWN;


    private boolean mHasChanged = false;

    private void savePet() {

        String descripString = mdescripEditText.getText().toString().trim();
        String subString = msubEditText.getText().toString().trim();



        if (mCurrentUri == null &&
                TextUtils.isEmpty(descripString) && TextUtils.isEmpty(subString) &&
                mGenre == LifeContracts.LifeEntry.GENRE_UNKNOWN) {

            return;
        }


        ContentValues values = new ContentValues();
        values.put(LifeContracts.LifeEntry.COLUMN_DESCRIPTION,descripString);
        values.put(LifeContracts.LifeEntry.COLUMN_SUBJECT, subString);
        values.put(LifeContracts.LifeEntry.COLUMN_GENRE, mGenre);




        if (mCurrentUri == null) {

            Uri newUri = getContentResolver().insert(LifeContracts.LifeEntry.CONTENT_URI, values);


            if (newUri == null) {

                Toast.makeText(this,"Failed",
                        Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, "successful",
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);


            if (rowsAffected == 0) {

                Toast.makeText(this, "update failed",
                        Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, "Update successful",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
               LifeContracts.LifeEntry._ID,
                LifeContracts.LifeEntry.COLUMN_GENRE,
                LifeContracts.LifeEntry.COLUMN_SUBJECT,
                LifeContracts.LifeEntry.COLUMN_DESCRIPTION};


        return new CursorLoader(this,
                mCurrentUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) {
            return;
        }


        if (cursor.moveToFirst()) {

            int descripColumnIndex = cursor.getColumnIndex(LifeContracts.LifeEntry.COLUMN_DESCRIPTION);
            int genreColumnIndex = cursor.getColumnIndex(LifeContracts.LifeEntry.COLUMN_GENRE);
            int subjectColumnIndex = cursor.getColumnIndex(LifeContracts.LifeEntry.COLUMN_SUBJECT);



            String descrip = cursor.getString(descripColumnIndex);
            String subject = cursor.getString(subjectColumnIndex);
            int genre = cursor.getInt(genreColumnIndex);


            mdescripEditText.setText(descrip);
            msubEditText.setText(subject);



            switch (genre) {
                case LifeContracts.LifeEntry.GENRE_VERY_GOOD:
                    mGenreSpinner.setSelection(1);
                    break;
                case LifeContracts.LifeEntry.GENRE_AVERAGE_:
                    mGenreSpinner.setSelection(2);
                    break;

                case LifeContracts.LifeEntry.GENRE_BAD_;
                default:
                    mGenreSpinner.setSelection(0);
                    break;


    }

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
           msubEditText.setText("");
            mdescripEditText.setText("");

            mGenreSpinner.setSelection(0);
        }

    }



}
    private void deletePet() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentUri != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);


            if (rowsDeleted == 0) {

                Toast.makeText(this, getString(R.string.editor_delete_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, getString(R.string.editor_delete_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }
}

