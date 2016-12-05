package com.rss.siulp.siulp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

/**
 * Created by marco on 04/12/2016.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    ArrayList<FeedItem> feedItems;
    Context context;
    public MyAdapter(Context context, ArrayList<FeedItem> feedItems){
        this.feedItems = feedItems;
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_row_news_item,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        YoYo.with(Techniques.FadeIn).playOn(holder.cardView);
        final FeedItem current = feedItems.get(position);
        holder.Title.setText(Html.fromHtml(current.getTitle()));
        holder.Description.setText(Html.fromHtml(current.getDescription()));
        holder.Date.setText(Html.fromHtml(current.getPubDate().toString()));
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent((context),NewsDetails.class);
                intent.putExtra("Link",current.getLink());
                context.startActivity(intent);

            }
        }

        );
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Title,Description,Date;
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            Title = (TextView) itemView.findViewById(R.id.title_text);
            Description = (TextView) itemView.findViewById(R.id.decription_text);
            Date = (TextView) itemView.findViewById((R.id.date_text));
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}
