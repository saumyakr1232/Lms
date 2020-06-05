package com.labstechnology.project1.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.labstechnology.project1.R;
import com.labstechnology.project1.Utils;
import com.labstechnology.project1.models.Announcement;

import java.util.ArrayList;
import java.util.HashMap;

public class AnnouncementRecViewAdapter extends RecyclerView.Adapter<AnnouncementRecViewAdapter.AnnouncementViewHolder> {
    private static final String TAG = "AnnouncementRecViewAdap";

    private Context context;
    private ArrayList<Announcement> announcements = new ArrayList<>();

    public AnnouncementRecViewAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcements_rec_view_list_item, parent, false);
        AnnouncementViewHolder holder = new AnnouncementViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.txtTitle.setText(announcements.get(position).getTitle());
        HashMap<String, Object> timestamp = announcements.get(position).getTimestamp();
        /**date=5, hours=21, seconds=21, month=5, nanos=226000000,
         *  timezoneOffset=-330, year=120, minutes=30,
         *  time=1591372821226, day=5**/
        if (timestamp.get("time") != null) {
            long timeMills = Long.parseLong(timestamp.get("time").toString());
            String dateTime = Utils.getDateTimeFromTimeStamp(timeMills, "yyyy.MM.dd 'at' HH:mm:ss ");
            holder.txtTimestamp.setText(dateTime);
        }


    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public void setAnnouncements(ArrayList<Announcement> announcements) {
        this.announcements = announcements;
    }

    public class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "AnnouncementViewHolder";

        private CardView parent;
        private TextView txtTitle;
        private TextView txtTimestamp;

        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = (CardView) itemView.findViewById(R.id.parent);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtTimestamp = (TextView) itemView.findViewById(R.id.txtTimestamp);
        }
    }


}
