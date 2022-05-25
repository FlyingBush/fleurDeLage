package fr.fleurdelage.fleurdelage;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.telecom.CallScreeningService;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class CallBlocker extends CallScreeningService {

    @Override
    public void onScreenCall(@NonNull Call.Details details) {

        CallResponse.Builder responseBuilder = new CallResponse.Builder();

        int direction = details.getCallDirection();
        //String contactDisplayName = details.getContactDisplayName();
        Uri handle = details.getHandle();

        switch (direction) {
            case Call.Details.DIRECTION_INCOMING:
                System.out.println("Direction Incoming");
                break;
            case Call.Details.DIRECTION_OUTGOING:
                System.out.println("Direction Outgoing");
                break;
            case Call.Details.DIRECTION_UNKNOWN:
                System.out.println("Direction Unknown");
                break;
        }
        if (handle != null && handle.getScheme().equals("tel")) {
            String number = handle.getSchemeSpecificPart();

            if(!hasContactName(number)){
                responseBuilder.setDisallowCall(true);
                responseBuilder.setSkipNotification(true);
            }

        }

        respondToCall(details, responseBuilder.build());
    }

    public boolean hasContactName(final String phoneNumber)
    {
        Context context = getApplicationContext();
        Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName="";
        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);

        if (cursor != null) {
            if(cursor.moveToFirst()) {
                contactName=cursor.getString(0);
            }
            cursor.close();
        }

        return !contactName.isEmpty();
    }
}
