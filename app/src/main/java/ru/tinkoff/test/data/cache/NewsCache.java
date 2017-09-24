package ru.tinkoff.test.data.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.tinkoff.test.data.News;
import ru.tinkoff.test.data.NewsTitle;
import rx.Single;
import rx.SingleSubscriber;
import rx.schedulers.Schedulers;


public class NewsCache {
    private static final String TAG = "NewsCache";

    private SQLiteDatabase mDatabase;
    private List<News> mNewsList;
    private HashMap<String, String> mContentsMap = new HashMap<>();
    private boolean mIsNewsContentsRestored = false;

    public NewsCache(Context context) {
        mDatabase = new CacheOpenHelper(context.getApplicationContext()).getWritableDatabase();
    }

    public void saveNewsBlocking(List<News> newsList) {
        ContentValues values = new ContentValues();

        for (News news : newsList) {
            values.clear();
            NewsTitle title = news.getTitle();

            values.put(CacheDB.TitlesCache.ID, title.getId());
            values.put(CacheDB.TitlesCache.NAME, title.getName());
            values.put(CacheDB.TitlesCache.TEXT, title.getText());
            values.put(CacheDB.TitlesCache.BANK_INFO_TYPE_ID, title.getBankInfoTypeId());
            values.put(CacheDB.TitlesCache.PUBLICATION_DATE, title.getPublicationDate().getMilliseconds());

            mDatabase.insertWithOnConflict(CacheDB.TitlesCache.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
        Log.d(TAG, newsList.size() + " news saved");
    }

    public void saveNewsNonBlocking(final List<News> newsList) {
        mNewsList = newsList;

        Single.create(new Single.OnSubscribe<News>() {
            @Override
            public void call(final SingleSubscriber<? super News> singleSubscriber) {
                saveNewsBlocking(newsList);
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void saveNewsContentNonBlocking(final String content, final String id) {
        Single.create(new Single.OnSubscribe<News>() {
            @Override
            public void call(final SingleSubscriber<? super News> singleSubscriber) {
                saveNewsContentBlocking(content, id);
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void saveNewsContentBlocking(String content, String id) {
        mContentsMap.put(id, content);

        ContentValues values = new ContentValues();

        values.put(CacheDB.DetailsCache.ID, id);
        values.put(CacheDB.DetailsCache.CONTENT, content);

        mDatabase.insertWithOnConflict(CacheDB.DetailsCache.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        Log.d(TAG, "news content saved for id " + id);
    }

    public Map<String, String> restoreNewsContentBlocking() {
        Cursor c = mDatabase.query(CacheDB.DetailsCache.TABLE_NAME, null,
                null, null, null, null, null);

        try {
            if (c.getCount() != 0) {
                while (c.moveToNext()) {
                    mContentsMap.put(c.getString(c.getColumnIndex(CacheDB.DetailsCache.ID)),
                            c.getString(c.getColumnIndex(CacheDB.DetailsCache.CONTENT)));
                }
            }
        } finally {
            c.close();
        }
        mIsNewsContentsRestored = true;

        Log.d(TAG, mContentsMap.size() + " news details restored");

        return mContentsMap;
    }

    public ArrayList<News> restoreNewsBlocking() {
        ArrayList<News> newsList = new ArrayList<>();

        Cursor c = mDatabase.query(CacheDB.TitlesCache.TABLE_NAME, null,
                null, null, null, null, CacheDB.TitlesCache.PUBLICATION_DATE + " DESC");

        try {
            if (c.getCount() != 0) {
                while (c.moveToNext()) {
                    newsList.add(new News(
                            c.getString(c.getColumnIndex(CacheDB.TitlesCache.ID)),
                            c.getString(c.getColumnIndex(CacheDB.TitlesCache.NAME)),
                            c.getString(c.getColumnIndex(CacheDB.TitlesCache.TEXT)),
                            c.getLong(c.getColumnIndex(CacheDB.TitlesCache.PUBLICATION_DATE)),
                            null,
                            c.getInt(c.getColumnIndex(CacheDB.TitlesCache.BANK_INFO_TYPE_ID))));
                }
            }
        } finally {
            c.close();
        }

        Log.d(TAG, newsList.size() + " news restored");
        mNewsList = newsList;

        return newsList;
    }

    public Single<List<News>> restoreNewsNonBlocking(final List<News> newsList) {
        return Single.create(new Single.OnSubscribe<List<News>>() {
            @Override
            public void call(final SingleSubscriber<? super List<News>> singleSubscriber) {
                saveNewsBlocking(newsList);
            }
        });
    }

    public List<News> getNewsList() {
        return mNewsList;
    }

    public boolean isNewsContentsRestored() {
        return mIsNewsContentsRestored;
    }

    public @Nullable String getNewsContentById(String id) {
        return mContentsMap.get(id);
    }
}
