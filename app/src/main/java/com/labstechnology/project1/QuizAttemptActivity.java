package com.labstechnology.project1;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.labstechnology.project1.CallBacks.FireBaseCallBack;
import com.labstechnology.project1.adapters.ViewPagerAdapter;
import com.labstechnology.project1.models.MultipleChoiceQuestion;
import com.labstechnology.project1.models.Quiz;
import com.labstechnology.project1.models.QuizScore;
import com.labstechnology.project1.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import xyz.hasnat.sweettoast.SweetToast;

public class QuizAttemptActivity extends AppCompatActivity {
    private static final String TAG = "QuizAttemptActivity";

    private ViewPager questionsViewPager;
    private TabLayout tabLayout;
    private TextView txtTotalQuestions, textTimeRemaining;
    private androidx.appcompat.widget.Toolbar toolbar;
    private Quiz incomingQuiz;
    private Button btnSubmit;
    private DatabaseReference rttDatabaseQuizRef;

    private ViewPagerAdapter adapter;

    private boolean timesUp = false;

    private Utils utils;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_attempt);
        initViews();
        setToolbar();

        utils = new Utils(QuizAttemptActivity.this);


        final DatabaseReference myRef = FirebaseDatabaseReference.DATABASE.getReference().child("quizzes");


        try {
            Intent intent = getIntent();
            incomingQuiz = intent.getParcelableExtra("quiz");
            rttDatabaseQuizRef = FirebaseDatabaseReference.DATABASE.getReference().child("quizzes").child(incomingQuiz.getId());
            if (incomingQuiz != null) {
                txtTotalQuestions.setText(incomingQuiz.getQuestions().size());
            }
        } catch (NullPointerException | Resources.NotFoundException e) {
            e.printStackTrace();
        }


        handleTimer();

        final ArrayList<QuizFragment> fragments = new ArrayList<>();

        final ArrayList<MultipleChoiceQuestion> questions = incomingQuiz.getQuestions();


        for (int i = 0; i < questions.size(); i++) {
            QuizFragment quizFragment = new QuizFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("question", incomingQuiz.getQuestions().get(i));
            bundle.putInt("questionNo", i + 1);
            quizFragment.setArguments(bundle);
            fragments.add(quizFragment);

        }
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);

        questionsViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(questionsViewPager);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(QuizAttemptActivity.this)
                        .setTitle("End Quiz")
                        .setMessage("Are you sure to end this quiz ?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final QuizScore quizScore = new QuizScore();
                                quizScore.setId(Utils.getCurrentUid().substring(0, 4) + incomingQuiz.getId().substring(0, 4));
                                quizScore.setQuizId(incomingQuiz.getId());
                                quizScore.setuId(Utils.getCurrentUid());
                                quizScore.setOutOf(incomingQuiz.getQuestions().size());
                                double score = 0;
                                boolean readyToSubmit = true;
                                ArrayList<HashMap<String, MultipleChoiceQuestion>> map = new ArrayList<>();
                                for (QuizFragment fragment : fragments
                                ) {

                                    try {
                                        score += fragment.getScore();
                                        map.add(fragment.getQuestionAnswerMap());
                                        Log.d(TAG, "onClick: Answers " + fragment.getAnswer());
                                        Log.d(TAG, "onClick: Score " + fragment.getScore());
                                        Log.d(TAG, "onClick: Map " + fragment.getQuestionAnswerMap());
                                        Log.d(TAG, "onClick: Actual Answer" + fragment.getIncomingQuesion().getAnswer());
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }

                                    if (fragment.isIsflagged()) {
                                        readyToSubmit = false;
                                    }


                                }
                                quizScore.setScore(score);
                                quizScore.setResult(map);

                                if (readyToSubmit) {
                                    saveAttempt(quizScore);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(QuizAttemptActivity.this)
                                            .setTitle("Flagged Questions")
                                            .setMessage("you flagged some question, Would you like to review them")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).setNegativeButton("Submit & Finish", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    saveAttempt(quizScore);
                                                }
                                            });
                                    builder.show();
                                }
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder2.show();


            }
        });

        if (timesUp) {
            final QuizScore quizScore = new QuizScore();
            quizScore.setId(Utils.getCurrentUid().substring(0, 4) + incomingQuiz.getId().substring(0, 4));
            quizScore.setQuizId(incomingQuiz.getId());
            quizScore.setuId(Utils.getCurrentUid());
            quizScore.setOutOf(incomingQuiz.getQuestions().size());
            double score = 0;
            boolean readyToSubmit = true;
            ArrayList<HashMap<String, MultipleChoiceQuestion>> map = new ArrayList<>();
            for (QuizFragment fragment : fragments
            ) {

                try {
                    score += fragment.getScore();
                    map.add(fragment.getQuestionAnswerMap());
                    Log.d(TAG, "onClick: Answers " + fragment.getAnswer());
                    Log.d(TAG, "onClick: Score " + fragment.getScore());
                    Log.d(TAG, "onClick: Map " + fragment.getQuestionAnswerMap());
                    Log.d(TAG, "onClick: Actual Answer" + fragment.getIncomingQuesion().getAnswer());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                if (fragment.isIsflagged()) {
                    readyToSubmit = false;
                }


            }
            quizScore.setScore(score);
            quizScore.setResult(map);
            saveAttempt(quizScore);
        }


    }

    private void saveAttempt(final QuizScore quizScore) {
        Log.d(TAG, "saveAttempt: called");
        utils.getCurrentUser(new FireBaseCallBack() {
            @Override
            public void onSuccess(Object object) {
                User user = (User) object;
                ArrayList<User> attemptedBy = incomingQuiz.getAttemptedBy();
                ArrayList<User> attempting = incomingQuiz.getAttempting();
                if (attemptedBy == null) {
                    attemptedBy = new ArrayList<>();
                    attemptedBy.add(user);
                } else {
                    attemptedBy.add(user);
                    Log.d(TAG, "onSuccess: new AttemptedBy" + attemptedBy.toString());
                }

                if (attempting == null) {
                    attempting = new ArrayList<>();
                } else {
                    for (User u : attempting
                    ) {
                        if (u.getuId().equals(Utils.getCurrentUid())) {
                            attempting.remove(u);
                        }
                    }
                    Log.d(TAG, "onSuccess: New attempting" + attempting.toString());
                }
                ArrayList<QuizScore> scores = incomingQuiz.getScores();

                if (scores == null) {
                    scores = new ArrayList<>();
                    scores.add(quizScore);
                } else {
                    scores.add(quizScore);
                    Log.d(TAG, "onSuccess: New Scores" + scores.toString());
                }

                HashMap<String, Object> updateChild = new HashMap<String, Object>();
                updateChild.put("attemptedBy", attemptedBy);
                updateChild.put("attempting", attempting);
                updateChild.put("scores", scores);

//                            incomingQuiz.setAttemptedBy(attemptedBy);
//                            incomingQuiz.setAttempting(attempting);
//                            incomingQuiz.setScores(scores);
                rttDatabaseQuizRef.updateChildren(updateChild)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    SweetToast.success(QuizAttemptActivity.this, "Quiz is submitted");
                                    Intent intent = new Intent(QuizAttemptActivity.this, QuizActivity.class);
                                    intent.putExtra("quiz", incomingQuiz);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: error while uploading quiz submission" + e.getLocalizedMessage());
                    }
                });
            }

            @Override
            public void onError(Object object) {

            }
        });
    }

    private void handleTimer() {
        String limit = incomingQuiz.getTimeLimit();
        if (limit != null) {
            String[] l = limit.split(":");
            try {
                long mills = Integer.parseInt(l[0]) * 3600 * 1000 + Integer.parseInt(l[1]) * 60 * 1000;
                new CountDownTimer(mills, 1000) {

                    @SuppressLint("SetTextI18n")
                    public void onTick(long millisUntilFinished) {
                        long hrRemaining = (millisUntilFinished / 3600000);
                        long minRemaining = (millisUntilFinished % 3600000) / 60000;
                        textTimeRemaining.setText(hrRemaining + ":" + minRemaining + " Remaining");

                    }

                    @SuppressLint("SetTextI18n")
                    public void onFinish() {
                        textTimeRemaining.setText("Time's up");
                        timesUp = true;


                    }
                }.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void initViews() {
        Log.d(TAG, "initViews: called");
        questionsViewPager = (ViewPager) findViewById(R.id.questionViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        txtTotalQuestions = (TextView) findViewById(R.id.NoOfQuestions);
        textTimeRemaining = (TextView) findViewById(R.id.textTimeRemaining);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        if (incomingQuiz != null) {
            toolbar.setTitle(incomingQuiz.getTitle());
        } else {
            toolbar.setTitle("Quiz");
        }

        toolbar.setTitleTextColor(getColor(R.color.white1));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
    }


    @Override
    public boolean onNavigateUp() {
        btnSubmit.performClick();

        return true;
    }
}