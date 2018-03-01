package com.example.sonupc.visitpreferencemanager.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sonupc.visitpreferencemanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstituteSelector extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = InstituteSelector.class.getSimpleName();

    Spinner spinner;

    private DatabaseReference mDatabase;

    private List<String> spinnerData;
    private ArrayAdapter<String> dataAdapter;

    private OnInstituteSelectedListener mListener;

    private int count = -1;
    public InstituteSelector() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        if(context instanceof OnInstituteSelectedListener) {
            mListener = (OnInstituteSelectedListener) context;
        }
        super.onAttach(context);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {

        super.onAttachFragment(childFragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreate()");
        spinnerData = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Log.d(TAG, "children " + child.getKey());
                    spinnerData.add(child.getKey());
                }
                dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerData);
                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                spinner.setAdapter(dataAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.addListenerForSingleValueEvent(postListener);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_institute_selector, container, false);

        spinner = view.findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(this);
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        // On selecting a spinner item
        count++;
        String item = adapterView.getItemAtPosition(position).toString();

        if(count >= 1) {
            if (mListener != null) {
                mListener.onInstituteSelected(item);
            }
        }
        // Showing selected spinner item
        Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface OnInstituteSelectedListener {
        void onInstituteSelected(String id);
    }
}
