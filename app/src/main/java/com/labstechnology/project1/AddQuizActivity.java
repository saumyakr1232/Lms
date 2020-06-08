package com.labstechnology.project1;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import ru.katso.livebutton.LiveButton;
import xyz.hasnat.sweettoast.SweetToast;

import static android.view.View.GONE;

public class AddQuizActivity extends AppCompatActivity {
    private static final String TAG = "AddQuizActivity";
    private static final int FILE_SELECT_CODE = 0;
    private ProgressBar progressBar;
    private MaterialEditText editTextTitle, editTextDescription, editTextDate, editTextTime;
    private LiveButton btnDone;
    private CardView cardDocument;
    private ImageView imgDocument;
    private NestedScrollView parent;
    private androidx.appcompat.widget.Toolbar toolbar;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private TextView txtChooseFile, txtFileName, txtFileSize;
    private StorageReference quizDocumentsRef;
    private DatabaseReference rttDatabaseQuizRef;
    private String id;

    private Uri documentUri;

    private Utils utils;

    private String FileName, FileSize;
    private long sizeOfFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);


        initViews();
        utils = new Utils(this);

        final DatabaseReference myRef = FirebaseDatabaseReference.DATABASE.getReference().child("quizzes");
        id = myRef.push().getKey();

        rttDatabaseQuizRef = FirebaseDatabaseReference.DATABASE.getReference().child("quizzes").child(id);

        quizDocumentsRef = FirebaseStorage.getInstance().getReference().child("quizzesDocument").child(id);

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeKeyboard();
            }
        });

        handleEditTextColors();


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Add Quiz");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white1));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
        editTextDate.addValidator(new RegexpValidator("Not a valid Date", Utils.getRegexDatePattern()));

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
        editTextTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showTimePicker();
                }
            }
        });

        editTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker();
                }
            }
        });

        cardDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressBar.getVisibility() == GONE) {
                    showFileChooser();
                } else {
                    SweetToast.info(AddQuizActivity.this, "wait File is uploading");
                }

            }
        });


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextTitle.getText().toString().equals("") || editTextDescription.getText().toString().equals("")
                        || editTextDate.getText().toString().equals("") || editTextTime.getText().toString().equals("")
                        || documentUri == null) {
                    if (documentUri == null) {
                        SweetToast.error(AddQuizActivity.this, "You have not selected any document");
                    }
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
                } else if (progressBar.getVisibility() == GONE) {
                    progressBar.setVisibility(View.VISIBLE);

                    Date date = new Date();
                    long time = date.getTime();
                    Timestamp timestamp = new Timestamp(time);
                    HashMap<String, Object> quiz = new HashMap<>();

                    quiz.put("id", id);
                    quiz.put("title", editTextTitle.getText().toString());
                    quiz.put("description", editTextDescription.getText().toString());
                    quiz.put("deadLineDate", editTextDate.getText().toString());
                    quiz.put("deadLineTime", editTextTime.getText().toString());
                    quiz.put("timestamp", timestamp);

                    assert id != null;
                    myRef.child(id).updateChildren(quiz).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                SweetToast.success(AddQuizActivity.this, "Quiz Added Successfully");
                                Log.d(TAG, "onComplete: Quiz is pushed");
                                clearFields();
                            } else {
                                SweetToast.error(AddQuizActivity.this, "Try Again");
                                Log.d(TAG, "onComplete: error occurred during pushing Quiz " + task.getException().getLocalizedMessage());
                            }
                        }
                    });
                    uploadDocumentAndLinkToAssignment();

                } else {
                    SweetToast.error(AddQuizActivity.this, "Wait Until file is uploading");
                }
            }
        });

    }

    private void handleEditTextColors() {
        Log.d(TAG, "handleEditTextColors: called");
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
        parent = (NestedScrollView) findViewById(R.id.parent);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtChooseFile = (TextView) findViewById(R.id.txtChooseFile);
        txtFileName = (TextView) findViewById(R.id.txtFileName);
        txtFileSize = (TextView) findViewById(R.id.txtFileSize);

    }

    private void showDatePicker() {
        Log.d(TAG, "showDatePicker: called");
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddQuizActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "-" + (month) + "-" + year; //TODO : hereljadsl foidsfakld fjdsji
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

    private void closeKeyboard() {
        View view = AddQuizActivity.this.getCurrentFocus();
        if (null != view) {
            InputMethodManager imm = (InputMethodManager) AddQuizActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showFileChooser() {
        Log.d(TAG, "showFileChooser: called");

        String[] mimeTypes =
                {"application/pdf", "text/plain",
                        "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        // Only pick openable and local files. Theoretically we could pull files from google drive
        // or other applications that have networked files, but that's unnecessary for this example.
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            SweetToast.warning(this, "Please install a File Manager.", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "onActivityResult: got the uri of document");
                    // Get the Uri of the selected file
                    documentUri = data.getData();
                    Log.d(TAG, "File Uri: " + documentUri.toString());
                    String documentPath = data.getType();
                    String mimeType = getContentResolver().getType(documentUri);
                    Log.d(TAG, "onActivityResult: mimeType" + mimeType);
                    if (mimeType.equals("image/jpeg")) {
                        SweetToast.error(AddQuizActivity.this, "This Format is not allowed");
                        documentUri = null;

                    } else {

                        Cursor returnCursor =
                                getContentResolver().query(documentUri, null, null, null, null);
                        /*
                         * Get the column indexes of the data in the Cursor,
                         * move to the first row in the Cursor, get the data,
                         * and display it.
                         */
                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                        returnCursor.moveToFirst();
                        FileName = returnCursor.getString(nameIndex);
                        FileSize = Long.toString(returnCursor.getLong(sizeIndex));
                        sizeOfFile = returnCursor.getLong(sizeIndex);
                        Log.d(TAG, "onActivityResult: Name " + returnCursor.getString(nameIndex));
                        Log.d(TAG, "onActivityResult: Size" + Long.toString(returnCursor.getLong(sizeIndex)));

                        setDocumentCard();
//
//                    Log.d(TAG, "onActivityResult: file extension"+ documentPath);
//                    // Get the path
//                    String path = documentUri.getPath();
//                    Log.d(TAG, "onActivityResult: file"+ path);

                        // Get the file instance
                        // File file = new File(path);
//                    Log.d(TAG, "onActivityResult: file"+ file.canRead() + "(((( "+ file.getAbsolutePath());
//                    // Upload the file or save offline  in temp


                    }
                }
                break;
            default:
                SweetToast.warning(AddQuizActivity.this, "you did'nt select any file");
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void setDocumentCard() {
        Log.d(TAG, "setDocumentCard: called");
        if (documentUri != null) {
            if (sizeOfFile < 999999) {
                FileSize = String.valueOf(sizeOfFile / 1000) + " KB";
            } else if (sizeOfFile > 999999) {
                FileSize = String.valueOf(sizeOfFile / 1000000) + " MB";
            }
            txtFileSize.setText(FileSize);
            txtFileName.setText(FileName);
            //Load thumbnail of a specific media item.
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                try {
                    Bitmap thumbnail =
                            getApplicationContext().getContentResolver().loadThumbnail(
                                    documentUri, new Size(640, 480), null);

                    try {
                        txtChooseFile.setVisibility(GONE);
                        imgDocument.setImageBitmap(thumbnail);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void uploadDocumentAndLinkToAssignment() {
        Log.d(TAG, "uploadDocumentAndLinkToAssignment: called");
        if (documentUri != null) {
            txtChooseFile.setVisibility(GONE);
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference filePath = quizDocumentsRef.child(id + "upload");
            filePath.putFile(documentUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        SweetToast.success(AddQuizActivity.this, "success");
                        Log.d(TAG, "onComplete: document update successful");
                        filePath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Log.d(TAG, "onComplete: HERE" + task.getResult());
                                Uri url = task.getResult();
                                assert url != null;
                                final String downloadUrl = url.toString();
                                Log.d(TAG, "onComplete: download url Document" + downloadUrl);
                                HashMap<String, Object> downloadMap = new HashMap<>();
                                downloadMap.put("resourceUrl", downloadUrl);
                                rttDatabaseQuizRef.updateChildren(downloadMap).addOnCompleteListener(
                                        new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    txtFileName.setText("File Name");
                                                    txtFileSize.setText("0.0 MB");
                                                    progressBar.setVisibility(GONE);
                                                    txtChooseFile.setVisibility(View.VISIBLE);
                                                    Log.d(TAG, "onComplete: Document is linked to Quiz successful");
                                                    SweetToast.info(AddQuizActivity.this, "Document is linked to your Quiz successfully");

                                                } else {
                                                    progressBar.setVisibility(GONE);
                                                    Log.d(TAG, "onComplete: Document link to Assignment is unsuccessful 😎");
                                                    SweetToast.error(AddQuizActivity.this, Objects.requireNonNull(task.getException()).getMessage());
                                                }
                                            }
                                        }
                                ).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        SweetToast.error(AddQuizActivity.this, "Unable to link Document to assignment, try again");
                                        progressBar.setVisibility(GONE);
                                    }
                                });
                            }
                        });
                    } else {
                        Log.d(TAG, "onComplete: error " + task.getException().getLocalizedMessage());
                        SweetToast.error(AddQuizActivity.this, "Document upload unsuccessful 😞");

                    }
                }
            });
        }
    }


}