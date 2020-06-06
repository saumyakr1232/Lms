package com.labstechnology.project1;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.labstechnology.project1.models.AssignmentResponse;
import com.labstechnology.project1.models.User;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import ru.katso.livebutton.LiveButton;

public class AddAssignmentActivity extends AppCompatActivity {
    private static final String TAG = "AddAssignmentActivity";

    private ProgressBar progressBar;
    private MaterialEditText editTextTitle, editTextDescription, editTextDate, editTextTime;
    private LiveButton btnDone;
    private CardView cardDocument;
    private ImageView imgDocument;
    private RelativeLayout parent;
    private androidx.appcompat.widget.Toolbar toolbar;

    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);

        initViews();

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: hide soft input
            }
        });

        editTextTitle.setPrimaryColor(this.getColor(R.color.colorAccent));
        editTextDescription.setPrimaryColor(this.getColor(R.color.colorAccent));
        editTextTime.setPrimaryColor(this.getColor(R.color.colorAccent));
        editTextDate.setPrimaryColor(this.getColor(R.color.colorAccent));
        if (Utils.getDarkThemePreference(this)) {
            editTextTitle.setTextColor(this.getColor(R.color.white1));
            editTextDescription.setTextColor(this.getColor(R.color.white1));
            editTextDate.setTextColor(this.getColor(R.color.white1));
            editTextTime.setTextColor(this.getColor(R.color.white1));
        }


        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        toolbar.setTitle("Add Assignments");
        toolbar.setTitleTextColor(getColor(R.color.white1));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);

        editTextDate.addValidator(new RegexpValidator("Not a valid Date", Utils.getRegexDatePattern()));
        //TODO: validate time


        editTextDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextTitle.getText().toString().equals("") || editTextDescription.getText().toString().equals("")
                        || editTextDate.getText().toString().equals("") || editTextTime.getText().toString().equals("")) {
                    editTextTitle.setMinCharacters(2);
                    editTextTitle.setMaxCharacters(30);
                    editTextDescription.setMinCharacters(5);
                    editTextTitle.validateWith(new METValidator("Field can'nt be empty. 😤") {
                        @Override
                        public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                            return !isEmpty;
                        }
                    });
                    editTextDescription.validateWith(new METValidator("Provide a description, maybe a 'Url' 🙁") {
                        @Override
                        public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                            return !isEmpty;
                        }
                    });
                    editTextDate.validateWith(new METValidator("Date??? ") {
                        @Override
                        public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                            return !isEmpty;
                        }
                    });

                    editTextTime.validateWith(new METValidator("Need a Time") {
                        @Override
                        public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                            return !isEmpty;
                        }
                    });
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    final DatabaseReference myRef = FirebaseDatabaseReference.DATABASE.getReference().child("assignments");
                    Date date = new Date();
                    long time = date.getTime();
                    Timestamp timestamp = new Timestamp(time);
                    HashMap<String, Object> assignment = new HashMap<>();
                    String id = myRef.push().getKey();
                    assignment.put("id", id);
                    assignment.put("title", editTextTitle.getText().toString());
                    assignment.put("description", editTextDescription.getText().toString());
                    assignment.put("deadLineDate", editTextDate.getText().toString());
                    assignment.put("deadLineTime", editTextTime.getText().toString());
                    assignment.put("timestamp", timestamp);
                    assignment.put("attemptedBy", new ArrayList<User>());
                    assignment.put("responses", new ArrayList<AssignmentResponse>());

                    assert id != null;
                    myRef.child(id).setValue(assignment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(AddAssignmentActivity.this, "assignment Added Successfully", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onComplete: assignment is pushed");
                                clearFields();
                            } else {
                                Toast.makeText(AddAssignmentActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onComplete: error occurred during pushing assignment " + task.getException().getLocalizedMessage());
                            }
                        }
                    });

                }
            }
        });

    }

    private void clearFields() {
        Log.d(TAG, "clearFields: called");
        editTextTime.setText("");
        editTextDate.setText("");
        editTextTitle.setText("");
        editTextDescription.setText("");
    }


    private void initViews() {
        Log.d(TAG, "initViews: called");
        editTextDate = (MaterialEditText) findViewById(R.id.editTextDeadLineDate);
        editTextTime = (MaterialEditText) findViewById(R.id.editTextDeadLineTime);
        editTextTitle = (MaterialEditText) findViewById(R.id.editTextTitle);
        editTextDescription = (MaterialEditText) findViewById(R.id.editTextDescription);
        btnDone = (LiveButton) findViewById(R.id.btnDone3);
        cardDocument = (CardView) findViewById(R.id.cardUpload);
        imgDocument = (ImageView) findViewById(R.id.imgDocument);
        progressBar = (ProgressBar) findViewById(R.id.progressBarFile);
        parent = (RelativeLayout) findViewById(R.id.parent);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void showDatePicker() {
        Log.d(TAG, "showDatePicker: called");
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddAssignmentActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "-" + month + "-" + year;
                editTextDate.setText(date);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Log.d(TAG, "showTimePicker: called");
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        editTextTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}