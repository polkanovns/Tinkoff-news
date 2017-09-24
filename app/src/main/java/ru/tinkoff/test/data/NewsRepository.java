package ru.tinkoff.test.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.tinkoff.test.data.network.NewsDetailsResponse;
import ru.tinkoff.test.data.network.NewsTitlesResponse;
import ru.tinkoff.test.data.network.TinkoffApi;
import rx.Single;
import rx.SingleSubscriber;
import rx.schedulers.Schedulers;

public class NewsRepository {

    private static NewsRepository sInstance;

    private TinkoffApi mApi;

    private NewsRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TinkoffApi.URL_BASE)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApi = retrofit.create(TinkoffApi.class);
    }

    public synchronized static NewsRepository getInstance() {
        if (sInstance == null) {
            sInstance = new NewsRepository();
        }

        return sInstance;
    }

    public Single<List<News>> getNews(boolean reload) {
        return Single.create(new Single.OnSubscribe<List<News>>() {
            @Override
            public void call(final SingleSubscriber<? super List<News>> singleSubscriber) {
                //TODO check connectivity
                mApi.getNewsTitles()
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(new SingleSubscriber<NewsTitlesResponse>() {

                            @Override
                            public void onSuccess(NewsTitlesResponse newsTitlesResponse) {
                                if (newsTitlesResponse.isSuccessful()) {
                                    singleSubscriber.onSuccess(
                                            handleNewsTitlesResponse(newsTitlesResponse.getNewsTitles()));
                                } else {
                                    singleSubscriber.onError(
                                            new RuntimeException("Unable to load news, result code: " + newsTitlesResponse.getResultCode()));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                singleSubscriber.onError(e);
                            }
                        });
            }
        });
    }

    public Single<News> getNewsDetails(final String id) {
        return Single.create(new Single.OnSubscribe<News>() {
            @Override
            public void call(final SingleSubscriber<? super News> singleSubscriber) {
                //TODO check connectivity
                mApi.getNewsDetails(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(new SingleSubscriber<NewsDetailsResponse>() {

                            @Override
                            public void onSuccess(NewsDetailsResponse newsDetailsResponse) {
                                if (newsDetailsResponse.isSuccessful()) {
                                    singleSubscriber.onSuccess(newsDetailsResponse.getNews());
                                } else {
                                    singleSubscriber.onError(
                                            new RuntimeException("Unable to load news, result code: " + newsDetailsResponse.getResultCode()));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                singleSubscriber.onError(e);
                            }
                        });
            }
        });
    }

    private ArrayList<News> handleNewsTitlesResponse(List<NewsTitle> titles) {
        ArrayList<News> newsList = new ArrayList<>();

        // There are many duplicates, we filter bigger part, but for now it's hard to formalize all cases
        NewsFilter.filter(titles);

        Collections.sort(titles, new Comparator<NewsTitle>() {
            @Override
            public int compare(NewsTitle o1, NewsTitle o2) {
                if (o1.getPublicationDate().getMilliseconds() < o2.getPublicationDate().getMilliseconds()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        for (NewsTitle newsTitle : titles) {
            News news = new News();
            news.setTitle(newsTitle);

            newsList.add(news);
        }

        return newsList;
    }
}
