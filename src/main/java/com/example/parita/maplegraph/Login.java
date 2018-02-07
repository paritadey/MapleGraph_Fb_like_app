package com.example.parita.maplegraph;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Login extends AppCompatActivity {
    EditText useremail, userpassword;
    Button login;
    TextView register, forgotpass;
    String PasswordHolder, EmailHolder;
    boolean CheckEditText;
    String finalResult ;
    String HttpURL="https://paritasampa95.000webhostapp.com/MapleGraph/login.php";
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    public static final String UserEmail = "";
    Context apContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                if(CheckEditText){
                    UserLoginFunction(EmailHolder, PasswordHolder);
                }
                else {
                    Toast.makeText(Login.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pass=new Intent(Login.this, ForgotPassword.class);
                startActivity(pass);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regis=new Intent(Login.this, Registration.class);
                startActivity(regis);
            }
        });
    }
    public void UserLoginFunction(final String email, final String password){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                isConnectingToInternet(apContext);
                progressDialog = ProgressDialog.show(Login.this,"Authenticating...",null,true,true);
            }
            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                if(httpResponseMsg.equalsIgnoreCase("Data Matched")){
                    finish();
                    Intent intent = new Intent(Login.this, HomeScreen.class);
                    intent.putExtra(UserEmail, email);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(Login.this,"Error Occured",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            protected String doInBackground(String... params) {
                hashMap.put("email",params[0]);
                hashMap.put("password",params[1]);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }
        }
        UserLoginClass userLoginClass = new UserLoginClass();
        userLoginClass.execute(email,password);
    }
    private boolean isConnectingToInternet(Context applicationContext){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            Toast.makeText(getApplicationContext(), "no internet", Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;
    }
    public void CheckEditTextIsEmptyOrNot(){
        EmailHolder = useremail.getText().toString();
        PasswordHolder = userpassword.getText().toString();
        if(TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder))
        {
            CheckEditText = false;
        }
        else {

            CheckEditText = true ;
        }
    }
    public void init(){
        useremail=(EditText)findViewById(R.id.mailid);
        userpassword=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login);
        register=(TextView)findViewById(R.id.regislink);
        forgotpass=(TextView)findViewById(R.id.forgotpass);
    }
}
