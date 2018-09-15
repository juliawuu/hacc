package com.gm.hellovehicle;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gm.android.vehicle.signals.Permissions;
import com.gm.android.vehicle.signals.config.Config;
import com.gm.android.vehicle.signals.motion.Motion;

public class MainActivity extends Activity implements MainActivityPresenter.View,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private TextView mVin;
    private TextView mSpeed;
    private ProgressBar mRotaryProgress;

    private MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVin = (TextView) findViewById(R.id.vin);
        mSpeed = (TextView) findViewById(R.id.speed);
        mRotaryProgress = (ProgressBar) findViewById(R.id.rotaryProgress);

        presenter = new MainActivityPresenter(this, getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return presenter.didHandleEvent(event) || super.dispatchKeyEvent(event);
    }

    @Override
    public void requestPermissions(int requestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{Permissions.getPermission(Config.VIN),
                        Permissions.getPermission(Motion.SPEED)}, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // Callbacks from MainActivityPresenter
    @Override
    public void updateDriveMode(String state) {
        Toast.makeText(this, String.format("Current driving mode is: %s", state), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateVin(String vin) {
        mVin.setText(vin);
    }

    @Override
    public void updateSpeed(final String speed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSpeed.setText(speed);
            }
        });
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void rotaryClockwise() {
        mRotaryProgress.incrementProgressBy(5);
    }

    @Override
    public void rotaryCounterClockwise() {
        mRotaryProgress.incrementProgressBy(-5);
    }
}