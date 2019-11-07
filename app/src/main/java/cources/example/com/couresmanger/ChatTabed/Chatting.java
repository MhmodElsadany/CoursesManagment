package cources.example.com.couresmanger.ChatTabed;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

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

import cources.example.com.couresmanger.Models.ChatMessage;
import cources.example.com.couresmanger.R;


public class Chatting extends android.support.v4.app.Fragment {
    String id_course;
    String id_user;
    FloatingActionButton fab;
    private FirebaseListAdapter<ChatMessage> adapter;
    View rootView;

    EditText input;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_chatting, container, false);
        Intent intent = getActivity().getIntent();
        id_course = intent.getStringExtra("id_course");
        id_user = intent.getStringExtra("id_user");
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        input = (EditText) rootView.findViewById(R.id.input);
        if (savedInstanceState != null) {
            input.setText(savedInstanceState.getString("edittext"));
        }
        new ChatAsyncTask().execute("logIn", id_user);

        return rootView;
    }

    private void displayChatMessages() {
        ListView listOfMessages = (ListView) rootView.findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(getActivity(), ChatMessage.class,
                R.layout.rowinfochatingofcourse, FirebaseDatabase.getInstance().getReference().child("all").child(id_course)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("edittext", input.getText().toString());
    }

    private class ChatAsyncTask extends AsyncTask<String, String, String> {


        String jsonstr = "";

        @Override
        protected void onPostExecute(final String strings) {
            super.onPostExecute(strings);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference().child("all").child(id_course).push().
                            setValue(new ChatMessage(input.getText().toString(), strings));

                    input.setText("");
                }
            });
            displayChatMessages();

        }


        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String userid = params[1];
            Log.v(userid, userid);

            if (type.equals("logIn")) {

                try {
                    URL url = new URL("https://mahmoudllsadany.000webhostapp.com/center/value_of_user_and_passward.php");

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    httpURLConnection.connect();
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("id_user", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");

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

                    JSONObject jsonObject = new JSONObject(res);


                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return jsonstr;
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            return jsonstr;

        }
    }


}
