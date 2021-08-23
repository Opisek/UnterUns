package net.opisek.unteruns.views;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import net.opisek.unteruns.R;
import net.opisek.unteruns.models.QrModel;
import net.opisek.unteruns.models.RouteQrModel;
import net.opisek.unteruns.repositories.MainRepository;

public class QrActivity extends AppCompatActivity {

    private CodeScanner scanner;
    private CodeScannerView scannerView;

    private int pendingRequest;

    // https://github.com/yuriy-budiyev/code-scanner

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        // qr scanner
        scannerView = findViewById(R.id.scanner_view);
        scanner = new CodeScanner(this, scannerView);
        scanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(QrActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        QrModel res = MainRepository.getInstance().getQrCode(result.getText());
                        if (res != null && res instanceof RouteQrModel) {
                            Toast.makeText(QrActivity.this, ((RouteQrModel)res).location.name, Toast.LENGTH_SHORT).show();
                        }
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
}
