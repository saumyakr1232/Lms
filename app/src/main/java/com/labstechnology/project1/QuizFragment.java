package com.labstechnology.project1;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.labstechnology.project1.models.MultipleChoiceQuestion;

import java.util.HashMap;

public class QuizFragment extends Fragment {
    private static final String TAG = "QuizFragment";

    private TextView Question;
    private RadioGroup rgOptions;
    private RadioButton rbOption1, rbOption2, rbOption3, rbOption4, rbAnswer;
    private ImageView emptyFlag, filledFlag;
    private MultipleChoiceQuestion incomingQuesion;
    private int questionNo;
    private boolean isflagged;
    private String answer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        Bundle bundle = getArguments();

        initViews(view);
        handleFlag();

        try {
            if (bundle != null) {
                incomingQuesion = bundle.getParcelable("question");
                questionNo = bundle.getInt("questionNo", 0);

                if (isflagged) {
                    filledFlag.setVisibility(View.VISIBLE);
                    emptyFlag.setVisibility(View.GONE);
                } else {
                    filledFlag.setVisibility(View.GONE);
                    emptyFlag.setVisibility(View.VISIBLE);
                }


                try {
                    Question.setText(incomingQuesion.getQuestion());
                    rbOption1.setText(incomingQuesion.getOptions().get(0));
                    rbOption2.setText(incomingQuesion.getOptions().get(1));
                    rbOption3.setText(incomingQuesion.getOptions().get(2));
                    rbOption4.setText(incomingQuesion.getOptions().get(3));
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }


            }
        } catch (NullPointerException | Resources.NotFoundException e) {
            e.printStackTrace();
        }


        handleAnswer(view);


        return view;
    }

    private void handleFlag() {
        emptyFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filledFlag.setVisibility(View.VISIBLE);
                emptyFlag.setVisibility(View.GONE);
                isflagged = true;
            }
        });
        filledFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filledFlag.setVisibility(View.GONE);
                emptyFlag.setVisibility(View.VISIBLE);
                isflagged = false;
            }
        });
    }

    private void handleAnswer(final View view) {
        Log.d(TAG, "handleAnswer: called");

        rgOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                handleAnswer(view);
                int rbId = rgOptions.getCheckedRadioButtonId();
                rbAnswer = (RadioButton) view.findViewById(rbId);
                if (rbAnswer != null) {

                    answer = rbAnswer.getText().toString();
                    Log.d(TAG, "handleAnswer: answer " + answer);
                }
            }
        });

    }

    private void initViews(View view) {
        Log.d(TAG, "initViews: called");
        emptyFlag = (ImageView) view.findViewById(R.id.emptyFlag);
        filledFlag = (ImageView) view.findViewById(R.id.filledFlag);
        Question = (TextView) view.findViewById(R.id.Question);
        rgOptions = (RadioGroup) view.findViewById(R.id.rgOptions);
        rbOption1 = (RadioButton) view.findViewById(R.id.rbOption1);
        rbOption2 = (RadioButton) view.findViewById(R.id.rbOption2);
        rbOption3 = (RadioButton) view.findViewById(R.id.rbOption3);
        rbOption4 = (RadioButton) view.findViewById(R.id.rbOption4);


    }

    public String getAnswer() {
        if (answer != null) {
            return answer;
        }
        return "";
    }

    public int getScore() {
        if (incomingQuesion.getAnswer() != null) {
            if (getAnswer().equals(incomingQuesion.getAnswer())) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }

    }

    public HashMap<String, MultipleChoiceQuestion> getQuestionAnswerMap() {
        HashMap<String, MultipleChoiceQuestion> questionAnswerMap = new HashMap<>();
        if (!answer.equals("")) {
            questionAnswerMap.put(answer, incomingQuesion);
            return questionAnswerMap;
        } else {
            questionAnswerMap.put(incomingQuesion.getId() + "NONE", incomingQuesion);
        }
        return questionAnswerMap;
    }

    public boolean isIsflagged() {
        return isflagged;
    }

    public MultipleChoiceQuestion getIncomingQuesion() {
        return incomingQuesion;
    }
}
