package com.example.sonupc.visitpreferencemanager;

        import android.support.annotation.NonNull;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.Toast;

        import com.example.sonupc.visitpreferencemanager.fragment.CameraFragment;
        import com.example.sonupc.visitpreferencemanager.fragment.InstituteSelector;
        import com.example.sonupc.visitpreferencemanager.fragment.RatingFragment;
        import com.example.sonupc.visitpreferencemanager.fragment.ScreenSelector;
        import com.example.sonupc.visitpreferencemanager.fragment.SuccessFragment;
        import com.example.sonupc.visitpreferencemanager.fragment.SuggestionFragment;
        import com.example.sonupc.visitpreferencemanager.fragment.SurveyFragment;
        import com.example.sonupc.visitpreferencemanager.fragment.TextInputFragment;
        import com.example.sonupc.visitpreferencemanager.preference.CameraPreference;
        import com.example.sonupc.visitpreferencemanager.preference.MasterWorkflow;
        import com.example.sonupc.visitpreferencemanager.preference.Preference;
        import com.example.sonupc.visitpreferencemanager.preference.PreferencesModel;
        import com.example.sonupc.visitpreferencemanager.preference.RatingPreferenceModel;
        import com.example.sonupc.visitpreferencemanager.preference.SuggestionPreference;
        import com.example.sonupc.visitpreferencemanager.preference.SurveyPreferenceModel;
        import com.example.sonupc.visitpreferencemanager.preference.TextInputPreferenceModel;
        import com.example.sonupc.visitpreferencemanager.preference.ThankYouPreference;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.firestore.CollectionReference;
        import com.google.firebase.firestore.FirebaseFirestore;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.LinkedHashMap;
        import java.util.List;
        import java.util.Map;

