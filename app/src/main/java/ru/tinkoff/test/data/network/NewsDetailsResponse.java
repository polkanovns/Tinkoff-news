package ru.tinkoff.test.data.network;

import com.google.gson.annotations.SerializedName;

import ru.tinkoff.test.data.News;

public class NewsDetailsResponse extends Response {

    @SerializedName(KEY_PAYLOAD)
    private News mNews;

    public News getNews() {
        return mNews;
    }

    public void setNews(News news) {
        mNews = news;
    }
}
