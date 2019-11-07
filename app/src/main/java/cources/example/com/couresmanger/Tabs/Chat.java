package cources.example.com.couresmanger.Tabs;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import cources.example.com.couresmanger.Models.DetailItems;
import cources.example.com.couresmanger.Models.ItemPoster;
import cources.example.com.couresmanger.R;
import cources.example.com.couresmanger.RecyleviewCustomAdapter.CourseAdapterChat;


public class Chat extends android.support.v4.app.Fragment {
    String jsonString;
    RecyclerView recyclerView;
    CourseAdapterChat adapter;
    ArrayList<ItemPoster> data_poster;
    DetailItems[] detailItems;
    ArrayList<String> lengthid = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.chat, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.course_user_enrolled_chatting);
        Log.i("id_user", id_user());
        return rootView;

    }

    public String id_user() {

        Intent oo = getActivity().getIntent();
        String id_user = oo.getExtras().getString("id_user");
        return id_user;
    }

    private class ChatCources extends AsyncTask<String, String, ArrayList<ItemPoster>> {


        String jsonstr = "";

        @Override
        protected void onPostExecute(ArrayList<ItemPoster> strings) {
            super.onPostExecute(strings);
            if (strings == null) {

            } else {
                adapter = new CourseAdapterChat(strings, getActivity(), detailItems);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter.notifyDataSetChanged();
            }

        }


        @Override
        protected ArrayList<ItemPoster> doInBackground(String... params) {
            String type = params[0];
            String userid = params[1];
            Log.v(userid, userid);

            if (type.equals("logIn")) {

                try {
                    URL url = new URL("https://mahmoudllsadany.000webhostapp.com/center/enrollcourses.php");

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    httpURLConnection.connect();
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("user__id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");

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
                    jsonstr = res.toString();
                    Log.v("htmml : ", jsonstr);
                    jsonString = jsonstr;
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("message");
                    data_poster = new ArrayList<>();
                    detailItems = new DetailItems[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String imageposter = jsonObject1.getString("image_heeader ");
                        Log.v("aaana", imageposter);

                        data_poster.add(new ItemPoster(imageposter, jsonObject1.getString("course_name")));
                        Log.v("jsson", data_poster.get(i).getImages());
                        Log.v("jssson", data_poster.get(i).getname());


                        detailItems[i] = new DetailItems(jsonObject1.getString("id_course"), jsonObject1.getString("course_name"), jsonObject1.getString("instructor")
                                , jsonObject1.getString("price"), jsonObject1.getString("image_icon "), id_user());
                        Log.v("instractor", detailItems[i].getInstractor_course());
                        lengthid.add(jsonObject1.getString("id_course"));

                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return data_poster;
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            return data_poster;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new ChatCources().execute("logIn", id_user());

    }


}
