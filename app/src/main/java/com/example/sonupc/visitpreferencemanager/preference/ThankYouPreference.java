package com.example.sonupc.visitpreferencemanager.preference;

/**
 * Created by sonupc on 27-02-2018.
 */

public class ThankYouPreference extends Preference {

    private String thank_you_text;
    public final String wipe = "CLASS_THANKYOU";

    public ThankYouPreference() {
    }

    public String getThank_you_text() {
        return thank_you_text;
    }

    public void setThank_you_text(String thank_you_text) {
        this.thank_you_text = thank_you_text;
    }

    public String getWipe() {
        return wipe;
    }
}
