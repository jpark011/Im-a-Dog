package com.cs446w18.a16.imadog.bluetooth;

import android.app.Activity;
import android.app.VoiceInteractor;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.cs446w18.a16.imadog.commands.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Omar on 14/07/2015.
 */
public class Bluetooth {
    protected static final int REQUEST_ENABLE_BT = 1111;
    protected static final int REQUEST_DISCOVERABLE_BT = 2222;
    protected static final int REQUEST_CODE_LOC = 3333;

    protected Activity activity;
    protected Context context;
    protected UUID uuid;

    protected BluetoothAdapter bluetoothAdapter;
    protected BluetoothSocket socket;
    protected BluetoothDevice device, devicePair;
    protected ObjectInputStream input;
    protected ObjectOutputStream out;

    protected CommunicationCallback communicationCallback;
    protected DiscoveryCallback discoveryCallback;
    protected BluetoothCallback bluetoothCallback;
    protected boolean connected;

    protected boolean runOnUi;

    public Bluetooth(Context context){
        initialize(context, UUID.fromString("00000000-0000-1000-8000-00805F9B34FB"));
        onStart();
    }

    public Bluetooth(Context context, UUID uuid){
        initialize(context, uuid);
    }

    private void initialize(Context context, UUID uuid){
        this.context = context;
        this.uuid = uuid;
        this.communicationCallback = null;
        this.discoveryCallback = null;
        this.bluetoothCallback = null;
        this.connected = false;
        this.runOnUi = false;
    }

