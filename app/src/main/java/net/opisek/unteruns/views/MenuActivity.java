package net.opisek.unteruns.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import net.opisek.unteruns.R;

public class MenuActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViewById(R.id.button_menu_start).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                startGame();
            }
        });

        findViewById(R.id.button_menu_continue).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                continueGame();
            }
        });

        findViewById(R.id.button_menu_credits).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                showCredits();
            }
        });
    }

    private void startGame() {
        Intent intent = new Intent(this, RoutesActivity.class);
        intent.putExtra("state", RoutesActivity.GameState.NEW);
        startActivity(intent);
    }
    private void continueGame() {
        Intent intent = new Intent(this, RoutesActivity.class);
        intent.putExtra("state", RoutesActivity.GameState.CONTINUE);
        startActivity(intent);
    }
    private void showCredits() {

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
