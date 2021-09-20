package net.opisek.unteruns.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import net.opisek.unteruns.R;

public class VoteResultActivity extends AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voteresult);

        boolean correct = getIntent().getExtras().getBoolean("correct");
        String result = correct ? getResources().getString(R.string.label_vote_right) : getResources().getString(R.string.label_vote_wrong);
        ((TextView)findViewById(R.id.textview_result)).setText(result);

        if (correct) ((ImageView)findViewById(R.id.imageview_result)).setImageResource(R.drawable.endscreen_win);
        else ((ImageView)findViewById(R.id.imageview_result)).setImageResource(R.drawable.endscreen_lose);

        findViewById(R.id.button_tomenu).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                backToMenu();
            }
        });
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
