package net.opisek.unteruns.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import net.opisek.unteruns.R;
import net.opisek.unteruns.viewmodels.RoutesViewModel;

import java.util.ArrayList;

public class RoutesActivity extends AppCompatActivity {

    public enum GameState {
        NEW,
        CONTINUE
    }

    private GameState myState;

    private RoutesViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        viewModel = ViewModelProviders.of(this).get(RoutesViewModel.class);

        myState = (GameState)getIntent().getExtras().getSerializable("state");

        ArrayList<Pair<String, String>> routes = viewModel.getRoutes();

        LinearLayout buttonsList = findViewById(R.id.linear_routes_buttons);
        for (int i = 0; i < routes.size(); i++) {
            Pair<String, String> route = routes.get(i);

            Button button = new Button(this);
            button.setText(route.first);
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

            final int buttonIndex = i;
            button.setOnClickListener(new View.OnClickListener(){
                @Override public void onClick(View v) {
                    viewModel.pickRoute(buttonIndex);
                }
            });

            buttonsList.addView(button);
        }

        viewModel.getPicked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean picked) {
                if (picked) onPicked();
            }
        });
    }

    private void onPicked() {
        Intent intent;
        if (myState == GameState.NEW) {
            intent = new Intent(this, CompassActivity.class);
        } else {
            intent = new Intent(this, QrActivity.class);
            intent.putExtra("type", QrActivity.QrType.CONTINUE);
        }

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.key_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("inProgress", true);
        editor.putInt("routeNumber", viewModel.getRouterNumber());
        editor.apply();

        startActivity(intent);
    }

    private float dpToPx(float dp) {
        return dp * getApplicationContext().getResources().getDisplayMetrics().density;
    }
}
