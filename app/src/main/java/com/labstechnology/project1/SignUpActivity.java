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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();


        mAuth = FirebaseAuth.getInstance();

        btnBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


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
                passwordEditText.setTransformationMethod(null);
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

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if (emailEditText.getText().toString().isEmpty()
                        || nameEditText.getText().toString().isEmpty()
                        || passwordEditText.getText().toString().isEmpty()
                        || confPassEditText.getText().toString().isEmpty()) {
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
                    return;

                } else if (Objects.equals(passwordEditText.getText().toString(), confPassEditText.getText().toString())) {
                    mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, getString(R.string.registration_successful), Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this, SetupUserActivity.class);
                                        utils.setSignedIn(true);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        utils.setSignedIn(false);
                                    }


                                }
                            });

                } else {
                    passwordEditText.setError(getString(R.string.passwords_did_not_match));
                    confPassEditText.setError(getString(R.string.passwords_did_not_match));
                    ;
                    utils.setSignedIn(false);
                }

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

    }
}
