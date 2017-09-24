package ru.tinkoff.test.data.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.tinkoff.test.data.NewsTitle;

public class NewsTitlesResponse extends Response {
    private static final String KEY_RESULT_CODE = "resultCode";

    @SerializedName(KEY_PAYLOAD)
    private List<NewsTitle> mNewsTitles;

    public List<NewsTitle> getNewsTitles() {
        return mNewsTitles;
    }

    public void setNewsTitles(List<NewsTitle> newsTitles) {
        mNewsTitles = newsTitles;
    }
}
