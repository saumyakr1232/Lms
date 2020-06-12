package com.labstechnology.project1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.labstechnology.project1.CallBacks.FireBaseCallBack;
import com.labstechnology.project1.models.MultipleChoiceQuestion;
import com.labstechnology.project1.models.Quiz;
import com.labstechnology.project1.models.QuizScore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.hasnat.sweettoast.SweetToast;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";

    private TextView textDesc, textTimeLimit, textStatus, textGrade;
    private Button btnStartQuiz;
    private androidx.appcompat.widget.Toolbar toolbar;
    private Utils utils;
    private Quiz incomingQuiz, rttQuiz;
    private ProgressBar progressBar;

    private StorageReference QuizDocumentsRef, QuizDocumentsRef2;
    private DatabaseReference rttQuizDatabaseRef;
    private String id;
    private boolean isQuizReady = false;

    //private boolean isSubmitted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        utils = new Utils(this);

        final DatabaseReference myRef = FirebaseDatabaseReference.DATABASE.getReference().child("quizzes");
        setRttQuiz(myRef);
        initViews();


        final Intent intent = getIntent();
        try {
            incomingQuiz = intent.getParcelableExtra("quiz");

            id = myRef.push().getKey();

            rttQuizDatabaseRef = FirebaseDatabaseReference.DATABASE.getReference()
                    .child("quizzes").child(incomingQuiz.getId());


            QuizDocumentsRef2 = FirebaseStorage.getInstance().getReference().child("quizzesDocument").child(incomingQuiz.getId());


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        setToolbar();


        btnStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareTheQuiz();
                Intent intent1 = new Intent(QuizActivity.this, QuizAttemptActivity.class);
                if (isQuizReady) {
                    intent1.putExtra("quiz", incomingQuiz);
                    startActivity(intent1);
                } else {
                    SweetToast.defaultShort(QuizActivity.this, "Wait quiz is building");
                }

            }
        });

    }

    private void prepareTheQuiz() {
        Log.d(TAG, "prepareTheQuiz: called");
        try {
            downloadQuizDocument(new FireBaseCallBack() {
                @Override
                public void onSuccess(Object object) {
                    try {
                        File file = (File) object;
                        //Log.d(TAG, "onSuccess: FILE CONTENTS " + utils.readDocxFile(file));
                        ArrayList<MultipleChoiceQuestion> questions = utils.getQuestionsFromFile(file);
                        isQuizReady = true;
                        Log.d(TAG, "onSuccess: Question HEer e " + questions.toString());
                        incomingQuiz.setQuestions(questions);
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "onSuccess: FILE " + file.getPath());
                        Log.d(TAG, "onSuccess: FILE " + file.getName());
                        Log.d(TAG, "onSuccess: FILE" + file.getTotalSpace());
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(Object object) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setRttQuiz(DatabaseReference myRef) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot oneSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: dataSnapshot" + dataSnapshot);
                    if (Objects.equals(oneSnapshot.getKey(), incomingQuiz.getId())) {
                        try {
                            rttQuiz = oneSnapshot.getValue(Quiz.class);
                            setViewsValues();
                            Log.d(TAG, "onDataChange: rttQuiz" + rttQuiz);
                            changeViewsBasedOnStatus();
                        } catch (ClassCastException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitle(incomingQuiz.getTitle());
        toolbar.setTitleTextColor(getColor(R.color.white1));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
    }

    private void downloadQuizDocument(final FireBaseCallBack callBack) throws IOException {
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "downloadQuizDocument: called");
        Log.d(TAG, "downloadQuizDocument: ID" + "/quizzesDocument/" + incomingQuiz.getId() + "/" + incomingQuiz.getId() + "upload");
        final StorageReference filePath = QuizDocumentsRef2.child(incomingQuiz.getId() + "upload");

        final File localFileDocx = File.createTempFile("upload", "docx");

        filePath.getFile(localFileDocx).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                SweetToast.success(QuizActivity.this, "local file has been created");
                callBack.onSuccess(localFileDocx);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });


    }

    private void changeViewsBasedOnStatus() {
        Log.d(TAG, "changeViewsBasedOnStatus: called");
    }

    private void initViews() {
        Log.d(TAG, "initViews: called");
        btnStartQuiz = (Button) findViewById(R.id.btnStartQuiz);
        textDesc = (TextView) findViewById(R.id.textDesc);
        textGrade = (TextView) findViewById(R.id.textGrade);
        textStatus = (TextView) findViewById(R.id.textStatus);
        textTimeLimit = (TextView) findViewById(R.id.textTimeLimit);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @SuppressLint("SetTextI18n")
    private void setViewsValues() {
        Log.d(TAG, "setViewsValues: called");

        String description = incomingQuiz.getDescription();

        Pattern trimmer = Pattern.compile("(?:\\b(?:http|ftp|www\\.)\\S+\\b)|(?:\\b\\S+\\.com\\S*\\b)");
        Matcher m = trimmer.matcher(description);
        StringBuffer out = new StringBuffer();
        int i = 1;
        Log.d(TAG, "setViewsValues: Trimmer" + trimmer.toString());
        ArrayList<String> links = new ArrayList<>();
        while (m.find()) {
            Log.d(TAG, "setViewsValues: group " + m.group() + "\n");
            links.add(m.group());
            m.appendReplacement(out, "<a href=\"" + m.group() + "\">URL" + i++ + "</a>");

        }
        m.appendTail(out);


        textDesc.setText(Html.fromHtml(out.toString()));
        textDesc.setMovementMethod(LinkMovementMethod.getInstance());
        String[] limit = incomingQuiz.getTimeLimit().split(":");
        try {
            textTimeLimit.setText(limit[0] + " hr " + limit[1] + " mins");
        } catch (Exception e) {
            e.getLocalizedMessage();
            textTimeLimit.setText(incomingQuiz.getTimeLimit());
        }

        if (utils.isUserAttemptThisQuiz(rttQuiz)) {
            textStatus.setText("Submitted");
            textStatus.setTextColor(getColor(R.color.green));
            btnStartQuiz.setVisibility(View.GONE);
        } else if (utils.isUserAttemptingThisQuiz(rttQuiz)) {
            textStatus.setText("Attempting");
            textStatus.setTextColor(getColor(R.color.colorWarning));
            btnStartQuiz.setVisibility(View.VISIBLE);
            btnStartQuiz.setText("Continue attempt");
        } else {
            textStatus.setText("Not Submitted");
            textStatus.setTextColor(getColor(R.color.colorRed));
            btnStartQuiz.setVisibility(View.VISIBLE);
            btnStartQuiz.setText("Start Quiz");
        }
        if (utils.isUserAttemptThisQuiz(rttQuiz)) {
            QuizScore Quizscore = utils.getQuizScore(rttQuiz);
            double score = Quizscore.getScore();
            double outOf = Quizscore.getOutOf();
            textGrade.setText(score + "/" + outOf);

        } else {
            textGrade.setText("0");
            textGrade.setTextColor(getColor(R.color.colorRed));
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(QuizActivity.this, TestActivity.class);
        startActivity(intent);
        return true;
    }

}