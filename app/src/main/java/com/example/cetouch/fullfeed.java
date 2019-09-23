package com.example.cetouch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class fullfeed extends AppCompatActivity {


    private String baseURL="http://smktech.in/cet/images/";
    FloatingActionButton like;
    TextView title,content;
    ImageView photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_full);

        title=(TextView)findViewById(R.id.title);
        content=(TextView)findViewById(R.id.content);
        photo=(ImageView)findViewById(R.id.photo);

        Intent intent=getIntent();
        String data="["+intent.getStringExtra("data")+"]" ;

        try {
            JSONArray jsonarray = new JSONArray(data);
            final JSONObject jsonobject = jsonarray.getJSONObject(0);

            Glide.with(this).load(Uri.parse(baseURL+jsonobject.getString("image"))).into(photo);

            title.setText(jsonobject.getString("title"));
            content.setText(jsonobject.getString("content"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
