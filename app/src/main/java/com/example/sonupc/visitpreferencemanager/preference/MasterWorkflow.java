package com.example.sonupc.visitpreferencemanager.preference;

import java.util.Map;

/**
 * Created by sonupc on 27-02-2018.
 */

public class MasterWorkflow {

    private Map<String, PreferencesModel> workflows_map;

    public MasterWorkflow() {
    }

    public MasterWorkflow(Map<String, PreferencesModel> workflows_map) {
        this.workflows_map = workflows_map;
    }

    public Map<String, PreferencesModel> getWorkflows_map() {
        return workflows_map;
    }

    public void setWorkflows_map(Map<String, PreferencesModel> workflows_map) {
        this.workflows_map = workflows_map;
    }
}