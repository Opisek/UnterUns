package net.opisek.unteruns.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import net.opisek.unteruns.R;
import net.opisek.unteruns.repositories.GpsRepository;
import net.opisek.unteruns.viewmodels.CompassViewModel;

import static androidx.core.content.ContextCompat.startActivity;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {

    private CompassViewModel viewModel;
    private ImageView compass;

    private float bearing;

    private SensorManager sensorManager;
    private float desiredRotation;
    private float currentRotation;

    private float[] gravity = new float[3];
    private float[] geomagnetic = new float[3];

    private void setImageRotation() {
        float R[] = new float[9];
        float I[] = new float[9];
        boolean success = SensorManager.getRotationMatrix(R,I,gravity,geomagnetic);
        if (success) {
            float orientation[] = new float[3];
            SensorManager.getOrientation(R, orientation);
            desiredRotation = (float)Math.toDegrees(orientation[0]);
            desiredRotation = (desiredRotation+360)%360;

            Animation anim = new RotateAnimation(-currentRotation,-desiredRotation, Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF,.5f);
            currentRotation = desiredRotation;

            anim.setDuration(500);
            anim.setRepeatCount(0);
            anim.setFillAfter(true);

            compass.startAnimation(anim);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        desiredRotation = 0f;
        currentRotation = 0f;

        compass = findViewById(R.id.compass_image);

        sensorManager = (SensorManager)getApplicationContext().getSystemService(SENSOR_SERVICE);

        checkLocationPerms();
    }

    int pendingRequest;

    private void checkLocationPerms() {
        String perms = Manifest.permission.ACCESS_FINE_LOCATION;
        int val = getApplicationContext().checkCallingOrSelfPermission(perms);
        if (val == PackageManager.PERMISSION_GRANTED) {
            startGps();
        } else {
            pendingRequest = (int)(System.currentTimeMillis()/1000);
            ActivityCompat.requestPermissions(CompassActivity.this, new String[] {perms}, pendingRequest);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != pendingRequest) return;
        checkLocationPerms();
    }

    private void startGps() {
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, GpsRepository.getInstance());
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, GpsRepository.getInstance());
        }

        viewModel = ViewModelProviders.of(this).get(CompassViewModel.class);

        bearing = 0f;
        viewModel.getBearing().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float rotation) {
                bearing = rotation;
            }
        });
        viewModel.getDistanceWaypoint().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float distance) {
                ((TextView)findViewById(R.id.label_compass_distance)).setText(Math.round(distance) + " Meter");
            }
        });
        viewModel.getNameStop().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String name) {
                ((TextView)findViewById(R.id.label_compass_stopname)).setText(name);
            }
        });
        viewModel.getStopReached().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean reached) {
                if (reached) stopReached();
            }
        });
    }

    private void stopReached() {
        Intent intent = new Intent(this, QrActivity.class);
        intent.putExtra("type", QrActivity.QrType.ROUTE);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = .97f;
        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                gravity[0] = alpha * gravity[0] + (1-alpha)*event.values[0];
                gravity[1] = alpha * gravity[1] + (1-alpha)*event.values[1];
                gravity[2] = alpha * gravity[2] + (1-alpha)*event.values[2];
                setImageRotation();
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                geomagnetic[0] = alpha * geomagnetic[0] + (1-alpha)*event.values[0];
                geomagnetic[1] = alpha * geomagnetic[1] + (1-alpha)*event.values[1];
                geomagnetic[2] = alpha * geomagnetic[2] + (1-alpha)*event.values[2];
                setImageRotation();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

}