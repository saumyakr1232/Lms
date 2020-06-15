package com.labstechnology.project1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import xyz.hasnat.sweettoast.SweetToast;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private LiveButton btnLogIn;
    private MaterialEditText editTextEmail, editTextPassword;
    private TextView forgotPassword;
    private ImageView imageHide, imageShow;
    private RelativeLayout parent;

    private FirebaseAuth mAuth;


    private boolean validEmail = false;
    private ProgressDialog progressDialog;

    private Utils utils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();


        mAuth = FirebaseAuth.getInstance();
        utils = new Utils(this);

        progressDialog = new ProgressDialog(this);

        if (Utils.getDarkThemePreference(this)) {
            editTextEmail.setTextColor(getColor(R.color.white1));
            editTextPassword.setTextColor(getColor(R.color.white1));
        }

        imageHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageHide.setVisibility(View.GONE);
                imageShow.setVisibility(View.VISIBLE);
                editTextPassword.setTransformationMethod(null);
            }
        });

        imageShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageHide.setVisibility(View.VISIBLE);
                imageShow.setVisibility(View.GONE);
                editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validEmail = editTextEmail.validate();
                Log.d(TAG, "onTextChanged: valid :" + validEmail);
            }

            @Override
            public void afterTextChanged(Editable s) {
                validEmail = editTextEmail.validate();
                Log.d(TAG, "onTextChanged: valid :" + validEmail);
            }
        });


        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editTextPassword.setMinCharacters(8);
                editTextPassword.setMaxCharacters(20);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO: check for password strength

            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextEmail.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty()) {
                    editTextPassword.setMinCharacters(8);
                    editTextPassword.setMaxCharacters(20);
                    editTextEmail.setMinCharacters(5);
                    editTextEmail.validateWith(new METValidator("Field con'nt be empty") {
                        @Override
                        public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                            return !isEmpty;
                        }
                    });
                    editTextPassword.validateWith(new METValidator("Field can'nt be empty") {
                        @Override
                        public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                            return !isEmpty;
                        }
                    });
                } else {
                    progressDialog.setTitle("Logging in...");
                    progressDialog.setMessage("Please wail, this will take a moment.");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        SweetToast.success(LoginActivity.this, "Logged In 🎆");
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);

                                        utils.setSignedIn(true);
                                    } else {
                                        progressDialog.dismiss();
                                        String wrongPasswordErrorMessage = "The password is invalid or the user does not have a password.";
                                        String userDoesNotExistErrorMessage = "There is no user record corresponding to this identifier. The user may have been deleted.";
                                        if (Objects.equals(Objects.requireNonNull(task.getException()).getLocalizedMessage(), wrongPasswordErrorMessage)) {
                                            editTextPassword.setError("Wrong Password");
                                        } else if (Objects.equals(task.getException().getLocalizedMessage(), userDoesNotExistErrorMessage)) {
                                            editTextEmail.setError("Email Not registered");
                                        }
                                        Log.d(TAG, "onComplete: error message: " + task.getException().getLocalizedMessage());
                                        utils.setSignedIn(false);
                                    }
                                    progressDialog.dismiss();
                                }
                            });
                }


            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                intent.putExtra("email", editTextEmail.getText().toString());
                startActivity(intent);
            }
        });

    }

    private void initViews() {
        Log.d(TAG, "initViews: called");
        btnLogIn = (LiveButton) findViewById(R.id.btnLogin);
        editTextEmail = (MaterialEditText) findViewById(R.id.editTextEmail);
        editTextPassword = (MaterialEditText) findViewById(R.id.editTextPassword);
        forgotPassword = (TextView) findViewById(R.id.textViewForgetPassword);
        editTextEmail.addValidator(new RegexpValidator("Not a valid email", Utils.getRegexEmailPattern()));
        imageHide = (ImageView) findViewById(R.id.imageHide);
        imageShow = (ImageView) findViewById(R.id.imageShow);
        parent = (RelativeLayout) findViewById(R.id.parent);

    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        Log.d(TAG, "closeKeyboard: called");
        if (null != view) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
