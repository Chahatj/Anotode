package com.example.chahat.anotode;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chahat on 3/9/16.
 */
public class HighlightRecyclerAdapter extends RecyclerView.Adapter<HighlightRecyclerAdapter.MyViewHolder> {

    private List<Highlight> highlightList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView time,url,title,text,commment;
        public Button tag,category;

        public MyViewHolder(View view) {
            super(view);

            time = (TextView) view.findViewById(R.id.tv_time);
            url = (TextView) view.findViewById(R.id.tv_url);
            title = (TextView) view.findViewById(R.id.tv_title);
            text = (TextView) view.findViewById(R.id.tv_highlight);
            tag = (Button) view.findViewById(R.id.bt_tag);
            category = (Button) view.findViewById(R.id.bt_category);
            commment = (TextView) view.findViewById(R.id.tv_comment);
        }
    }


    public HighlightRecyclerAdapter(List<Highlight> highlightList) {
        this.highlightList = highlightList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.highlight, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Highlight highlight = highlightList.get(position);
        holder.time.setText(highlight.getTime());
        holder.url.setText(highlight.getUrl());
        holder.title.setText(highlight.getTitle());
        holder.text.setText(highlight.getNotedtext());
        holder.commment.setText(highlight.getComment());
        holder.tag.setText(highlight.getTag());
        holder.category.setText(highlight.getCategory());
    }

    @Override
    public int getItemCount() {
        return highlightList.size();
    }
}
