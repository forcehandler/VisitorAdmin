package com.example.sonupc.visitpreferencemanager.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.sonupc.visitpreferencemanager.R;
import com.example.sonupc.visitpreferencemanager.preference.CameraPreference;
import com.example.sonupc.visitpreferencemanager.preference.SurveyPreferenceModel;


public class CameraFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = CameraFragment.class.getSimpleName();

    private EditText pageTitleEt;
    private Button submitBtn;


    private CameraFragmentInteraction mListener;

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        if(context instanceof CameraFragmentInteraction){
            mListener = (CameraFragmentInteraction) context;
        }
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        pageTitleEt = view.findViewById(R.id.title);
        submitBtn = view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if(!TextUtils.isEmpty(pageTitleEt.getText().toString())){
            if(mListener != null){
                CameraPreference cameraPreference = new CameraPreference();
                cameraPreference.setCamera_hint_text(pageTitleEt.getText().toString());
                mListener.onCameraFragmentInteraction(cameraPreference);
            }
        }
    }

    public interface CameraFragmentInteraction{
        void onCameraFragmentInteraction(CameraPreference cameraPreference);
    }
}
