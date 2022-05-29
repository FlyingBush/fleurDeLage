package fr.fleurdelage.fleurdelage;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.role.RoleManager;
import android.content.Intent;
import android.content.UriMatcher;
import android.os.Build;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

import java.lang.reflect.Array;
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

    ActivityResultLauncher<String> permRequest = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    // Your app is now the call screening app
                } else {
                    // Your app is not the call screening app
                }
            });

    public void requestRole() {
        RoleManager roleManager = (RoleManager) getSystemService(ROLE_SERVICE);
        if (roleManager.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING)) {
            Intent intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING);
            roleRequest.launch(intent);
        }
    }

    private void requestPerms() {
        permRequest.launch("android.permission.READ_CONTACTS");
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
        Button internet = (Button) findViewById(R.id.Internet);
        internet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_APP_BROWSER);
                startActivity(i);
            }
        });
        Button gallery=(Button) findViewById(R.id.Photos);
        gallery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_APP_GALLERY);
                startActivity(i);
            }
        });

    }
}