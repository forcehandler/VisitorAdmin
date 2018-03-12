package com.example.sonupc.visitpreferencemanager.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.sonupc.visitpreferencemanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenSelector extends Fragment implements View.OnClickListener {

    private static final String TAG = InstituteSelector.class.getSimpleName();

    private EditText textInputEt, surveyEt, cameraEt, thankYouEt, workflowEt;
    private Button submitBtn;

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

        submitBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        int textNos, surveyNos, cameraNos;
        String thankYou, email, workflow;
        textNos = Integer.parseInt(textInputEt.getText().toString());
        surveyNos = Integer.parseInt(surveyEt.getText().toString());
        cameraNos = Integer.parseInt(cameraEt.getText().toString());

        workflow = workflowEt.getText().toString();
        thankYou = thankYouEt.getText().toString();

        if (mListener != null) {
            mListener.onScreensSelected(textNos, surveyNos, cameraNos, thankYou, workflow);
        }
    }

    public interface OnScreenSelectorListener{
        void onScreensSelected(int text, int survey, int camera, String thankYou, String workflow_name);
    }
}
