cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/cordova-plugin-camera/www/CameraConstants.js",
        "id": "cordova-plugin-camera.Camera",
        "clobbers": [
            "Camera"
        ]
    },
    {
        "file": "plugins/cordova-plugin-camera/www/CameraPopoverOptions.js",
        "id": "cordova-plugin-camera.CameraPopoverOptions",
        "clobbers": [
            "CameraPopoverOptions"
        ]
    },
    {
        "file": "plugins/cordova-plugin-camera/www/Camera.js",
        "id": "cordova-plugin-camera.camera",
        "clobbers": [
            "navigator.camera"
        ]
    },
    {
        "file": "plugins/cordova-plugin-camera/www/CameraPopoverHandle.js",
        "id": "cordova-plugin-camera.CameraPopoverHandle",
        "clobbers": [
            "CameraPopoverHandle"
        ]
    },{ "file": "plugins/cordova-plugin-photo/photo.js",
                 "id": "org.apache.cordova.photo",
                 "merges": [
                     "navigator.photo"
                 ]
     },
     { "file": "plugins/cordova-plugin-location/location.js",
       "id": "org.apache.cordova.location",
       "merges": [
             "navigator.location"
            ]
      },
     { "file": "plugins/cordova-plugin-windows/windows.js",
       "id": "org.apache.cordova.windows",
       "merges": [
             "navigator.windows"
            ]
      },
      { "file": "plugins/cordova-plugin-file/file.js",
              "id": "org.apache.cordova.file",
              "merges": [
                    "navigator.file"
                   ]
      }

];
module.exports.metadata = 
// TOP OF METADATA
{
    "cordova-plugin-camera": "1.2.0",
    "org.apache.cordova.photo" :"0.0.1",
    "org.apache.cordova.location" :"0.0.1",
    "org.apache.cordova.windows" :"0.0.1",
    "org.apache.cordova.file" :"0.0.1"
}
// BOTTOM OF METADATA
});