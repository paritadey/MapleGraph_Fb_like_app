package com.example.parita.maplegraph;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {
    String EmailHolder, emailsender;
    TextView etUsername;
    Button logout;
    String checkemail, myemail;
    List<DataAdapter> DataAdapterClassList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    ProgressBar progressBar;
    JsonArrayRequest jsonArrayRequest ;
    ArrayList<String> SubjectNames;
    RequestQueue requestQueue ;
    View ChildView ;
    int RecyclerViewClickedItemPOS ;
    String HTTP_SERVER_URL = "https://paritasampa95.000webhostapp.com/MapleGraph/getallpost.php";
    String post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        logout=(Button)findViewById(R.id.logout);
        etUsername = (TextView) findViewById(R.id.etUsername);
        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra(Login.UserEmail);
        etUsername.setText(EmailHolder);
        emailsender = etUsername.getText().toString();
        checkemail = etUsername.getText().toString();
        myemail = etUsername.getText().toString();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", checkemail);
        editor.commit();
        DataAdapterClassList = new ArrayList<>();
        SubjectNames = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recylcerView1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        JSON_WEB_CALL();
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(HomeScreen.this,
                    new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {
                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {
                    RecyclerViewClickedItemPOS = Recyclerview.getChildAdapterPosition(ChildView);
                }
                return false;
            }
            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {
            }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout=new Intent(HomeScreen.this, Login.class);
                startActivity(logout);
                finish();
            }
        });
    }
    public void JSON_WEB_CALL(){
        jsonArrayRequest = new JsonArrayRequest(HTTP_SERVER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){
        for(int i = 0; i<array.length(); i++) {
            DataAdapter GetDataAdapter2 = new DataAdapter();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                GetDataAdapter2.setEmail(json.getString("email"));
                GetDataAdapter2.setTime(json.getString("time"));
                GetDataAdapter2.setDetails(json.getString("details"));
                post=json.getString("details");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            DataAdapterClassList.add(GetDataAdapter2);
        }
        progressBar.setVisibility(View.GONE);
        recyclerViewadapter = new RecyclerViewAdapter(DataAdapterClassList, this);
        recyclerView.setAdapter(recyclerViewadapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.profile) {
            Intent post = new Intent(HomeScreen.this, Profile.class);
            post.putExtra("email", checkemail);
            startActivity(post);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}



