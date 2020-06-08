package com.labstechnology.project1.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.labstechnology.project1.AssignmentActivity;
import com.labstechnology.project1.R;
import com.labstechnology.project1.Utils;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: clled");
        //TODO: validate Date and time in only this format
        holder.txtTitle.setText(assignments.get(position).getTitle());
        try {
            @SuppressLint("SimpleDateFormat") Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(assignments.get(position).getDeadLineDate());

            Date time = new SimpleDateFormat("hh:mm").parse(assignments.get(position).getDeadLineTime());

            Log.d(TAG, "onBindViewHolder: date Here" + date1);
            Log.d(TAG, "onBindViewHolder: time Here" + time);

            holder.txtDate.setText(Utils.formatDateToFormat(date1, "d MMM"));
            holder.txtDate.setTextColor(context.getColor(R.color.colorBlack));
            holder.txtTime.setText(Utils.formatDateToFormat(time, "HH:mm a"));
            holder.txtTime.setTextColor(context.getColor(R.color.colorBlack));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onBindViewHolder: assignments " + assignments.get(position));
        if (utils.isUserAttemptThisAssignment(assignments.get(position))) {
            holder.textStatus.setText("Attempted");
            holder.textStatus.setTextColor(context.getColor(R.color.green));
        }
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AssignmentActivity.class);
                intent.putExtra("assignment", assignments.get(position));

                if (utils.isUserAttemptThisAssignment(assignments.get(position))) {
                    intent.putExtra("isSubmitted", true);
                    context.startActivity(intent);
                } else {
                    intent.putExtra("isSubmitted", false);
                    context.startActivity(intent);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    public void setAssignments(ArrayList<Assignment> assignments) {
        this.assignments = assignments;
        Log.d(TAG, "setAssignments: assignments" + assignments.toString());
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";

        private CardView parent;
        private TextView txtTitle, txtDate;
        private TextView txtTime, textStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = (CardView) itemView.findViewById(R.id.parent);
            txtTitle = (TextView) itemView.findViewById(R.id.textTitle);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtDate = (TextView) itemView.findViewById(R.id.txtdate);
            textStatus = (TextView) itemView.findViewById(R.id.txtStatus);
        }
    }
}
