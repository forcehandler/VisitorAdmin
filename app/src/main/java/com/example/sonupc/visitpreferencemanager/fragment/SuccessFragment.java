package com.example.sonupc.visitpreferencemanager.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sonupc.visitpreferencemanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuccessFragment extends Fragment {


    public SuccessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_success, container, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().finish();
            }
        }, 3000);

        return view;
    }

}
