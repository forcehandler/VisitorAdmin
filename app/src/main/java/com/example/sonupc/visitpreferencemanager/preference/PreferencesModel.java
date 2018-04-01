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

    private boolean workflowForSms;

    private String signOutField;
    private String smsField;

    public PreferencesModel() {
    }

    public boolean isWorkflowForSignOut() {
        return workflowForSignOut;
    }

    public void setWorkflowForSignOut(boolean workflowForSignOut) {
        this.workflowForSignOut = workflowForSignOut;
    }

    public PreferencesModel(long signup_time, ArrayList<Preference> order_of_screens, boolean workflowForSignOut, boolean workflowForSms, String signOutField, String smsField) {
        this.signup_time = signup_time;

        this.order_of_screens = order_of_screens;

        this.workflowForSignOut = workflowForSignOut;
        this.workflowForSms = workflowForSms;
        this.signOutField = signOutField;
        this.smsField = smsField;

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

    public boolean isWorkflowForSms() {
        return workflowForSms;
    }

    public void setWorkflowForSms(boolean workflowForSms) {
        this.workflowForSms = workflowForSms;
    }

    public String getSignOutField() {
        return signOutField;
    }

    public void setSignOutField(String signOutField) {
        this.signOutField = signOutField;
    }

    public String getSmsField() {
        return smsField;
    }

    public void setSmsField(String smsField) {
        this.smsField = smsField;
    }
}