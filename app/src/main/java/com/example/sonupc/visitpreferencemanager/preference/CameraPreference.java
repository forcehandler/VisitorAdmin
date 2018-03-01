package com.example.sonupc.visitpreferencemanager.preference;

/**
 * Created by sonupc on 27-02-2018.
 */

public class CameraPreference extends Preference {

    private String camera_hint_text;
    public final String wipe = "CLASS_CAMERA";

    public CameraPreference() {
    }

    public String getCamera_hint_text() {
        return camera_hint_text;
    }

    public void setCamera_hint_text(String camera_hint_text) {
        this.camera_hint_text = camera_hint_text;
    }

    public String getWipe() {
        return wipe;
    }
}
