package com.example.bookadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminHomepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepage);
    }

    public void btnupload(View view) {

        Intent i = new Intent(AdminHomepage.this,Try.class);
        startActivity(i);
        finish();
    }
}
