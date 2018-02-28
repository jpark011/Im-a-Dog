package com.cs446w18.a16.imadog.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.cs446w18.a16.imadog.Global;
import com.cs446w18.a16.imadog.R;

/**
 * Created by Jean-Baptiste on 18/02/2018.
 */

public class QuestionFragment extends SuperFragment {

    public interface Delegate {
        void answeredQuestion(String answer);
    }

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    TextView questionLabel;
    TextView timerLabel;

    EditText answerField;


    /* ----------------------------- SETUP ----------------------------- */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_question, container, false);
        Bundle bundle = getArguments();

        // Set the question label
        questionLabel = view.findViewById(R.id.titleLabel);
        questionLabel.setText(bundle.getString("question"));

        // Answer field
        answerField = view.findViewById(R.id.answerField);
        answerField.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // Lock the view if player is dead
        if (Global.user.isDead()) { // KAREN: Replace by "if player is dead"
            answerField.setEnabled(false);
        }

        // Set the return action
        TextView.OnEditorActionListener fieldListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH ||
                        i == EditorInfo.IME_ACTION_DONE ||
                        keyEvent != null &&
                                keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                                keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    // When the user press enter
                    getGameActivity().hideSystemUI();
                    getGameActivity().answeredQuestion(textView.getText().toString());

                }
                return false;

            }
        };
        answerField.setOnEditorActionListener(fieldListener);

        timerLabel = view.findViewById(R.id.timerLabel);

        long duration = 15;

        CountDownTimer timer = new CountDownTimer(duration*1000, 1000) {
            @Override
            public void onTick(long l) {
                setTimerLabel(l);
            }

            @Override
            public void onFinish() {
                // TEST: to be replaced by a direct call from the model.
//                if (GameConstants.INTERFACE_TEST) {
//                    getGameActivity().showVictimPage("Player1", "Cat");
//                }

                // TODO: show "Waiting for results..."
            }
        }.start();

        return view;
    }


    /* ----------------------------- METHODS ----------------------------- */

    /// Updates the timer label with the given time
    private void setTimerLabel(long time) {
        int minutes = (int) time / 60000;
        int seconds = (int) time % 60000 / 1000;
        timerLabel.setText(minutes + ":" + String.format("%02d", seconds));
    }

}
