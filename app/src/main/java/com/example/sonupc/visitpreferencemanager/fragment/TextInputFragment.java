package com.example.sonupc.visitpreferencemanager.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sonupc.visitpreferencemanager.R;
import com.example.sonupc.visitpreferencemanager.preference.TextInputPreferenceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TextInputFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = TextInputFragment.class.getSimpleName();

    private EditText pageTitleEt,et1,et2,et3,et4;
    private Button submitBtn;
    private Spinner signoutSpinner, smsSpinner;
    private CheckBox signoutRadioBtn, smsRadioBtn;

    private String signOutField, smsField;

    private OnTextPreferenceListener mListener;
    public TextInputFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        if(context instanceof OnTextPreferenceListener){
            mListener = (OnTextPreferenceListener) context;
        }
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text_input, container, false);
        pageTitleEt = view.findViewById(R.id.title);
        et1 = view.findViewById(R.id.et1);
        et2 = view.findViewById(R.id.et2);
        et3 = view.findViewById(R.id.et3);
        et4 = view.findViewById(R.id.et4);

        submitBtn = view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);

        signoutRadioBtn = view.findViewById(R.id.rbtn_signout);
        smsRadioBtn = view.findViewById(R.id.rbtn_sms);
        signoutRadioBtn.setOnClickListener(this);
        smsRadioBtn.setOnClickListener(this);

        signoutSpinner = view.findViewById(R.id.spinner_signout);
        smsSpinner = view.findViewById(R.id.spinner_sms);

        signoutSpinner.setOnItemSelectedListener(this);
        smsSpinner.setOnItemSelectedListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.submitBtn:{
                TextInputPreferenceModel textInputPreferenceModel = new TextInputPreferenceModel();
                List<String> hints;

                if(!TextUtils.isEmpty(pageTitleEt.getText().toString())){
                    textInputPreferenceModel.setPage_title(pageTitleEt.getText().toString());
                }

                hints = getFieldTexts();
                textInputPreferenceModel.setHints(hints);

                if(mListener != null) {
                    if (signoutRadioBtn.isChecked() || smsRadioBtn.isChecked()) {
                        if (!signoutRadioBtn.isChecked()) {
                            if(!TextUtils.isEmpty(smsField)) {
                                mListener.onTextPreferenceListener(textInputPreferenceModel, false, "",
                                        true, smsField);
                            }
                            else{
                                Toast.makeText(getActivity(), "Please select smsField", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(!smsRadioBtn.isChecked()){
                            if(!TextUtils.isEmpty(signOutField)) {
                                mListener.onTextPreferenceListener(textInputPreferenceModel, true, signOutField,
                                        false, "");
                            }
                            else{
                                Toast.makeText(getActivity(), "Please select signOutField", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            if(!TextUtils.isEmpty(signOutField) && !TextUtils.isEmpty(smsField)) {
                                mListener.onTextPreferenceListener(textInputPreferenceModel, true, signOutField,
                                        true, smsField);
                            }
                            else{
                                Toast.makeText(getActivity(), "Please select signOut and smsField", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else{
                        mListener.onTextPreferenceListener(textInputPreferenceModel, false, "",
                                false, "");
                    }
                }
                else{
                    Log.e(TAG, "mListener is null");
                }
                /*if(mListener != null){
                    mListener.onTextPreferenceListener(textInputPreferenceModel);
                }*/
            }
            break;

            case R.id.rbtn_signout:
                if(signoutRadioBtn.isChecked()){
                    signoutSpinner.setVisibility(View.VISIBLE);
                    List<String> signoutSpinnerData = new ArrayList<>();
                    signoutSpinnerData.add("Select SignOut Field");
                    signoutSpinnerData.addAll(getFieldTexts());
                    ArrayAdapter<String> dataAdapter;
                    dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, signoutSpinnerData);
                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // attaching data adapter to spinner
                    signoutSpinner.setAdapter(dataAdapter);
                }
                break;
            case R.id.rbtn_sms:
                if(smsRadioBtn.isChecked()){
                    smsSpinner.setVisibility(View.VISIBLE);
                    List<String> smsSpinnerData = new ArrayList<>();
                    smsSpinnerData.add("Select Sms Field");
                    smsSpinnerData.addAll(getFieldTexts());
                    ArrayAdapter<String> dataAdapter;
                    dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, smsSpinnerData);
                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // attaching data adapter to spinner
                    smsSpinner.setAdapter(dataAdapter);
                }
                break;
        }

    }

    private List<String> getFieldTexts(){

        List<String> hints = new ArrayList<>();

        if(!TextUtils.isEmpty(et1.getText().toString())){
            hints.add(et1.getText().toString());
        }
        if(!TextUtils.isEmpty(et2.getText().toString())){
            hints.add(et2.getText().toString());
        }
        if(!TextUtils.isEmpty(et3.getText().toString())){
            hints.add(et3.getText().toString());
        }
        if(!TextUtils.isEmpty(et4.getText().toString())){
            hints.add(et4.getText().toString());
        }

        return hints;
    }

    private void initSpinner(){

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if(adapterView == signoutSpinner){
            Log.d(TAG, position + " selected of signOut spinner");
            if(position == 0){
                signOutField = "";
            }
            else {
                signOutField = adapterView.getItemAtPosition(position).toString();
            }
        }
        if(adapterView == smsSpinner){
            Log.d(TAG, position + " selected of sms spinner");
            if(position == 0){
                smsField = "";
            }
            else {
                smsField = adapterView.getItemAtPosition(position).toString();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.d(TAG, "Nothing selected");
    }

    public interface OnTextPreferenceListener{
        void onTextPreferenceListener(TextInputPreferenceModel textInputPreferenceModel);
        void onTextPreferenceListener(TextInputPreferenceModel textInputPreferenceModel, boolean workflowForSignOut,
                                      String signOutField, boolean workflowForSms, String smsField);
    }
}
