package com.example.cetouch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class profile extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    TextView nameview,admnoview,departmentyearview,mailview;
    String login_id,tag="",ttt="111";
    ArrayList<String> tags;
    ArrayList<ToggleButton> toggleButtons;
    Set<String> set;
    CardView logoutbutton,savebutton;
    ToggleButton nss,h2o,womencell,ieee,iste,robocet,tinkerhub,cetsat,herackles,iet,aerocet,litsoc,malayalam,cetfilmclub,rangavedhi,cetunes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameview=(TextView)findViewById(R.id.name);
        admnoview=(TextView)findViewById(R.id.admno);
        departmentyearview=(TextView)findViewById(R.id.departmentyear);
        mailview=(TextView)findViewById(R.id.email);
        logoutbutton=(CardView) findViewById(R.id.logoutbutton);
        savebutton=(CardView)findViewById(R.id.savebutton);

        nss=(ToggleButton)findViewById(R.id.nss);
        h2o=(ToggleButton)findViewById(R.id.h2o);
        womencell=(ToggleButton)findViewById(R.id.womencell);
        ieee=(ToggleButton)findViewById(R.id.ieee);
        iste=(ToggleButton)findViewById(R.id.iste);
        robocet=(ToggleButton)findViewById(R.id.robocet);
        tinkerhub=(ToggleButton)findViewById(R.id.tinkerhub);
        cetsat=(ToggleButton)findViewById(R.id.cetsat);
        herackles=(ToggleButton)findViewById(R.id.herackles);
        iet=(ToggleButton)findViewById(R.id.iet);
        aerocet=(ToggleButton)findViewById(R.id.aerocet);
        litsoc=(ToggleButton)findViewById(R.id.litsoc);
        malayalam=(ToggleButton)findViewById(R.id.malayalam);
        cetfilmclub=(ToggleButton)findViewById(R.id.cetfilmclub);
        rangavedhi=(ToggleButton)findViewById(R.id.rangavedhi);
        cetunes=(ToggleButton)findViewById(R.id.cetunes);

        nss.setOnCheckedChangeListener(this);
        h2o.setOnCheckedChangeListener(this);
        womencell.setOnCheckedChangeListener(this);
        ieee.setOnCheckedChangeListener(this);
        iste.setOnCheckedChangeListener(this);
        robocet.setOnCheckedChangeListener(this);
        tinkerhub.setOnCheckedChangeListener(this);
        cetsat.setOnCheckedChangeListener(this);
        herackles.setOnCheckedChangeListener(this);
        iet.setOnCheckedChangeListener(this);
        aerocet.setOnCheckedChangeListener(this);
        aerocet.setOnCheckedChangeListener(this);
        litsoc.setOnCheckedChangeListener(this);
        malayalam.setOnCheckedChangeListener(this);
        cetfilmclub.setOnCheckedChangeListener(this);
        rangavedhi.setOnCheckedChangeListener(this);
        cetunes.setOnCheckedChangeListener(this);

        tags=new ArrayList<String>();
        toggleButtons=new ArrayList<ToggleButton>(Arrays.asList(nss,h2o,womencell,ieee,iste,robocet,tinkerhub,cetsat,herackles,iet,aerocet,litsoc,malayalam,cetfilmclub,rangavedhi,cetunes));
        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        login_id= prefs.getString("id", "0");



         logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                editor.putString("id", "0");
                editor.apply();
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });

         savebutton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 JSONArray j=new JSONArray(tags);
                 tag=j.toString();
                 new add_tag(getApplicationContext()).execute();
             }
         });

        new fetch_profile(getApplicationContext()).execute();
        new fetch_tags(getApplicationContext()).execute();


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {

                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new Instance ID token
                        ttt = task.getResult().getToken().toString();
                        new tok(profile.this,ttt).execute();
                        // Log and toast
                    }
                });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        set = new HashSet<String>(tags);
        tags.clear();
        tags.addAll(set);

        ToggleButton tb=(ToggleButton)buttonView;
        if(isChecked)
        {
            tags.add(tb.getText().toString());

        }
        else
        {
            tags.remove(tb.getText().toString());
        }

    }


    class fetch_profile extends AsyncTask<String, String, String> {

        Context now;

        fetch_profile(Context c) {
            now = c;
        }

        String output = "error";

        @Override
        protected String doInBackground(String... arg0) {
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("studentID",login_id));


                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://smktech.in/cet/index.php/data/get_student_by_studentID");
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
                    nameview.setText(jsonobject.getString("name"));
                    admnoview.setText(jsonobject.getString("admno"));
                    departmentyearview.setText(jsonobject.getString("department")+" | "+jsonobject.getString("year"));
                    mailview.setText(jsonobject.getString("email"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

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


    class fetch_tags extends AsyncTask<String, String, String> {

        Context now;

        fetch_tags(Context c) {
            now = c;
        }
        String output = "error";

        @Override
        protected String doInBackground(String... arg0) {
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("studentID",login_id));


                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://smktech.in/cet/index.php/data/get_tag_by_studentID");
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
            Log.e("man",result);
            try {
                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);


                    for (ToggleButton tbs: toggleButtons) {
                        if (tbs.getText().toString().equals(jsonobject.getString("tag")))
                        {
                            tbs.setChecked(true);
                            tags.add(tbs.getText().toString());
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }





    class add_tag extends AsyncTask<String, String, String> {

        Context now;

        add_tag(Context c) {
            now = c;
        }

        String output = "error";

        @Override
        protected String doInBackground(String... arg0) {
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("studentID",login_id));
                nameValuePairs.add(new BasicNameValuePair("tag",tag));


                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://smktech.in/cet/index.php/data/add_tag");
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
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            /* try {
                JSONArray jsonarray = new JSONArray(result);

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    nameview.setText(jsonobject.getString("name"));
                    admnoview.setText(jsonobject.getString("admno"));
                    departmentyearview.setText(jsonobject.getString("department")+" "+jsonobject.getString("year"));
                    mailview.setText(jsonobject.getString("email"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            */
        }
    }


}
