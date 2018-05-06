package com.example.sonupc.visitpreferencemanager.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sonupc.visitpreferencemanager.MainActivity;
import com.example.sonupc.visitpreferencemanager.R;
import com.example.sonupc.visitpreferencemanager.adapter.RatingAdapter;
import com.example.sonupc.visitpreferencemanager.preference.RatingPreferenceModel;

import java.util.ArrayList;
import java.util.List;

public class RatingFragment extends Fragment implements View.OnClickListener, RatingAdapter.ClickListener{

    private static final String TAG = RatingFragment.class.getSimpleName();

    private RatingFragmentListener mListener;

    private RecyclerView mRecyclerView;
    private FloatingActionButton mAddQuesFAB;
    private Button mSubmitBtn;

    private List<String> questionsList;
    private RatingAdapter ratingAdapter;


    public RatingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        questionsList = new ArrayList<>();
        questionsList.add("Item1");
        questionsList.add("Item2");
        questionsList.add("Item3");
        questionsList.add("Item4");
        ratingAdapter = new RatingAdapter(getContext(), questionsList, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rating, container, false);
        mRecyclerView = view.findViewById(R.id.recylerView);
        mAddQuesFAB = view.findViewById(R.id.addQuesFAB);
        mSubmitBtn = view.findViewById(R.id.submitBtn);
        mSubmitBtn.setOnClickListener(this);
        mAddQuesFAB.setOnClickListener(this);
        mRecyclerView.setAdapter(ratingAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RatingFragmentListener) {
            mListener = (RatingFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, view.getId() + " clicked!!");
        int id = view.getId();
        switch (id){
            case R.id.addQuesFAB:
                Toast.makeText(getContext(), "FAB Clicked", Toast.LENGTH_SHORT)
                    .show();
                showAlertDialog();
                break;
            case R.id.submitBtn:
                RatingPreferenceModel ratingPreferenceModel = new RatingPreferenceModel();
                ratingPreferenceModel.setQuestions(ratingAdapter.items);
                mListener.onRatingQuestionsSubmit(ratingPreferenceModel);
                break;
        }
    }

    private void showAlertDialog(){
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final EditText input = new EditText(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setTitle("Add Question");

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                String question = input.getText().toString();
                Log.d(TAG, "question to add: " + question);
                addQuestionToAdapter(question);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });
        // Set other dialog properties


        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.setView(input); // uncomment this line
        dialog.show();

    }

    /*Adds the question added through fab to the ratings Adapter list*/
    private void addQuestionToAdapter(String question){
        ratingAdapter.items.add(question);
        ratingAdapter.notifyItemInserted(ratingAdapter.items.size() - 1);
    }
    @Override
    public void onDeleteClicked(int position) {
        Log.d(TAG, "Delete button clicked: " + position);
        ratingAdapter.items.remove(position);
        ratingAdapter.notifyItemRemoved(position);
    }

    public interface RatingFragmentListener {
        void onRatingQuestionsSubmit(RatingPreferenceModel ratingPreferenceModel);
    }
}
