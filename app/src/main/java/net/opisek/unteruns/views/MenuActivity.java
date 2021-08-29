package net.opisek.unteruns.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        findViewById(R.id.label_menu_title).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                cheatDialog();
            }
        });
    }

    private void startGame() {
        Intent intent = new Intent(this, RoutesActivity.class);
        intent.putExtra("state", RoutesActivity.GameState.NEW);
        startActivity(intent);
    }
    private void continueGame() {
        //Intent intent = new Intent(this, RoutesActivity.class);
        //intent.putExtra("state", RoutesActivity.GameState.CONTINUE);
        //startActivity(intent);
    }
    private void showCredits() {
        startActivity(new Intent(this, CreditsActivity.class));
    }

    private void cheatMenu() {
        startActivity(new Intent(this, CheatMenuActivity.class));
    }
    private void cheatDialog() {
        CheatDialog dialog = new CheatDialog();
        dialog.setCheatDialogListenerListener(new CheatDialog.CheatDialogListener() {
            @Override
            public void onUserResponse(Boolean result) {
                if (result) cheatMenu();
            }
        });
        dialog.show(getSupportFragmentManager(), "Cheat Dialog");
    }
}
