package com.example.cmgandroidappdemo.DeviceCommunication;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;

//import com.example.scoreboard.ChooseConnectionActivity;
//import com.example.scoreboard.DeviceCommunications.SerialService2;
import com.felhr.usbserial.SerialInputStream;
import com.felhr.usbserial.SerialOutputStream;
import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.content.Context.USB_SERVICE;

public class SerialHelper {

    private static final String ACTION_USB_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    private static final String ACTION_USB_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private UsbManager usbManager;
    private UsbDevice selectedDevice;
    private UsbDeviceConnection deviceConnection;
    private UsbSerialDevice serialPort;
    private Context context;
    private SerialInputStream serialInputStream;
    private SerialOutputStream serialOutputStream;
    private int baudRate;
    private Handler handler = new Handler();


    public interface OnDeviceAttachmentEventListener{
        int EVENT_DEVICE_ATTACHED = 0;
        int EVENT_DEVICE_DETACHED = 1;
        void OnAttachmentEvent(int event);
    }

    public interface OnPermissionEventListener {
        int EVENT_PERMISSION_REQUESTED = 0;
        int EVENT_PERMISSION_GRANTED = 1;
        int EVENT_PERMISSION_DENIED = 2;
        void OnPermissionEvent(int event);
    }

    public interface OnDataConnectionChangedListener {
        int STATUS_CONNECTED = 0;
        int STATUS_DISCONNECTED = 1;
        void OnConnectionChanged(int status);
    }

    public interface OnDataReceivedListener {
        void OnDataReceived(byte[] packet);
        //void OnDataReceived(String packet);
    }

    //added by B.C. on 20190819
    //public interface OnDataSendingListener {
    //    void OnDataSending(byte[] packet);
    //}

    private OnPermissionEventListener onPermissionEventListener;
    private OnDataReceivedListener onDataReceivedListener;
    private OnDataConnectionChangedListener onDataConnectionChangedListener;
    private OnDeviceAttachmentEventListener onDeviceAttachmentEventListener;

    //add by B.C. on 20190819
    //private OnDataSendingListener onDataSendingListener;


    public SerialHelper(Context context){
        this.context = context;
        usbManager = (UsbManager) context.getSystemService(USB_SERVICE);
        IntentFilter serialActionFilter = new IntentFilter();
        serialActionFilter.addAction(ACTION_USB_PERMISSION);
        serialActionFilter.addAction(ACTION_USB_DETACHED);
        serialActionFilter.addAction(ACTION_USB_ATTACHED);
        this.context.registerReceiver(usbActionReceiver, serialActionFilter);
    }


    public ArrayList<UsbDevice> getAttachedDeviceList(){
        //notice that the attachedDeviceList could be empty.
        ArrayList<UsbDevice> returnList = new ArrayList<>();
        HashMap<String, UsbDevice> usbDevices = this.usbManager.getDeviceList();
        for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
            UsbDevice device = entry.getValue();
            returnList.add(device);
        }

