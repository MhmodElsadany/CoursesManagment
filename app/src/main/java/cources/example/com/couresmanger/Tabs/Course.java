package cources.example.com.couresmanger.Tabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cources.example.com.couresmanger.Models.DetailItems;
import cources.example.com.couresmanger.Models.ItemPoster;
import cources.example.com.couresmanger.R;
import cources.example.com.couresmanger.RecyleviewCustomAdapter.CourseAdapter;


public class Course extends android.support.v4.app.Fragment {
    RecyclerView recyclerView;
    ArrayList<ItemPoster> data_poster;
    String jsonString;
    CourseAdapter adapter;
    DetailItems[] detailItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.course, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.grid_courses);

        new movie_asyn().execute();

        return rootView;

    }

    public String id_user() {
        return getActivity().getIntent().getExtras().getString("id_user");
    }


    private class movie_asyn extends AsyncTask<String, String, ArrayList<ItemPoster>> {

        HttpURLConnection httpURLConnection = null;
        URL url = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        String line = "";
        String jsonstr = "";

        @Override
        protected void onPostExecute(ArrayList<ItemPoster> strings) {
            super.onPostExecute(strings);
            if (strings == null) {

            } else {
                adapter = new CourseAdapter(data_poster, getActivity(), detailItems, "enroll");
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                adapter.notifyDataSetChanged();
            }

        }


        @Override
        protected ArrayList<ItemPoster> doInBackground(String... params) {

            try {
                url = new URL("https://mahmoudllsadany.000webhostapp.com/center/select.php");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                inputStream = httpURLConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //       Log.e("buffer",buffer.toString());
                jsonstr = buffer.toString();
                Log.v("html : ", jsonstr);
                jsonString = jsonstr;
                JSONObject jsonObject = new JSONObject(jsonstr);
                JSONArray jsonArray = jsonObject.getJSONArray("message");
                data_poster = new ArrayList<>();
                detailItems = new DetailItems[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Log.v("mmmmahmoud", jsonObject1.getString("image_heeader "));
                    String imageposter = jsonObject1.getString("image_heeader ");
                    data_poster.add(new ItemPoster(imageposter, jsonObject1.getString("course_name")));
                    Log.v("json", data_poster.get(i).getImages());
                    detailItems[i] = new DetailItems(jsonObject1.getString("id_course"), jsonObject1.getString("course_name"), jsonObject1.getString("instructor")
                            , jsonObject1.getString("price"), jsonObject1.getString("image_icon "), id_user());


                }


            } catch (Exception e) {
                System.out.println("mhmoud : " + e.getMessage());

            }

            return data_poster;

        }


    }


}
