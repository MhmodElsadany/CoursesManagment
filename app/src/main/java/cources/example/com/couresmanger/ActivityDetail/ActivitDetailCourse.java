package cources.example.com.couresmanger.ActivityDetail;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import cources.example.com.couresmanger.Models.TimeItem;
import cources.example.com.couresmanger.R;
import cources.example.com.couresmanger.RecyleviewCustomAdapter.TimeAdapter;


public class ActivitDetailCourse extends AppCompatActivity {
    AppCompatTextView name;
    AppCompatTextView instractor;
    AppCompatTextView price;
    String id;
    String id_user;
    String res;
    String namest, istst, pricest;
    ImageView imageicon;
    TimeItem timeItem[];
    TimeAdapter timeAdapter;
    ArrayList<TimeItem> time;
    RecyclerView recyclerView;
    Button enroll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activit_detail_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.titletoolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (AppCompatTextView) findViewById(R.id.course_name);
        instractor = (AppCompatTextView) findViewById(R.id.instractot);
        price = (AppCompatTextView) findViewById(R.id.Price);
        imageicon = (ImageView) findViewById(R.id.img_cource_header);
        enroll = (Button) findViewById(R.id.reseve);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);

        Intent n = getIntent();
        id = n.getExtras().getString("id_course");
        id_user = n.getExtras().getString("iduser");
        namest = n.getExtras().getString("name_course");
        istst = n.getStringExtra("insrator_course");
        pricest = n.getStringExtra("price_course");
        String resrve = n.getStringExtra("reseve");
        String path_image = n.getStringExtra("image_icon");


        enroll.setText(resrve);
        name.setText(namest);
        instractor.setText(istst);
        price.setText(pricest);

        Picasso.with(this).load("http://mahmoudllsadany.000webhostapp.com/center/images/icomimg/" + path_image)
                .placeholder(R.drawable.loading).error(R.drawable.loading).into(imageicon);
        new TimeAsync().execute("logIn", this.id);
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enroll.getText().equals("enroll")) {
                    new EnrollAsync(ActivitDetailCourse.this).execute("logIn", id, id_user, "insert_users_course");
                } else {
                    new EnrollAsync(ActivitDetailCourse.this).execute("logIn", id, id_user, "uuenroll");

                }
            }
        });
    }


    public class TimeAsync extends AsyncTask<String, Void, ArrayList> {


        @Override
        protected void onPostExecute(ArrayList s) {
            super.onPostExecute(s);
            timeAdapter = new TimeAdapter(ActivitDetailCourse.this, s);
            recyclerView.setLayoutManager(new LinearLayoutManager(ActivitDetailCourse.this));
            recyclerView.setAdapter(timeAdapter);
        }

        @Override
        public void onPreExecute() {

        }


        @Override
        public ArrayList<TimeItem> doInBackground(String... params) {
            String type = params[0];
            String id = params[1];
            if (type.equals("logIn")) {
                try {
                    URL url = new URL("https://mahmoudllsadany.000webhostapp.com/center/selectwithtime.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    httpURLConnection.connect();
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    res = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        res += line;
                    }

                    time = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("message");
                    timeItem = new TimeItem[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String day = jsonObject1.getString("day");
                        String from = jsonObject1.getString("fro");
                        String to = jsonObject1.getString("too");
                        timeItem[i] = new TimeItem(day, from, to);
                        time.add(timeItem[i]);


                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    Log.v("mmmmmmmm5", res + "jkjjhjjjjjjjjj");


                } catch (Exception e) {

                }
                return time;
            }


            return time;
        }
    }

    public class EnrollAsync extends AsyncTask<String, Void, String> {
        Context c;
        AlertDialog alertDialog;

        public EnrollAsync(Context c) {
            this.c = c;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            alertDialog.setMessage(s);
            alertDialog.show();
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(c).create();
            alertDialog.setTitle(getString(R.string.enroll));
        }

        @Override
        protected String doInBackground(String... params) {

            String type = params[0];
            String cours_id = params[1];
            String user_id = params[2];
            String reseve = params[3];


            if (type.equals("logIn")) {
                try {

                    URL url = new URL("https://mahmoudllsadany.000webhostapp.com/center/" + reseve + ".php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    httpURLConnection.connect();
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&" +
                            URLEncoder.encode("course_id", "UTF-8") + "=" + URLEncoder.encode(cours_id, "UTF-8");

                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String res = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        res += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    ;
                    return res;
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            return null;
        }
    }


}