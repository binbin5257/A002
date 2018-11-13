package com.bluebox;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by xuchen on 16/7/13.
 */
public class DataExchanger extends Thread {
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private InputStream tmpIn = null;
    private OutputStream tmpOut = null;
    private String mSocketType;
    private boolean isContinueReading;
    IBlueBoxContoller de;
    // Unique UUID for this application
    private static final UUID MY_UUID_SECURE = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    // private static final UUID MY_UUID_INSECURE =
    // UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    private static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public DataExchanger(BluetoothDevice device, boolean secure, IBlueBoxContoller de) {
        mmDevice = device;
        this.de = de;
        BluetoothSocket tmp = null;
        mSocketType = secure ? "Secure" : "Insecure";
        isContinueReading = true;
        // Get a BluetoothSocket for a connection with the
        // given BluetoothDevice
        try {
            if (secure) {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
            } else {
                tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID_INSECURE);
            }
        } catch (IOException e) {
            restartConnect();
            return;
//                Log.e(TAG, "ggggg Socket Type: " + mSocketType + "create() failed: " + e);
        }
        mmSocket = tmp;

    }

    public void run() {
        Log.i("xuchen", "  BEGIN mConnectThread SocketType:" + mSocketType);


        // Make a connection to the BluetoothSocket
        try {
            this.de.addRetryCount();
            // This is a blocking call and will only return on a
            // successful connection or an exception
            mmSocket.connect();

            this.de.sendMessage(BlueBoxControllerBelowSdk17.BLUE_STATE_CONNECTION_SUCCESS);
            Log.i("xuchen", " SppBrChatService- ConnectThread-run:connected success--" + mmDevice.getName()
                    + "--curRetryConCount--" + this.de.getRetryCount());
        } catch (IOException e) {

            Log.i("xuchen", " SppBrChatService- ConnectThread-run:connected failure--" + mmDevice.getName()
                    + "--curRetryConCount--" + this.de.getRetryCount());
            if(isContinueReading)
                restartConnect();
            return;
        }


        // Get the BluetoothSocket input and output streams
        try {
            tmpIn = mmSocket.getInputStream();
            tmpOut = mmSocket.getOutputStream();
            Log.i("xuchen", "  SppBtChatSerive.ConnectedThread.ConnectedThread()--create out or in stream success ");
        } catch (IOException e) {
            if(isContinueReading)
                restartConnect();
            return;
        }


        byte[] buffer = new byte[1024];
        int bytes;

        // Keep listening to the InputStream while connected
        while (isContinueReading) {
            try {
                // Read from the InputStream
                bytes = tmpIn.read(buffer);
                // Send the obtained bytes to the UI Activity
                this.de.getHandler().obtainMessage(BlueBoxControllerBelowSdk17.BLUE_STATE_READ, bytes, -1, buffer).sendToTarget();
            } catch (IOException e) {
//                Log.i("xuchen", "  SppBtChatSerive.ConnectedThread.run()--read bytes happen exception-");
                if(isContinueReading)
                    restartConnect();

                return;
            }
        }


    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     */
    public void write(final byte[] out) {

        new Thread() {
            @Override
            public void run() {
                try {
                    tmpOut.write(out);
                } catch (IOException e) {
                    DataExchanger.this.de.sendMessage(BlueBoxControllerBelowSdk17.BLUE_STATE_WRITE_CMD_FAILURE);

                }

            }
        }.start();


    }

    // 重启连接
    private void restartConnect() {
        cancel();
        this.de.sendMessage(BlueBoxControllerBelowSdk17.BLUE_STATE_CONNECTION_FAILURE, 1000);
    }

    public void cancel() {
        Log.i("xuchen", "    mConnectThread cancel");
//            if (mmSocket.isConnected()) {
        isContinueReading = false;
        try {
            if (tmpIn != null)
                tmpIn.close();
            tmpIn = null;
            Log.i("xuchen", "    mConnectThread tmpIn.close();");
        } catch (IOException e) {

        }
        try {
            if (tmpOut != null)
                tmpOut.close();
            tmpOut = null;
            Log.i("xuchen", "    mConnectThread tmpOut.close();");
        } catch (IOException e) {

        }
        try {
            if (mmSocket != null)
                mmSocket.close();
            mmSocket = null;
            Log.i("xuchen", "    mConnectThread mmSocket.close();");
        } catch (IOException e) {

        }
//            }
    }
}