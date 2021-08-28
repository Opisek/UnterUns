package net.opisek.unteruns.views;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public abstract class RiddleActivity extends AppCompatActivity {
    public void riddleSolved() {
        Intent intent = new Intent(this, CompassActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        this.finish();
    }
}
