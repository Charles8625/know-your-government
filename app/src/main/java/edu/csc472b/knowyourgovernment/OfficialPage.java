package edu.csc472b.knowyourgovernment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

public class OfficialPage extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "OfficialPage";
    private TextView office;
    private TextView party;
    private TextView name;
    private TextView address;
    private TextView phone;
    private TextView email;
    private TextView website;
    private ImageView photo;
    private String facebookID;
    private String twitterID;
    private String youtubeID;
    private TextView locationDisplay;
    private String location;
    private String websiteString;
    private String emailString;
    private String partyString;
    private String addressString;
    private String phoneString;
    private String photoURL;
    private Official official;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_page);

        office = findViewById(R.id.officialPageOffice);
        party = findViewById(R.id.officialPageParty);
        name = findViewById(R.id.officialPageName);
        address = findViewById(R.id.officialPageAddress);
        phone = findViewById(R.id.officialPagePhone);
        email = findViewById(R.id.officialPageEmail);
        website = findViewById(R.id.officialPageWebsite);
        photo = findViewById(R.id.officialPagePic);
        TextView emailTextView = findViewById(R.id.emailTextView);
        TextView addressTextView = findViewById(R.id.addressTextView);
        TextView phoneTextView = findViewById(R.id.phoneTextView);
        TextView websiteTextView = findViewById(R.id.websiteTextView);
        ImageView facebookIV = findViewById(R.id.facebookButton);
        ImageView twitterIV = findViewById(R.id.twitterButton);
        ImageView youtubeIV = findViewById(R.id.youtubeButton);
        ImageView partyLogo = findViewById(R.id.partyLogo);
        ConstraintLayout officialBackground = findViewById(R.id.officialBackground);
        locationDisplay = findViewById(R.id.locationDisplay);

        Intent officialPage = getIntent();

        if (officialPage.hasExtra("location")) {
            locationDisplay.setText((String) officialPage.getSerializableExtra("location"));
            location = (String) officialPage.getSerializableExtra("location");
        }

        if (officialPage.hasExtra("Official")) {
            official = (Official) officialPage.getSerializableExtra("Official");

            if (official != null) {

                emailString = official.getOfficialEmail();
                partyString = official.getOfficialParty();
                addressString = official.getOfficialAddress();
                phoneString = official.getOfficialPhone();
                websiteString = official.getOfficialWebsite();
                photoURL = official.getOfficialPhotoURL();
                facebookID = official.getFacebookID();
                twitterID = official.getTwitterID();
                youtubeID = official.getYoutubeID();


                if (emailString.isEmpty()) {
                    email.setVisibility(View.GONE);
                    emailTextView.setVisibility(View.GONE);
                }

                if (partyString.contains("Democrat")) {
                    partyLogo.setImageResource(R.drawable.dem_logo);
                    officialBackground.setBackgroundColor(Color.BLUE);
                } else if (partyString.contains("Republican")) {
                    partyLogo.setImageResource(R.drawable.rep_logo);
                    officialBackground.setBackgroundColor(Color.RED);
                } else {
                    partyLogo.setVisibility(View.GONE);
                }

                if (addressString.isEmpty()) {
                    address.setVisibility(View.GONE);
                    addressTextView.setVisibility(View.GONE);
                }

                if (phoneString.isEmpty()) {
                    phone.setVisibility(View.GONE);
                    phoneTextView.setVisibility(View.GONE);
                }

                if (websiteString.isEmpty()) {
                    website.setVisibility(View.GONE);
                    websiteTextView.setVisibility(View.GONE);
                }

                if (facebookID.isEmpty()) {

                    facebookIV.setVisibility(View.GONE);
                }

                if (twitterID.isEmpty()) {
                    twitterIV.setVisibility(View.GONE);
                }

                if (youtubeID.isEmpty()) {
                    youtubeIV.setVisibility(View.GONE);

                } else {


                    office.setText(official.getOfficialOffice());
                    party.setText(partyString);
                    name.setText(official.getOfficialName());
                    address.setText(addressString);
                    phone.setText(phoneString);
                    email.setText(emailString);
                    website.setText(websiteString);
                    String imageURL = official.getOfficialPhotoURL();
                    if (!imageURL.isEmpty()) loadRemoteImage(imageURL);
                }


            }
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            //Facebook Button Clicked
            case R.id.facebookButton:


                String FACEBOOK_URL = "https://www.facebook.com/" + facebookID; // Need to get from JSON
                String urlToUse;

                PackageManager packageManager = getPackageManager();

                try {
                    int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                    if (versionCode >= 3002850) {
                        urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                    } else {
                        urlToUse = "fb//page/" + facebookID;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    urlToUse = FACEBOOK_URL;
                }
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                facebookIntent.setData(Uri.parse(urlToUse));
                startActivity(facebookIntent);


                Toast.makeText(this, "Facebook", Toast.LENGTH_SHORT).show();
                break;

            // Twitter Button Clicked
            case R.id.twitterButton:
                Toast.makeText(this, "Twitter", Toast.LENGTH_SHORT).show();

                Intent intentTwitter = null;
                String nameTwitter = twitterID;

                try {
                    getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intentTwitter = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + nameTwitter));
                    intentTwitter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    intentTwitter = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + nameTwitter));
                }
                startActivity(intentTwitter);
                break;

            // YouTube Button Clicked
            case R.id.youtubeButton:
                String nameYouTube = youtubeID;
                Intent intentYouTube = null;
                try {
                    intentYouTube = new Intent(Intent.ACTION_VIEW);
                    intentYouTube.setPackage("com.google.android.youtube");
                    intentYouTube.setData(Uri.parse("https://www.youtube.com/" + nameYouTube));
                    startActivity(intentYouTube);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/" + nameYouTube)));
                }

                Toast.makeText(this, "YouTube", Toast.LENGTH_SHORT).show();
                break;

            case R.id.officialPagePic:

                if (!photoURL.isEmpty()) {
                    Intent startOfficialPicture = new Intent(this, OfficialPicture.class);
                    Log.d(TAG, "onClick: " + official.toString());
                    startOfficialPicture.putExtra("office", official);
                    startOfficialPicture.putExtra("location", location);
                    startActivity(startOfficialPicture);
                }
                break;

            case R.id.officialPageAddress:

                String address = addressString;

                Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));

                Intent addressIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                addressIntent.setPackage("com.google.android.apps.maps");

                if (addressIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(addressIntent);
                } else {
                    makeErrorAlert("No Application found that handles ACTION_VIEW (geo) intents");
                }

                break;

            case R.id.officialPagePhone:

                String number = phoneString;

                Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                phoneIntent.setData(Uri.parse("tel:" + number));

                if (phoneIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(phoneIntent);
                } else {
                    makeErrorAlert("No Application found that handles ACTION_DIAL (tel) intents");
                }

                break;

            case R.id.officialPageEmail:

                String[] addresses = new String[]{emailString};
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses);

                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(emailIntent, 111);
                } else {
                    makeErrorAlert("No Application found that handles SENDTO (mailto) intents");
                }
                break;

            case R.id.officialPageWebsite:
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteString));
                startActivity(websiteIntent);
                break;

            case R.id.partyLogo:

                if (partyString.contains("Democrat")) {
                    Intent pLogo = new Intent(Intent.ACTION_VIEW, Uri.parse("https://democrats.org"));
                    startActivity(pLogo);
                }

                if (partyString.contains("Republican")) {
                    Intent pLogo = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gop.com"));
                    startActivity(pLogo);
                }
                break;
        }
    }

    private void makeErrorAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle("No App Found");

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void loadRemoteImage(final String imageURL) {

        Picasso.get().load(imageURL)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(photo);

    }

}