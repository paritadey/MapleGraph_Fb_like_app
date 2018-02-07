package com.example.parita.maplegraph;

import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Registration extends AppCompatActivity {
    EditText fullname, email, phone, address, password, retypepass;
    TextView setdate, loginlink, dob, timeofreg;
    Spinner sex;
    Button  reg;
    DatePickerDialog.OnDateSetListener datePickerListener;
    private Calendar myCalendar = Calendar.getInstance();
    Boolean CheckEditText,passwordmatch ;
    ProgressDialog progressDialog;
    String uname, uemail, uphone, uadd, usetdate, ugen, upass, urepass, uregtime;
    String finalResult ;
    public String msg="Email Already Exist";
    String flag;
    String HttpURL="https://paritasampa95.000webhostapp.com/MapleGraph/registration.php";
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String chk_gender="Choose Gender";
    Context apContext, ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();

        SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd/MM/yyyy : HH:mm:ss");
        Date myDate = new Date();
        String filename = timeStampFormat.format(myDate);
        timeofreg=(TextView)findViewById(R.id.timeofreg);
        timeofreg.setText(filename);

        loginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent log=new Intent(Registration.this, Login.class);
                log.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(log);
                finish();
            }
        });

        datePickerListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                setdate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Registration.this, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                Checkpassword();

                if (CheckEditText && passwordmatch) {


                    UserRegisterFunction(uemail,uname, uphone, upass, uadd, usetdate,ugen,uregtime);


                } else {
                    Toast.makeText(Registration.this, "Please fill all form fields correctly.",
                            Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void UserRegisterFunction(final String email,  final String name, final String phone,final String password,
                                     final String dob, final String gender, final String address, final String timeofreg){

        class UserRegisterFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                isConnectingToInternet(apContext);
                progressDialog = ProgressDialog.show(Registration.this,"Registering...",
                        null,true,true);
            }



            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email",params[0]);

                hashMap.put("name",params[1]);

                hashMap.put("phone",params[2]);

                hashMap.put("password", params[3]);

                hashMap.put("address", params[4]);

                hashMap.put("dob",params[5]);

                hashMap.put("gender", params[6]);

                hashMap.put("timeofreg", params[7]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                isInternet(ctx);
                flag=httpResponseMsg;
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Server Result: "+httpResponseMsg, Toast.LENGTH_LONG).show();
                clearTextFields();
            }
        }

        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();

        userRegisterFunctionClass.execute(email,name, phone, password,dob, gender, address, timeofreg);
    }
    public void CheckEditTextIsEmptyOrNot(){
        uname=fullname.getText().toString().trim();
        uemail=email.getText().toString().trim();
        uphone=phone.getText().toString().trim();
        uadd=address.getText().toString().trim();
        usetdate=setdate.getText().toString().trim();
        ugen=sex.getSelectedItem().toString();
        upass=password.getText().toString().trim();
        urepass=retypepass.getText().toString();
        uregtime=timeofreg.getText().toString().trim();
        if(TextUtils.isEmpty(uemail) || TextUtils.isEmpty(uname) || TextUtils.isEmpty(uphone)
                || TextUtils.isEmpty(uadd) || TextUtils.isEmpty(usetdate) || TextUtils.isEmpty(ugen)
                || TextUtils.isEmpty(upass))
        {
            CheckEditText = false;
        }
        else {
            if(ugen.equals(chk_gender)){
                CheckEditText = false ;
            }
            else {
                CheckEditText = true;
            }
        }
    }

    public void Checkpassword(){
        if(password.getText().toString().equals(retypepass.getText().toString()) && password.length()>0 && retypepass.length()>0){
            passwordmatch=true;
            Toast.makeText(getApplicationContext(), "Password match",
                    Toast.LENGTH_LONG).show();
        }
        else{
            passwordmatch = false;
            Toast.makeText(getApplicationContext(), "Password does not match!",
                    Toast.LENGTH_LONG).show();

        }
    }
    public void clearTextFields(){
        fullname.setText("");
        email.setText("");
        phone.setText("");
        address.setText("");
        setdate.setText("");
        sex.setSelection(0);
        password.setText("");
        retypepass.setText("");
    }
    public void init(){
        fullname=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.mailid);
        phone=(EditText)findViewById(R.id.userphoneno);
        address=(EditText)findViewById(R.id.place);
        setdate=(TextView) findViewById(R.id.dateset);
        password=(EditText)findViewById(R.id.password);
        retypepass=(EditText)findViewById(R.id.retypepassword);


        sex=(Spinner)findViewById(R.id.gender);
        ArrayAdapter<CharSequence> genadaptar = ArrayAdapter.createFromResource(this, R.array.gen,
                android.R.layout.simple_spinner_item);
        genadaptar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex.setAdapter(genadaptar);

        dob=(TextView) findViewById(R.id.date);
        reg=(Button)findViewById(R.id.register);
        loginlink=(TextView)findViewById(R.id.loginlink);
    }
    private boolean isConnectingToInternet(Context applicationContext){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            Toast.makeText(getApplicationContext(), "no internet", Toast.LENGTH_SHORT).show();
            Toast.makeText(Registration.this,"Connect to the Wifi/Mobile Data",Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;

    }

    private boolean isInternet(Context applicationContext){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            Toast.makeText(getApplicationContext(), "Registration cannot be possible without Internet", Toast.LENGTH_SHORT).show();
            return false;
        } else{
            Toast.makeText(Registration.this, "Successfully Registered !!!",Toast.LENGTH_LONG).show();
            return true;
        }


    }
}
