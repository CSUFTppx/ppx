package csuft.ppx.indoorlocation.beacon;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zf on 2017/3/27.
 */
public class BeaconOperation {

    private static BeaconOperation ourInstance = new BeaconOperation();
    private static List<BeaconCache> cacheList = new ArrayList<>();
    public static List<Beacon> beaconList = new ArrayList<>();
    private static BluetoothManager bluetoothManager;
    private static BluetoothAdapter bluetoothAdapter;

    public static BeaconOperation getInstance() {
        return ourInstance;
    }

    private BeaconOperation() {
    }

    public void init(Context context) {
        bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    public void startScanLe(int interval) {
        bluetoothAdapter.startLeScan(new ScanCallBack(interval));
    }


    //扫描回调
    class ScanCallBack implements BluetoothAdapter.LeScanCallback{
        int interval;
        public ScanCallBack(int interval) {
            this.interval = interval;
        }

        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            //从广播转化成beacon对象
            Beacon beacon = (new Beacon()).resolveFromBroadcast(bluetoothDevice, i, bytes);
            //把接收到的beacon对象放入缓存区
            if (!cacheList.isEmpty()) {
                for (BeaconCache beaconCache : cacheList) {
                    //存在beacon缓冲区，则放入缓冲区的缓存队列
                    if (beaconCache.beaconMac.equals(beacon.mac)) {
                        beaconCache.put(beacon);
                        return;
                    }
                }
            }
            //不存在beacon，发现新设备
            BeaconCache beaconCache = new BeaconCache(this.interval, beacon.mac);
            beaconCache.put(beacon);
            cacheList.add(beaconCache);
        }
    }


}
