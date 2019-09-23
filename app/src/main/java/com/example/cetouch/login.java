package com.example.cetouch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class login extends AppCompatActivity {

    EditText admnotext, passwordtext;
    String admno, password;
    Button signin;
    TextView signupbutton;
    String login_id,ttt="111";

    @Override
 protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.e("selin","ggsdgsdgsd");
        admnotext = (EditText) findViewById(R.id.admno);
        passwordtext = (EditText) findViewById(R.id.password);
        signin = (Button) findViewById(R.id.signin);
        signupbutton =  findViewById(R.id.signup);

        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        login_id= prefs.getString("id", "0");
//        Toast.makeText(getApplicationContext(),login_id,Toast.LENGTH_SHORT).show();
        if(!login_id.equals("0"))
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }


        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), signup.class));
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!admnotext.getText().toString().trim().equals("") && !passwordtext.getText().toString().trim().equals("")) {
                    //Toast.makeText(getApplicationContext(), admnotext.getText().toString() + "," + passwordtext.getText().toString(), Toast.LENGTH_LONG).show();
                    admno = admnotext.getText().toString();
                    password = passwordtext.getText().toString();
                    new sign_in(login.this).execute();

                }
            }
        });


    }


    class sign_in extends AsyncTask<String, String, String> {
        Context ccc;

        String signin_id;
        sign_in(Context c) {
            ccc = c;
        }

        String g = "error";

        @Override
        protected String doInBackground(String... arg0) {
            try {
                Date d = new Date();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("logg_pref", 0);


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email", admno));
                nameValuePairs.add(new BasicNameValuePair("password", password));

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://smktech.in/cet/index.php/data/student_login");
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
                signin_id = json.getString("studentID");

                if (signin_id.equals("0"))
                {
                    Toast.makeText(getApplicationContext(), "Wrong email or password !", Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                    editor.putString("id", signin_id);
                    editor.apply();
                } else
                    {

                    SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                    editor.putString("id", signin_id);
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}