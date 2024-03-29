package com.labstechnology.project1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class DialogAddLiveEvent extends DialogFragment {
    private static final String TAG = "DialogAddLiveEvent";

    private LiveButton btnDone;
    private MaterialEditText titleEditText, descriptionEditText, urlEditText;
    private ProgressBar progressBar;
    private ImageView btnBackArrow, imgAnnounce;
    private LinearLayout parent;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_live_event, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);

        initViews(view);

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();

            }
        });

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
                if (titleEditText.getText().toString().equals("") || descriptionEditText.getText().toString().equals("")
                        || urlEditText.getText().toString().equals("")) {
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
                    urlEditText.validateWith(new METValidator("Field con'nt be empty") {
                        @Override
                        public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                            return !isEmpty;
                        }
                    });
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    final DatabaseReference myRef = FirebaseDatabaseReference.DATABASE.getReference().child("liveEvents");
                    Date date = new Date();
                    long time = date.getTime();
                    Timestamp timestamp = new Timestamp(time);
                    HashMap<String, Object> liveEvent = new HashMap<>();
                    liveEvent.put("title", titleEditText.getText().toString());
                    liveEvent.put("description", descriptionEditText.getText().toString());
                    liveEvent.put("timestamp", timestamp);
                    liveEvent.put("url", urlEditText.getText().toString());
                    String id = myRef.push().getKey();
                    liveEvent.put("id", id);

                    assert id != null;
                    myRef.child(id).setValue(liveEvent).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dismiss();
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Announcement Added Successfully", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onComplete: liveEvent is pushed");
                            } else {
                                Toast.makeText(getContext(), "Try Again", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onComplete: error occurred during pushing liveEvent " + task.getException().getLocalizedMessage());
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
        parent = (LinearLayout) view.findViewById(R.id.parentAddAnnouncementDialog);
        imgAnnounce = (ImageView) view.findViewById(R.id.imgAnnounce);
        urlEditText = (MaterialEditText) view.findViewById(R.id.editTextUrl);


    }

    private void closeKeyboard() {
        View view = getDialog().getCurrentFocus();
        Log.d(TAG, "closeKeyboard: view" + view);
        if (null != view) {

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }
}
