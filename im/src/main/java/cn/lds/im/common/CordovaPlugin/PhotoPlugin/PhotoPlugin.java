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
//package cn.lds.im.common.CordovaPlugin.PhotoPlugin;
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
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.lds.chatcore.common.CoreHttpApiKey;
//import cn.lds.chatcore.common.LogHelper;
//import cn.lds.chatcore.data.HttpResult;
//import cn.lds.chatcore.event.FileUploadErrorEvent;
//import cn.lds.chatcore.event.HttpRequestErrorEvent;
//import cn.lds.chatcore.event.HttpRequestEvent;
//import cn.lds.chatcore.manager.FileManager;
//import cn.lds.chat.R;
//import de.greenrobot.event.EventBus;
//
///**
// * This class launches the camera view, allows the user to take a picture, closes the camera view,
// * and returns the captured image.  When the camera view is closed, the screen displayed before
// * the camera view was shown is redisplayed.
// */
//public class PhotoPlugin extends CordovaPlugin implements MediaScannerConnectionClient {
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
//
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
//        if (action.equals("takePicture")) {
//            if (cn.lds.chatcore.common.FileHelper.existSDCard()) {// 有内存卡才可以选择
//
//                Intent intent = new Intent(cordova.getActivity(), MultiImageSelectorActivity.class);
//                // whether show camera
//                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
//                // max select image amount
//                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
//                // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
//                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
//                // default select images (support array list)
//                ArrayList<String> defaultDataArray = new ArrayList<String>();
//                intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
//                        defaultDataArray);
//                try {
//                    this.cordova.startActivityForResult((CordovaPlugin) this, intent, 100);
//                } catch (Exception e) {
//                    this.callbackContext.error("查找图片错误");
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
//        if (100 == requestCode) {
//            if (-1 == resultCode) {
//                List<String> d = intent.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//                if (null != d && d.size() > 0) {
//                    if (4 == cn.lds.chatcore.common.FileHelper.getFileTypeIcon(d.get(0))) {
//                        key = (int) (Math.random() * (999999 - 100000) + 100000);
//                        //上传图片
//                        FileManager.getInstance().uploadChatImage(d.get(0), String.valueOf(key).toString());
//                    } else {
//                        PluginResult r = new PluginResult(PluginResult.Status.ERROR, "文件类型错误");
//                        this.callbackContext.sendPluginResult(r);
//                    }
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
//                this.callbackContext.error("图片上传失败");
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
//            this.callbackContext.error("图片上传失败");
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
//        if (String.valueOf(key).toString().equals(id))
//            this.callbackContext.error("图片上传失败");
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
