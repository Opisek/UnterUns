package net.opisek.unteruns.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import net.opisek.unteruns.R;

public class CheatMenuActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheatmenu);

        findViewById(R.id.button_cheat_continue).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                continueGame();
            }
        });
        findViewById(R.id.button_cheat_qr).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                qrInterpreter();
            }
        });
    }

    private void continueGame() {
        Intent intent = new Intent(this, RoutesActivity.class);
        intent.putExtra("state", RoutesActivity.GameState.CONTINUE);
        startActivity(intent);
    }
    private void qrInterpreter() {
        Intent intent = new Intent(this, QrActivity.class);
        intent.putExtra("type", QrActivity.QrType.TEST);
        startActivity(intent);
    }
}