        return returnList;


    }


    public void requestDataConnection(UsbDevice device, int baudRate) {
        this.selectedDevice = device;
        this.baudRate = baudRate;
        //if (device)
        int deviceVID = device.getVendorId();
        int devicePID = device.getProductId();
        if (deviceVID != 0x1d6b && (devicePID != 0x0001 || devicePID != 0x0002 || devicePID != 0x0003)) {
            if(!usbManager.hasPermission(device)){
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
                usbManager.requestPermission(device, pendingIntent);
                if(onPermissionEventListener!=null){
                    onPermissionEventListener.OnPermissionEvent(OnPermissionEventListener.EVENT_PERMISSION_REQUESTED);
                }
            }else {
                new ConnectionThread().run();
            }
        }
    }


    public void closeDataConnection(){
        this.selectedDevice = null;
        this.deviceConnection = null;
        if(this.serialPort!=null){
            this.serialPort.close();
            this.serialPort = null;

        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(onDataConnectionChangedListener !=null){
                    onDataConnectionChangedListener.OnConnectionChanged(OnDataConnectionChangedListener.STATUS_DISCONNECTED);
                }
            }
        });

    }


    public boolean hasDataConnection(){
        return (serialPort!=null && selectedDevice !=null && deviceConnection !=null);
    }


    public void writeToDevice(byte[] data){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(hasDataConnection()){
                    serialPort.write(data);
                }
            }
        });
    }


    private final BroadcastReceiver usbActionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_USB_PERMISSION)) {
                boolean granted = intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
                if (granted) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(onPermissionEventListener !=null){
                                onPermissionEventListener.OnPermissionEvent(OnPermissionEventListener.EVENT_PERMISSION_GRANTED);
                            }
                        }
                    });
                    //selectedDevice = getAttachedDeviceList().get(0);
                    selectedDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    new ConnectionThread().run();
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(onPermissionEventListener !=null){
                                onPermissionEventListener.OnPermissionEvent(OnPermissionEventListener.EVENT_PERMISSION_DENIED);
                            }
                        }
                    });
                }
            } else if (action.equals(ACTION_USB_ATTACHED)) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(onDeviceAttachmentEventListener!=null){
                            onDeviceAttachmentEventListener.OnAttachmentEvent(OnDeviceAttachmentEventListener.EVENT_DEVICE_ATTACHED);
                        }
                    }
                });
            } else if (action.equals(ACTION_USB_DETACHED)) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(onDeviceAttachmentEventListener!=null){
                            onDeviceAttachmentEventListener.OnAttachmentEvent(OnDeviceAttachmentEventListener.EVENT_DEVICE_DETACHED);
                        }
                        if(hasDataConnection()){
                            closeDataConnection();
                        }
                    }
                });
            }
        }
    };


    private UsbSerialInterface.UsbReadCallback readCallback = new UsbSerialInterface.UsbReadCallback() {
        @Override
        public void onReceivedData(final byte[] data) {
            //try {
            //    String s = new String(data, "UTF-8");
            //    Thread thread = new Thread(new ReceiverThread(s));
            //    thread.start();
            //} catch (UnsupportedEncodingException e) {
            //    e.printStackTrace();
            //}

            //Thread thread = new Thread(new ReceiverThread(data));
            //thread.start();

            if(onDataReceivedListener != null){
                onDataReceivedListener.OnDataReceived(data);
            }

        }
    };


    private class ConnectionThread extends Thread {
        @Override
        public void run() {
            deviceConnection = usbManager.openDevice(selectedDevice);
            serialPort = UsbSerialDevice.createUsbSerialDevice(selectedDevice, deviceConnection);
            if (serialPort != null) {
                if (serialPort.open()) {
                    serialPort.setBaudRate(baudRate);
                    serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                    serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                    serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                    serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                    serialPort.read(readCallback);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(onDataConnectionChangedListener !=null){
                                onDataConnectionChangedListener.OnConnectionChanged(OnDataConnectionChangedListener.STATUS_CONNECTED);
                            }
                        }
                    });
                }
            }
        }
    }

    /*
    private class ReceiverThread implements Runnable{

        byte[] data;
        private ReceiverThread(byte[] data){
            this.data = data;
        }
        //String data;
        //private ReceiverThread(String s){
        //    this.data = s;
        //}

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(onDataReceivedListener != null){
                        onDataReceivedListener.OnDataReceived(data);
                    }
                }
            });
        }
    }
    */

    public void setOnPermissionEventListener(OnPermissionEventListener onPermissionEventListener) {
        this.onPermissionEventListener = onPermissionEventListener;
    }

    public void setOnDataReceivedListener(OnDataReceivedListener onDataReceivedListener) {
        this.onDataReceivedListener = onDataReceivedListener;
    }

    public void setOnDataConnectionChangedListener(OnDataConnectionChangedListener onDataConnectionChangedListener) {
        this.onDataConnectionChangedListener = onDataConnectionChangedListener;
    }

    public void setOnDeviceAttachmentEventListener(OnDeviceAttachmentEventListener onDeviceAttachmentEventListener) {
        this.onDeviceAttachmentEventListener = onDeviceAttachmentEventListener;
    }

}
