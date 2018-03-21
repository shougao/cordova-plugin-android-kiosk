# cordova-plugin-android-kiosk
Cordova single purpose applicaion plugin for Android. The plugin make user can't excapt from you app because your app is device owner it cann't unpin. 
your app will start when boot complete or upgrade complete, It is available after Android 5.0 bacause it using the LockTask mode feature .

## install
```
cordova plugin add cordova-plugin-android-kiosk
```

## usage
``Kiosk`` is a ``cordova.plugin.kiosk.Kiosk`` to lock or unlock you app to the Only one app.

lock or unlock the app to Single Pourse device
```
function success(message){
    console.log("progress = " + message);
}

function error(message){
    console.log("error: reason is " + message);
}

Kiosk.lock(success, error, enable);
```
check the current lock state.
```
function success(message){
    console.log("locked = " + message); //true or false.
}
Kiosk.isLocked(success)
```

you could switch the launcher using ```switchLauncher```, it will unlock and show launcher chooser.

```
Kiosk.switchLauncher();
```

you have to set you app to the device owner forllow google AOSP design.
```
adb shell dpm set-device-owner com.example.template/.kiosk.plugin.MyAdmin
```
the setting will sotren at /data/system/device_policy.xml(before android 6.0)
/data/system/device_policy_2.xml(> android 6.0)

First, create a file device_owner.xml with following content:
```
<?xml version="1.0" encoding="utf-8" standalone="yes" ?>
<root>
    <device-owner package="com.myDomain.myPackage" name="" component="com.myDomain.myPackage/com.myDomain.myPackage.myComponent" userRestrictionsMigrated="true" />
</root>
```
chown system:system device_owner.xml

the source and target tag in plugin.xml is sutable for my project, the package name is ``com.example.template``, if your project name is different, please using your pakcage name instand of it.
