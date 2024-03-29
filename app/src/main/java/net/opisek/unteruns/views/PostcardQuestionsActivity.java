package net.opisek.unteruns.views;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import net.opisek.unteruns.R;
import net.opisek.unteruns.viewmodels.PostcardQuestionsViewModel;
import net.opisek.unteruns.viewmodels.RoutesViewModel;

import java.util.ArrayList;

public class PostcardQuestionsActivity extends AppCompatActivity {
    private PostcardQuestionsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcardquestions);
        viewModel = ViewModelProviders.of(this).get(PostcardQuestionsViewModel.class);

        String[] questions = viewModel.getQuestions();

        LinearLayout buttonsList = findViewById(R.id.linear_postcardquestions_buttons);
        for (int i = 0; i < questions.length; i++) {
            MutableLiveData<Boolean> isDone = viewModel.getQuestionDone(i);

            final Button button = new Button(this);
            button.setText(questions[i]);
            button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            button.setGravity(Gravity.CENTER);
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBackgroundDark));
            int pad = (int)dpToPx(5f);
            button.setPadding(pad, pad, pad, pad);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
            params.setMargins(0, 0, 0, (int)dpToPx(10f));
            button.setLayoutParams(params);

            final int buttonIndex = i;
            button.setOnClickListener(new View.OnClickListener(){
                @Override public void onClick(View v) {
                    onPicked(buttonIndex);
                }
            });

            button.setEnabled(!isDone.getValue());
            if (isDone.getValue()) {
                button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorForegroundLightDisabled));
                button.setPaintFlags(button.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorForegroundLight));
                button.setPaintFlags(button.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
            isDone.observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean done) {
                    button.setEnabled(!done);
                    if (done) {
                        button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorForegroundLightDisabled));
                        button.setPaintFlags(button.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorForegroundLight));
                        button.setPaintFlags(button.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    }
                }
            });

            buttonsList.addView(button);
        }

        if (viewModel.getAllDone().getValue()) addAllDoneButton();
        else viewModel.getAllDone().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean allDone) {
                if (allDone) {
                    addAllDoneButton();
                }
            }
        });
    }

    public void addAllDoneButton() {
        Button button = new Button(this);
        button.setText(R.string.button_postcardquestions_done);
        button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        button.setGravity(Gravity.CENTER);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorForegroundLight));
        button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBackgroundDark));
        int pad = (int)dpToPx(5f);
        button.setPadding(pad, pad, pad, pad);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
        params.setMargins(0, 0, 0, (int)dpToPx(10f));
        button.setLayoutParams(params);

        button.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                goBack();
            }
        });

        ((LinearLayout)findViewById(R.id.linear_postcardquestions_buttons)).addView(button);
    }

    public void goBack() {
        finish();
    }

    private void onPicked(int question) {
        Intent intent = new Intent(this, PostcardQuestionActivity.class);
        intent.putExtra("question", question);
        startActivity(intent);
    }

    private float dpToPx(float dp) {
        return dp * getApplicationContext().getResources().getDisplayMetrics().density;
    }
}