    public void onStart(){
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        context.registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    public void onStop(){
        context.unregisterReceiver(bluetoothReceiver);
    }

   public void enable(Activity activity){
        if(bluetoothAdapter!=null) {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
   }

    public void disable(){
        if(bluetoothAdapter!=null) {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
            }
        }
    }

    public boolean isEnabled(){
        return bluetoothAdapter.isEnabled();
    }

    public void setCallbackOnUI(Activity activity){
        this.activity = activity;
        this.runOnUi = true;
    }

    public void onActivityResult(int requestCode, final int resultCode, Intent intent){
        if(bluetoothCallback!=null){
            if(requestCode==REQUEST_ENABLE_BT){
                ThreadHelper.run(runOnUi, activity, new Runnable() {
                    @Override
                    public void run() {
                        if(resultCode==Activity.RESULT_CANCELED){
                            bluetoothCallback.onUserDeniedActivation();
                        }
                    }
                });
            } else if (requestCode==REQUEST_DISCOVERABLE_BT) {
                ThreadHelper.run(runOnUi, activity, new Runnable() {
                    @Override
                    public void run() {
                        if(resultCode != Activity.RESULT_CANCELED){
                            discoveryCallback.onDiscoverable();
                        } else {
                            discoveryCallback.onError("User denied");
                        }
                    }
                });
            }
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CODE_LOC:
//                if (grantResults.length > 0) {
//                    for (int gr : grantResults) {
//                        // Check if request is granted or not
//                        if (gr != PackageManager.PERMISSION_GRANTED) {
//                            return;
//                        }
//                    }
//
//                    //TODO - Add your code here to start Discovery
//
//                }
//                break;
//            default:
//                return;
//        }
//    }

    public void connectToAddress(String address, boolean insecureConnection) {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        connectToDevice(device, insecureConnection);
    }

    public void connectToAddress(String address) {
        connectToAddress(address, false);
    }

    public void connectToName(String name, boolean insecureConnection) {
        for (BluetoothDevice blueDevice : bluetoothAdapter.getBondedDevices()) {
            if (blueDevice.getName().equals(name)) {
                connectToDevice(blueDevice, insecureConnection);
                return;
            }
        }
    }

    public void connectToName(String name) {
        connectToName(name, false);
    }

    public void connectToDevice(BluetoothDevice device, boolean insecureConnection){
        new ConnectThread(device, insecureConnection).start();
    }

    public void connectToDevice(BluetoothDevice device){
        connectToDevice(device, false);
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (final IOException e) {
            if(communicationCallback!=null) {
                ThreadHelper.run(runOnUi, activity, new Runnable() {
                    @Override
                    public void run() {
                        communicationCallback.onError(e.getMessage());
                    }
                });
            }
        }
    }

    public boolean isConnected(){
        return connected;
    }

    public void send(Command cmd, ObjectOutputStream out){
        try {
            out.writeObject(cmd);
        } catch (final IOException e) {
            connected=false;
            if(communicationCallback!=null){
                ThreadHelper.run(runOnUi, activity, new Runnable() {
                    @Override
                    public void run() {
                        communicationCallback.onDisconnect(device, e.getMessage());
                    }
                });
            }
        }
    }

    public void send(Command cmd){
        send(cmd, this.out);
    }

    public List<BluetoothDevice> getPairedDevices(){
        List<BluetoothDevice> devices = new ArrayList<>();
        devices.addAll(bluetoothAdapter.getBondedDevices());
        return devices;
    }

    public void startScanning(Activity activity){
        // for Android Mashmallow and above
        if (23 <= Build.VERSION.SDK_INT) {
            int accessCoarseLocation = activity.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            List<String> listRequestPermission = new ArrayList<String>();
            if (accessCoarseLocation != PackageManager.PERMISSION_GRANTED) {
                listRequestPermission.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            }

            if (!listRequestPermission.isEmpty()) {
                String[] strRequestPermission = listRequestPermission.toArray(new String[listRequestPermission.size()]);
                activity.requestPermissions(strRequestPermission, REQUEST_CODE_LOC);
            }
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        context.registerReceiver(scanReceiver, filter);
        bluetoothAdapter.startDiscovery();
    }

    public void stopScanning(){
        bluetoothAdapter.cancelDiscovery();
    }

    public void pair(BluetoothDevice device){
        context.registerReceiver(pairReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
        devicePair=device;
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (final Exception e) {
            if(discoveryCallback!=null) {
                ThreadHelper.run(runOnUi, activity, new Runnable() {
                    @Override
                    public void run() {
                        discoveryCallback.onError(e.getMessage());
                    }
                });
            }
        }
    }

    public void unpair(BluetoothDevice device) {
        context.registerReceiver(pairReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
        devicePair=device;
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (final Exception e) {
            if(discoveryCallback!=null) {
                ThreadHelper.run(runOnUi, activity, new Runnable() {
                    @Override
                    public void run() {
                        discoveryCallback.onError(e.getMessage());
                    }
                });
            }
        }
    }

    private class ReceiveThread extends Thread implements Runnable{
        public void run(){
            Command msg;
            try {
                while((msg = (Command)input.readObject()) != null) {
                    if(communicationCallback != null){
                        final Command msgCopy = msg;
                        ThreadHelper.run(runOnUi, activity, new Runnable() {
                            @Override
                            public void run() {
                                communicationCallback.onMessage(msgCopy);
                            }
                        });
                    }
                }
            } catch (final IOException e) {
                connected=false;
                if(communicationCallback != null){
                    ThreadHelper.run(runOnUi, activity, new Runnable() {
                        @Override
                        public void run() {
                            communicationCallback.onDisconnect(device, e.getMessage());
                        }
                    });
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectThread extends Thread {
        ConnectThread(BluetoothDevice device, boolean insecureConnection) {
            Bluetooth.this.device=device;
            try {
                if(insecureConnection){
                    Bluetooth.this.socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
                }
                else{
                    Bluetooth.this.socket = device.createRfcommSocketToServiceRecord(uuid);
                }
                discoveryCallback.onFinish();
            } catch (IOException e) {
                if(communicationCallback!=null){
                    communicationCallback.onError(e.getMessage());
                }
            }
        }

        public void run() {
            bluetoothAdapter.cancelDiscovery();

            try {
                socket.connect();
                out = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
                connected=true;

                new ReceiveThread().start();

                if(communicationCallback!=null) {
                    ThreadHelper.run(runOnUi, activity, new Runnable() {
                        @Override
                        public void run() {
                            communicationCallback.onConnect(device);
                        }
                    });
                }
            } catch (final IOException e) {
                if(communicationCallback!=null) {
                    ThreadHelper.run(runOnUi, activity, new Runnable() {
                        @Override
                        public void run() {
                            communicationCallback.onConnectError(device, e.getMessage());
                        }
                    });
                }

                try {
                    socket.close();
                } catch (final IOException closeException) {
                    if (communicationCallback != null) {
                        ThreadHelper.run(runOnUi, activity, new Runnable() {
                            @Override
                            public void run() {
                                communicationCallback.onError(closeException.getMessage());
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action!=null) {
                switch (action) {
                    case BluetoothAdapter.ACTION_STATE_CHANGED:
                        final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                        if (state == BluetoothAdapter.STATE_OFF) {
                            if (discoveryCallback != null) {
                                ThreadHelper.run(runOnUi, activity, new Runnable() {
                                    @Override
                                    public void run() {
                                        discoveryCallback.onError("Bluetooth turned off");
                                    }
                                });
                            }
                        }
                        break;
                    case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                        context.unregisterReceiver(scanReceiver);
                        if (discoveryCallback != null){
                            ThreadHelper.run(runOnUi, activity, new Runnable() {
                                @Override
                                public void run() {
                                    discoveryCallback.onFinish();
                                }
                            });
                        }
                        break;
                    case BluetoothDevice.ACTION_FOUND:
                        final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if (discoveryCallback != null){
                            ThreadHelper.run(runOnUi, activity, new Runnable() {
                                @Override
                                public void run() {
                                    discoveryCallback.onDevice(device);
                                }
                            });
                        }
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver pairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Log.d("BLE", "PAIR RECEIVER");

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    context.unregisterReceiver(pairReceiver);
                    if(discoveryCallback!=null){
                        ThreadHelper.run(runOnUi, activity, new Runnable() {
                            @Override
                            public void run() {
                                discoveryCallback.onPair(devicePair);
                            }
                        });
                    }
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                    context.unregisterReceiver(pairReceiver);
                    if(discoveryCallback!=null){
                        ThreadHelper.run(runOnUi, activity, new Runnable() {
                            @Override
                            public void run() {
                                discoveryCallback.onUnpair(devicePair);
                            }
                        });
                    }
                }
            }
        }
    };

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action!=null && action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if(bluetoothCallback!=null) {
                    ThreadHelper.run(runOnUi, activity, new Runnable() {
                        @Override
                        public void run() {
                            switch (state) {
                                case BluetoothAdapter.STATE_OFF:
                                    bluetoothCallback.onBluetoothOff();
                                    break;
                                case BluetoothAdapter.STATE_TURNING_OFF:
                                    bluetoothCallback.onBluetoothTurningOff();
                                    break;
                                case BluetoothAdapter.STATE_ON:
                                    bluetoothCallback.onBluetoothOn();
                                    break;
                                case BluetoothAdapter.STATE_TURNING_ON:
                                    bluetoothCallback.onBluetoothTurningOn();
                                    break;
                            }                        }
                    });
                }
            }
        }
    };

    public void setCommunicationCallback(CommunicationCallback communicationCallback) {
        this.communicationCallback = communicationCallback;
    }

    public void removeCommunicationCallback(){
        this.communicationCallback = null;
    }

    public void setDiscoveryCallback(DiscoveryCallback discoveryCallback){
        this.discoveryCallback = discoveryCallback;
    }

    public void removeDiscoveryCallback(){
        this.discoveryCallback = null;
    }

    public void setBluetoothCallback(BluetoothCallback bluetoothCallback){
        this.bluetoothCallback = bluetoothCallback;
    }

    public void removeBluetoothCallback(){
        this.bluetoothCallback = null;
    }
}