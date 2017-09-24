package ru.tinkoff.test.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import ru.tinkoff.test.R;
import ru.tinkoff.test.data.NewsRepository;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsContentActivity extends BaseActivity {
    private static final String TAG = "NewsContentActivity";

    public static final String EXTRA_NEWS_ID = "news_id";

    public static final String HTML_MIME = "text/html";
    public static final String HTML_ENCODING = "utf-8";

    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(0, 0);

        setContentView(R.layout.activity_details);

        mWebView = (WebView) findViewById(R.id.webview);

        String id = null;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString(EXTRA_NEWS_ID);
        }

        if (!TextUtils.isEmpty(id)) {
            loadData(id);
        } else {
            Log.e(TAG, "News id is empty");
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(0, 0);
    }

    private void loadData(String id) {
        showProgressDialog();

        NewsRepository.getInstance(this).getNewsContent(this, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<String>() {
                    @Override
                    public void onSuccess(String content) {
                        mWebView.loadDataWithBaseURL(null, content, HTML_MIME, HTML_ENCODING, null);
                        hideProgressDialog();
                        hideNoDataText();
                    }

                    @Override
                    public void onError(Throwable error) {
                        hideProgressDialog();
                        showNoDataText();
                    }
                });
    }
}
