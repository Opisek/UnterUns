package net.opisek.unteruns.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import net.opisek.unteruns.R;
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

    private int pendingRequest;

    // https://github.com/yuriy-budiyev/code-scanner

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        myQrType = (QrType)getIntent().getExtras().getSerializable("type");

        TextView title = findViewById(R.id.text_qr_title);
        switch(myQrType) {
            case ROUTE:
                title.setText(R.string.title_activity_qr_route);
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
                title.setText(R.string.title_activity_qr_continue);
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

        // qr scanner
        scannerView = findViewById(R.id.scanner_view);
        scanner = new CodeScanner(this, scannerView);
        scanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewModel.interpretUUID(result.getText());
                        scanner.startPreview();
                    }
                });
            }
        });
        checkCameraPerms();
    }

    private void checkCameraPerms() {
        String perms = Manifest.permission.CAMERA;
        int val = getApplicationContext().checkCallingOrSelfPermission(perms);
        if (val == PackageManager.PERMISSION_GRANTED) {
            scanner.startPreview();
        } else {
            pendingRequest = (int)(System.currentTimeMillis()/1000);
            ActivityCompat.requestPermissions(QrActivity.this, new String[] {perms}, pendingRequest);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != pendingRequest) return;
        checkCameraPerms();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
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
        switch(riddle) {
            case POSTCARDS:
                break;
            default:
                startActivity(new Intent(this, CompassActivity.class));
        }
    }

    private void makeToast(String toast) {
        Toast toastt = Toast.makeText(this, toast, Toast.LENGTH_LONG);
        toastt.setGravity(Gravity.BOTTOM,0,40);
        toastt.show();
    }

    private void lostWaypoint() {
        startActivity(new Intent(this, CompassActivity.class));
    }
}
