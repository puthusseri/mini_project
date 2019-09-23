package com.example.cetouch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<feeds> data;
    String login_id;
    ImageButton profilebutton,logoutbutton;
    String ttt="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        profilebutton=(ImageButton)findViewById(R.id.profile);
        logoutbutton=(ImageButton)findViewById(R.id.logout);

        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        login_id= prefs.getString("id", "0");
//        Toast.makeText(getApplicationContext(),login_id,Toast.LENGTH_SHORT).show();
         if(login_id.equals("0"))
         {
             startActivity(new Intent(getApplicationContext(),login.class));
         }

         profilebutton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getApplicationContext(),profile.class));
             }
         });

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                editor.putString("id", "0");
                editor.apply();
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });


        data=new ArrayList<feeds>();
        new fetch_stores(this).execute();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {

                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("selinaa", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        ttt = task.getResult().getToken().toString();
                        new tok(MainActivity.this,ttt).execute();
                    }
                });

    }


    class fetch_stores extends AsyncTask<String, String, String> {

        Context now;
        fetch_stores(Context c) { now = c; }
        String output = "error";

        @Override
        protected String doInBackground(String... arg0) {
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("studentID", login_id));
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://smktech.in/cet/index.php/data/get_notification");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                output = httpClient.execute(httpPost, responseHandler);

            } catch (NullPointerException e) {
                //Toast.makeText(ccc, e.toString(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
//		Toast.makeText(ccc,e.toString(), Toast.LENGTH_LONG).show();
                return e.toString();
            }
            return output;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonarray = new JSONArray(result);

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    Log.e("dept",jsonobject.getString("department"));
                    data.add(new feeds(jsonobject.getString("notificationID"),
                            jsonobject.getString("title"),jsonobject.getString("content"),
                            jsonobject.getString("image"),jsonobject.getString("department"), jsonobject.toString()));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RecyclerView recyclerView= (RecyclerView)findViewById(R.id.feedsview);
            feeds_recyclerview_adapter feedsAdapter=new feeds_recyclerview_adapter(getApplicationContext(),data);
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
            recyclerView.setAdapter(feedsAdapter);


        }
    }
    class tok extends AsyncTask<String, String, String> {
        Context ccc;
        String t="";
        String signin_id;
        tok(Context c,String tt) {
            ccc = c;
            t=tt;
        }

        String g = "error";

        @Override
        protected String doInBackground(String... arg0) {
            Log.e("selin","ggsdgsdgsd");
            try {
                Date d = new Date();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("logg_pref", 0);


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("studentID", login_id));
                nameValuePairs.add(new BasicNameValuePair("reg", t));

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://smktech.in/cet/index.php/data/add_push_notification");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                g = httpClient.execute(httpPost, responseHandler);

                // HttpEntity entity = response.getEntity();


            } catch (NullPointerException e) {
                //Toast.makeText(ccc, e.toString(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
//		Toast.makeText(ccc,e.toString(), Toast.LENGTH_LONG).show();
                return e.toString();
            }
            return g;

        }


        @Override
        protected void onPostExecute(String result) {
            Log.e("selina",result);

        }

    }
}
