package net.opisek.unteruns.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import net.opisek.unteruns.R;
import net.opisek.unteruns.repositories.GpsRepository;
import net.opisek.unteruns.repositories.MainRepository;
import net.opisek.unteruns.viewmodels.ContinueQrViewModel;
import net.opisek.unteruns.viewmodels.MorseQrViewModel;
import net.opisek.unteruns.viewmodels.QrViewModel;
import net.opisek.unteruns.viewmodels.RightWrongQrViewModel;
import net.opisek.unteruns.viewmodels.RouteQrViewModel;
import net.opisek.unteruns.viewmodels.TestQrViewModel;

public class QrActivity extends AppCompatActivity {

    public enum QrType {
        ROUTE,
        CONTINUE,
        TEST,
        MORSE,
        RIGHTWRONG
    }
    private QrType myQrType;

    private QrViewModel viewModel;
    private Handler handler;

    private TextView wrongLabel;
    private boolean animationInProgress;

    private CodeScanner scanner;
    private CodeScannerView scannerView;

    private boolean camDisabled;
    private boolean soundDisabled;
    private boolean hasParent;

    private MediaPlayer[] sounds;

    // https://github.com/yuriy-budiyev/code-scanner

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        handler = new Handler();

        myQrType = (QrType)getIntent().getExtras().getSerializable("type");

        hasParent = false;
        soundDisabled = false;

        wrongLabel = findViewById(R.id.label_qr_wrong);

