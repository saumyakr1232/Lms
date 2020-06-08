package com.labstechnology.project1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.labstechnology.project1.models.Assignment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AssignmentRecViewAdapter extends RecyclerView.Adapter<AssignmentRecViewAdapter.ViewHolder> {
    private static final String TAG = "AssignmentRecViewAdapte";
    private Context context;
    private ArrayList<Assignment> assignments = new ArrayList<>();
    private Utils utils;


    public AssignmentRecViewAdapter(Context context) {
        this.context = context;
        this.utils = new Utils(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_rec_view_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: clled");
        //TODO: validate Date and time in only this format
        holder.txtTitle.setText(assignments.get(position).getTitle());
        try {
            @SuppressLint("SimpleDateFormat") Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(assignments.get(position).getDeadLineDate());

            Date time = new SimpleDateFormat("hh:mm").parse(assignments.get(position).getDeadLineTime());

            Log.d(TAG, "onBindViewHolder: date Here" + date1);
            Log.d(TAG, "onBindViewHolder: time Here" + time);

            holder.txtDate.setText(Utils.formatDateToFormat(date1, "d MMM"));
            holder.txtTime.setText(Utils.formatDateToFormat(time, "HH:mm a"));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (utils.isUserAttemptThisAssignment(assignments.get(position))) {
            holder.textStatus.setText("Attempted");
            holder.textStatus.setTextColor(context.getColor(R.color.green));
        }

    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    public void setAssignments(ArrayList<Assignment> assignments) {
        this.assignments = assignments;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";

        private CardView parent;
        private TextView txtTitle, txtDate;
        private TextView txtTime, TxtDate, textStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = (CardView) itemView.findViewById(R.id.parent);
            txtTitle = (TextView) itemView.findViewById(R.id.textTitle);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtDate = (TextView) itemView.findViewById(R.id.txtdate);
        }
    }
}
