package com.cs446w18.a16.imadog.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.cs446w18.a16.imadog.commands.Command;

public interface CommunicationCallback{
    void onConnect(BluetoothDevice device);
    void onDisconnect(BluetoothDevice device, String message);
    void onMessage(Command command);
    void onError(String message);
    void onConnectError(BluetoothDevice device, String message);
    void onAccept(BluetoothSocket socket);
    void onRequest(Command command);
}