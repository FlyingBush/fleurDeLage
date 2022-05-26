package fr.fleurdelage.fleurdelage;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.telephony.SmsManager;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


public class Accelerometer extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;


    @Override
    public void onSensorChanged(@NonNull SensorEvent event){
        float x=event.values[0];
        float y=event.values[1];
        float z=event.values[2];
        float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

        if(acceleration >20f){
            /*Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_APP_GALLERY);
            startActivity(i);
            Intent intent = getApplicationContext().getPackageManager().getLaunchIntentForPackage(
                    getApplicationContext().getPackageName());
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
            msg("0666003183");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onCreate(){
        super.onCreate();
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onCreate();
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        final String CHANNELID = "Foreground";
        NotificationChannel channel =new NotificationChannel(CHANNELID,CHANNELID, NotificationManager.IMPORTANCE_LOW);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification= new Notification.Builder(this,CHANNELID)
                .setContentText("Fall Detection Operational")
                .setContentTitle("Fleur de l'age");
        startForeground(1001, notification.build());
        return START_STICKY;
    }

    public void onDestroy(){

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void msg(String number){
        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, "send help", null, null);
    }
}
