package ru.tinkoff.test.ui;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import ru.tinkoff.test.R;
import ru.tinkoff.test.data.News;
import ru.tinkoff.test.data.NewsTitle;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsTitleViewHolder> {
    private ArrayList<NewsTitle> mNewsTitles;

    private OnItemClickListener mClickListener;

    public NewsAdapter(List<NewsTitle> newsTitles) {
        setNewsTitles(newsTitles);
    }

    public void setNewsTitles(List<NewsTitle> newsTitles) {
        //copy links to ensure that array won't be changed accidentally
        mNewsTitles = new ArrayList<>(newsTitles);
    }

    public void setClickListener(OnItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public NewsTitleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());

        return new NewsTitleViewHolder(li.inflate(R.layout.list_item_news_title, parent, false));
    }

    @Override
    public void onBindViewHolder(NewsTitleViewHolder holder, int position) {
        final NewsTitle newsTitle = mNewsTitles.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onClick(newsTitle);
                }
            }
        });

        holder.text.setText(Html.fromHtml(newsTitle.getText()));

        DateTime dateTime = new DateTime(newsTitle.getPublicationDate().getMilliseconds());
        holder.time.setText(dateTime.toString(holder.itemView.getResources().getString(R.string.date_pattern)));
    }

    @Override
    public int getItemCount() {
        return mNewsTitles.size();
    }

    public static class NewsTitleViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public TextView time;

        public NewsTitleViewHolder(View content) {
            super(content);

            text = (TextView) content.findViewById(R.id.text);
            time = (TextView) content.findViewById(R.id.time);
        }
    }

    public interface OnItemClickListener {
        void onClick(NewsTitle newsTitle);
    }
}
