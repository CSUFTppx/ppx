package csuft.ppx.indoorlocation.beacon;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;

/**
 * Created by zf on 2017/3/27.
 */
public class BeaconOperation {

    private static BeaconOperation ourInstance = new BeaconOperation();
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
        bluetoothAdapter.startLeScan(new ScanCallBack());
    }


    //扫描回调
    class ScanCallBack implements BluetoothAdapter.LeScanCallback{

        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            //从广播转化成beacon对象
            Beacon beacon = Beacon.fromScanData2Beacon(bluetoothDevice, i, bytes);
            //把接收到的beacon对象放入缓存区
            BeaconCache.getInstance().put(beacon);
        }
    }


}
