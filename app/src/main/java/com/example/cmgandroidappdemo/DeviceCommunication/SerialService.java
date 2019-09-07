package com.example.cmgandroidappdemo.DeviceCommunication;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Arrays;

public class SerialService extends Service {
    private SerialHelper serialHelper;
    private UsbDevice connectedDevice;
    private int baudrate;

    public static final String TARGET_DEVICE = "myUSBtoSerialDevice.target";
    public static final String TARGET_DEVICE_BAUDRATE = "myUSBtoSerialDevice.target.baudrate";
    public static final String ACTION_DATA_RECEIVED = "myAction.data.rx";
    public static final String ACTION_DATA_TO_SEND = "myAction.data.tx";
    public static final String EXTRA_PACKET_DATA_RECEIVED = "rx.data";
    public static final String EXTRA_PACKET_DATA_TO_SEND = "tx.data";

    private BroadcastReceiver writeSerialDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_DATA_TO_SEND)) {
                byte[] packetToSend = intent.getByteArrayExtra(EXTRA_PACKET_DATA_TO_SEND);
                if (serialHelper.hasDataConnection()){
                    serialHelper.writeToDevice(packetToSend);
                }
            }
        }
    };

    public SerialService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return new UsbBinder();
    }

    @Override
    public void onCreate() {
        IntentFilter writeSerialDataIntentFilter = new IntentFilter();
        writeSerialDataIntentFilter.addAction(ACTION_DATA_TO_SEND);
        registerReceiver(writeSerialDataReceiver, writeSerialDataIntentFilter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        connectedDevice = (UsbDevice) intent.getExtras().get(TARGET_DEVICE);
        baudrate = (int) intent.getExtras().get(TARGET_DEVICE_BAUDRATE);
        serialHelper = new SerialHelper(this);
        serialHelper.setOnDataConnectionChangedListener(new SerialHelper.OnDataConnectionChangedListener() {
            @Override
            public void OnConnectionChanged(int status) {
                if (status == STATUS_CONNECTED){
                    Toast.makeText(getApplicationContext(), "Connected!", Toast.LENGTH_SHORT).show();
                }
                else if (status == STATUS_DISCONNECTED){
                    Toast.makeText(getApplicationContext(), "Disconnected!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        serialHelper.setOnDataReceivedListener(new SerialHelper.OnDataReceivedListener() {
            @Override
            public void OnDataReceived(byte[] packet) {
                //Toast.makeText(getApplicationContext(), new String(packet), Toast.LENGTH_SHORT).show();
                Intent dataIntent = new Intent();
                dataIntent.putExtra(EXTRA_PACKET_DATA_RECEIVED, packet);
                dataIntent.setAction(ACTION_DATA_RECEIVED);
                sendBroadcast(dataIntent);
                //trying to clear the data receiver buffer
                // Arrays.fill(packet, (byte)0);

            }
        });


        serialHelper.requestDataConnection(connectedDevice, baudrate);



        return Service.START_NOT_STICKY;
    }

    public class UsbBinder extends Binder {
        public SerialService getService() {
            return SerialService.this;
        }
    }
}

