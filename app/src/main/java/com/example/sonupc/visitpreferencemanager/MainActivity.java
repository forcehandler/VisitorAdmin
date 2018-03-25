package com.example.sonupc.visitpreferencemanager;

        import android.support.annotation.NonNull;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;

        import com.example.sonupc.visitpreferencemanager.fragment.CameraFragment;
        import com.example.sonupc.visitpreferencemanager.fragment.InstituteSelector;
        import com.example.sonupc.visitpreferencemanager.fragment.ScreenSelector;
        import com.example.sonupc.visitpreferencemanager.fragment.SuccessFragment;
        import com.example.sonupc.visitpreferencemanager.fragment.SurveyFragment;
        import com.example.sonupc.visitpreferencemanager.fragment.TextInputFragment;
        import com.example.sonupc.visitpreferencemanager.preference.CameraPreference;
        import com.example.sonupc.visitpreferencemanager.preference.MasterWorkflow;
        import com.example.sonupc.visitpreferencemanager.preference.Preference;
        import com.example.sonupc.visitpreferencemanager.preference.PreferencesModel;
        import com.example.sonupc.visitpreferencemanager.preference.SurveyPreferenceModel;
        import com.example.sonupc.visitpreferencemanager.preference.TextInputPreferenceModel;
        import com.example.sonupc.visitpreferencemanager.preference.ThankYouPreference;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.util.ArrayList;
        import java.util.LinkedHashMap;
        import java.util.List;

public class MainActivity extends AppCompatActivity implements InstituteSelector.OnInstituteSelectedListener,
        ScreenSelector.OnScreenSelectorListener, TextInputFragment.OnTextPreferenceListener,
        SurveyFragment.OnSurveyPreferenceListener, CameraFragment.CameraFragmentInteraction{

    private static final String TAG = MainActivity.class.getSimpleName();

    private DatabaseReference mDatabase;

    private int state = 0;
    private int statecount = -1;

    private String instituteId;
    private String email;
    private String workflowName;
    private boolean isWfSignOut;

    private FragmentManager fragmentManager;
    private List<Integer> stateCountList;

    ArrayList<Preference> workflow_order;

    private ThankYouPreference mThankYouPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        stateCountList = new ArrayList<>();
        workflow_order = new ArrayList<>();

        fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container_fragment);

        if(fragment == null){
            fragmentManager.beginTransaction()
                    .add(R.id.container_fragment, new InstituteSelector())
                    .commit();
        }
    }

    private void driver(){
        Log.d(TAG, "driver()");
        switch (state){
            case 0:
                Log.d(TAG, "state 0, statecount = " + statecount);
                if(statecount < stateCountList.get(state)){
                    fragmentManager.beginTransaction()
                            .replace(R.id.container_fragment, new TextInputFragment())
                            .commit();
                }
                break;

            case 1:
                Log.d(TAG, "state 1, statecount = " + statecount);
                if(statecount < stateCountList.get(state)){

                    fragmentManager.beginTransaction()
                            .replace(R.id.container_fragment, new SurveyFragment())
                            .commit();
                }
                break;

            case 2:
                Log.d(TAG, "state 2, statecount = " + statecount);
                if(statecount < stateCountList.get(state)){

                    fragmentManager.beginTransaction()
                            .replace(R.id.container_fragment, new CameraFragment())
                            .commit();
                }
                break;

            case 3:
                Log.d(TAG, "state 3, uploading....");

                PreferencesModel preferencesModel = new PreferencesModel();
                preferencesModel.setSignup_time(System.currentTimeMillis());

                workflow_order.add(mThankYouPreference);
                preferencesModel.setOrder_of_screens(workflow_order);
                preferencesModel.setWorkflowForSignOut(isWfSignOut);

                mDatabase.child(instituteId).child("workflows_map").child(workflowName)
                        .setValue(preferencesModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Successfully registered prefmodel on realtime database");
                                fragmentManager.beginTransaction()
                                        .replace(R.id.container_fragment, new SuccessFragment())
                                        .commit();
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "failed to add preferences object", e);
                            }
                        });
                break;
        }
    }

    @Override
    public void onInstituteSelected(String id) {
        Log.d(TAG, "Received institute id = " + id);
        instituteId = id;
        fragmentManager.beginTransaction()
                .replace(R.id.container_fragment, new ScreenSelector())
                .commit();
    }

    @Override
    public void onScreensSelected(int text, int survey, int camera, String thankYou, String workflow_name, boolean isWfSignOut) {
        stateCountList.add(text);
        stateCountList.add(survey);
        stateCountList.add(camera);

        this.isWfSignOut = isWfSignOut;

        workflowName = workflow_name;
        mThankYouPreference = new ThankYouPreference();
        mThankYouPreference.setThank_you_text(thankYou);

        updateState();
    }

    @Override
    public String getInstituteId() {
        return instituteId;
    }

    @Override
    public void onTextPreferenceListener(TextInputPreferenceModel textInputPreferenceModel) {
        Log.d(TAG, textInputPreferenceModel.getPage_title());
        workflow_order.add(textInputPreferenceModel);
        updateState();
    }

    @Override
    public void onSurveyPreferenceSubmit(SurveyPreferenceModel surveyPreferenceModel) {
        Log.d(TAG, surveyPreferenceModel.getSurvey_title());
        workflow_order.add(surveyPreferenceModel);
        updateState();
    }

    @Override
    public void onCameraFragmentInteraction(CameraPreference cameraPreference) {
        Log.d(TAG, cameraPreference.getCamera_hint_text());
        workflow_order.add(cameraPreference);
        updateState();
    }

    private void updateState(){
        Log.d(TAG, "state: " + state);
        Log.d(TAG, "statecount: " + statecount);
        statecount++;
        if(stateCountList.get(state) != 0) {
            Log.d(TAG, "state: " + state + " count is " + stateCountList.get(state));
            if (statecount == stateCountList.get(state)) {
                Log.d(TAG, "state: " + state + " count exhauseted");
                statecount = 0;
                state++;
            }
            else{
                Log.d(TAG, "state: " + state + " moving to count: " + statecount);

                driver();
            }
        }
        if((state < stateCountList.size()) && stateCountList.get(state) == 0){
            Log.d(TAG, "state: " + state + " has 0 screens");
            int i = state;
            while((i < stateCountList.size()) && (stateCountList.get(i) == 0)){
                i++;
            }

            state = i;
            statecount = 0;
            Log.d(TAG, "after looping setting state: " + state );
            driver();
        }
        else{
            driver();
        }
    }

}
