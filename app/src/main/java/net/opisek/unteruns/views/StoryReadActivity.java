package net.opisek.unteruns.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import net.opisek.unteruns.R;

public class StoryReadActivity extends AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storyread);

        correct = getIntent().getExtras().getBoolean("correct");

        findViewById(R.id.button_vote_read_continue).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                toVoteResults();
            }
        });
    }

    private boolean correct;

    private void toVoteResults() {
        Intent intent = new Intent(this, VoteResultActivity.class);
        intent.putExtra("correct", correct);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.key_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("inProgress", false);
        editor.apply();

        startActivity(intent);
        finish();
    }

    private void backToMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backToMenu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
