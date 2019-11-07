package cources.example.com.couresmanger.Tabs;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import cources.example.com.couresmanger.MyWidget;
import cources.example.com.couresmanger.R;
import cources.example.com.couresmanger.RecyleviewCustomAdapter.CourseAdapter;

import static android.content.Context.MODE_PRIVATE;


public class Mine extends android.support.v4.app.Fragment {
    String jsonString;
    RecyclerView recyclerView;
    CourseAdapter adapter;
    ArrayList<ItemPoster> data_poster;
    DetailItems[] detailItems;
    ArrayList<String> lengthid = new ArrayList<>();
    SharedPreferences sharedPreferences1;
    SharedPreferences.Editor editor1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedPreferences1 = context.getSharedPreferences("daataa", MODE_PRIVATE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mine, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.grid_courses_enroll);


        return rootView;
    }

    public String id_user() {

        Intent oo = getActivity().getIntent();
        String id_user = oo.getExtras().getString("id_user");
        return id_user;
    }

    private class movie_asyn extends AsyncTask<String, String, ArrayList<ItemPoster>> {


        String jsonstr = "";
        String data = "";

        @Override
        protected void onPostExecute(ArrayList<ItemPoster> strings) {
            super.onPostExecute(strings);
            if (strings == null) {
                Toast.makeText(getActivity(), getString(R.string.checkintrernat), Toast.LENGTH_LONG).show();
            } else {
                adapter = new CourseAdapter(strings, getActivity(), detailItems, "unenroll");
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

                adapter.notifyDataSetChanged();
            }

            for (int i = 0; i < strings.size(); i++) {

                data += strings.get(i).getname() + "\n";

            }
            editor1 = sharedPreferences1.edit();
            editor1.putString("data", data);
            editor1.commit();

        }


        @Override
        protected ArrayList<ItemPoster> doInBackground(String... params) {
            String type = params[0];
            String userid = params[1];

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


                        detailItems[i] = new DetailItems(jsonObject1.getString("id_course"), jsonObject1.getString("course_name"), jsonObject1.getString("instructor")
                                , jsonObject1.getString("price"), jsonObject1.getString("image_icon "), id_user());
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
        new movie_asyn().execute("logIn", id_user());
        Intent intent = new Intent(getActivity(), MyWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] ids = AppWidgetManager.getInstance(getActivity())
                .getAppWidgetIds(new ComponentName(getActivity(), MyWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getActivity().sendBroadcast(intent);

    }


}

