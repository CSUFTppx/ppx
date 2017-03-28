package csuft.ppx.indoorlocation.beacon;

import android.bluetooth.BluetoothDevice;

/**
 * Created by zf on 2017/3/27.
 */

public class Beacon {
    public String mac;
    public int rssi;
    public String uuid;

    public static Beacon resolveFromBroadcast(BluetoothDevice device, int rssi, byte[] data) {
        Beacon beacon = new Beacon();
        beacon.mac = device.getAddress();
        beacon.rssi = rssi;
        return beacon;
    }
}
