package com.example.chahat.anotode;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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
        public TextView time,url,title,highlightss,tv_readmore;
        public Button category, tag1,tag2,tag3,tag4;



        public MyViewHolder(View view) {
            super(view);

            time = (TextView) view.findViewById(R.id.tv_time);
            url = (TextView) view.findViewById(R.id.tv_url);
            title = (TextView) view.findViewById(R.id.tv_title);
            tag1 = (Button) view.findViewById(R.id.bt_tag1);
            tag2 = (Button) view.findViewById(R.id.bt_tag2);
            tag3 = (Button) view.findViewById(R.id.bt_tag3);
            tag4 = (Button) view.findViewById(R.id.bt_tag4);
            highlightss= (TextView) view.findViewById(R.id.tv_highlight);
            category = (Button) view.findViewById(R.id.bt_category);
            tv_readmore = (TextView) view.findViewById(R.id.tv_readmore);
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
    public void onBindViewHolder(MyViewHolder holder,final int position) {
        Highlight highlight = highlightList.get(position);
        holder.time.setText(highlight.getTime());
        holder.url.setText(highlight.getUrl());
        holder.title.setText(highlight.getTitle());
        holder.tag1.setText(highlight.getTag1());
        if (!(highlight.getTag2().isEmpty()))
        {
            holder.tag2.setVisibility(View.VISIBLE);
            holder.tag2.setText(highlight.getTag2());
        }
        else{
            holder.tag2.setVisibility(View.GONE);
        }
        if (!(highlight.getTag3().isEmpty()))
        {
            holder.tag3.setVisibility(View.VISIBLE);
            holder.tag3.setText(highlight.getTag3());
        }
        else{
            holder.tag3.setVisibility(View.GONE);
        }
        if (!(highlight.getTag4().isEmpty()))
        {
            holder.tag4.setVisibility(View.VISIBLE);
            holder.tag4.setText(highlight.getTag4());
        }
        else{
            holder.tag4.setVisibility(View.GONE);
        }

        holder.category.setText(highlight.getCategory());
        holder.highlightss.setText(highlight.getNotedtext());
        holder.tv_readmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Highlight highlight  = highlightList.get(position);
                Intent intent = new Intent(HighlightFragment.getInstance().getActivity(),highlight_detail.class);
                String[] myStrings = new String[] {highlight.getId(),highlight.getTime(), highlight.getUrl(),highlight.getTitle(),highlight.getNotedtext(),highlight.getComment(),highlight.getTag1(),highlight.getTag2(),highlight.getTag3(),highlight.getTag4(),highlight.getCategory()};
                intent.putExtra("strings", myStrings);
                intent.putExtra("updateChecking",786);
                HighlightFragment.getInstance().getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return highlightList.size();
    }
}
