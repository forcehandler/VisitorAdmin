package com.example.sonupc.visitpreferencemanager.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.sonupc.visitpreferencemanager.R;
import com.example.sonupc.visitpreferencemanager.preference.SuggestionPreference;


public class SuggestionFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = SuggestionFragment.class.getSimpleName();

    private SuggestionFragmentListener mListener;

    private EditText mSuggestionEt;
    private Button mSubmitBtn;

    public SuggestionFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggestion, container, false);
        mSuggestionEt = view.findViewById(R.id.et_suggestion);
        mSubmitBtn = view.findViewById(R.id.btn_submit);
        mSubmitBtn.setEnabled(false);
        Log.d(TAG, "Submit button disabled");
        mSuggestionEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Log.d(TAG , "beforeTextChanged: " + charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Log.d(TAG, "onTextchanged: " + charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Log.d(TAG, "afterTextChanged: " + editable.toString());
                if(!TextUtils.isEmpty(editable.toString())) {
                    mSubmitBtn.setEnabled(true);
                    Log.d(TAG, "Submit button enabled");
                }
                else{
                    mSubmitBtn.setEnabled(false);
                    Log.d(TAG, "Submit button disabled");
                }
            }
        });

        mSubmitBtn.setOnClickListener(this);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SuggestionFragmentListener) {
            mListener = (SuggestionFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SuggestionFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_submit:
                SuggestionPreference suggestionPreference = new SuggestionPreference();
                suggestionPreference.setSuggestion_text(mSuggestionEt.getText().toString());
                mListener.onSuggestionQuestionSubmit(suggestionPreference);
        }
    }

    public interface SuggestionFragmentListener {
        void onSuggestionQuestionSubmit(SuggestionPreference suggestionPreference);
    }
}
