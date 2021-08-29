package net.opisek.unteruns.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import net.opisek.unteruns.R;
import net.opisek.unteruns.repositories.MainRepository.inputQuestionID;
import net.opisek.unteruns.viewmodels.InputQuestionViewModel;

public class InputQuestionActivity extends RiddleActivity {

    private InputQuestionViewModel viewModel;
    private Handler handler;

    private EditText textField;
    private Button checkButton;

    private String defaultTextFieldText;
    private String defaultCheckButtonText;
    private String wrongCheckButtonText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputquestion);

        viewModel = ViewModelProviders.of(this).get(InputQuestionViewModel.class);
        viewModel.setAnswer((inputQuestionID)(getIntent().getExtras().getSerializable("id")));
        viewModel.getIsCorrect().observe(this, new Observer<Pair<Boolean, Integer>>() {
            @Override
            public void onChanged(Pair<Boolean, Integer> correct) {
                if (correct.first) {
                    riddleSolved();
                } else {
                    if (correct.second > 0) wrongAnswer();
                }
            }
        });

        handler = new Handler();

        defaultTextFieldText = getResources().getString(R.string.field_inputquestion_answer);
        textField = findViewById(R.id.field_inputquestion_answer);
        textField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hasFocus();
                return false;
            }
        });
        textField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    lostFocus();
                    return true;
                }
                return false;
            }
        });

        defaultCheckButtonText = getResources().getString(R.string.button_inputquestion_check);
        wrongCheckButtonText = getResources().getString(R.string.label_inputquestion_wrong);
        checkButton = findViewById(R.id.button_inputquestion_check);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
            }
        });
    }

    private void hasFocus() {
        if (textField.getText().toString().equals(defaultTextFieldText))
            textField.setText("");
    }

    private void lostFocus() {
        textField.clearFocus();
        ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(textField.getWindowToken(), 0);
        if (textField.getText().toString().replace(" ", "").length() == 0)
            textField.setText(defaultTextFieldText);
    }

    private void wrongAnswer() {
        checkButton.setText(wrongCheckButtonText);
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resetButtonText();
            }
        }, 1500);
    }

    private void resetButtonText() {
        checkButton.setText(defaultCheckButtonText);
    }

    private void checkAnswer() {
        viewModel.checkCorrect(textField.getText().toString());
    }

    private void mainMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        this.finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            BackDialog dialog = new BackDialog();
            dialog.setBackListener(new BackDialog.BackListener() {
                @Override
                public void onUserResponse(Boolean result) {
                    if (result) mainMenu();
                }
            });
            dialog.show(getSupportFragmentManager(), "Back Dialog");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
