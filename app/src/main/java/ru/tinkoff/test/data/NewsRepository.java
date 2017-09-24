package ru.tinkoff.test.data;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.tinkoff.test.data.cache.NewsCache;
import ru.tinkoff.test.data.network.NewsDetailsResponse;
import ru.tinkoff.test.data.network.NewsTitlesResponse;
import ru.tinkoff.test.data.network.TinkoffApi;
import ru.tinkoff.test.data.network.Utils;
import rx.Single;
import rx.SingleSubscriber;
import rx.schedulers.Schedulers;

public class NewsRepository {
    private static final String TAG = "NewsRepository";

    private static NewsRepository sInstance;

    private TinkoffApi mApi;
    private NewsCache mNewsCache;

    private NewsRepository(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TinkoffApi.URL_BASE)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApi = retrofit.create(TinkoffApi.class);

        mNewsCache = new NewsCache(context);
    }

    public synchronized static NewsRepository getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NewsRepository(context);
        }

        return sInstance;
    }

    public Single<List<News>> getNews(final Context context, final boolean reload) {
        return Single.create(new Single.OnSubscribe<List<News>>() {
            @Override
            public void call(final SingleSubscriber<? super List<News>> singleSubscriber) {
                if (Utils.isNetworkConnectionAvailable(context) && reload) {
                    mApi.getNewsTitles()
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(new SingleSubscriber<NewsTitlesResponse>() {

                                @Override
                                public void onSuccess(NewsTitlesResponse newsTitlesResponse) {
                                    if (newsTitlesResponse.isSuccessful()) {
                                        List<News> newsList = handleNewsTitlesResponse(newsTitlesResponse.getNewsTitles());

                                        mNewsCache.saveNewsNonBlocking(newsList);

                                        singleSubscriber.onSuccess(newsList);
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
                } else {
                    List<News> newsList = mNewsCache.getNewsList();
                    if (newsList != null && newsList.size() > 0) {
                        singleSubscriber.onSuccess(newsList);

                    } else {
                        //news aren't loaded yet, try to restore them
                        newsList = mNewsCache.restoreNewsBlocking();
                        if (newsList != null && newsList.size() > 0) {
                            singleSubscriber.onSuccess(newsList);

                        } else {
                            singleSubscriber.onError(
                                    new RuntimeException("Unable to load news, no data available"));
                        }
                    }
                }
            }
        });
    }

    public Single<String> getNewsContent(final Context context, final String id) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(final SingleSubscriber<? super String> singleSubscriber) {
                if (!mNewsCache.isNewsContentsRestored()) {
                    mNewsCache.restoreNewsContentBlocking();
                }

                String content = mNewsCache.getNewsContentById(id);
                if (content != null && !TextUtils.isEmpty(content)) {
                    singleSubscriber.onSuccess(content);

                } else if (Utils.isNetworkConnectionAvailable(context)) {
                    mApi.getNewsDetails(id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(new SingleSubscriber<NewsDetailsResponse>() {

                                @Override
                                public void onSuccess(NewsDetailsResponse newsDetailsResponse) {
                                    if (newsDetailsResponse.isSuccessful()) {
                                        String content = newsDetailsResponse.getNews().getContent();
                                        mNewsCache.saveNewsContentNonBlocking(content, id);
                                        singleSubscriber.onSuccess(content);
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
                } else {
                    singleSubscriber.onError(
                            new RuntimeException("Unable to load news, no data available"));
                }
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
            news.setBankInfoTypeId(newsTitle.getBankInfoTypeId());

            newsList.add(news);
        }

        return newsList;
    }
}
