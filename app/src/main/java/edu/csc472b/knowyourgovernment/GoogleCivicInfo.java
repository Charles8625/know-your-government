package edu.csc472b.knowyourgovernment;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import javax.xml.namespace.QName;

public class GoogleCivicInfo implements Runnable {

    private static final String TAG = "GoogleCivicInfo";
    private final MainActivity mainActivity;
    private OfficialPage officialPage;
    private final String gciURL;

    private String line1;
    private String line2;
    private String line3;
    private String officialAddress;
    private String officialCity;
    private String officialState;
    private String officialZip;

    private String locationCity;
    private String locationState;
    private String locationZip;
    private String officialWebsite;
    private String officialPhone;
    private String officialPhoto;
    private String officialParty;
    private String officialName;
    private String officialEmail;
    private String officialOffice;

    private String facebookID;
    private String twitterID;
    private String youtubeID;




    GoogleCivicInfo(MainActivity mainActivity, String locationCode) {
        Log.d(TAG, "GoogleCivicInfo: " + locationCode);
        this.mainActivity = mainActivity;
        gciURL = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIz" +
                "aSyCDMrkTlpI-8hHQLuweYldaP1EmkvwprEQ&address=" + locationCode;

    }

    @Override
    public void run() {
        Uri dataUri = Uri.parse(gciURL);
        String urlToUse = dataUri.toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {

                Log.d(TAG, "run: HTTP ResponseCode NOT OK: " + conn.getResponseCode());
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.wrongFormat();
                    }
                });

                return;
            }

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }


        } catch (Exception e) {
            Log.d(TAG, "run: " + e);
            return;
        }

        Log.d(TAG, "run: " + sb.toString());
        process(sb.toString());
    }


    private void process(String url) {
        try {

            JSONObject gciJSON = new JSONObject(url);

            // Current Location City, State and Zip
            JSONObject normalizedInput = gciJSON.getJSONObject("normalizedInput");
            locationCity = normalizedInput.getString("city");
            locationState = normalizedInput.getString("state");
            locationZip = normalizedInput.getString("zip");

            // Offices of current location
            JSONArray offices = gciJSON.getJSONArray("offices");
            for (int i = 0; i < offices.length(); i++) {


                JSONObject oObj = offices.getJSONObject(i);
                JSONArray indicesArray = oObj.getJSONArray("officialIndices");

                officialOffice = oObj.getString("name");

                for (int j = 0; j < indicesArray.length(); j++) {



                    int index = indicesArray.getInt(j);
                    JSONArray officials = gciJSON.getJSONArray("officials");
                    JSONObject officialObject = officials.getJSONObject(index);


                    if (!officials.getJSONObject(index).has("emails")) {
                        officialEmail = "";
                    } else {
                        JSONArray emailsArray = officialObject.getJSONArray("emails");
                        officialEmail = emailsArray.getString(0);
                    }

                    if (!officials.getJSONObject(index).has("urls")) {
                        officialWebsite = "";
                    } else {
                        JSONArray urlsArray = officialObject.getJSONArray("urls");
                        officialWebsite = urlsArray.getString(0);
                    }

                    if (!officials.getJSONObject(index).has("phones")) {
                        officialPhone = "";
                    } else {
                        JSONArray phonesArray = officialObject.getJSONArray("phones");
                        officialPhone = phonesArray.getString(0);
                    }

                    if (!officials.getJSONObject(index).has("photoUrl")) {
                        officialPhoto = "";
                    } else {
                        officialPhoto = officialObject.getString("photoUrl");
                    }

                    if (!officials.getJSONObject(index).has("party")) {
                        officialParty = "";
                    } else {
                        officialParty = officialObject.getString("party");
                    }

                    if (officials.getJSONObject(index).has("channels")) {
                        JSONArray channelsArray = officialObject.getJSONArray("channels");


                        for (int c = 0; c < channelsArray.length(); c++){
                            JSONObject channelsObj = channelsArray.getJSONObject(c);

                            if(channelsObj.has("type")){
                                if(channelsObj.getString("type").equals("Facebook")){
                                    facebookID = channelsObj.getString("id");

                                }
                                if(channelsObj.getString("type").equals("Twitter")){
                                    twitterID = channelsObj.getString("id");

                                }
                                if(channelsObj.getString("type").equals("YouTube")){
                                    youtubeID = channelsObj.getString("id");

                                }


                            }


                        }




                    }

                    JSONArray addressArray = officialObject.getJSONArray("address");
                    for (int k = 0; k < addressArray.length(); k++) {
                        JSONObject addressObj = addressArray.getJSONObject(k);

                        if (!addressObj.has("line1")) {
                            line1 = "";
                        } else {
                            line1 = addressObj.getString("line1");
                            Log.d(TAG, "process: line1: " + line1);
                        }
                        if (!addressObj.has("line2")) {
                            line2 = "";
                        } else {
                            line2 = addressObj.getString("line2");
                            Log.d(TAG, "process: line2: " + line2);
                        }
                        if (!addressObj.has("line3")) {
                            line3 = "";
                        } else {
                            line3 = addressObj.getString("line3");
                            Log.d(TAG, "process: line3: " + line3);
                        }

                        if (!addressObj.has("city")) {
                            officialCity = "";
                        } else {
                            officialCity = addressObj.getString("city");
                        }

                        if (!addressObj.has("state")) {
                            officialState = "";
                        } else {
                            officialState = addressObj.getString("state");
                        }

                        if (!addressObj.has("zip")) {
                            officialZip = "";
                        } else {
                            officialZip = addressObj.getString("zip");
                        }

                    }

                    String lines = line1 + "\n" + line2 + "\n" + line3 + "\n";
                    lines = lines.trim();
                    officialAddress = lines + "\n" + officialCity + ", " + officialState +
                            " " + officialZip;

                    Log.d(TAG, "process: address: " + officialAddress);
                    officialName = officialObject.getString("name");


                    final Official official = new Official(officialName, officialOffice, officialParty,
                            officialAddress, officialPhone, officialEmail,
                            officialWebsite, officialPhoto, facebookID, twitterID, youtubeID);

                    final String locationDisplay = locationCity + ", " + locationState + " " + locationZip;
                    Log.d(TAG, "process: " + locationDisplay);

                    mainActivity.runOnUiThread(() -> {
                        mainActivity.setLocationDisplay(locationDisplay);
                        mainActivity.addOfficial(official);
                    });



                }

            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "process: Exception " + e);
        }

    }


}
