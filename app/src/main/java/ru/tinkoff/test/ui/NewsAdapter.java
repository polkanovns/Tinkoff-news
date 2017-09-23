package ru.tinkoff.test.ui;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.tinkoff.test.R;
import ru.tinkoff.test.data.News;
import ru.tinkoff.test.data.NewsTitle;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsTitleViewHolder> {
    private ArrayList<News> mNews;

    public NewsAdapter(List<News> news) {
        //copy links to ensure that array won't be changed accidentally
        mNews = new ArrayList<>(news);
    }

    @Override
    public NewsTitleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());

        return new NewsTitleViewHolder(li.inflate(R.layout.list_item_news_title, parent, false));
    }

    @Override
    public void onBindViewHolder(NewsTitleViewHolder holder, int position) {
        NewsTitle newsTitle = mNews.get(position).getTitle();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.text.setText(Html.fromHtml(newsTitle.getText()));
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    public static class NewsTitleViewHolder extends RecyclerView.ViewHolder {
        public TextView text;

        public NewsTitleViewHolder(View content) {
            super(content);

            text = (TextView) content.findViewById(R.id.text);
        }
    }
}
