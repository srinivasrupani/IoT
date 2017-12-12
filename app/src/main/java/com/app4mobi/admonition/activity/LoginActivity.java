package com.app4mobi.admonition.activity;
/**
 * Created by Srinivas Rupani on 10/14/2016.
 */

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.app4mobi.admonition.AdmonitionApplication;
import com.app4mobi.admonition.R;

public class LoginActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private boolean color = false, isReadC = false, isWriteC = false;
    private View view;
    private long lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        view = findViewById(R.id.sensorText);
        view.setBackgroundColor(Color.GREEN);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();

        findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AdmonitionApplication.isManifestPermissionGranted(LoginActivity.this, Manifest.permission
                    .READ_CALENDAR, 1) && !AdmonitionApplication.isManifestPermissionGranted(LoginActivity.this, Manifest
                    .permission.WRITE_CALENDAR, 2)) {
                    return;
                } else {
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }
            }
        });

        /*findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword("srinivasony@gmail.com", "9949272772")
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(Task task) {
                            if (task.isSuccessful()) {
                                //FirebaseUser user = task.getResult().getUser();
                                //String email = user.getEmail();
                                // ...
                            }
                        }
                    });
            }
        });*/

        findViewById(R.id.mqttButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
            / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = event.timestamp;
        if (accelationSquareRoot >= 2) //
        {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            Toast.makeText(this, "Device was shuffled", Toast.LENGTH_SHORT).show();
            if (color) {
                view.setBackgroundColor(Color.GREEN);
            } else {
                view.setBackgroundColor(Color.RED);
            }
            color = !color;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //previewImage();
                isReadC = true;
            } else if (resultCode == RESULT_CANCELED) {
                isReadC = false;
                //CommonUtil.getInstance().displayToast(getApplicationContext(), "User cancelled image capture");
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                isWriteC = true;
                //previewImage();
            } else if (resultCode == RESULT_CANCELED) {
                isWriteC = false;
                //CommonUtil.getInstance().displayToast(getApplicationContext(), "User cancelled image capture");
            }
        }
        if (isReadC == true && isWriteC == true) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }
    }
}
