package com.labstechnology.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import ru.katso.livebutton.LiveButton;

public class ForgetPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ForgetPasswordActivity";

    private MaterialEditText editTextEmail;
    private ImageView btnBackArrow;
    private LiveButton btnRecoverPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initViews();

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        editTextEmail.setText(email);

        btnBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Arrow Back : Called");
                Intent intent1 = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(intent1);
            }
        });

        btnRecoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ForgetPasswordActivity.this, "Recover Password clicked", Toast.LENGTH_SHORT).show();
                if (editTextEmail.getText().toString().isEmpty()) {
                    editTextEmail.validateWith(new METValidator("Field can'nt be empty") {
                        @Override
                        public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
                            return !isEmpty;
                        }
                    });
                } else {
                    if (editTextEmail.validate()) {
                        String userEmail = editTextEmail.getText().toString();
                        Log.d(TAG, "onClick: sending email");
                        mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgetPasswordActivity.this, "Please check your email Inbox", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                                } else {
                                    Toast.makeText(ForgetPasswordActivity.this, "Some error Occurred, Try again 😥", Toast.LENGTH_SHORT).show();
                                    String error = task.getException().getLocalizedMessage();
                                    Log.d(TAG, "onComplete: error occured :" + error);
                                }
                            }
                        });
                    }
                }
                //TODO: send email to recover password
            }
        });


    }

    private void initViews() {
        Log.d(TAG, "initViews: called");
        editTextEmail = (MaterialEditText) findViewById(R.id.editTextEmail);
        btnBackArrow = (ImageView) findViewById(R.id.btnBackArrow);
        btnRecoverPassword = (LiveButton) findViewById(R.id.btnRecoverPassword);
        editTextEmail.addValidator(new RegexpValidator("Not a valid email", Utils.getRegexEmailPattern()));
    }
}
