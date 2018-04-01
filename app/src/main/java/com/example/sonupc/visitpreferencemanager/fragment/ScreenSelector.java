package com.example.sonupc.visitpreferencemanager.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.sonupc.visitpreferencemanager.ImagePicker;
import com.example.sonupc.visitpreferencemanager.R;

public class ScreenSelector extends Fragment implements View.OnClickListener {

    private static final String TAG = InstituteSelector.class.getSimpleName();

    private EditText textInputEt, surveyEt, cameraEt, thankYouEt, workflowEt;
    private Button submitBtn, uploadLogoBtn;
    private RadioButton signOutRadioBtn;

    private OnScreenSelectorListener mListener;

    public ScreenSelector() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        if(context instanceof OnScreenSelectorListener){
            mListener = (OnScreenSelectorListener) context;
        }
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_screen_selector, container, false);
        textInputEt = view.findViewById(R.id.textinput);
        surveyEt = view.findViewById(R.id.survey);
        cameraEt = view.findViewById(R.id.camera);
        thankYouEt = view.findViewById(R.id.thankyou);
        workflowEt = view.findViewById(R.id.workflow);
        submitBtn = view.findViewById(R.id.submitBtn);
        uploadLogoBtn = view.findViewById(R.id.btn_logo_upload);
        signOutRadioBtn = view.findViewById(R.id.rbtn_signout);

        submitBtn.setOnClickListener(this);
        uploadLogoBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_logo_upload:
                Intent intent = new Intent(getActivity(), ImagePicker.class);
                intent.putExtra(getString(R.string.intent_key_instituteId), mListener.getInstituteId());
                startActivity(intent);
                break;
            case R.id.submitBtn:
                int textNos, surveyNos, cameraNos;
                String thankYou, email, workflow;
                boolean isWfSignOut = signOutRadioBtn.isChecked();
                textNos = Integer.parseInt(textInputEt.getText().toString());
                surveyNos = Integer.parseInt(surveyEt.getText().toString());
                cameraNos = Integer.parseInt(cameraEt.getText().toString());

                workflow = workflowEt.getText().toString();
                thankYou = thankYouEt.getText().toString();

                if (mListener != null) {
                    mListener.onScreensSelected(textNos, surveyNos, cameraNos, thankYou, workflow);
                }
                break;
        }

    }

    public interface OnScreenSelectorListener{
        void onScreensSelected(int text, int survey, int camera, String thankYou, String workflow_name);
        String getInstituteId();
    }
}
