package com.labstechnology.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetupUserActivity extends AppCompatActivity {
    private static final String TAG = "SetupUserActivity";
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ProgressDialog progressDialog;

    //    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_user);
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Warning");
        progressDialog.setMessage("This app is currently under Development");
        progressDialog.setCancelable(false);//TODO test this
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent(SetupUserActivity.this, HomeActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                progressDialog.dismiss();
                startActivity(intent1);
            }
        }, 3000);


//        user = new User("saumyakr181999@gmail.com","saumyakr1232",FirebaseAuth.getInstance().getCurrentUser().getUid());
//        myRef = database.getReference("user/"+ user.id);
//
//
//        myRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    Intent intent1 = new Intent(SetupUserActivity.this, HomeActivity.class);
//                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent1);
//                }else{
//                    //TODO: show try again dialog box;
//                }
//            }
//        });


    }
}
