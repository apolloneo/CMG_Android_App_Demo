package com.example.cmgandroidappdemo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cmgandroidappdemo.DeviceCommunication.DataMediator;
import com.example.cmgandroidappdemo.DeviceCommunication.SerialHelper;
import com.example.cmgandroidappdemo.DeviceCommunication.SerialService;

import java.util.ArrayList;
import java.util.List;

public class Tab1_Main extends Fragment {
    private TextView feedback;
    private Button sendButton,connectButton, clearButton;
    private EditText inputMsg;
    private Spinner usbToSerialDeviceSpinner, baudrateSpinner;
    private ArrayList<String> usbToSerialDeviceInfoList = new ArrayList<>();
    private ArrayList<UsbDevice> usbToSerialDeviceList = new ArrayList<>();
    private ArrayList<Integer> baudrateList = new ArrayList<Integer>(){
        {
            add(110);
            add(300);
            add(600);
            add(1200);
            add(2400);
            add(4800);
            add(9600);
            add(14400);
            add(19200);
            add(38400);
            add(57600);
            add(115200);
            add(128000);
            add(256000);
        }
    };

    private ArrayAdapter<String> usbToSerialDevicesAdapter;
    private ArrayAdapter<Integer> baudrateOptionsAdapter;
    private SerialHelper serialHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.tab1_main, container,false);

        usbToSerialDeviceSpinner = rootView.findViewById(R.id.spinner_USB_to_Serial_Device);
        baudrateSpinner = rootView.findViewById(R.id.spinner_Baudrate);
        connectButton = rootView.findViewById(R.id.Button_Connect);
        feedback = rootView.findViewById(R.id.textView_Feedback);
        feedback.setMovementMethod(new ScrollingMovementMethod());
        inputMsg = rootView.findViewById(R.id.editText_Input_Msg);
        clearButton = rootView.findViewById(R.id.Button_Clear);
        sendButton = rootView.findViewById(R.id.Button_Send);
        sendButton.setEnabled(false);

        serialHelper = new SerialHelper(getContext());

        usbToSerialDevicesAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, usbToSerialDeviceInfoList);
        usbToSerialDeviceSpinner.setAdapter(usbToSerialDevicesAdapter);

        baudrateOptionsAdapter = new ArrayAdapter<Integer>(getContext(), R.layout.support_simple_spinner_dropdown_item, baudrateList);
        baudrateSpinner.setAdapter(baudrateOptionsAdapter);
        baudrateSpinner.setSelection(6);




        refreshSpinner();

        if (isMyServiceRunning(SerialService.class)){
            connectButton.setText(R.string.connect_button_caption_disconnect);
        }
        else {
            connectButton.setText(R.string.connect_button_caption_connect);
        }

        serialHelper.setOnDeviceAttachmentEventListener(new SerialHelper.OnDeviceAttachmentEventListener() {
            @Override
            public void OnAttachmentEvent(int event) {
                refreshSpinner();
            }
        });

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsbDevice m_UsbDevice = usbToSerialDeviceList.get(usbToSerialDeviceSpinner.getSelectedItemPosition());

                /*if (isMyServiceRunning(SerialService.class)){
                    connectButton.setText(R.string.connect_button_caption_connect);
                    Intent serialServiceInent = new Intent(getActivity(), SerialService.class);
                    serialServiceInent.putExtra(SerialService.TARGET_DEVICE,m_UsbDevice);
                    getActivity().stopService(serialServiceInent);
                }else{
                    connectButton.setText(R.string.connect_button_caption_disconnect);


                }
                */

                if (connectButton.getText().equals(getString(R.string.connect_button_caption_connect) )){
                    connectButton.setText(R.string.connect_button_caption_disconnect);
                    Intent serialServiceIntent = new Intent(getActivity(), SerialService.class);
                    //serialServiceIntent.putExtra(SerialService.TARGET_DEVICE, m_UsbDevice);
                    Bundle extras = new Bundle();
                    extras.putParcelable(SerialService.TARGET_DEVICE, m_UsbDevice);
                    extras.putInt(SerialService.TARGET_DEVICE_BAUDRATE,baudrateList.get(baudrateSpinner.getSelectedItemPosition()));
                    serialServiceIntent.putExtras(extras);
                    getActivity().startService(serialServiceIntent);

                    //Toast.makeText(getContext(), "Service Started!", Toast.LENGTH_SHORT).show();
                }
                else if (connectButton.getText().equals(getString(R.string.connect_button_caption_disconnect) )){
                    connectButton.setText(R.string.connect_button_caption_connect);
                    //getActivity().stopService(new Intent(getActivity(), SerialService.class).putExtra(SerialService.TARGET_DEVICE,m_UsbDevice));
                    Intent serialServiceIntent = new Intent(getActivity(), SerialService.class);
                    Bundle extras = new Bundle();
                    extras.putParcelable(SerialService.TARGET_DEVICE, m_UsbDevice);
                    extras.putInt(SerialService.TARGET_DEVICE_BAUDRATE,baudrateList.get(baudrateSpinner.getSelectedItemPosition()));
                    serialServiceIntent.putExtras(extras);
                    getActivity().stopService(serialServiceIntent);
                    //Toast.makeText(getContext(), "Service Stopped!", Toast.LENGTH_SHORT).show();
                }

                if (isMyServiceRunning(SerialService.class)) {
                    sendButton.setEnabled(true);
                    //((MainActivity) getActivity()).hideSystemUI();
                }
                else{
                    sendButton.setEnabled(false);
                    //((MainActivity) getActivity()).showSystemUI();
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inputMsg.getText().toString().equals("")) {
                    String dataWriteToSerial = inputMsg.getText().toString();
                    if (isMyServiceRunning(SerialService.class)) {
                        Intent dataPacketToServiceIntent = new Intent();
                        dataPacketToServiceIntent.putExtra(SerialService.EXTRA_PACKET_DATA_TO_SEND,dataWriteToSerial.getBytes());
                        dataPacketToServiceIntent.setAction(SerialService.ACTION_DATA_TO_SEND);
                        getActivity().sendBroadcast(dataPacketToServiceIntent);
                    }
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedback.setText("");
            }
        });

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //restore the feedback value
        if (savedInstanceState != null){
            feedback.setText(savedInstanceState.getString("FEEDBACK_DATA"));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter serialDataRcvFilter = new IntentFilter();
        serialDataRcvFilter.addAction(SerialService.ACTION_DATA_RECEIVED);
        getActivity().registerReceiver(serialDataRcver,serialDataRcvFilter);
    }

    private void refreshSpinner(){
        usbToSerialDeviceInfoList.clear();
        usbToSerialDeviceList.clear();
        if (serialHelper.getAttachedDeviceList().isEmpty()){
            connectButton.setText(R.string.connect_button_caption_connect);
            connectButton.setEnabled(false);
        }
        else{
            usbToSerialDeviceList = serialHelper.getAttachedDeviceList();
            connectButton.setEnabled(true);
            for (UsbDevice usbDevice:usbToSerialDeviceList){
                String deviceInfo, vid, pid;
                vid = Integer.toHexString(usbDevice.getVendorId());
                pid = Integer.toHexString(usbDevice.getProductId());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    deviceInfo = "Mfc Name: " + usbDevice.getManufacturerName() + " Product Name: " + usbDevice.getProductName() + " VID: 0x" + vid + " PID: 0x" + pid;
                }else{
                    deviceInfo = " VID: 0x" + vid + " PID: 0x" + pid;
                }
                usbToSerialDeviceInfoList.add(deviceInfo);

            }
        }
        usbToSerialDevicesAdapter.notifyDataSetChanged();
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfoList = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo serviceInfo: runningServiceInfoList) {
            if (serviceInfo.service.getClassName().equals(serviceClass.getName())) {
                return true;
            }
        }

        return false;
    }

    private BroadcastReceiver serialDataRcver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(SerialService.ACTION_DATA_RECEIVED)){
                byte[] receivedData = intent.getExtras().getByteArray(SerialService.EXTRA_PACKET_DATA_RECEIVED);
                //String s = intent.getExtras().getString(SerialService.EXTRA_PACKET_DATA_RECEIVED);
                //String s = DataMediator.getReadableHexString(receivedData).trim();
                String s = new String(receivedData);
                //String s = receivedData.toString();
                //feedback.append(receivedData.toString() + "\n");
                //String receivedData = intent.getExtras().getString(SerialService.EXTRA_PACKET_DATA_RECEIVED);
                feedback.append(s);
            }
        }
    };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //save the feedback value
        outState.putString("FEEDBACK_DATA", feedback.getText().toString());
    }
}
