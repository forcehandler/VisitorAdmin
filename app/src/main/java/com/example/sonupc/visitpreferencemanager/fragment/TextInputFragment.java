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
import com.example.sonupc.visitpreferencemanager.preference.TextInputPreferenceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TextInputFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = TextInputFragment.class.getSimpleName();

    private EditText pageTitleEt,et1,et2,et3,et4;
    private Button submitBtn;

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
        return view;
    }

    @Override
    public void onClick(View view) {
        TextInputPreferenceModel textInputPreferenceModel = new TextInputPreferenceModel();
        List<String> hints = new ArrayList<>();

        if(!TextUtils.isEmpty(pageTitleEt.getText().toString())){
            textInputPreferenceModel.setPage_title(pageTitleEt.getText().toString());
        }
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
        textInputPreferenceModel.setHints(hints);

        if(mListener != null){
            mListener.onTextPreferenceListener(textInputPreferenceModel);
        }
    }

    public interface OnTextPreferenceListener{
        void onTextPreferenceListener(TextInputPreferenceModel textInputPreferenceModel);
    }
}
