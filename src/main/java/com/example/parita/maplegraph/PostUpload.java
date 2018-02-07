package com.example.parita.maplegraph;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class PostUpload extends AppCompatActivity {
    TextView etUsername, timeofreg;
    String ImagePath = "image_path" ;
    String checkemail, checkpost;
    ProgressDialog progressDialog;
    EditText sharepost;
    Button send, uploadpic;
    String email, uregtime;
    boolean CheckEditText;
    String posttoupload;
    String dateofupload;
    ImageView uploadimage;
    String postsender="details";
    String emailsender="email";
    String timesender="timeofreg";
    String finalResult ;
    Bitmap bitmap;
    String HttpURL="https://paritasampa95.000webhostapp.com/MapleGraph/posts.php";
    boolean check = true;
    String ServerUploadPath="https://paritasampa95.000webhostapp.com/MapleGraph/posts.php";
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String picture="No image uploaded";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);

        init();
        etUsername = (TextView) findViewById(R.id.etUsername);

        Intent intent = getIntent();
        etUsername.setText(intent.getCharSequenceExtra("email"));
        email=etUsername.getText().toString().trim();
        checkemail=etUsername.getText().toString();

        sharepost=(EditText)findViewById(R.id.sharepost);

        SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd/MM/yyyy : HH:mm:ss");
        Date myDate = new Date();
        String filename = timeStampFormat.format(myDate);
        timeofreg=(TextView)findViewById(R.id.date);
        timeofreg.setText(filename);
        uregtime=timeofreg.getText().toString().trim();
        dateofupload=timeofreg.getText().toString().trim();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                if (bitmap==null) {
                    Postupload(checkemail, posttoupload, picture, dateofupload);

                }else{
                    ImageUploadToServerFunction();
                }
            }
        });


        uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);
            }
        });
    }
    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                uploadimage.setVisibility(View.VISIBLE);
                uploadimage.setImageBitmap(bitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public void Postupload(final String email,final  String details,  String image, String timeofreg){

        class PostuploadClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(PostUpload.this,"Uploading...",
                        null,true,true);
            }



            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email",params[0]);

                hashMap.put("details", params[1]);

                hashMap.put("image_path", params[2]);

                hashMap.put("timeofreg", params[3]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Server Result: "+httpResponseMsg, Toast.LENGTH_LONG).show();
                clearTextFields();
            }
        }

        PostuploadClass PostuploadFunctionClass = new PostuploadClass();

        PostuploadFunctionClass.execute(email, details, image, timeofreg);
    }


    public void ImageUploadToServerFunction() {


            ByteArrayOutputStream byteArrayOutputStreamObject;

            byteArrayOutputStreamObject = new ByteArrayOutputStream();


            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

            byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

            final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

            class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() {

                    super.onPreExecute();

                    progressDialog = ProgressDialog.show(PostUpload.this, "Your post is been uploading",
                            "Please Wait", false, false);
                }

                @Override
                protected void onPostExecute(String string1) {

                    super.onPostExecute(string1);
                    progressDialog.dismiss();

                    Toast.makeText(PostUpload.this, string1, Toast.LENGTH_LONG).show();
                    clearTextFields();
                }

                @Override
                protected String doInBackground(Void... params) {

                    PostUpload.ImageProcessClass imageProcessClass = new PostUpload.ImageProcessClass();

                    HashMap<String, String> HashMapParams = new HashMap<String, String>();

                    HashMapParams.put(emailsender, checkemail);

                    HashMapParams.put(postsender, posttoupload);

                    HashMapParams.put(ImagePath, ConvertImage);

                    HashMapParams.put(timesender, uregtime);

                    String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);

                    return FinalData;
                }
            }
            AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

            AsyncTaskUploadClassOBJ.execute();

    }
    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }
    public void CheckEditTextIsEmptyOrNot(){
        posttoupload=sharepost.getText().toString().trim();
        if(TextUtils.isEmpty(posttoupload) || TextUtils.isEmpty(dateofupload)){
            CheckEditText = false;
        }
        else
            CheckEditText = true;
    }
    public void clearTextFields() {
        sharepost.setText("");
        uploadimage.setVisibility(View.INVISIBLE);
    }
    public void init(){

        send=(Button)findViewById(R.id.send);
        uploadpic=(Button)findViewById(R.id.uploadpic);
        uploadimage=(ImageView)findViewById(R.id.imageView2);
        uploadimage.setVisibility(View.INVISIBLE);
    }
}
