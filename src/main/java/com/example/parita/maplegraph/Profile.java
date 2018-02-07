package com.example.parita.maplegraph;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Profile extends AppCompatActivity {
    String  id;
    TextView etUsername;
    String checkemail;
    RelativeLayout RelativeLayout03;
    ImageView displaypic;
    TextView useraddress, usergender, userbirth, userphone, username;
    String suseraddress, susergender, suserbirth, suserphone, susername;
    String myemail;
    public static final int CONNECTION_TIMEOUT = 20000;
    public static final int READ_TIMEOUT = 20000;
    Button about,post, camera, mytimeline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        etUsername = (TextView) findViewById(R.id.etUsername);
        Intent intent = getIntent();
        etUsername.setText(intent.getCharSequenceExtra("email"));
        id=etUsername.getText().toString();
        checkemail=etUsername.getText().toString();
        myemail= etUsername.getText().toString();
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout03.setVisibility(View.VISIBLE);
                new AsyncFetch(myemail).execute();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent post=new Intent(Profile.this, PostUpload.class);
                post.putExtra("email", checkemail);
                startActivity(post);
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadpic=new Intent(Profile.this, Upload_profilepic.class);
                uploadpic.putExtra("email", checkemail);
                startActivity(uploadpic);
            }
        });
        mytimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timeline=new Intent(Profile.this, MyTimeline.class);
                timeline.putExtra("email", checkemail);
                startActivity(timeline);
            }
        });
    }
    public void init(){
        displaypic=(ImageView)findViewById(R.id.displaypic);
        useraddress=(TextView)findViewById(R.id.useraddress);
        usergender=(TextView)findViewById(R.id.usergender);
        userbirth=(TextView)findViewById(R.id.userbirth);
        userphone=(TextView)findViewById(R.id.userphone);
        username=(TextView)findViewById(R.id.username);
        about=(Button)findViewById(R.id.about);
        post=(Button)findViewById(R.id.post);
        camera=(Button)findViewById(R.id.camera);
        mytimeline=(Button)findViewById(R.id.mytimeline);
        RelativeLayout03=(RelativeLayout)findViewById(R.id.RelativeLayout03);
        RelativeLayout03.setVisibility(View.INVISIBLE);
    }
    public class AsyncFetch extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(Profile.this);
        HttpURLConnection conn;
        URL url = null;
        String searchQuery;

        public AsyncFetch(String searchQuery){
            this.searchQuery=searchQuery;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                   url = new URL("https://paritasampa95.000webhostapp.com/MapleGraph/profiledetails.php");

            } catch (MalformedURLException e) {

                e.printStackTrace();
                return e.toString();
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("myemail", myemail);
                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                e1.printStackTrace();
                return e1.toString();
            }
            try {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return (result.toString());
                } else {
                    return("Connection error");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }
        }
        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            pdLoading.dismiss();
            if(result.equals("no rows")) {
                Toast.makeText(Profile.this, "No Results found", Toast.LENGTH_LONG).show();
            }else{
                try {
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        susername = json_data.getString("name");
                        suserphone = json_data.getString("phone");
                        suserbirth=json_data.getString("dob");
                        susergender=json_data.getString("gender");
                        suseraddress=json_data.getString("address");
                    }
                    username.setText(susername);
                    userphone.setText(suserphone);
                    userbirth.setText(suserbirth);
                    usergender.setText(susergender);
                    useraddress.setText(suseraddress);
                } catch (JSONException e) {
                    Log.d(e.toString(),"error");
                    Toast.makeText(Profile.this, "Error in fetching details", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
