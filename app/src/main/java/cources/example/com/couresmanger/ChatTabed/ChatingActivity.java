package cources.example.com.couresmanger.ChatTabed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cources.example.com.couresmanger.R;

public class ChatingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chating_tabed);

        if (savedInstanceState == null) {
            Chatting mChatting = new Chatting();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mChatting, "").commit();
        }
    }


}