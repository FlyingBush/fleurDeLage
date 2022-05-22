package fr.fleurdelage.fleurdelage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button internet = (Button) findViewById(R.id.Internet);
        internet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_MAIN);
                PackageManager web = getPackageManager();
                i=web.getLaunchIntentForPackage("com.google.android.gallery3d");
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(i);
            }

        });
    }
}