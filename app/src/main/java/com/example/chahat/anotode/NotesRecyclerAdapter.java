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
public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.MyViewHolder> {

    private List<Highlight> notesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView time,title,text;
        public Button tag,category;

        public MyViewHolder(View view) {
            super(view);

            time = (TextView) view.findViewById(R.id.tv_time);
            title = (TextView) view.findViewById(R.id.tv_title);
            text = (TextView) view.findViewById(R.id.tv_notes);
            tag = (Button) view.findViewById(R.id.bt_tag);
            category = (Button) view.findViewById(R.id.bt_category);

        }
    }


    public NotesRecyclerAdapter(List<Highlight> notesList) {
        this.notesList = notesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Highlight highlight = notesList.get(position);
        holder.time.setText(highlight.getTime());
        holder.title.setText(highlight.getTitle());
        holder.text.setText(highlight.getNotedtext());
        holder.tag.setText(highlight.getTag());
        holder.category.setText(highlight.getCategory());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