        // qr scanner
        scannerView = findViewById(R.id.scanner_qr);
        scanner = new CodeScanner(this, scannerView);
        scanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Log.v("cam1", ((RouteQrViewModel)viewModel).myStop.location.name);
                        if (!camDisabled) {
                            //Log.v("cam2", ((RouteQrViewModel)viewModel).myStop.location.name);
                            viewModel.interpretUUID(result.getText());
                            scanner.startPreview();
                        }
                    }
                });
            }
        });

        checkCamPerms();
    }

    private void checkCamPerms() {
        final String perms = Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(getApplicationContext(), perms) == PackageManager.PERMISSION_GRANTED) {
            if (myQrType.equals(QrType.ROUTE)) checkLocPerms();
            else permsPassed();
        } else {
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    checkCamPerms();
                }
            }).launch(perms);
        }
    }
    private void checkLocPerms() {
        final String perms = Manifest.permission.ACCESS_FINE_LOCATION;
        if (ContextCompat.checkSelfPermission(getApplicationContext(), perms) == PackageManager.PERMISSION_GRANTED) {
            permsPassed();
        } else {
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    checkLocPerms();
                }
            }).launch(perms);
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void permsPassed() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, GpsRepository.getInstance());
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, GpsRepository.getInstance());

        TextView title = findViewById(R.id.text_qr_title);
        switch(myQrType) {
            case ROUTE:
                viewModel = ViewModelProviders.of(this).get(RouteQrViewModel.class);
                title.setText(getResources().getString(R.string.title_qr_route).replace("%0%",((RouteQrViewModel)viewModel).getStationName()));
                observerRiddle(((RouteQrViewModel)viewModel).getRiddle());
                ((RouteQrViewModel)viewModel).getLostWaypoint().observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isLost) {
                        if (isLost) lostWaypoint();
                    }
                });
                break;
            case CONTINUE:
                title.setText(R.string.title_qr_continue);
                viewModel = ViewModelProviders.of(this).get(ContinueQrViewModel.class);
                observerRiddle(((ContinueQrViewModel)viewModel).getRiddle());
                break;
            case TEST:
                title.setText(R.string.title_qr_test);
                viewModel = ViewModelProviders.of(this).get(TestQrViewModel.class);
                ((TestQrViewModel)viewModel).getToast().observe(this, new Observer<Pair<String, Long>>() {
                    @Override
                    public void onChanged(Pair<String, Long> toast) {
                        makeToast(toast.first);
                    }
                });
                hasParent = true;
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case MORSE:
                title.setText(R.string.title_qr_morse);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                setVolumeControlStream(AudioManager.STREAM_MUSIC);
                viewModel = ViewModelProviders.of(this).get(MorseQrViewModel.class);
                hasParent = true;
                sounds = new MediaPlayer[] {
                        MediaPlayer.create(this, R.raw.morse_sound_short),
                        MediaPlayer.create(this, R.raw.morse_sound_long),
                        MediaPlayer.create(this, R.raw.morse_pause_short),
                        MediaPlayer.create(this, R.raw.morse_pause_long)
                };
                final Context context = getApplicationContext();
                ((MorseQrViewModel)viewModel).getMorseSequence().observe(this, new Observer<Pair<int[], Integer>>() {
                    @Override
                    public void onChanged(final Pair<int[], Integer> morseSequence) {
                        Log.v("QrActivity", "change");
                        final Thread thread = new Thread(new Runnable() {
                            public void run() {
                                handler.post(new Runnable() {
                                    public void run() {
                                        playMorse(morseSequence.first, 0);
                                    }
                                });
                            }
                        });
                        thread.start();
                    }
                });
                break;
            case RIGHTWRONG:
                title.setText(R.string.title_qr_rightwrong);
                viewModel = ViewModelProviders.of(this).get(RightWrongQrViewModel.class);
                break;
        }

        animationInProgress = false;
        viewModel.getCorrectQr().observe(this, new Observer<Pair<Boolean, Integer>>() {
            @Override
            public void onChanged(Pair<Boolean, Integer> correct) {
                if (correct.second == 0) return;
                if (!correct.first) animateWrongIn();
                else {
                    if (myQrType.equals(QrType.RIGHTWRONG)) riddleSolved();
                }
            }
        });

        camDisabled = false;
        scanner.startPreview();
    }

    private void riddleSolved() {
        Intent intent = new Intent(this, CompassActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        this.finish();
    }

    private void animateWrongIn() {
        if (animationInProgress) return;
        animationInProgress = true;
        wrongLabel.setAlpha(0f);
        wrongLabel.animate()
                .alpha(1f)
                .setDuration(200);
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateWrongOut();
            }
        }, 1200);
    }

    private void animateWrongOut() {
        wrongLabel.setAlpha(1f);
        wrongLabel.animate()
                .alpha(0f)
                .setDuration(200);
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animeWrongDone();
            }
        }, 200);
    }

    private void animeWrongDone() {
        animationInProgress = false;
    }

    private void playMorse(final int[] seq, int i) {
        if (i >= seq.length) ((MorseQrViewModel)viewModel).playbackFinished();
        else {
            MediaPlayer plr = sounds[seq[i++]];
            final int newIndex = i;
            plr.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (!soundDisabled) playMorse(seq, newIndex);
                }
            });
            plr.start();
        }
    }

    @Override
    protected void onPause() {
        //Log.v("qr", "stop3");
        camDisabled = true;
        soundDisabled = true;
        scanner.releaseResources();
        super.onPause();
    }

    @Override
    protected void onResume() {
        //Log.v("qr", "resume");
        camDisabled = false;
        soundDisabled = false;
        scanner.startPreview();
        super.onResume();
    }

    @Override
    protected void onStop() {
        if (sounds != null) {
            for (MediaPlayer sound : sounds) {
                if (sound != null) {
                    if (sound.isPlaying()) sound.stop();
                    sound.reset();
                    sound.release();
                }
            }
        }
        super.onStop();
    }

    private void observerRiddle(MutableLiveData<MainRepository.Riddle> mutableLiveData) {
        mutableLiveData.observe(this, new Observer<MainRepository.Riddle>() {
            @Override
            public void onChanged(MainRepository.Riddle riddle) {
                if (riddle != null) handleRiddle(riddle);
            }
        });
    }
    private void handleRiddle(MainRepository.Riddle riddle) {
        camDisabled = true;
        scanner.stopPreview();
        scanner.releaseResources();
        Intent intent;
        switch(riddle) {
            case POSTCARDS:
                intent = new Intent(this, PostcardsActivity.class);
                break;
            case CROSSWORD:
                intent = new Intent(this, InputQuestionActivity.class);
                intent.putExtra("id", MainRepository.inputQuestionID.CROSSWORD);
                break;
            case NAVIGATION:
                intent = new Intent(this, InputQuestionActivity.class);
                intent.putExtra("id", MainRepository.inputQuestionID.NAVIGATION);
                break;
            case RIGHTWRONG:
                intent = new Intent(this, QrActivity.class);
                intent.putExtra("type", QrType.RIGHTWRONG);
                break;
            case FINAL:
                intent = new Intent(this, VoteRiddleActivity.class);
                break;
            default:
                intent = new Intent(this, CompassActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        }
        startActivity(intent);
        this.finish();
    }

    private void makeToast(String toast) {
        Toast toastt = Toast.makeText(this, toast, Toast.LENGTH_LONG);
        toastt.setGravity(Gravity.BOTTOM,0,40);
        toastt.show();
    }

    private void lostWaypoint() {
        camDisabled = true;
        scanner.stopPreview();
        scanner.releaseResources();
        Intent intent = new Intent(this, CompassActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            soundDisabled = true;
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            if (hasParent) {
                this.finish();
                return true;
            } else {
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
        }
        return super.onKeyDown(keyCode, event);
    }
}
