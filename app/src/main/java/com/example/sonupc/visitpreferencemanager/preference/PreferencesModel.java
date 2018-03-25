package com.example.sonupc.visitpreferencemanager.preference;

import java.util.ArrayList;

/**
 * Created by sonupc on 27-02-2018.
 */

public class PreferencesModel {

    public static final String SIGNUP_TIME_KEY = "signup_time";
    private long signup_time;        // not really required

    // Map of order of screens and the Preference of the screen
    private ArrayList<Preference> order_of_screens;

    private boolean workflowForSignOut;

    public PreferencesModel() {
    }

    public boolean isWorkflowForSignOut() {
        return workflowForSignOut;
    }

    public void setWorkflowForSignOut(boolean workflowForSignOut) {
        this.workflowForSignOut = workflowForSignOut;
    }

    public PreferencesModel(long signup_time, ArrayList<Preference> order_of_screens, boolean workflowForSignOut) {
        this.signup_time = signup_time;

        this.order_of_screens = order_of_screens;

        this.workflowForSignOut = workflowForSignOut;

    }

    public long getSignup_time() {
        return signup_time;
    }

    public void setSignup_time(long signup_time) {
        this.signup_time = signup_time;
    }

    public ArrayList<Preference> getOrder_of_screens() {
        return order_of_screens;
    }

    public void setOrder_of_screens(ArrayList<Preference> order_of_screens) {
        this.order_of_screens = order_of_screens;
    }
}