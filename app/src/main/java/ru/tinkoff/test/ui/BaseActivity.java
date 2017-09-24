package ru.tinkoff.test.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.tinkoff.test.R;


public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    private ViewGroup mContentContainer;
    protected Toolbar mToolbar;

    private TextView mNoDataText;

    private Dialog mCurrentDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_base);

        mContentContainer = (ViewGroup) findViewById(R.id.content);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNoDataText = (TextView) findViewById(R.id.no_data_text);

        setSupportActionBar(mToolbar);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        layoutInflater.inflate(layoutResID, mContentContainer, true);

        mNoDataText.bringToFront();
    }

    protected void showProgressDialog() {
        hideProgressDialog();

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle(getString(R.string.loading));
        dialog.setMessage(getString(R.string.loading_description));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        mCurrentDialog = dialog;
        mContentContainer.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentDialog != null && !mCurrentDialog.isShowing()) {
                    mCurrentDialog.show();
                }
            }
        });
    }

    protected void hideProgressDialog() {
        if (mCurrentDialog != null) {
            if (mCurrentDialog.isShowing()) {
                mCurrentDialog.dismiss();
            }

            mCurrentDialog = null;
        }
    }

    protected void showNoDataText() {
        mNoDataText.setVisibility(View.VISIBLE);
    }

    protected void hideNoDataText() {
        mNoDataText.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        hideProgressDialog();
    }
}
