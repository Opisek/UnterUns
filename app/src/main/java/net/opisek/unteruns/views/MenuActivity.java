package net.opisek.unteruns.views;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import net.opisek.unteruns.R;
import net.opisek.unteruns.viewmodels.MenuViewModel;

public class MenuActivity extends AppCompatActivity {
    private MenuViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        viewModel = ViewModelProviders.of(this).get(MenuViewModel.class);

        findViewById(R.id.button_menu_start).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                viewModel.pickRoute();
            }
        });

        findViewById(R.id.button_menu_continue).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                viewModel.continueRoute();
            }
        });

        findViewById(R.id.button_menu_credits).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                viewModel.showCredits();
            }
        });
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
