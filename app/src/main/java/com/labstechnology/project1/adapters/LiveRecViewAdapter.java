package com.labstechnology.project1.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.labstechnology.project1.R;
import com.labstechnology.project1.Utils;
import com.labstechnology.project1.VideoActivity;
import com.labstechnology.project1.models.LiveEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class LiveRecViewAdapter extends RecyclerView.Adapter<LiveRecViewAdapter.ViewHolder> {
    private static final String TAG = "LiveRecViewAdapter";

    private Context context;
    private ArrayList<LiveEvent> LiveEvents = new ArrayList<>();

    public LiveRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_rec_view_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called ");
        holder.txtTitle.setText(LiveEvents.get(position).getTitle());
        HashMap<String, Object> timestamp = LiveEvents.get(position).getTimestamp();
        if (timestamp.get("time") != null) {
            long timeMills = Long.parseLong(timestamp.get("time").toString());
            long millsNow = System.currentTimeMillis();
            if (millsNow > timeMills) {
                holder.imageLive.setVisibility(View.VISIBLE);
            }
            String date = Utils.getDateTimeFromTimeStamp(timeMills, "d MMM ");
            String Time = Utils.getDateTimeFromTimeStamp(timeMills, "HH:mm a");
            holder.txtTimestamp.setText(Time);
            holder.txtDate.setText(date);
        }

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("incomingLiveEvent", LiveEvents.get(position));
                context.startActivity(intent);
            }
        });

    }

    public void setLiveEvents(ArrayList<LiveEvent> liveEvents) {
        LiveEvents = liveEvents;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return LiveEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "AnnouncementViewHolder";

        private CardView parent;
        private TextView txtTitle, txtDate;
        private TextView txtTimestamp;
        private ImageView imageLive;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = (CardView) itemView.findViewById(R.id.parent);
            txtTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            txtTimestamp = (TextView) itemView.findViewById(R.id.txtTime);
            txtDate = (TextView) itemView.findViewById(R.id.txtdate);
            imageLive = (ImageView) itemView.findViewById(R.id.imageLive);

        }
    }
}
