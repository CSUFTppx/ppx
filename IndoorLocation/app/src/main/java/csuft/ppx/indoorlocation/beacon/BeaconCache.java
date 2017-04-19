package csuft.ppx.indoorlocation.beacon;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import csuft.ppx.indoorlocation.beacon.utils.DefaultStaticValues;
import csuft.ppx.indoorlocation.position.Point;
import csuft.ppx.indoorlocation.position.PositionUtil;

/**
 * Created by zf on 2017/3/27.
 */
public class BeaconCache {

    private static final String TAG = "BeaconCache";
    private static BeaconCache mINSTANCE;
    private static int interval;//刷新间隔
    private static Map<String, List<Beacon>> cache = new HashMap<>();//缓存区
    private static List<Beacon> result = new ArrayList<>();

    public static BeaconCache getInstance() {
        if (mINSTANCE == null) {
            mINSTANCE = new BeaconCache();
        }
        return mINSTANCE;
    }

    private BeaconCache() {
        interval = 2000;//刷新间隔默认为2s
        new Looper(interval);
    }

    public void put(Beacon beacon) {
        String mac = beacon.mac;
        if (mac.equals(DefaultStaticValues.DEFAULT_BEACON_DEVICE_ADDRESS)) {
            Log.i(TAG, "无效beacon");
            return;
        }
        if (cache.containsKey(mac)) {
            cache.get(mac).add(beacon);
        } else {
            List<Beacon> beaconList = new ArrayList<>();
            beaconList.add(beacon);
            cache.put(mac, beaconList);
        }
    }

    //Looper循环处理数据
    private class Looper {
        int range = 3;
        int interval;
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                doTask();
            }
        };

        public Looper(int interval) {
            //设置时间间隔，定时执行任务
            this.interval = interval;
            timer.schedule(timerTask, interval, interval);
        }

        //执行处理数据任务
        private synchronized void doTask() {
            result.clear();
            if (!cache.isEmpty()) {
                for (List<Beacon> beaconList : cache.values()) {
                    //按rssi降序排列
                    Collections.sort(beaconList, new Comparator<Beacon>() {
                        @Override
                        public int compare(Beacon beacon, Beacon t1) {
                            return t1.rssi - beacon.rssi;
                        }
                    });
                    if (beaconList.size() >= 3) {
                        Beacon currentBeacon = beaconList.get(0);
                        //去掉最大值和最小值
                        beaconList.remove(0);
                        beaconList.remove(beaconList.size() - 1);
                        currentBeacon.rssi = hitTarget(beaconList);
                        currentBeacon.distance = currentBeacon.calculateAccuracy(currentBeacon.measuredPower, currentBeacon.rssi);
                        result.add(currentBeacon);
                        System.out.println("mac:"+currentBeacon.mac+"   RSSI****************"+currentBeacon.rssi);
                    } else {
                        Log.i(TAG, "beacon列表过短，丢弃");
                    }
                }
                Collections.sort(result, new Comparator<Beacon>() {
                    @Override
                    public int compare(Beacon o1, Beacon o2) {
                        return (int) (o1.distance - o2.distance);
                    }
                });
                for (int i = 0; i < result.size(); i++) {
                    System.out.println(result.get(i).mac + " | " + result.get(i).measuredPower + " | " + result.get(i).rssi + " | " + result.get(i).distance);
                }
                result = result.subList(0, 3);
                System.out.println("result size:"+result.size());

                Point point=PositionUtil.getIstance().Position(result);
                System.out.println("定位坐标为   ("+point.getX()+","+point.getY()+")");
                cache.clear();//清空缓存区
            } else {
                Log.i(TAG, "缓存区为空");
                return;
            }
            System.out.println("@@@@@@@@@@@@@@@@@@@");
        }

        //命中
        private synchronized int hitTarget(List<Beacon> beaconList) {
            int length = beaconList.size();
            int MAX_COUNT = 0;
            int MAX_SCALE_RSSI = 0;//占最大比例的RSSI
            int target = 0;
            List<Integer> rangeList = new ArrayList<>();
            if (!beaconList.isEmpty()) {
                for (int i = 0; i < length; i++) {
                    rangeList.clear();
                    int currentRSSI = beaconList.get(i).rssi;//当前RSSI
                    int count = 0;//累加数
                    for (Beacon beacon : beaconList) {
                        if (Math.abs(currentRSSI - beacon.rssi) < range) {
                            rangeList.add(beacon.rssi);
                            count++;
                            continue;
                        }
                    }
                    //存储最大比例
                    if (rangeList.size() > MAX_COUNT) {
                        MAX_COUNT = rangeList.size();
                        MAX_SCALE_RSSI = currentRSSI;
                    }
                }
                if (rangeList.size() % 2 == 0) {
                    target = (rangeList.get(rangeList.size() / 2) + rangeList.get(rangeList.size() / 2 - 1)) / 2;
                } else {
                    target = rangeList.get(rangeList.size() / 2);
                }
            }
            return MAX_SCALE_RSSI;
        }
    }
}


