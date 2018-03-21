package com.example.template.kiosk.plugin;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Kiosk extends CordovaPlugin {

    private DevicePolicyManager mDpm;
    private boolean mIsKioskEnabled;
    private CallbackContext mCallbackContext;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        mCallbackContext = callbackContext;
        if (action.equals("lockLauncher")) {
            Boolean locked = args.getBoolean(0);
            lockLauncher(locked);
            return true;
        } else if (action.equals("isLocked")) {
            callbackContext.success(String.valueOf(mIsKioskEnabled));
            return true;
        } else if (action.equals("switchLauncher")) {
            switchLauncher(callbackContext);
            return true;
        } else if (action.equals("deleteDeviceAdmin")){
            deleteDeviceAdmin();
        }
        return false;
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        lockLauncher(true);
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideSystemUI();
            }
        });
    }

    private void hideSystemUI() {
        View mDecorView = cordova.getActivity().getWindow().getDecorView();
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void switchLauncher(CallbackContext callbackContext) {
        if (mIsKioskEnabled) {
            lockLauncher(false);
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent chooser = Intent.createChooser(intent, "Select destination...");
        if (intent.resolveActivity(cordova.getActivity().getPackageManager()) != null) {
            cordova.getActivity().startActivity(chooser);
        }
        callbackContext.success();
    }

    private void lockLauncher(boolean locked) {
        if (locked && !mIsKioskEnabled) {
            mDpm = (DevicePolicyManager) cordova.getActivity().getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName deviceAdmin = new ComponentName(cordova.getActivity(), MyAdmin.class);
            if (!mDpm.isAdminActive(deviceAdmin)) {
                callbackMessage(false, "not admin active");
            }
            if (mDpm.isDeviceOwnerApp(cordova.getActivity().getPackageName())) {
                mDpm.setLockTaskPackages(deviceAdmin, new String[]{cordova.getActivity().getPackageName()});
            } else {
                callbackMessage(false, "not device owner app");
            }
            enableKioskMode(true);
        } else if(!locked){
            enableKioskMode(false);
        }
    }

    private void enableKioskMode(boolean enabled) {
        try {
            if (enabled) {
                if (mDpm.isLockTaskPermitted(cordova.getActivity().getPackageName())) {
                    cordova.getActivity().startLockTask();
                    mIsKioskEnabled = true;
                } else {
                    callbackMessage(false, "no permission to lock");
                }
            } else {
                cordova.getActivity().stopLockTask();
                mIsKioskEnabled = false;
            }
        } catch (Exception e) {
            callbackMessage(false, "android lock task exception");
            e.printStackTrace();
        }
    }

    private void deleteDeviceAdmin(){
        mDpm.clearDeviceOwnerApp(cordova.getActivity().getPackageName());
    }


    private void callbackMessage(boolean success, String message) {
        if (mCallbackContext == null) {
            return;
        }
        if(success){
            PluginResult dataResult = new PluginResult(PluginResult.Status.OK, message);
            dataResult.setKeepCallback(true);
            mCallbackContext.sendPluginResult(dataResult);
        } else {
            PluginResult dataResult = new PluginResult(PluginResult.Status.ERROR, message);
            dataResult.setKeepCallback(true);
            mCallbackContext.sendPluginResult(dataResult);
        }
        mCallbackContext = null;
    }
}
