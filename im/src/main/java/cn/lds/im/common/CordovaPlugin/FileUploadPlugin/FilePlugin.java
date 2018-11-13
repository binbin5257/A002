///*
//       Licensed to the Apache Software Foundation (ASF) under one
//       or more contributor license agreements.  See the NOTICE file
//       distributed with this work for additional information
//       regarding copyright ownership.  The ASF licenses this file
//       to you under the Apache License, Version 2.0 (the
//       "License"); you may not use this file except in compliance
//       with the License.  You may obtain a copy of the License at
//
//         http://www.apache.org/licenses/LICENSE-2.0
//
//       Unless required by applicable law or agreed to in writing,
//       software distributed under the License is distributed on an
//       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
//       KIND, either express or implied.  See the License for the
//       specific language governing permissions and limitations
//       under the License.
//*/
//package cn.lds.im.common.CordovaPlugin.FileUploadPlugin;
//
//import android.content.Intent;
//import android.media.MediaScannerConnection.MediaScannerConnectionClient;
//import android.net.Uri;
//import android.widget.Toast;
//
//import org.apache.cordova.CallbackContext;
//import org.apache.cordova.CordovaPlugin;
//import org.apache.cordova.PluginResult;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//
//import cn.lds.chat.R;
//import cn.lds.chatcore.common.CoreHttpApiKey;
//import cn.lds.chatcore.common.LogHelper;
//import cn.lds.chatcore.common.ToolsHelper;
//import cn.lds.chatcore.data.HttpResult;
//import cn.lds.chatcore.event.FileUploadErrorEvent;
//import cn.lds.chatcore.event.HttpRequestErrorEvent;
//import cn.lds.chatcore.event.HttpRequestEvent;
//import cn.lds.chatcore.manager.FileManager;
//import de.greenrobot.event.EventBus;
//
///**
// * This class launches the camera view, allows the user to take a picture, closes the camera view,
// * and returns the captured image.  When the camera view is closed, the screen displayed before
// * the camera view was shown is redisplayed.
// */
//public class FilePlugin extends CordovaPlugin implements MediaScannerConnectionClient {
//
//
//    public CallbackContext callbackContext;
//    private int key;
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        try {
//            EventBus.getDefault().register(this);
//        } catch (Exception e) {
//
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
//        this.callbackContext = callbackContext;
//        if (action.equals("takeFile")) {
//            if (cn.lds.chatcore.common.FileHelper.existSDCard()) {// 有内存卡才可以选择
//                Intent intent = null;
//                try {
//                    Class<?> c = (Class<?>) Class.forName("cn.lds.im.view.appview.AppSelectFileActivity");
//                    if (c == null) {
//                        intent = new Intent(cordova.getActivity(), SelectFileActivity.class);
//                    } else {
//                        intent = new Intent(cordova.getActivity(), c);
//                    }
//                } catch (ClassNotFoundException e) {
//                    intent = new Intent(cordova.getActivity(), SelectFileActivity.class);
//                }
//
//                try {
//                    this.cordova.startActivityForResult((CordovaPlugin) this, intent, 101);
//                } catch (android.content.ActivityNotFoundException ex) {
////                    ToolsHelper.showInfo(this.cordova.getActivity(), "没有发现文件管理软件");
//                    this.callbackContext.error("没有发现文件管理软件");
//                }
//
//            } else {
//                this.callbackContext.error(this.cordova.getActivity().getString(R.string.chat_no_sdcard));
//                Toast.makeText(this.cordova.getActivity(), this.cordova.getActivity().getString(R.string
//                        .chat_no_sdcard), Toast.LENGTH_LONG).show();
//            }
//
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        if (101 == requestCode) {
//            if (-1 == resultCode) {
//                String path = intent.getStringExtra("path");
//
//                if (ToolsHelper.isNull(path) || null == new File(path)) {
//                    PluginResult r = new PluginResult(PluginResult.Status.ERROR, "获取文件路径失败");
//                    this.callbackContext.sendPluginResult(r);
//                } else {
//                    key = (int) (Math.random() * (999999 - 100000) + 100000);
//                    LogHelper.e(path);
//                    FileManager.getInstance().uploadFile(String.valueOf(key).toString(), path);
//                }
//            } else {
//                PluginResult r = new PluginResult(PluginResult.Status.ERROR, "取消选择");
//                this.callbackContext.sendPluginResult(r);
//            }
//        }
//
//    }
//
//    public void onEventBackgroundThread(HttpRequestEvent event) {
//        HttpResult httpResult = event.getResult();
//        String apiNo = httpResult.getApiNo();
//        if (!(CoreHttpApiKey.registerFile.equals(apiNo))) {
//            return;
//        }
//
//        String messageId = httpResult.getExtras().get("owner");
//        if (String.valueOf(key).toString().equals(messageId)) {
//            //上传文件后，返回的服务器文件ID
//            String fileStorageId;
//            try {
//                JSONObject data = httpResult.getJsonResult().getJSONObject("data");
//                fileStorageId = data.getString("id");
//                this.callbackContext.success(fileStorageId);
//            } catch (JSONException e) {
//                LogHelper.e(this.getClass().getName(), e);
//                this.callbackContext.error("文件上传失败");
//                return;
//            }
//        }
//
//
//    }
//
//
//    public void onEventBackgroundThread(HttpRequestErrorEvent event) {
//        HttpResult httpResult = event.getResult();
//        String apiNo = httpResult.getApiNo();
//        if (!(CoreHttpApiKey.registerFile.equals(apiNo))) {
//            return;
//        }
//        String messageId = httpResult.getExtras().get("owner");
//        if (String.valueOf(key).toString().equals(messageId)) {
//            this.callbackContext.error("文件上传失败");
//        }
//
//    }
//
//    /**
//     * 文件上传失败
//     */
//    public void onEventBackgroundThread(FileUploadErrorEvent event) {
//        // TODO 修改该条消息的状态
//        String id = event.getOwner();
//        if (String.valueOf(key).toString().equals(event.getOwner()))
//            this.callbackContext.error("文件上传失败");
//
//
//    }
//
//    @Override
//    public void onMediaScannerConnected() {
//
//    }
//
//    @Override
//    public void onScanCompleted(String path, Uri uri) {
//
//    }
//}
