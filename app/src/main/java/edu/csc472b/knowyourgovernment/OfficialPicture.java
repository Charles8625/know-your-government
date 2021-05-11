package edu.csc472b.knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class OfficialPicture extends AppCompatActivity {

    private static final String TAG = "OfficialPicture";
    private TextView opOffice;
    private TextView opName;
    private ImageView opPicture;
    private Official official;
    private TextView locationDisplay3;
    private ImageView partLogo2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_picture);

        opOffice = findViewById(R.id.opOffice);
        opName = findViewById(R.id.opName);
        opPicture = findViewById(R.id.opPicture);
        locationDisplay3 = findViewById(R.id.locationDisplay3);
        partLogo2 = findViewById(R.id.partyLogo2);

        ConstraintLayout opBackground = findViewById(R.id.opBackground);
        Intent officialPicture = getIntent();

        if(officialPicture.hasExtra("location")){
            locationDisplay3.setText((String) officialPicture.getSerializableExtra("location"));
        }

        if (officialPicture.hasExtra("office")) {
            official = (Official) officialPicture.getSerializableExtra("office");
            if (official != null) {

                Log.d(TAG, "onCreate: " + official.getOfficialName());

                opOffice.setText(official.getOfficialOffice());
                opName.setText(official.getOfficialName());

                String imageURL = official.getOfficialPhotoURL();
                if (!imageURL.isEmpty()) loadRemoteImage(imageURL);

                if(official.getOfficialParty().contains("Democrat")){
                    opBackground.setBackgroundColor(Color.BLUE);
                    partLogo2.setImageResource(R.drawable.dem_logo);
                }
                else if(official.getOfficialParty().contains("Republican")){
                    opBackground.setBackgroundColor(Color.RED);
                    partLogo2.setImageResource(R.drawable.rep_logo);
                }
                else{
                    partLogo2.setVisibility(View.GONE);
                }
            }
        }
    }

    public void onClick(View view) {

        if(official.getOfficialParty().contains("Democrat")){
            Intent pLogo = new Intent(Intent.ACTION_VIEW, Uri.parse("https://democrats.org"));
            startActivity(pLogo);
        }

        if(official.getOfficialParty().contains("Republican")){
            Intent pLogo = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gop.com"));
            startActivity(pLogo);
        }

    }



    private void loadRemoteImage(final String imageURL) {

        Picasso.get().load(imageURL)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(opPicture);

    }
}