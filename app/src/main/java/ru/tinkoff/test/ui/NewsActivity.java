package ru.tinkoff.test.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.tinkoff.test.R;
import ru.tinkoff.test.data.News;
import ru.tinkoff.test.data.NewsRepository;
import ru.tinkoff.test.data.NewsTitle;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class NewsActivity extends BaseActivity implements NewsAdapter.OnItemClickListener {
    private static final String TAG = "NewsActivity";

    private static final String INSTANCE_STATE_DATA_LOADED_KEY = "dataLoaded";

    private RecyclerView mNewsRecycler;
    private SwipeRefreshLayout mNewsRefresh;

    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news);

        mNewsRecycler = (RecyclerView) findViewById(R.id.news_recycler);
        mNewsRefresh = (SwipeRefreshLayout) findViewById(R.id.news_refresh);

        mNewsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(true);
            }
        });

        // Add dummy adapter to make sure that recycler view will pass layout stage
        mAdapter = new NewsAdapter(new ArrayList<News>());
        mAdapter.setClickListener(this);

        mNewsRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mNewsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mNewsRecycler.setAdapter(mAdapter);

        boolean reload = true;
        if (savedInstanceState != null && savedInstanceState.getBoolean(INSTANCE_STATE_DATA_LOADED_KEY, false)) {
            reload = false;
        }

        if (reload) {
            showProgressDialog();
        }

        loadData(reload);
    }

    @Override
    public void onClick(NewsTitle newsTitle) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_NEWS_ID, newsTitle.getId());
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(INSTANCE_STATE_DATA_LOADED_KEY, isDataLoaded());
    }

    private void loadData(boolean forceReload) {
        NewsRepository.getInstance().getNews(forceReload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<List<News>>() {
                    @Override
                    public void onSuccess(List<News> news) {
                        mAdapter.setNews(news);
                        mAdapter.notifyDataSetChanged();

                        mNewsRefresh.setRefreshing(false);
                        hideNoDataText();

                        hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable error) {
                        mNewsRefresh.setRefreshing(false);

                        if (!isDataLoaded()) {
                            showNoDataText();
                        }

                        hideProgressDialog();
                    }
                });
    }

    private boolean isDataLoaded() {
        return mAdapter != null && mAdapter.getItemCount() > 0;
    }
}
