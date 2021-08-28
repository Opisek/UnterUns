package net.opisek.unteruns.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import net.opisek.unteruns.R;
import net.opisek.unteruns.viewmodels.PostcardsViewModel;

public class PostcardsActivity extends RiddleActivity {
    private PostcardsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcards);
        viewModel = ViewModelProviders.of(this).get(PostcardsViewModel.class);

        findViewById(R.id.button_postcards_scan).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                scan();
            }
        });
        findViewById(R.id.button_postcards_questions).setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                questions();
            }
        });

        MutableLiveData<Boolean> allDone = viewModel.getAllDone();
        if (allDone.getValue()) {
            riddleSolved();
        } else {
            allDone.observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean done) {
                    if (done) riddleSolved();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        viewModel.checkAllDone();
        super.onResume();
    }

    private void scan() {
        Intent intent = new Intent(this, QrActivity.class);
        intent.putExtra("type", QrActivity.QrType.MORSE);
        startActivity(intent);
    }
    private void questions() {
        startActivity(new Intent(this, PostcardQuestionsActivity.class));
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
