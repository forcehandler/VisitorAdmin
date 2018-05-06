package com.example.sonupc.visitpreferencemanager.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonupc.visitpreferencemanager.R;

import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {

    private static final String TAG = RatingAdapter.class.getSimpleName();

    public Context context;
    public List<String> items;

    private ClickListener clickListener;

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder()");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rating_item_layout, parent, false);
        return new RatingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RatingViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder() position: " + position);

        final String title = items.get(position);
        holder.bind(title);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked: " + holder.getAdapterPosition(), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    public class RatingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textView;
        public Button delButton;

        public View view;
        public RatingViewHolder(View view){
            super(view);
            this.view = view;
            textView = view.findViewById(R.id.textView1);
            delButton = view.findViewById(R.id.del_btn);
            delButton.setOnClickListener(this);
        }

        public void bind(String title){
            Log.d(TAG+".MyViewHolder", "bind()");
            textView.setText(title);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.del_btn:
                    clickListener.onDeleteClicked(this.getAdapterPosition());
                    break;
            }
        }
    }

    public RatingAdapter(Context context, List<String> items, ClickListener listener) {
        Log.d(TAG, "Rating Adapter Constructor");
        this.context = context;
        this.items = items;
        this.clickListener = listener;
    }

    public interface ClickListener {
        void onDeleteClicked(int position);
    }

}
