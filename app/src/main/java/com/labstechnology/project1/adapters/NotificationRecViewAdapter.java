package com.labstechnology.project1.adapters;

import android.app.Notification;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.labstechnology.project1.R;
import com.labstechnology.project1.models.Announcement;

import java.util.ArrayList;

public class NotificationRecViewAdapter extends RecyclerView.Adapter<NotificationRecViewAdapter.ViewHolder> {
    private static final String TAG = "NotificationRecViewAdap";
    private Context context;

    private ArrayList<Announcement> announcements = new ArrayList<>();


    public interface DeleteAnnouncementNotification {
        void onDeletingResult(Announcement announcement);
    }

    private DeleteAnnouncementNotification deleteAnnouncementNotification;

    public NotificationRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_rec_view_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.textNotificationTitle.setText(announcements.get(position).getTitle());
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    deleteAnnouncementNotification = (DeleteAnnouncementNotification) context;
                    deleteAnnouncementNotification.onDeletingResult(announcements.get(position));

                    // this is temporary solution
                    //TODO: write proper logic
                    announcements.remove(announcements.get(position));
                    notifyDataSetChanged();

                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";
        private ImageView btnCancel;
        private TextView textNotificationTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnCancel = (ImageView) itemView.findViewById(R.id.btnCancel);
            textNotificationTitle = (TextView) itemView.findViewById(R.id.textNotificationTitle);

        }
    }

    public void setItems(ArrayList<Announcement> announcements) {
        this.announcements = announcements;
        notifyDataSetChanged();
    }
}
