package com.ctel_rtc.secretnotifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartMyActivityAtBootReceiver extends BroadcastReceiver {
 
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, NotificationService.class);
            context.startService(serviceIntent);
        }
    }
}