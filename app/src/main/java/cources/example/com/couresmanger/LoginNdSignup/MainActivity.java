package cources.example.com.couresmanger.LoginNdSignup;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

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

public class MainActivity extends AppCompatActivity {
    private TextView tvSignupInvoker;
    private LinearLayout llSignup;
    private TextView tvSigninInvoker;
    private LinearLayout llSignin;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private String TAG = "accountfragment";
    private int REQUEST_CODE = 9001;


    EditText username;
    Typeface typefaceuser, typefacepass;
    EditText passward;
    Button login;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;

    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor1;

    CheckBox checkBox;
    String res;
    AlertDialog alertDialog;
    //sign up
    EditText first_name;
    EditText last_name;
    EditText email;
    EditText uusername;
    EditText ppasward;
    String getfirst_name;
    String getlast_name;
    String getemail;
    String getuusername;
    String getppasward;
    Button create_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvSigninInvoker = (TextView) findViewById(R.id.tvSigninInvoker);
        tvSignupInvoker = (TextView) findViewById(R.id.tvSignupInvoker);
        llSignin = (LinearLayout) findViewById(R.id.llSignin);
        llSignup = (LinearLayout) findViewById(R.id.llSignup);


        username = (EditText) findViewById(R.id.usertxt);
        passward = (EditText) findViewById(R.id.passwardtxt);
        login = (Button) findViewById(R.id.loginbuton);
        checkBox = (CheckBox) findViewById(R.id.remember);
        create_account = (Button) findViewById(R.id.create);


        tvSigninInvoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSigninForm();
            }
        });

        tvSignupInvoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignupForm();
            }
        });
        initElement();
        font();
        restorepreferance();

        sharedPreferences1 = getSharedPreferences("daataa", MODE_PRIVATE);

        //sign in with  google
        findViewById(R.id.login_with_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail().build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!registration()) {
                    Toast.makeText(MainActivity.this, getString(R.string.error), Toast.LENGTH_SHORT);

                } else {

                    new Async().execute("logIn", username.getText().toString(), passward.getText().toString());


                }
            }
        });

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_element()) {
                    String type = "logIn";

                    new SignAsync(getApplicationContext()).execute(type
                            , first_name.getText().toString()
                            , last_name.getText().toString()
                            , email.getText().toString()
                            , uusername.getText().toString()
                            , ppasward.getText().toString());
                }

            }
        });


    }


    private void signInWithGoogle() {

        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, REQUEST_CODE);

    }

    public class Async extends AsyncTask<String, Void, String> {


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("")) {
                alertDialog.setMessage(getString(R.string.errorlogin));
                alertDialog.show();
            } else {

                editor1 = sharedPreferences1.edit();
                editor1.putString("id_useer", s);
                editor1.commit();

                Intent in = new Intent(MainActivity.this, Main2Activity.class);
                in.putExtra("id_user", s);
                startActivity(in);

            }


        }

        @Override
        public void onPreExecute() {
            alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle(getString(R.string.status));

        }


        @Override
        public String doInBackground(String... params) {
            String type = params[0];
            String user_name = params[1];
            String user_pass = params[2];
            if (type.equals("logIn")) {
                try {

                    URL url = new URL("https://mahmoudllsadany.000webhostapp.com/center/registeraion_user.php");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    httpURLConnection.connect();
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") +
                            "&" + URLEncoder.encode("user_pass", "UTF-8") + "=" + URLEncoder.encode(user_pass, "UTF-8");
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
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();


                } catch (Exception e) {

                }
                return res;
            }


            return res;
        }

    }

    //overide
    @Override
    protected void onPause() {
        super.onPause();
        checkbox();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.exitapp));
        builder.setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e("on", "onAcivity Result");

        if (requestCode == REQUEST_CODE) {


            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.e("email", account.getEmail());
                firebaseAuthWithGoogle(account);


            } catch (ApiException ex) {
                Log.e("error", "API EXCEPTION");
                ex.printStackTrace();
            }


        }


    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.e(TAG, "firebaseAuthWithGoogle");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task != null) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Log.i("name", user.getDisplayName());
                    Log.i("id", user.getUid());


                    new SignGmailAsync(MainActivity.this).execute("logIn", user.getDisplayName(), user.getUid());


                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    Toast.makeText(MainActivity.this, getString(R.string.authenticationfailed), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void restorepreferance() {
        sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        username.setText(sharedPreferences.getString("username", ""));
        passward.setText(sharedPreferences.getString("passward", ""));
        checkBox.setChecked(true);


    }


    private void showSignupForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.15f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.85f;
        llSignup.requestLayout();

        tvSignupInvoker.setVisibility(View.GONE);
        tvSigninInvoker.setVisibility(View.VISIBLE);

        Animation translate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right_to_left);
        llSignup.startAnimation(translate);

        Animation clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_right_to_left);
        login.startAnimation(clockwise);

    }

    private void showSigninForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.85f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.15f;
        llSignup.requestLayout();

        Animation translate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_left_to_right);
        llSignin.startAnimation(translate);

        tvSignupInvoker.setVisibility(View.VISIBLE);
        tvSigninInvoker.setVisibility(View.GONE);
        Animation clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_left_to_right);
        create_account.startAnimation(clockwise);
    }

    // login
    public Boolean registration() {
        String usertxt = username.getText().toString();
        String passtxt = passward.getText().toString();

        if (TextUtils.isEmpty(usertxt)) {
            username.setError(getString(R.string.errorusername));

            username.requestFocus();
            return false;

        }
        if (TextUtils.isEmpty(passtxt)) {
            passward.setError(getString(R.string.errorpassward));
            passward.requestFocus();
            return false;
        }

        return true;

    }

    public void checkbox() {
        if (checkBox.isChecked()) {


            sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("username", username.getText().toString());
            editor.putString("passward", passward.getText().toString());
            editor.commit();
        }


    }

    public void font() {


        typefaceuser = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Stencbab.ttf");
        username.setTypeface(typefaceuser);
        typefacepass = Typeface.createFromAsset(getAssets(), "fonts/gtw.ttf");
        passward.setTypeface(typefacepass);

    }

    //sign up
    public void initElement() {

        first_name = (EditText) findViewById(R.id.firstname);
        last_name = (EditText) findViewById(R.id.secondnam);
        email = (EditText) findViewById(R.id.email);
        uusername = (EditText) findViewById(R.id.userf);
        ppasward = (EditText) findViewById(R.id.passwadin);
    }

    public boolean check_element() {
        boolean result = true;
        getfirst_name = first_name.getText().toString();
        getlast_name = last_name.getText().toString();
        getemail = email.getText().toString();
        getuusername = uusername.getText().toString();
        getppasward = ppasward.getText().toString();


        if (getfirst_name.isEmpty()) {
            first_name.setError(getString(R.string.errorfirstname));
            first_name.requestFocus();
            result = false;
        }
        if (getlast_name.isEmpty()) {
            last_name.setError(getString(R.string.errorlastname));
            last_name.requestFocus();
            result = false;
        }
        if (getemail.isEmpty()) {
            email.setError(getString(R.string.erroremail));
            email.requestFocus();
            result = false;
        }

        if (getuusername.isEmpty()) {
            uusername.setError(getString(R.string.errorusername));
            uusername.requestFocus();
            result = false;
        }
        if (getppasward.isEmpty() || getppasward.length() < 9) {
            ppasward.setError(getString(R.string.errorpassward));
            ppasward.requestFocus();
            result = false;
        }
        return result;
    }

}