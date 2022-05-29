package fr.fleurdelage.fleurdelage;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import fr.fleurdelage.fleurdelage.tellowsdb.PhoneNumber;
import fr.fleurdelage.fleurdelage.tellowsdb.PhoneNumberDao;
import fr.fleurdelage.fleurdelage.tellowsdb.PhoneNumberRoomDataBase;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class MainActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> roleRequest = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            activityResult -> {
                if (activityResult.getResultCode() == Activity.RESULT_OK) {
                    // Your app is now the call screening app
                } else {
                    // Your app is not the call screening app
                }
            });

    ActivityResultLauncher<String[]> permRequest = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            stringBooleanMap -> stringBooleanMap.forEach((perm, isGranted) -> {
                if (isGranted) {
                    // Your app is now the call screening app
                } else {
                    // Your app is not the call screening app
                }
            }));

    public void requestRole() {
        RoleManager roleManager = (RoleManager) getSystemService(ROLE_SERVICE);
        if (roleManager.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING)) {
            Intent intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING);
            roleRequest.launch(intent);
        }
    }

    private void requestPerms() {
        ArrayList<String> perms = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.READ_CONTACTS") == PackageManager.PERMISSION_DENIED) {
            perms.add("android.permission.READ_CONTACTS");
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.READ_CONTACTS") == PackageManager.PERMISSION_DENIED) {
            perms.add("android.permission.SEND_SMS");
        }
        String[] permsArray = perms.toArray(new String[0]);
        permRequest.launch(permsArray);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestRole();
        requestPerms();
        PhoneNumberRoomDataBase dataBase = PhoneNumberRoomDataBase.getDatabase(getApplicationContext());
        PhoneNumberDao dao = dataBase.getPhoneNumberDao();
        Executor executor = dataBase.getQueryExecutor();
        executor.execute(() -> {
            List<PhoneNumber> list = dao.getAll();
            System.out.println(list);
        });
        Button internet = findViewById(R.id.Internet);
        internet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_APP_BROWSER);
                startActivity(i);
            }
        });
        Button gallery= findViewById(R.id.Photos);
        gallery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_APP_GALLERY);
                startActivity(i);
            }
        });

        if (!ForegroundRunning()){
            startForegroundService(new Intent( this, Accelerometer.class ) );
        }
    }
    public boolean ForegroundRunning(){
        ActivityManager activityManager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service:activityManager.getRunningServices(Integer.MAX_VALUE)
             ) {
            if (Accelerometer.class.getName().equals(service.service.getClassName())) {
            return true;}
        }
        return false;
    }
}