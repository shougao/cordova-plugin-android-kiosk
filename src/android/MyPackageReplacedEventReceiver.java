package com.example.template.kiosk.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyPackageReplacedEventReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            System.out.println("Kiosk application start after boot");
            Intent newIntent = new Intent(context, KioskActivity.class);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newIntent);
        } else if(intent.getAction().equals("android.intent.action.MY_PACKAGE_REPLACED")){
            System.out.println("Kiosk application restarting after upgrade");
            Intent newIntent = new Intent(context, KioskActivity.class);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newIntent);
        }
    }
}
