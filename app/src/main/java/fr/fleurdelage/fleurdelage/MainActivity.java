package fr.fleurdelage.fleurdelage;

import androidx.appcompat.app.AppCompatActivity;
import java.lang.Math;

import android.graphics.Color;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Accelerometer accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accelerometer = new Accelerometer(this);

        accelerometer.setListener(new Accelerometer.Listener(){
            @Override
            public void onTranslation(float tx, float ty, float tz) {
                float SVt;
                SVt= (float) Math.sqrt(Math.pow(tx,2)+Math.pow(ty,2)+Math.pow(tz,2));
                if (SVt>8f)
                    getWindow().getDecorView().setBackgroundColor(Color.RED);

                else {
                getWindow().getDecorView().setBackgroundColor(Color.BLUE);

            }    TextView textView = findViewById(R.id.Acceleration);
                textView.setText(String.valueOf(SVt));}


            });}
    // create on resume method
    @Override
    protected void onResume() {
        super.onResume();

        // this will send notification to
        // both the sensors to register
        accelerometer.register();
    }

    // create on pause method
    @Override
    protected void onPause() {
        super.onPause();

        // this will send notification in
        // both the sensors to unregister
        accelerometer.unregister();
    }
}



