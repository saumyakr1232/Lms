package com.labstechnology.project1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.labstechnology.project1.CallBacks.FireBaseCallBack;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import java.util.HashMap;
import java.util.Objects;

import ru.katso.livebutton.LiveButton;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    private MaterialEditText nameEditText, emailEditText, passwordEditText, confPassEditText;
    private LiveButton btnSignUp;
    private ImageView imageHide, imageShow, imageShow1, imageHide1;

    private FirebaseAuth mAuth;
    private Utils utils;
    private ImageView btnBackArrow;
    private ProgressBar progressBar;

    private DatabaseReference userDatabase;

    private String currentUserID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();
        utils = new Utils(this);

        if (Utils.getDarkThemePreference(this)) {
            emailEditText.setTextColor(getColor(R.color.white1));
            passwordEditText.setTextColor(getColor(R.color.white1));
            nameEditText.setTextColor(getColor(R.color.white1));
            confPassEditText.setTextColor(getColor(R.color.white1));
        }

        mAuth = FirebaseAuth.getInstance();

        btnBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        handleEditText();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(emailEditText.getText()).toString().isEmpty()
                        || Objects.requireNonNull(nameEditText.getText()).toString().isEmpty()
                        || Objects.requireNonNull(passwordEditText.getText()).toString().isEmpty()
                        || Objects.requireNonNull(confPassEditText.getText()).toString().isEmpty()) {
                    addValidations();
                } else if (Objects.equals(passwordEditText.getText().toString(), confPassEditText.getText().toString())) {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, getString(R.string.registration_successful), Toast.LENGTH_SHORT).show();
                                        signInUser();
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        utils.setSignedIn(false);
                                    }


                                }
                            });

                } else {
                    progressBar.setVisibility(View.GONE);
                    passwordEditText.setError(getString(R.string.passwords_did_not_match));
                    confPassEditText.setError(getString(R.string.passwords_did_not_match));
                    utils.setSignedIn(false);
                }

            }
        });

    }

    private void signInUser() {
        Log.d(TAG, "signInUser: called");
        mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            handleEnrollmentSituation();

                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(SignUpActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, SetupUserActivity.class);
                            String email = emailEditText.getText().toString();
                            intent.putExtra("email", email);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }

                    }
                });

    }

    private void handleEnrollmentSituation() {
        Log.d(TAG, "handleEnrollmentSituation: called");
        //Finding last enrollment no to add to users data
        Utils.findLastEnrollmentNo(new FireBaseCallBack() {
            @Override
            public void onSuccess(Object object) {
                Toast.makeText(SignUpActivity.this, "Getting enrollment no for you", Toast.LENGTH_SHORT).show();
                String value = (String) object;
                Integer enrollment = Integer.parseInt(value) + 1;
                value = enrollment.toString();
                Log.d(TAG, "onSuccess: got last enrollment no" + enrollment);
                HashMap<String, Object> userMap = new HashMap<>();
                userMap.put("enrollmentNo", value);
                currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                userDatabase = FirebaseDatabaseReference.DATABASE.getReference().child(FirebaseConstants.USERS).child(currentUserID);
                //adding enrollment no to user database
                userDatabase.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: enrollment no is set");
                            Toast.makeText(SignUpActivity.this, "EnrollmentNo is set ", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(SignUpActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //Updated lastEnrollment No
                Utils.updateLastEnrollmentNo(value);

            }

            @Override
            public void onError(Object object) {

            }
        });

    }

    private void handleEditText() {
        Log.d(TAG, "handleEditText: called");
        imageHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageHide.setVisibility(View.GONE);
                imageShow.setVisibility(View.VISIBLE);
                passwordEditText.setTransformationMethod(null);
            }
        });

        imageShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageHide.setVisibility(View.VISIBLE);
                imageShow.setVisibility(View.GONE);
                passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        imageHide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageHide1.setVisibility(View.GONE);
                imageShow1.setVisibility(View.VISIBLE);
                confPassEditText.setTransformationMethod(null);
            }
        });

        imageShow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageHide1.setVisibility(View.VISIBLE);
                imageShow1.setVisibility(View.GONE);
                confPassEditText.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailEditText.validate();
            }

            @Override
            public void afterTextChanged(Editable s) {
                emailEditText.validate();
            }
        });

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameEditText.validate();
            }

            @Override
            public void afterTextChanged(Editable s) {
                nameEditText.validate();
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                passwordEditText.setMinCharacters(8);
                passwordEditText.setMaxCharacters(20);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO: Validate password
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confPassEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                confPassEditText.setMinCharacters(8);
                confPassEditText.setMaxCharacters(20);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO: validate password
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void addValidations() {
        Log.d(TAG, "addValidations: called");
        progressBar.setVisibility(View.GONE);
        emailEditText.setMinCharacters(5);
        confPassEditText.setMinCharacters(8);
        passwordEditText.setMinCharacters(8);
        passwordEditText.setMaxCharacters(20);
        confPassEditText.setMaxCharacters(20);
        nameEditText.setMinCharacters(2);
        nameEditText.validateWith(new METValidator("Field can'nt be empty") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        emailEditText.validateWith(new METValidator("Field con'nt be empty") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        passwordEditText.validateWith(new METValidator("Field can'nt be empty") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
        confPassEditText.validateWith(new METValidator("Field can'nt be empty") {
            @Override
            public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                return !isEmpty;
            }
        });
    }

    private void initViews() {
        Log.d(TAG, "initViews: called");
        nameEditText = (MaterialEditText) findViewById(R.id.editTextName);
        emailEditText = (MaterialEditText) findViewById(R.id.editTextEmail);
        passwordEditText = (MaterialEditText) findViewById(R.id.editTextPassword);
        confPassEditText = (MaterialEditText) findViewById(R.id.editTextPasswordConfirm);
        btnSignUp = (LiveButton) findViewById(R.id.btnSignUp);
        btnBackArrow = (ImageView) findViewById(R.id.btnBackArrow);
        imageHide = (ImageView) findViewById(R.id.imageHide);
        imageShow = (ImageView) findViewById(R.id.imageShow);
        imageHide1 = (ImageView) findViewById(R.id.imageHide1);
        imageShow1 = (ImageView) findViewById(R.id.imageShow1);
        nameEditText.addValidator(new RegexpValidator("Not a valid name", Utils.getRegexNamePattern()));
        emailEditText.addValidator(new RegexpValidator("Not a valid Email", Utils.getRegexEmailPattern()));
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }
}
