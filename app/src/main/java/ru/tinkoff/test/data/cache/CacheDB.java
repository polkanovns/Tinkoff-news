package ru.tinkoff.test.data.cache;


/* package */ class CacheDB {
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "NewsCache";

    static class TitlesCache {
        static final String TABLE_NAME = "titlesCache";

        static final String ID = "id";
        static final String NAME = "name";
        static final String TEXT = "text";
        static final String PUBLICATION_DATE = "publicationDate";
        static final String BANK_INFO_TYPE_ID = "bankInfoTypeId";

        static final String TABLE_CREATE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        ID + " TEXT PRIMARY KEY, " +
                        NAME + " TEXT, " +
                        TEXT + " TEXT, " +
                        BANK_INFO_TYPE_ID + " INTEGER, " +
                        PUBLICATION_DATE + " INTEGER);";
    }

    static class DetailsCache {
        static final String TABLE_NAME = "detailsCache";

        static final String ID = "id";
        static final String CONTENT = "content";

        static final String TABLE_CREATE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        ID + " TEXT PRIMARY KEY, " +
                        CONTENT + " TEXT);";
    }
}
