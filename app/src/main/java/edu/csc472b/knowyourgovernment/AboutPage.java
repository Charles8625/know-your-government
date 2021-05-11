package edu.csc472b.knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class AboutPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
    }
    public void onClick(View view) {

        Intent civicInfoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://developers.google.com/civic-information/"));
        startActivity(civicInfoIntent);

    }
}