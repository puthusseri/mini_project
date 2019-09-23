package com.example.cetouch;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class feeds_recyclerview_adapter extends RecyclerView.Adapter<feeds_recyclerview_adapter.mViewHolder> {

    Dialog dialog;
    final private Context mContext;
    private List<feeds> data;
    Typeface baloo;
    String baseURL="http://smktech.in/cet/images/";

    public feeds_recyclerview_adapter(Context context, List<feeds> mdata)
    {
        mContext=context;
        data=mdata;
        baloo=Typeface.createFromAsset(mContext.getAssets(),"fonts/baloo.ttf");
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mLayoutInflater=LayoutInflater.from(mContext);
        view=mLayoutInflater.inflate(R.layout.feed_card,viewGroup,false);

        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder viewHolder, final int i) {


        viewHolder.cardview_titleview.setText(data.get(i).getFeed_title());
        viewHolder.cardview_clubview.setText(data.get(i).getFeed_department());
        Glide.with(mContext).load(Uri.parse(baseURL+data.get(i).getFeed_image())).into(viewHolder.cardview_imageview);
        viewHolder.cardview_titleview.setTypeface(baloo);
        viewHolder.cardview_clubview.setTypeface(baloo);

        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,fullfeed.class);
                intent.putExtra("data",data.get(i).getFeed_object());
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return  data.size();
    }

    public static class mViewHolder extends RecyclerView.ViewHolder {

        TextView cardview_titleview,cardview_clubview;
        ImageView cardview_imageview;
        CardView card;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);

            cardview_imageview=(ImageView)itemView.findViewById(R.id.feed_image);
            cardview_titleview=(TextView)itemView.findViewById(R.id.feed_title);
            cardview_clubview=(TextView)itemView.findViewById(R.id.feed_club);
            card=(CardView)itemView.findViewById(R.id.offercard);

        }
    }
}
