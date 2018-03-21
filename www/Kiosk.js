var exec = require('cordova/exec');

exports.lockLauncher = function(enabled, success, error) {
    exec(null, error, 'Kiosk', 'lockLauncher', [!!enabled]);
};

exports.isLocked = function(arg0, success, error) {
    exec(success, null, 'Kiosk', 'isLocked', null);
};

exports.switchLauncher = function() {
    exec(null, null, 'Kiosk', 'switchLauncher', null);
};

exports.deleteDeviceAdmin = function() {
    exec(null, null, 'Kiosk', 'deleteDeviceAdmin', null);
};