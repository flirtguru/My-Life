package com.example.hp.mylife;

/**
 * Created by hp on 14-Oct-17.
 */

import android.content.ContentProvider;
import android.net.Uri;
import android.provider.BaseColumns;



/**
 * Created by hp on 14-Oct-17.
 */

public class LifeContracts {

    private LifeContracts(){}


    public static final String  CONTENT_AUTHORITY="com.example.hp.mylife";

    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_LIFE="mylife";
    public static class LifeEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_LIFE);

        public static final String CONTENT_LIST_TYPE= ContentProvider.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_LIFE;

        public static final String CONTENT_ITEM_TYPE=ContentProvider.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_LIFE;

        public static final String TABLE_NAME="mylife";




        public static final String _ID =BaseColumns._ID;

        public static final String COLUMN_GENRE="Genre";

        public static final String COLUMN_SUBJECT="Subject";

        public static final String COLUMN_DESCRIPTION="Description";
        public static final int GENRE_UNKNOWN=0;

        public static final int GENRE_VERY_GOOD=1;

        public static final int GENRE_AVERAGE_=2;

        public static final int GENRE_BAD_=3;
        public static boolean isvalidgenre(int genre)
        {
            if ((genre == GENRE_VERY_GOOD) || (genre == GENRE_AVERAGE_) || genre==GENRE_BAD_)
            {return true;}
            return false;
        }




    }

}