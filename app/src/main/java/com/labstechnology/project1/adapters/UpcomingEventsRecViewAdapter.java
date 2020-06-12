package com.labstechnology.project1.adapters;

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

import com.labstechnology.project1.QuizActivity;
import com.labstechnology.project1.R;
import com.labstechnology.project1.models.Assignment;
import com.labstechnology.project1.models.Quiz;
import com.labstechnology.project1.models.UpcomingEvents;

import java.util.ArrayList;

public class UpcomingEventsRecViewAdapter extends RecyclerView.Adapter<UpcomingEventsRecViewAdapter.ViewHolder> {
    private static final String TAG = "UpcomingEventsRecViewAd";

    private Context context;
    private ArrayList<Quiz> quizzes = new ArrayList<>();
    private ArrayList<Assignment> assignments = new ArrayList<>();
    private ArrayList<UpcomingEvents> events = new ArrayList<>();

    public UpcomingEventsRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_event_rec_view_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.txtTitle.setText(events.get(position).getTitle());
        // long mills = events.get(position).getDeadlineMills();
//        new CountDownTimer(mills, 1000) {
//
//            @SuppressLint("SetTextI18n")
//            public void onTick(long millisUntilFinished) {
//                long daysRemaining = millisUntilFinished / 86400000;
//                long hrRemaining = (millisUntilFinished % 86400000) / 3600000;
//                holder.timeLeft.setText(daysRemaining + " Days " + hrRemaining + " hr Remaining");
//
//            }
//
//            @SuppressLint("SetTextI18n")
//            public void onFinish() {
//                holder.timeLeft.setText("Time's up");
//            }
//        }.start();
//
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (events.get(position).getType().equals("quiz")) {
                    Intent intent = new Intent(context, QuizActivity.class);
                    for (Quiz q : quizzes
                    ) {
                        if (q.getId().equals(events.get(position).getEventId())) {
                            intent.putExtra("quiz", q);
                            context.startActivity(intent);
                            break;
                        }
                    }

                } else if (events.get(position).getType().equals("assignment")) {
                    Intent intent = new Intent(context, QuizActivity.class);
                    for (Assignment a : assignments
                    ) {
                        if (a.getId().equals(events.get(position).getEventId())) {
                            intent.putExtra("quiz", a);
                            context.startActivity(intent);
                            break;
                        }
                    }


                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setQuizzes(ArrayList<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public void setAssignments(ArrayList<Assignment> assignments) {
        this.assignments = assignments;
    }

    public void setEvents(ArrayList<UpcomingEvents> events) {
        this.events = events;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";

        private CardView parent;
        private TextView txtTitle, timeLeft;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = (CardView) itemView.findViewById(R.id.parent);
            txtTitle = (TextView) itemView.findViewById(R.id.textTitle);
            timeLeft = (TextView) itemView.findViewById(R.id.timeLeft);
        }
    }

}
