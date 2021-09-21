package net.opisek.unteruns.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import net.opisek.unteruns.R;
import net.opisek.unteruns.repositories.MainRepository;
import net.opisek.unteruns.viewmodels.InputQuestionViewModel;
import net.opisek.unteruns.viewmodels.VoteRiddleViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class VoteRiddleActivity extends RiddleActivity {

    private VoteRiddleViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riddlevote);

        viewModel = ViewModelProviders.of(this).get(VoteRiddleViewModel.class);

        findViewById(R.id.button_vote_red).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                changeVoteTo(viewModel.getVote("Red"));
            }
        });
        findViewById(R.id.button_vote_pink).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                changeVoteTo(viewModel.getVote("Pink"));
            }
        });
        findViewById(R.id.button_vote_white).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                changeVoteTo(viewModel.getVote("White"));
            }
        });
        findViewById(R.id.button_vote_confirm).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                riddleSolved();
            }
        });

        vote = new int[2];
    }

    private int[] vote;

    private void changeVoteTo(int newVote) {
        if (vote[0] == newVote) {
            vote[0] = vote[1];
            vote[1] = 0;
        } else if (vote[1] == newVote) {
            vote[1] = 0;
        } else {
            if (vote[1] != 0) vote[0] = vote[1];
            vote[1] = newVote;
        }
        updateButtonColors();
    }

    private void updateButtonColors() {
        int selectedColor = ContextCompat.getColor(getApplicationContext(), R.color.colorBackgroundSelect);
        int normalColor = ContextCompat.getColor(getApplicationContext(), R.color.colorBackgroundDark);

        if (voteToList().contains(MainRepository.getInstance().redVote)) findViewById(R.id.button_vote_red).setBackgroundColor(selectedColor);
        else findViewById(R.id.button_vote_red).setBackgroundColor(normalColor);
        if (voteToList().contains(MainRepository.getInstance().pinkVote)) findViewById(R.id.button_vote_pink).setBackgroundColor(selectedColor);
        else findViewById(R.id.button_vote_pink).setBackgroundColor(normalColor);
        if (voteToList().contains(MainRepository.getInstance().whiteVote)) findViewById(R.id.button_vote_white).setBackgroundColor(selectedColor);
        else findViewById(R.id.button_vote_white).setBackgroundColor(normalColor);
    }

    @Override
    public void riddleSolved() {
        if (vote == null || vote[0] == 0 || vote[1] == 0) return;
        boolean correct = ((VoteRiddleViewModel)viewModel).isVoteCorrect(voteToList());
        Intent intent = new Intent(this, StoryReadActivity.class);
        intent.putExtra("correct", correct);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.key_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("inProgress", false);
        editor.apply();

        startActivity(intent);
        finish();
    }

    private ArrayList<Integer> voteToList() {
        ArrayList<Integer> voteList = new ArrayList<>();
        voteList.add(vote[0]);
        voteList.add(vote[1]);
        return voteList;
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
