package ru.tinkoff.test.data.network;

import com.google.gson.annotations.SerializedName;

import ru.tinkoff.test.data.News;

public class NewsDetailsResponse extends Response {
    protected static final String KEY_TRACKING_ID = "trackingId";

    @SerializedName(KEY_PAYLOAD)
    private News mNews;

    @SerializedName(KEY_TRACKING_ID)
    private News mTrackingId;

    public News getNews() {
        return mNews;
    }

    public void setNews(News news) {
        mNews = news;
    }

    public News getTrackingId() {
        return mTrackingId;
    }

    public void setTrackingId(News trackingId) {
        mTrackingId = trackingId;
    }
}
