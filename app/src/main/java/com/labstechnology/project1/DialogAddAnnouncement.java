package com.labstechnology.project1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import ru.katso.livebutton.LiveButton;

public class DialogAddAnnouncement extends DialogFragment {
    private static final String TAG = "DialogAddAnnouncement";

    private LiveButton btnDone;
    private MaterialEditText titleEditText, descriptionEditText;
    private ProgressBar progressBar;
    private ImageView btnBackArrow;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_announcement, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);

        initViews(view);


        titleEditText.setPrimaryColor(getContext().getColor(R.color.colorAccent));
        descriptionEditText.setPrimaryColor(getContext().getColor(R.color.colorAccent));
        if (Utils.getDarkThemePreference(getContext())) {
            titleEditText.setTextColor(getContext().getColor(R.color.white1));
            descriptionEditText.setTextColor(getContext().getColor(R.color.white1));


        }
        btnBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleEditText.getText().toString().equals("") || descriptionEditText.getText().toString().equals("")) {
                    titleEditText.setMinCharacters(2);
                    titleEditText.setMaxCharacters(30);
                    descriptionEditText.setMinCharacters(5);
                    titleEditText.validateWith(new METValidator("Field can'nt be empty") {
                        @Override
                        public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                            return !isEmpty;
                        }
                    });
                    descriptionEditText.validateWith(new METValidator("Field con'nt be empty") {
                        @Override
                        public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                            return !isEmpty;
                        }
                    });
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    final DatabaseReference myRef = FirebaseDatabaseReference.DATABASE.getReference().child("announcements");
                    Date date = new Date();
                    long time = date.getTime();
                    Timestamp timestamp = new Timestamp(time);
                    HashMap<String, Object> announcement = new HashMap<>();
                    announcement.put("title", titleEditText.getText().toString());
                    announcement.put("description", descriptionEditText.getText().toString());
                    announcement.put("timestamp", timestamp);
                    String id = myRef.push().getKey();
                    announcement.put("id", id);

                    assert id != null;
                    myRef.child(id).setValue(announcement).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dismiss();
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Announcement Added Successfully", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onComplete: announcement is pushed");
                            } else {
                                Toast.makeText(getContext(), "Try Again", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onComplete: error occurred during pushing announcement " + task.getException().getLocalizedMessage());
                            }
                        }
                    });

                }
            }
        });

        return builder.create();
    }

    private void initViews(View view) {
        Log.d(TAG, "initViews: called");
        btnDone = (LiveButton) view.findViewById(R.id.btnDone2);
        titleEditText = (MaterialEditText) view.findViewById(R.id.editTextTitle);
        descriptionEditText = (MaterialEditText) view.findViewById(R.id.editTextDescription);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        btnBackArrow = (ImageView) view.findViewById(R.id.btnBackArrow);


    }
}
