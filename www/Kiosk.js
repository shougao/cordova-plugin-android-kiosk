var exec = require('cordova/exec');

exports.lockLauncher = function(enabled) {
    exec(null, null, 'Kiosk', 'lockLauncher', [!!enabled]);
};

exports.isLocked = function() {
    exec(success, null, 'Kiosk', 'isLocked', null);
};

exports.switchLauncher = function() {
    exec(null, null, 'Kiosk', 'switchLauncher', null);
};

exports.deleteDeviceAdmin = function() {
    exec(null, null, 'Kiosk', 'deleteDeviceAdmin', null);
};