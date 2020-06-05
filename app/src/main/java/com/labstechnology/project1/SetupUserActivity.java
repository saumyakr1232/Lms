package com.labstechnology.project1;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;
import com.rengwuxian.materialedittext.validation.RegexpValidator;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import ru.katso.livebutton.LiveButton;

public class SetupUserActivity extends AppCompatActivity {
    private static final String TAG = "SetupUserActivity";


    private FirebaseAuth mAuth;
    private DatabaseReference userDatabase;
    private final static int CHOOSE_PHOTO = 1;
    private String currentUserID;

    private ProgressDialog progressDialog;

    private CardView CardProfileImage;
    private ImageView imgProfile;
    private MaterialEditText FNameEditText, LNameEditText, EmailEditText,
            PhoneEditText, DOBEditText;
    private RadioGroup radioGroupGender;
    private RadioButton rbGender;
    private LiveButton btnDone;
    private int mYear, mMonth, mDay;
    private ProgressBar progressBar;
    private StorageReference userProfileImageRef;
    private String downloadImageUrl;
    private String testString;

    //    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: SetupUserActivity");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_user);

        initViews();

        mAuth = FirebaseAuth.getInstance();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        userDatabase = FirebaseDatabaseReference.DATABASE.getReference().child(FirebaseConstants.USERS).child(currentUserID);

        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("users").child(currentUserID);

        validateFields();

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(SetupUserActivity.this);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                saveUserData();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Log.d(TAG, "onActivityResult: bitmap" + resultUri.toString());
                imgProfile.setImageURI(resultUri);

                //OutputStream stream = new ByteArrayOutputStream();


                final StorageReference filePath = userProfileImageRef.child(currentUserID + ".jpg");
                filePath.putFile(resultUri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SetupUserActivity.this, "Profile image is uploaded successfully", Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "onComplete: image upload successful");
                                    filePath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            Log.d(TAG, "onComplete: HERE" + task.getResult());
                                            Uri url = task.getResult();
                                            assert url != null;
                                            final String downloadUrl = url.toString();
                                            Log.d(TAG, "onComplete: download url" + downloadUrl);
                                            userDatabase.child("profileImage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "onComplete: image linked to profile successful");
                                                        Toast.makeText(SetupUserActivity.this, "image linked to your profile successfully", Toast.LENGTH_LONG).show();

                                                    } else {
                                                        Log.d(TAG, "onComplete: image link to profile is unsuccessful");
                                                        Toast.makeText(SetupUserActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SetupUserActivity.this, "Failed to get the image download url", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    });

                                } else {
                                    Log.d(TAG, "onComplete: image upload unsuccessful");
                                }
                            }
                        });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void saveUserData() {
        Log.d(TAG, "saveUserData: called");
        int id = radioGroupGender.getCheckedRadioButtonId();
        rbGender = (RadioButton) findViewById(id);
        if (EmailEditText.getText().toString().isEmpty()
                || FNameEditText.getText().toString().isEmpty()
                || PhoneEditText.getText().toString().isEmpty()
                || DOBEditText.getText().toString().isEmpty()) {
            EmailEditText.setMinCharacters(5);
            FNameEditText.setMinCharacters(1);
            PhoneEditText.setMinCharacters(10);
            PhoneEditText.setMaxCharacters(10);
            FNameEditText.validateWith(new METValidator("Field can'nt be empty") {
                @Override
                public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                    return !isEmpty;
                }
            });
            EmailEditText.validateWith(new METValidator("Field con'nt be empty") {
                @Override
                public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                    return !isEmpty;
                }
            });
            PhoneEditText.validateWith(new METValidator("Field can'nt be empty") {
                @Override
                public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                    return !isEmpty;
                }
            });
            DOBEditText.validateWith(new METValidator("Field can'nt be empty") {
                @Override
                public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                    return !isEmpty;
                }
            });
        } else {
            progressBar.setVisibility(View.VISIBLE);
            final String emailId = EmailEditText.getText().toString(); //TODO Remove email field from here
            final String fName = FNameEditText.getText().toString();
            String lName = "";
            final String mobileNo = PhoneEditText.getText().toString();
            if (LNameEditText.getText() != null) {
                lName = LNameEditText.getText().toString();
            }
            final String gender = rbGender.getText().toString();
            final String dob = DOBEditText.getText().toString(); //TODO:Validate date of birth
            final String finalLName = lName;
            final String finalLName1 = lName;
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("emailId", emailId);
            userMap.put("firstName", fName);
            userMap.put("lastName", finalLName1);
            userMap.put("dateOfBirth", dob);
            userMap.put("gender", gender);
            userDatabase.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: sending user to HomeActivity");
                        progressBar.setVisibility(View.GONE);
                        Intent intent1 = new Intent(SetupUserActivity.this, HomeActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                        Toast.makeText(SetupUserActivity.this, "Data Updated Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SetupUserActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
    }

    private void validateFields() {
        Log.d(TAG, "validateFields: called");
        FNameEditText.addValidator(new RegexpValidator("Not a valid name", Utils.getRegexNamePattern()));
        LNameEditText.addValidator(new RegexpValidator("Not a valid name", Utils.getRegexNamePattern()));
        EmailEditText.addValidator(new RegexpValidator("Not a valid Email", Utils.getRegexEmailPattern()));
        PhoneEditText.addValidator(new RegexpValidator("Not a valid Mobile No", Utils.getRegexMobileNoPattern()));
        DOBEditText.addValidator(new RegexpValidator("Not a valid Date", Utils.getRegexDatePattern()));

        FNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                FNameEditText.validate();
            }
        }); //TODO: test this validation

        LNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LNameEditText.validate();
            }
        }); //TODO : test these validation


        EmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EmailEditText.validate();
            }
        });

        PhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                PhoneEditText.validate();
            }
        }); //TODO: generalize this validation valid only for indian mob Nos.

        DOBEditText.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

    }

    private void showDatePicker() {
        Log.d(TAG, "showDatePicker: called");
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(SetupUserActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "-" + month + "-" + year;
                DOBEditText.setText(date);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void initViews() {
        Log.d(TAG, "initViews: called");
        imgProfile = (ImageView) findViewById(R.id.imageProfile);
        btnDone = (LiveButton) findViewById(R.id.btnDone);
        FNameEditText = (MaterialEditText) findViewById(R.id.editTextFirstName);
        LNameEditText = (MaterialEditText) findViewById(R.id.editTextLastName);
        EmailEditText = (MaterialEditText) findViewById(R.id.editTextEmail);
        PhoneEditText = (MaterialEditText) findViewById(R.id.editTextMobileNo);
        DOBEditText = (MaterialEditText) findViewById(R.id.editDOB);
        radioGroupGender = (RadioGroup) findViewById(R.id.rgGender);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }
}
