package com.example.chahat.anotode;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
        public ImageView  icontitle;



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
            icontitle = (ImageView) view.findViewById(R.id.icon_title);
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

        if (highlight.getTitle().equals("notitle"))
        {
            holder.icontitle.setVisibility(View.GONE);
            holder.title.setVisibility(View.GONE);
        }
        else {
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(highlight.getTitle());
            holder.icontitle.setVisibility(View.VISIBLE);
        }

        if (highlight.getTag1().equals("notag1"))
        {
            holder.tag1.setVisibility(View.GONE);
        }
        else{
            holder.tag1.setVisibility(View.VISIBLE);
            holder.tag1.setText(highlight.getTag1());
        }

        if (highlight.getTag2().equals("notag2"))
        {
            holder.tag2.setVisibility(View.GONE);
        }
        else{
            holder.tag2.setVisibility(View.VISIBLE);
            holder.tag2.setText(highlight.getTag2());
        }

        if (highlight.getTag3().equals("notag3"))
        {
            holder.tag3.setVisibility(View.GONE);
        }
        else{
            holder.tag3.setVisibility(View.VISIBLE);
            holder.tag3.setText(highlight.getTag3());
        }

        if (highlight.getTag4().equals("notag4"))
        {
            holder.tag4.setVisibility(View.GONE);
        }
        else{
            holder.tag4.setVisibility(View.VISIBLE);
            holder.tag4.setText(highlight.getTag4());
        }


        if (highlight.getCategory().equals("nocategory"))
        {
            holder.category.setVisibility(View.GONE);
        }
        else
        {
            holder.category.setVisibility(View.VISIBLE);
            holder.category.setText(highlight.getCategory());
        }

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