public class MainActivity extends AppCompatActivity implements InstituteSelector.OnInstituteSelectedListener,
        ScreenSelector.OnScreenSelectorListener, TextInputFragment.OnTextPreferenceListener,
        SurveyFragment.OnSurveyPreferenceListener, CameraFragment.CameraFragmentInteraction,
        RatingFragment.RatingFragmentListener, SuggestionFragment.SuggestionFragmentListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private DatabaseReference mDatabase;

    private int state = 0;
    private int statecount = -1;

    private String instituteId;
    private String email;
    private String workflowName;
    private boolean isWfSignOut, isWfSms;
    private String signOutField, smsField;


    private FragmentManager fragmentManager;
    private List<Integer> stateCountList;       // stores no. of screens of each type

    ArrayList<Preference> workflow_order;

    private List<String> questionsList;

    private ThankYouPreference mThankYouPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        stateCountList = new ArrayList<>();
        workflow_order = new ArrayList<>();
        questionsList = new ArrayList<>();

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
                Log.d(TAG, "state 3, statecount = " + statecount);
                if(statecount < stateCountList.get(state)){

                    fragmentManager.beginTransaction()
                            .replace(R.id.container_fragment, new RatingFragment())
                            .commit();
                }
                break;

            case 4:
                Log.d(TAG, "state 4, statecount = " + statecount);
                if(statecount < stateCountList.get(state)){

                    fragmentManager.beginTransaction()
                            .replace(R.id.container_fragment, new SuggestionFragment())
                            .commit();
                }
                break;

            case 5:
                Log.d(TAG, "state 4, uploading....");
                Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show();
                PreferencesModel preferencesModel = new PreferencesModel();
                preferencesModel.setSignup_time(System.currentTimeMillis());

                workflow_order.add(mThankYouPreference);
                preferencesModel.setOrder_of_screens(workflow_order);

                preferencesModel.setWorkflowForSignOut(isWfSignOut);
                preferencesModel.setWorkflowForSms(isWfSms);
                preferencesModel.setSignOutField(signOutField);
                preferencesModel.setSmsField(smsField);

                mDatabase.child(instituteId).child("workflows_map").child(workflowName)
                        .setValue(preferencesModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Successfully registered prefmodel on realtime database");
                                fragmentManager.beginTransaction()
                                        .replace(R.id.container_fragment, new SuccessFragment())
                                        .commit();
                                setFieldsInWorkflowDoc();
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
    public void onScreensSelected(int text, int survey, int camera, int ratings, int suggestions, String thankYou, String workflow_name) {
        stateCountList.add(text);
        stateCountList.add(survey);
        stateCountList.add(camera);

        stateCountList.add(ratings);
        stateCountList.add(suggestions);

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
        questionsList.addAll(textInputPreferenceModel.getHints());
        workflow_order.add(textInputPreferenceModel);
        updateState();
    }

    @Override
    public void onTextPreferenceListener(TextInputPreferenceModel textInputPreferenceModel, boolean workflowForSignOut, String signOutField, boolean workflowForSms, String smsField) {
        Log.d(TAG, textInputPreferenceModel.getPage_title() + " signOutField: " + signOutField + ", smsField: " + smsField);
        workflow_order.add(textInputPreferenceModel);

        questionsList.addAll(textInputPreferenceModel.getHints());  // Add all the questions to set the fields in workflow doc
        if(workflowForSignOut){
            isWfSignOut = true;
            this.signOutField = signOutField;
        }
        if(workflowForSms){
            isWfSms = true;
            this.smsField = smsField;
        }
        updateState();
    }

    @Override
    public void onSurveyPreferenceSubmit(SurveyPreferenceModel surveyPreferenceModel) {
        Log.d(TAG, surveyPreferenceModel.getSurvey_title());
        workflow_order.add(surveyPreferenceModel);
        questionsList.addAll(surveyPreferenceModel.getSurvey_item_name());
        updateState();
    }

    @Override
    public void onCameraFragmentInteraction(CameraPreference cameraPreference) {
        Log.d(TAG, cameraPreference.getCamera_hint_text());
        workflow_order.add(cameraPreference);
        updateState();
    }

    @Override
    public void onRatingQuestionsSubmit(RatingPreferenceModel ratingPreferenceModel) {
        Log.d(TAG, "Got rating preference model");
        workflow_order.add(ratingPreferenceModel);
        questionsList.addAll(ratingPreferenceModel.getQuestions());
        updateState();
    }

    @Override
    public void onSuggestionQuestionSubmit(SuggestionPreference suggestionPreference) {
        Log.d(TAG, "Got rating preference model");
        workflow_order.add(suggestionPreference);
        questionsList.add(suggestionPreference.getSuggestion_text());
        updateState();
    }

    private void setFieldsInWorkflowDoc(){
        CollectionReference workflowCollectionRef;
        workflowCollectionRef = FirebaseFirestore.getInstance().collection(getString(R.string.collection_ref_institutes))
                .document(instituteId).collection(getString(R.string.collection_ref_workflows));

        // TODO : No need to upload signOut field for doc, will fetch the workflow status from realtime database.
        // Add a field to identify the SignOut enabled workflow in the firestore

        if(isWfSignOut){
            questionsList.add(getString(R.string.KEY_WORKFLOW_PREF_SIGNIN_TIME));
            questionsList.add(getString(R.string.KEY_WORKFLOW_PREF_IS_SIGNEDOUT));
            questionsList.add(getString(R.string.KEY_WORKFLOW_PREF_SIGNOUT_TIME));
        }

        Map<String, Boolean> map = new HashMap<>();
        map.put(getString(R.string.KEY_WORKFLOW_DATA_IS_WORKFLOW_SIGNOUT), isWfSignOut);
        workflowCollectionRef.document(workflowName).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "set signOutField for" + workflowName + " workflow");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "FAILED to set signOutField for" + workflowName + " workflow");
            }
        });

        Map<String, Object> quesMap = new HashMap<>();
        quesMap.put("questions", questionsList);

        workflowCollectionRef.document(workflowName).update(quesMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "uploaded the list of questions in " + workflowName + " workflow");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to upload questions list to the workflow");
            }
        });

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
