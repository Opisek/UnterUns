package net.opisek.unteruns.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.KeyEvent;
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
import net.opisek.unteruns.viewmodels.QrViewModel;
import net.opisek.unteruns.viewmodels.RouteQrViewModel;
import net.opisek.unteruns.viewmodels.TestQrViewModel;

public class QrActivity extends AppCompatActivity {

    public enum QrType {
        ROUTE,
        CONTINUE,
        TEST,
        POSTCARDS
    }
    private QrType myQrType;

    private QrViewModel viewModel;

    private CodeScanner scanner;
    private CodeScannerView scannerView;

    private boolean camDisabled;

    // https://github.com/yuriy-budiyev/code-scanner

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        myQrType = (QrType)getIntent().getExtras().getSerializable("type");

        // qr scanner
        scannerView = findViewById(R.id.scanner_view);
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
                title.setText(R.string.title_qr_route);
                viewModel = ViewModelProviders.of(this).get(RouteQrViewModel.class);
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
                title.setText("QR Tester");
                viewModel = ViewModelProviders.of(this).get(TestQrViewModel.class);
                ((TestQrViewModel)viewModel).getToast().observe(this, new Observer<Pair<String, Long>>() {
                    @Override
                    public void onChanged(Pair<String, Long> toast) {
                        makeToast(toast.first);
                    }
                });
                break;
        }

        camDisabled = false;
        scanner.startPreview();
    }

    @Override
    protected void onPause() {
        //Log.v("qr", "stop3");
        camDisabled = true;
        scanner.releaseResources();
        super.onPause();
    }

    @Override
    protected void onResume() {
        //Log.v("qr", "resume");
        camDisabled = false;
        //scanner.startPreview();
        super.onResume();
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
        //Log.v("qr", "stop2");
        camDisabled = true;
        scanner.stopPreview();
        scanner.releaseResources();
        //Log.v("qr", "BIN ANGEKOMMEN!");
        switch(riddle) {
            case POSTCARDS:
                break;
            default:
                Intent intent = new Intent(this, CompassActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
        }
        this.finish();
    }

    private void makeToast(String toast) {
        Toast toastt = Toast.makeText(this, toast, Toast.LENGTH_LONG);
        toastt.setGravity(Gravity.BOTTOM,0,40);
        toastt.show();
    }

    private void lostWaypoint() {
        //Log.v("qr", "stop1");
        camDisabled = true;
        scanner.stopPreview();
        scanner.releaseResources();
        Intent intent = new Intent(this, CompassActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        this.finish();
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
