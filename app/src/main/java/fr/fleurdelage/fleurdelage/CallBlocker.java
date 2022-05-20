package fr.fleurdelage.fleurdelage;

import android.os.Build;
import android.telecom.Call;
import android.telecom.CallScreeningService;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CallBlocker extends CallScreeningService {

    @Override
    public void onScreenCall(@NonNull Call.Details details) {

    }
}
