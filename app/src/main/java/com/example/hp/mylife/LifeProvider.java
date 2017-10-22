package com.example.hp.mylife;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by hp on 14-Oct-17.
 */

public class LifeProvider extends ContentProvider {


    public static final int MY_LIFE=0;
public static final int MY_LIFE_ID=1;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(LifeContracts.CONTENT_AUTHORITY,LifeContracts.PATH_LIFE,MY_LIFE);
         sUriMatcher.addURI(LifeContracts.CONTENT_AUTHORITY,LifeContracts.PATH_LIFE + "/#", MY_LIFE_ID);


    }
private LIfeDbHelper mdbhelper;


    @Override
    public boolean onCreate() {
        mdbhelper=new LIfeDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder)
    {
        SQLiteDatabase database=mdbhelper.getReadableDatabase();
        Cursor cursor;
        int match=sUriMatcher.match(uri);
switch (match)
{
    case MY_LIFE:
        cursor=database.query(LifeContracts.LifeEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
        break;

    case MY_LIFE_ID:
        selection= LifeContracts.LifeEntry._ID +"=?";
        selectionArgs= new String[] { String.valueOf(ContentUris.parseId(uri)) };
        cursor=database.query(LifeContracts.LifeEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
        break;
    default:
        throw new IllegalArgumentException("Cannot query unknown URI " + uri);
}
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {


int match =sUriMatcher.match(uri);
        switch(match)
        {
            case MY_LIFE: return LifeContracts.LifeEntry.CONTENT_LIST_TYPE;

            case MY_LIFE_ID :return LifeContracts.LifeEntry.CONTENT_ITEM_TYPE;

            default:  throw new IllegalStateException("Unknown URI " + uri + " with match " + match);


        }


    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match=sUriMatcher.match(uri);
        switch (match)
        {case MY_LIFE: return insertincident(uri,values);

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

    }

    private Uri insertincident(Uri uri,ContentValues values)
    {
        Integer  genre =values.getAsInteger(LifeContracts.LifeEntry.COLUMN_GENRE);
        if (genre==null) {
            throw new IllegalArgumentException("genre is empty");
        }
String sub=values.getAsString(LifeContracts.LifeEntry.COLUMN_SUBJECT);

        if (sub==null)
        {
            throw new IllegalArgumentException("NOT a valid subject");
        }

        String description=values.getAsString(LifeContracts.LifeEntry.COLUMN_DESCRIPTION);
        if(description==null)
        {
            throw new IllegalArgumentException("Invalid description");
        }

        SQLiteDatabase database=mdbhelper.getWritableDatabase();
        long id =database.insert(LifeContracts.LifeEntry.TABLE_NAME,null,values);


        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri,id);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match =sUriMatcher.match(uri);
        SQLiteDatabase database=mdbhelper.getWritableDatabase();
        int rowsDeleted;
        switch (match) {
            case MY_LIFE:
                rowsDeleted = database.delete(LifeContracts.LifeEntry.TABLE_NAME, selection, selectionArgs);
                 break;
            case MY_LIFE_ID:
                selection = LifeContracts.LifeEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(LifeContracts.LifeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("invalid Delete");
        }
                if(rowsDeleted!=0)
                    getContext().getContentResolver().notifyChange(uri, null);

return rowsDeleted;

        }



    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int match=sUriMatcher.match(uri);
        switch(match)
        {
            case MY_LIFE: return updateit(uri,values,selection,selectionArgs);

            case MY_LIFE_ID: selection=LifeContracts.LifeEntry._ID+"=?";

                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))} ;

                updateit(uri,values,selection,selectionArgs);


            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);


        }


    }
    public int updateit( Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        SQLiteDatabase database = mdbhelper.getWritableDatabase();


        int rowsUpdated = database.update(LifeContracts.LifeEntry.TABLE_NAME, values, selection, selectionArgs);


        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }





































}
