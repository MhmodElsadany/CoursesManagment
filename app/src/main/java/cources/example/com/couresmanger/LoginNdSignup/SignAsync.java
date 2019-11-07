package cources.example.com.couresmanger.LoginNdSignup;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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


public class SignAsync extends AsyncTask<String, Void, String> {
    Context c;

    public SignAsync(Context c) {
        this.c = c;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(c, c.getString(R.string.account), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {

        String type = params[0];
        String firstname = params[1];
        Log.v("frstname", firstname);

        String seconname = params[2];
        Log.v("secondname", seconname);

        String email = params[3];
        String user = params[4];
        String user_pass = params[5];
        if (type.equals("logIn")) {
            try {

                URL url = new URL("https://mahmoudllsadany.000webhostapp.com/center/newuser.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("firstname", "UTF-8") + "=" + URLEncoder.encode(firstname, "UTF-8") + "&" +
                        URLEncoder.encode("seconname", "UTF-8") + "=" + URLEncoder.encode(seconname, "UTF-8")
                        + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")
                        + "&" +
                        URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8")
                        + "&" +
                        URLEncoder.encode("user_pass", "UTF-8") + "=" + URLEncoder.encode(user_pass, "UTF-8");
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