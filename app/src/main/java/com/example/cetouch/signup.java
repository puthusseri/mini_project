package com.example.cetouch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class signup extends AppCompatActivity {

    EditText nametext,admnotext,yeartext,emailtext,passwordtext;
    Spinner departmentspinner;
    String   name,admno,year,department,email,password;

    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nametext=(EditText)findViewById(R.id.name);
        admnotext=(EditText)findViewById(R.id.admno);
        yeartext=(EditText)findViewById(R.id.year);
        departmentspinner=(Spinner)findViewById(R.id.departmentspinner);
        emailtext=(EditText)findViewById(R.id.email);
        passwordtext=(EditText)findViewById(R.id.password);
        register=(Button)findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(     !nametext.getText().toString().trim().equals("")&&
                        !admnotext.getText().toString().trim().equals("")&&
                        !yeartext.getText().toString().trim().equals("")&&
                        !departmentspinner.getSelectedItem().toString().trim().equals("DEPARTMENT")&&
                        !emailtext.getText().toString().trim().equals("")&&
                        !passwordtext.getText().toString().trim().equals(""))
                {
                    name = nametext.getText().toString();
                    admno=admnotext.getText().toString();
                    year=yeartext.getText().toString();
                    department=departmentspinner.getSelectedItem().toString();
                    email=emailtext.getText().toString();
                    password = passwordtext.getText().toString();
                    new sign_up(signup.this).execute();

                }
            }
        });
    }


    class sign_up extends AsyncTask<String, String, String> {
        Context ccc;

        String signin_id;
        sign_up(Context c) {
            ccc = c;
        }

        String g = "error";

        @Override
        protected String doInBackground(String... arg0) {
            try {
                Date d = new Date();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("logg_pref", 0);


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("admno", admno));
                nameValuePairs.add(new BasicNameValuePair("year", year));
                nameValuePairs.add(new BasicNameValuePair("department", department));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("password", password));

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://smktech.in/cet/index.php/data/student_reg");
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
            try {
                JSONObject json = new JSONObject(result);
                signin_id = json.getString("id");

                if (!signin_id.equals("0"))
                {
                    SharedPreferences.Editor editor = getSharedPreferences("SP", MODE_PRIVATE).edit();
                    editor.putString("admnno", signin_id);
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
