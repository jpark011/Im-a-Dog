package com.cs446w18.a16.imadog.bluetooth;

import android.bluetooth.BluetoothDevice;

public interface DiscoveryCallback{
    void onFinish();
    void onDevice(BluetoothDevice device);
    void onPair(BluetoothDevice device);
    void onUnpair(BluetoothDevice device);
    void onError(String message);
}