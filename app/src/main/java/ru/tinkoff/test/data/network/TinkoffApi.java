package ru.tinkoff.test.data.network;


import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Single;

public interface TinkoffApi {
    String URL_BASE = "https://api.tinkoff.ru/v1/";
    String URL_NEWS_POSTFIX = "news";
    String URL_NEWS_CONTENT_POSTFIX = "news_content";

    @GET(URL_NEWS_CONTENT_POSTFIX)
    Single<NewsDetailsResponse> getNewsDetails(@Query("id") String id);

    @GET(URL_NEWS_POSTFIX)
    Single<NewsTitlesResponse> getNewsTitles();
}
