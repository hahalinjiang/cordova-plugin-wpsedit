var exec = require('cordova/exec');

exports.OpenWps = function (arg0,arg1,arg2, success, error) {
    exec(success, error, 'WPSPlugin', 'OpenWps', [arg0,arg1,arg2]);
};
exports.deleteFile = function (arg0, success, error) {
    exec(success, error, 'WPSPlugin', 'deleteFile', [arg0]);
};
exports.upLoadFile = function (arg0,arg1,arg2, success, error) {
    exec(success, error, 'WPSPlugin', 'upLoadFile', [arg0,arg1,arg2]);
};
