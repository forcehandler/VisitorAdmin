package com.example.sonupc.visitpreferencemanager.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.sonupc.visitpreferencemanager.R;
import com.example.sonupc.visitpreferencemanager.preference.SurveyPreferenceModel;

import java.util.ArrayList;


public class SurveyFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = SurveyPreferenceModel.class.getSimpleName();

    private EditText pageTitleEt,et1,et2,et3,et4, opt1,opt2,opt3,opt4;
    private Button submitBtn, add1, add2, add3, add4;

    private int cnt1,cnt2,cnt3,cnt4;
    ArrayList<String> options1;
    ArrayList<String> options2;
    ArrayList<String> options3;
    ArrayList<String> options4;

    private OnSurveyPreferenceListener mListener;

    public SurveyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        if(context instanceof  OnSurveyPreferenceListener){
            mListener = (OnSurveyPreferenceListener) context;
        }
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        options1 = new ArrayList<>();
        options2 = new ArrayList<>();
        options3 = new ArrayList<>();
        options4 = new ArrayList<>();
        cnt1 = 1;
        cnt2 = 1;
        cnt3 = 1;
        cnt4 = 1;

        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_survey, container, false);
        pageTitleEt = view.findViewById(R.id.title);
        et1 = view.findViewById(R.id.et1);
        et2 = view.findViewById(R.id.et2);
        et3 = view.findViewById(R.id.et3);
        et4 = view.findViewById(R.id.et4);

        opt1 = view.findViewById(R.id.opt1);
        opt2 = view.findViewById(R.id.opt2);
        opt3 = view.findViewById(R.id.opt3);
        opt4 = view.findViewById(R.id.opt4);

        opt1.setHint("Enter option " + (cnt1));
        opt2.setHint("Enter option " + (cnt2));
        opt3.setHint("Enter option " + (cnt3));
        opt4.setHint("Enter option " + (cnt4));

        submitBtn = view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);

        add1 = view.findViewById(R.id.opt1Btn);
        add2 = view.findViewById(R.id.opt2Btn);
        add3 = view.findViewById(R.id.opt3Btn);
        add4 = view.findViewById(R.id.opt4Btn);

        add1.setOnClickListener(this);
        add2.setOnClickListener(this);
        add3.setOnClickListener(this);
        add4.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.opt1Btn:
                options1.add(opt1.getText().toString());
                opt1.setText("");
                opt1.setHint("Enter option " + (++cnt1));
                break;
            case R.id.opt2Btn:
                options2.add(opt2.getText().toString());
                opt2.setText("");
                opt2.setHint("Enter option " + (++cnt2));
                break;
            case R.id.opt3Btn:
                options3.add(opt3.getText().toString());
                opt3.setText("");
                opt3.setHint("Enter option " + (++cnt3));
                break;
            case R.id.opt4Btn:
                options4.add(opt4.getText().toString());
                opt4.setText("");
                opt4.setHint("Enter option " + (++cnt4));
                break;

            case R.id.submitBtn:
                SurveyPreferenceModel surveyPreferenceModel = new SurveyPreferenceModel();

                ArrayList<String> survey_item_names = new ArrayList<>();

                if(!TextUtils.isEmpty(et1.getText().toString())){
                    survey_item_names.add(et1.getText().toString());
                }
                if(!TextUtils.isEmpty(et2.getText().toString())){
                    survey_item_names.add(et2.getText().toString());
                }
                if(!TextUtils.isEmpty(et3.getText().toString())){
                    survey_item_names.add(et3.getText().toString());
                }
                if(!TextUtils.isEmpty(et4.getText().toString())){
                    survey_item_names.add(et4.getText().toString());
                }

                ArrayList<ArrayList<String>> survey_item_options = new ArrayList<>();
                if(options1.size() != 0){
                    survey_item_options.add(options1);
                }
                if(options2.size() != 0){
                    survey_item_options.add(options2);
                }
                if(options3.size() != 0){
                    survey_item_options.add(options3);
                }
                if(options4.size() != 0){
                    survey_item_options.add(options4);
                }

                surveyPreferenceModel.setSurvey_title(pageTitleEt.getText().toString());
                surveyPreferenceModel.setSurvey_item_name(survey_item_names);
                surveyPreferenceModel.setSurvey_item_options(survey_item_options);

                if(mListener != null){
                    mListener.onSurveyPreferenceSubmit(surveyPreferenceModel);
                }

                break;
        }
    }

    public interface OnSurveyPreferenceListener{
        void onSurveyPreferenceSubmit(SurveyPreferenceModel surveyPreferenceModel);
    }
}
