package com.example.template.kiosk.plugin;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;


public class MyAdmin extends DeviceAdminReceiver{

    /**
     * @param context The context of the application.
     * @return The component name of this component in the given context.
     */
    public static ComponentName getComponentName(Context context) {
        return new ComponentName(context.getApplicationContext(), MyAdmin.class);
    }

}
