package edu.csc472b.knowyourgovernment;

import android.util.Log;

import java.io.Serializable;

public class Official implements Serializable, Comparable<Official> {

    private static final String TAG = "Official";
    private String officialName;
    private String officialOffice;
    private String officialParty;
    private String officialAddress;
    private String officialPhone;
    private String officialEmail;
    private String officialWebsite;
    private String officialPhotoURL;
    private String facebookID;
    private String twitterID;
    private String youtubeID;


    Official(String officialName, String officialOffice, String officialParty,
             String officialAddress, String officialPhone,
             String officialEmail, String officialWebsite, String officialPhotoURL,
             String facebookID, String twitterID, String youtubeID) {


        this.officialName = officialName;
        this.officialOffice = officialOffice;
        this.officialParty = officialParty;
        this.officialAddress = officialAddress;
        this.officialPhone = officialPhone;
        this.officialEmail = officialEmail;
        this.officialWebsite = officialWebsite;
        this.officialPhotoURL = officialPhotoURL;
        this.facebookID = facebookID;
        this.twitterID = twitterID;
        this.youtubeID = youtubeID;


    }


    public String getOfficialName() {
        return officialName;
    }

    public String getOfficialOffice() {
        return officialOffice;
    }

    public String getOfficialParty() {
        return officialParty;
    }

    public String getOfficialAddress() {
        return officialAddress;
    }

    public String getOfficialPhone() {
        return officialPhone;
    }

    public String getOfficialEmail() {
        return officialEmail;
    }

    public String getOfficialWebsite() {
        return officialWebsite;
    }

    public String getOfficialPhotoURL(){
        return officialPhotoURL;
    }

    public String getFacebookID() {
        Log.d(TAG, "getFacebookID: " + facebookID);
        return facebookID;
    }

    public String getTwitterID() {
        return twitterID;
    }

    public String getYoutubeID() {
        return youtubeID;
    }

    @Override
    public String toString() {
        return officialName + " | " + officialParty + " | " + officialPhotoURL;
    }

    @Override
    public int compareTo(Official official) {
        return 0;
    }
}



