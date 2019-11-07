package cources.example.com.couresmanger.LoginNdSignup;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import cources.example.com.couresmanger.R;
import cources.example.com.couresmanger.Tabs.Main2Activity;


public class SignGmailAsync extends AsyncTask<String, Void, String> {
    Context c;
    AlertDialog alertDialog;

    public SignGmailAsync(Context c) {
        this.c = c;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null) {
            Intent intent = new Intent(c, Main2Activity.class);
            intent.putExtra("id_user", s);
            c.startActivity(intent);
        } else {
            Toast.makeText(c, c.getString(R.string.erroroccured), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected String doInBackground(String... params) {

        String type = params[0];
        String username = params[1];

        String iduser = params[2];

        if (type.equals("logIn")) {
            try {

                URL url = new URL("https://mahmoudllsadany.000webhostapp.com/center/signinup.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(iduser, "UTF-8");

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