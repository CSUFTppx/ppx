package csuft.ppx.indoorlocation.beacon;

import android.bluetooth.BluetoothDevice;

import java.util.Comparator;

import csuft.ppx.indoorlocation.beacon.utils.DefaultStaticValues;

/**
 * Created by zf on 2017/3/27.
 */

public class Beacon {
    //设备地址
    public String mac = DefaultStaticValues.DEFAULT_BEACON_DEVICE_ADDRESS;
    //场强
    public int rssi = DefaultStaticValues.DEFAULT_BEACON_RSSI_FALSE;
    //uuid
    public String uuid = DefaultStaticValues.DEFAULT_BEACON_PROXIMITY_UUID;
    //测量功率
    public int measuredPower = DefaultStaticValues.DEFAULT_BEACON_MEASURED_POWER_FALSE;
    //发射功率
    public int txPower = DefaultStaticValues.DEFAULT_SKY_BEACON_TXPOWER_FALSE;

    public static Beacon resolveFromBroadcast(BluetoothDevice device, int rssi, byte[] data) {
        Beacon beacon = new Beacon();
        beacon.mac = device.getAddress();
        beacon.rssi = rssi;
        return beacon;
    }

    public static Beacon fromScanData2Beacon(BluetoothDevice device, int rssi, byte[] data) {
        if (data == null) {
            return null;
        }
        int startByte = 2;
        boolean patternFound = false;
        int responseStartByte = 34;
        boolean responseFonud = false;
        while (startByte <= 5) {
            if (((int) data[startByte + 30] & 0xff) == 0x86 && ((int) data[startByte + 31] & 0xff) == 0x43) {
                responseStartByte = 34;
                responseFonud = true;
            }
            if (((int) data[startByte + 31] & 0xff) == 0x86 && ((int) data[startByte + 32] & 0xff) == 0x43) {
                responseStartByte = 35;
                responseFonud = true;
            }
            if (((int) data[startByte + 2] & 0xff) == 0x02 && ((int) data[startByte + 3] & 0xff) == 0x15) {
                // yes! This is an iBeacon
                patternFound = true;
                break;
            } else if (((int) data[startByte] & 0xff) == 0x2d && ((int) data[startByte + 1] & 0xff) == 0x24 && ((int) data[startByte + 2] & 0xff) == 0xbf && ((int) data[startByte + 3] & 0xff) == 0x16) {
                Beacon beacon = new Beacon();
                return beacon;
            } else if (((int) data[startByte] & 0xff) == 0xad && ((int) data[startByte + 1] & 0xff) == 0x77 && ((int) data[startByte + 2] & 0xff) == 0x00 && ((int) data[startByte + 3] & 0xff) == 0xc6) {
                Beacon beacon = new Beacon();
                return beacon;
            }
            startByte++;
        }

        if (patternFound == false) {
            // This is not an iBeacon
            return new Beacon();
        }

        Beacon beacon = new Beacon();
        if (responseFonud) {
            //解密数据
            byte[] decrypted = new byte[27];
            for(int i = 0; i< 27; i++){
                decrypted[i] = (byte) (data[responseStartByte+i] ^ data[responseStartByte+26]);
            }
            beacon.txPower = (int) decrypted[21];
        }
        beacon.mac = device.getAddress();
        beacon.rssi = rssi;
        beacon.measuredPower = (int) data[startByte + 24];
        //************uuid未完成

        return beacon;
    }

}
