package net.opisek.unteruns.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import net.opisek.unteruns.R;
import net.opisek.unteruns.viewmodels.InputQuestionViewModel;
import net.opisek.unteruns.viewmodels.VoteRiddleViewModel;

public class VoteRiddleActivity extends RiddleActivity {

    private VoteRiddleViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riddlevote);

        viewModel = ViewModelProviders.of(this).get(VoteRiddleViewModel.class);

        findViewById(R.id.button_vote_red).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                voteRed();
            }
        });
        findViewById(R.id.button_vote_white).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                voteWhite();
            }
        });
        findViewById(R.id.button_vote_confirm).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                riddleSolved();
            }
        });
    }

    private String vote;

    private void voteRed() {
        vote = "Red";
    }
    private void voteWhite() {
        vote = "White";
    }

    @Override
    public void riddleSolved() {
        if (vote == null || vote.equals("")) return;
        boolean correct = ((VoteRiddleViewModel)viewModel).isVoteCorrect(vote);
        Intent intent = new Intent(this, VoteResultActivity.class);
        intent.putExtra("correct", correct);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.key_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("inProgress", false);
        editor.apply();

        startActivity(intent);
        finish();
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
