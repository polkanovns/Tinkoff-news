package ru.tinkoff.test.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import ru.tinkoff.test.R;
import ru.tinkoff.test.data.News;
import ru.tinkoff.test.data.NewsRepository;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class NewsActivity extends AppCompatActivity {
    private static final String TAG = "NewsActivity";

    private static final String INSTANCE_STATE_DATA_LOADED_KEY = "dataLoaded";

    private RecyclerView mNewsRecycler;
    private SwipeRefreshLayout mNewsRefresh;

    private TextView mNoDataText;

    private Dialog mCurrentDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mNewsRecycler = (RecyclerView) findViewById(R.id.news_recycler);
        mNewsRefresh = (SwipeRefreshLayout) findViewById(R.id.news_refresh);

        mNoDataText = (TextView) findViewById(R.id.no_data_text);

        mNewsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData(true);
            }
        });

        mNewsRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mNewsRecycler.setLayoutManager(new LinearLayoutManager(this));

        boolean reload = true;
        if (savedInstanceState != null && savedInstanceState.getBoolean(INSTANCE_STATE_DATA_LOADED_KEY, false)) {
            reload = false;
        }

        if (reload) {
            showProgressDialog();
        }

        updateData(reload);
    }

    private void showProgressDialog() {
        hideProgressDialog();

        mNewsRefresh.post(new Runnable() {
            @Override
            public void run() {
                mCurrentDialog = ProgressDialog.show(NewsActivity.this, getString(R.string.loading), getString(R.string.loading_description), true, false);
            }
        });
    }

    private void hideProgressDialog() {
        if (mCurrentDialog != null && mCurrentDialog.isShowing()) {
            mCurrentDialog.dismiss();
            mCurrentDialog = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(INSTANCE_STATE_DATA_LOADED_KEY, isDataLoaded());
    }

    private void updateData(boolean forceReload) {
        NewsRepository.getInstance().getNews(forceReload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<List<News>>() {
                    @Override
                    public void onSuccess(List<News> news) {
                        mNewsRecycler.setAdapter(new NewsAdapter(news));

                        mNewsRefresh.setRefreshing(false);
                        mNoDataText.setVisibility(View.GONE);

                        hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable error) {
                        mNewsRefresh.setRefreshing(false);

                        if (!isDataLoaded()) {
                            mNoDataText.setVisibility(View.VISIBLE);
                        }

                        hideProgressDialog();
                    }
                });
    }

    private boolean isDataLoaded() {
        return mNewsRecycler.getAdapter() != null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        hideProgressDialog();
    }
}
