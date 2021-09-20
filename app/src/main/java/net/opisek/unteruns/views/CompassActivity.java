package net.opisek.unteruns.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import net.opisek.unteruns.R;
import net.opisek.unteruns.repositories.GpsRepository;
import net.opisek.unteruns.viewmodels.CompassViewModel;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {

    private CompassViewModel viewModel;
    private ImageView compass;
    private ImageView needle;

    private float bearing;

    private SensorManager sensorManager;
    private float desiredRotation;
    private float currentRotation;
    private float desiredNeedleRotation;
    private float currentNeedleRotation;

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
            desiredRotation = desiredRotation % 360f;

            Animation anim = new RotateAnimation(-currentRotation,-desiredRotation, Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF,.5f);
            currentRotation = desiredRotation;

            anim.setDuration(500);
            anim.setRepeatCount(0);
            anim.setFillAfter(true);

            compass.startAnimation(anim);
        }
    }

    private void setNeedleRotation() {
        desiredNeedleRotation = currentRotation + (bearing * -1f);
        desiredNeedleRotation = desiredNeedleRotation % 360f;

        Animation anim = new RotateAnimation(-currentNeedleRotation,-desiredNeedleRotation, Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF,.5f);
        currentNeedleRotation = desiredNeedleRotation;

        anim.setDuration(500);
        anim.setRepeatCount(0);
        anim.setFillAfter(true);

        needle.startAnimation(anim);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        desiredRotation = 0f;
        currentRotation = 0f;
        desiredNeedleRotation = 0f;
        currentNeedleRotation = 0f;

        compass = findViewById(R.id.compass_image);
        needle = findViewById(R.id.needle_image);

        sensorManager = (SensorManager)getApplicationContext().getSystemService(SENSOR_SERVICE);

        checkLocPerms();
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

    private float distanceWaypoint;

    @SuppressWarnings({"MissingPermission"})
    private void permsPassed() {
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, GpsRepository.getInstance());
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, GpsRepository.getInstance());

        viewModel = ViewModelProviders.of(this).get(CompassViewModel.class);

        bearing = 0f;
        viewModel.getBearing().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float rotation) {
                bearing = rotation;
                setNeedleRotation();
            }
        });
        viewModel.getDistanceWaypoint().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float distance) {
                distanceWaypoint = distance;
            }
        });
        viewModel.getDistanceStop().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float distance) {
                String text = Math.round(distance) + " Meter";
                if (distance != distanceWaypoint) text = Math.round(distanceWaypoint) + " Meter | " + text;
                ((TextView)findViewById(R.id.label_compass_distance)).setText(text);
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
        viewModel.getProgress().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer progress) {
                saveProgress(progress);
            }
        });
    }

    private void stopReached() {
        Intent intent = new Intent(this, QrActivity.class);
        intent.putExtra("type", QrActivity.QrType.ROUTE);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        this.finish();
    }

    private void saveProgress(int progress) {
        Log.v("test", "progress saved: " + progress);
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.key_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("routeProgress", progress);
        editor.apply();
    }

    @Override
    protected void onResume() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
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
                setNeedleRotation();
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                geomagnetic[0] = alpha * geomagnetic[0] + (1-alpha)*event.values[0];
                geomagnetic[1] = alpha * geomagnetic[1] + (1-alpha)*event.values[1];
                geomagnetic[2] = alpha * geomagnetic[2] + (1-alpha)*event.values[2];
                setImageRotation();
                setNeedleRotation();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

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