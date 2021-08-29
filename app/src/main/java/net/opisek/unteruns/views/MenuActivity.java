package net.opisek.unteruns.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import net.opisek.unteruns.R;
import net.opisek.unteruns.viewmodels.MenuViewModel;

public class MenuActivity extends AppCompatActivity {

    private MenuViewModel viewModel;
    private SharedPreferences prefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        viewModel = ViewModelProviders.of(this).get(MenuViewModel.class);

        /*if (viewModel.hasSavedProgress()) {
            findViewById(R.id.button_menu_continue).setVisibility(View.VISIBLE);
        }*/

        prefs = this.getSharedPreferences(getString(R.string.key_preferences), Context.MODE_PRIVATE);
        if (prefs.getBoolean("inProgress", false)) {
            findViewById(R.id.button_menu_continue).setVisibility(View.VISIBLE);
        }

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
        viewModel.setProgress(prefs.getInt("routeNumber", 0), prefs.getInt("routeProgress", -1));
        startActivity(new Intent(this, CompassActivity.class));
    }
    private void showCredits() {
        startActivity(new Intent(this, CreditsActivity.class));
    }

    private void cheatMenu() {
        Intent intent = new Intent(this, CheatMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
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
