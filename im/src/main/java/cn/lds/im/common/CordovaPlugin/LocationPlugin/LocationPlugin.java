/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
*/
package cn.lds.im.common.CordovaPlugin.LocationPlugin;

import com.baidu.location.BDLocation;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.lds.chatcore.common.LocationHelper;
import cn.lds.chatcore.common.LogHelper;

/**
 * This class launches the camera view, allows the user to take a picture, closes the camera view,
 * and returns the captured image.  When the camera view is closed, the screen displayed before
 * the camera view was shown is redisplayed.
 */
public class LocationPlugin extends CordovaPlugin {


    public CallbackContext mCallbackContext;

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.mCallbackContext = callbackContext;
        if (action.equals("getLocation")) {
            //这里应该需要定位权限确认

            try {
                final LocationHelper locationHelper = LocationHelper.getDefault(this.cordova.getActivity());
                locationHelper.setLocationListener(new LocationHelper.LocationListener() {
                    @Override
                    public void onReceiveLocation(BDLocation location) {
                        try {
                            if (location == null) {
                                PluginResult r = new PluginResult(PluginResult.Status.ERROR, "定位失败");
                                mCallbackContext.sendPluginResult(r);
                                return;
                            }
                            locationHelper.stop();
                            if (5e-324 == location.getLatitude() || 5e-324 == location.getLongitude()) {
                                PluginResult r = new PluginResult(PluginResult.Status.ERROR, "定位失败,请确认是否开启了定位权限");
                                mCallbackContext.sendPluginResult(r);
                            } else {

                                JSONObject object = new JSONObject();
                                object.put("accuracy", 65);
                                object.put("altitude", location.getAltitude());
                                object.put("altitudeAccuracy", "13.88387200515291");
                                object.put("heading", "-1");
                                object.put("latitude", location.getLatitude());
                                object.put("longitude", location.getLongitude());
                                object.put("timestamp", location.getTime());
                                object.put("velocity", "-1");
                                JSONObject o = new JSONObject();
                                o.put("coords", object);

                                PluginResult r = new PluginResult(PluginResult.Status.OK, o);
                                mCallbackContext.sendPluginResult(r);
                            }
//                            mCallbackContext.success(location.getAddrStr() + "Latitude:" + String.valueOf(location
//                                    .getLatitude()).toString() + "Longitude:" + String.valueOf(location.getLongitude
//                                    ()).toString());
                        } catch (Exception ex) {
                            LogHelper.e("上传公众号地理位置", ex);
                            mCallbackContext.error("获取当前位置失败");
                        }
                    }
                });
                if (!locationHelper.isStarted()) {
                    locationHelper.start();
                }
            } catch (Exception ex) {
                LogHelper.e("上传公众号地理位置", ex);
                this.mCallbackContext.error("获取当前位置失败");
            }


            return true;
        }
        return false;
    }

    @Override
    public boolean onOverrideUrlLoading(String url) {
        return true;
    }

    @Override
    public Boolean shouldAllowBridgeAccess(String url) {
        return true;
    }
}
