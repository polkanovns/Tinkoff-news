package ru.tinkoff.test.data.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/* package */ class CacheOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "CacheOpenHelper";

    /* package */ CacheOpenHelper(Context context) {
        super(context, CacheDB.DATABASE_NAME, null, CacheDB.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CacheDB.TitlesCache.TABLE_CREATE);
        sqLiteDatabase.execSQL(CacheDB.DetailsCache.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        // do nothing
    }
}
