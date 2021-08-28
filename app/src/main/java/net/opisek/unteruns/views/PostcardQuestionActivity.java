package net.opisek.unteruns.views;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import net.opisek.unteruns.R;
import net.opisek.unteruns.models.MorseModel;
import net.opisek.unteruns.repositories.MainRepository;
import net.opisek.unteruns.viewmodels.MorseQrViewModel;
import net.opisek.unteruns.viewmodels.PostcardQuestionViewModel;


public class PostcardQuestionActivity extends AppCompatActivity {
    private PostcardQuestionViewModel viewModel;

    private Button playButton;
    private TextView answerLabel;

    private boolean soundDisabled;
    private boolean playing;

    private MediaPlayer[] sounds;
    private int[] morseSequence;

    private Handler handler;

    private String currentInput;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcardquestion);
        viewModel = ViewModelProviders.of(this).get(PostcardQuestionViewModel.class);

        int questionIndex = getIntent().getExtras().getInt("question");
        viewModel.setQuestion(questionIndex);
        ((TextView)findViewById(R.id.label_postcardquestion_title)).setText("Frage " + (questionIndex+1));

        playing = false;
        sounds = new MediaPlayer[] {
                MediaPlayer.create(this, R.raw.morse_sound_short),
                MediaPlayer.create(this, R.raw.morse_sound_long),
                MediaPlayer.create(this, R.raw.morse_pause_short),
                MediaPlayer.create(this, R.raw.morse_pause_long)
        };

        morseSequence = viewModel.getMorseSequence();

        handler = new Handler();

        playButton = findViewById(R.id.button_postcardquestion_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchPlaystate(!playing);
            }
        });

        currentInput = "";
        answerLabel = findViewById(R.id.label_postcardquestion_answer);

        Button inputButton = findViewById(R.id.button_postcardquestion_input);
        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerInput(0);
            }
        });
        inputButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                registerInput(1);
                return true;
            }
        });

        findViewById(R.id.button_postcardquestion_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearInput();
            }
        });

        viewModel.getIsCorrect().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isCorrect) {
                if (isCorrect) answerCorrect();
            }
        });
    }

    public void answerCorrect() {

    }

    public void registerInput(int length) {
        if (currentInput.length() != 0) currentInput += ' ';
        currentInput +=  length == 0 ? '•' : '─';
        viewModel.registerInput(length);
        answerLabel.setText(currentInput);
    }

    public void clearInput() {
        currentInput = "";
        viewModel.clearInput();
        answerLabel.setText(R.string.label_postcardquestion_answer);
    }

    private void switchPlaystate(boolean desiredState) {
        if (desiredState == playing) return;
        playing = desiredState;
        if (playing) {
            playButton.setText(R.string.button_postcardquestion_stop);
            soundDisabled = false;
            final Context context = getApplicationContext();
            final Thread thread = new Thread(new Runnable() {
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            playMorse(0);
                        }
                    });
                }
            });
            thread.start();
        } else {
            playButton.setText(R.string.button_postcardquestion_play);
            soundDisabled = true;

        }
    }

    @Override
    protected void onPause() {
        switchPlaystate(false);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void playMorse(int i) {
        if (i >= morseSequence.length) switchPlaystate(false);
        else {
            MediaPlayer plr = sounds[morseSequence[i++]];
            final int newIndex = i;
            plr.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (!soundDisabled) playMorse(newIndex);
                }
            });
            plr.start();
        }
    }
}
