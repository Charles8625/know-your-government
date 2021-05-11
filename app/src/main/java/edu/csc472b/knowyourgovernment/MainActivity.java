package edu.csc472b.knowyourgovernment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public final static List<Official> officialList = new ArrayList<>();
    private static final String TAG = "Main Activity";
    private RecyclerView recyclerView;
    private OfficialAdapter officialAdapter;

    private static int MY_LOCATION_REQUEST_CODE_ID = 111;
    private LocationManager locationManager;
    private Criteria criteria;
    private String locationCode;
    private Official official;
    private TextView locationDisplay;
    private String location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        officialAdapter = new OfficialAdapter(officialList, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        locationDisplay = findViewById(R.id.locationDisplay);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    MY_LOCATION_REQUEST_CODE_ID);

        } else {

            if(!checkNetworkConnection()){
                noConnectionData();
            } else {
                setLocation();
                GoogleCivicInfo googleCivicInfo = new GoogleCivicInfo(this, locationCode);
                new Thread(googleCivicInfo).start();
            }
        }

    }


    public void noConnectionData(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Data cannot be accessed/loaded without an internet connection");
        builder.setTitle("No Network Connection");

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull
            String[] permissions, @NonNull
                    int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(!checkNetworkConnection()){
            noConnectionData();
        }

        if (requestCode == MY_LOCATION_REQUEST_CODE_ID) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PERMISSION_GRANTED) {

                setLocation();
                GoogleCivicInfo googleCivicInfo = new GoogleCivicInfo(this, locationCode);
                new Thread(googleCivicInfo).start();
                return;
            }
        }
        Toast.makeText(this, "No Permission", Toast.LENGTH_SHORT).show();

    }

    public void addOfficial(Official official){
        officialList.add(official);
        officialAdapter.notifyDataSetChanged();

    }

    @SuppressLint("MissingPermission")
    private void setLocation() {

        String bestProvider = locationManager.getBestProvider(criteria, true);

        Location currentLocation = null;
        if (bestProvider != null) {
            currentLocation = locationManager.getLastKnownLocation(bestProvider);

        }
        if (currentLocation != null) {

            double getLatitude = currentLocation.getLatitude();
            double getLongitude = currentLocation.getLongitude();
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            try {
                List<Address> location;

                location = geocoder.getFromLocation(getLatitude, getLongitude, 10);
                Address a = location.get(0);
                locationCode = a.getPostalCode();

            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }


    }

    public void setLocationDisplay(String locationDisplay){

        this.location = locationDisplay;
        this.locationDisplay.setText(locationDisplay);
    }


    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_official, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about:
                Intent startAboutPage = new Intent(this, AboutPage.class);
                startActivity(startAboutPage);
                return true;
            case R.id.search:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                final EditText locSelection = new EditText(this);
                locSelection.setInputType(InputType.TYPE_CLASS_TEXT);
                locSelection.setGravity(Gravity.CENTER_HORIZONTAL);
                builder.setView(locSelection);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        locationCode = locSelection.getText().toString();

                        sendLocationCode(locationCode);
                        Toast.makeText(MainActivity.this, locationCode, Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setTitle("Enter City, State or a Zip Code:");

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public void wrongFormat() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Not a valid City/State or Zip Code");
        builder.setMessage("Please Try Again");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void sendLocationCode(String locationCode){
        officialList.clear();
        Log.d(TAG, "sendLocationCode: " + locationCode);
        GoogleCivicInfo googleCivicInfo = new GoogleCivicInfo(this, locationCode);
        new Thread(googleCivicInfo).start();
    }

    @Override
    public void onClick(View view) {

        int pos = recyclerView.getChildLayoutPosition(view);
        official = officialList.get(pos);

        Log.d(TAG, "onClick: " + official);

        Intent startOfficialPage = new Intent(this, OfficialPage.class);
        startOfficialPage.putExtra("Official", official);
        startOfficialPage.putExtra("location", location);
        startActivity(startOfficialPage);

    }
}