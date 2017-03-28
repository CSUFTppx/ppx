package csuft.ppx.indoorlocation.beacon;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zf on 2017/3/27.
 */
public class BeaconCache {

    public int interval;
    public String beaconMac;
    private List<Beacon> beacons = new ArrayList<>();
    private Beacon result = null;

    public BeaconCache(int interval, String beaconMac) {
        this.interval = interval;
        this.beaconMac = beaconMac;
        new Looper(this.interval);
    }

    public void put(Beacon beacon) {
        if (beacon != null) {
            beacons.add(beacon);
        }
    }

    public Beacon getResult() {
        return result;
    }

    public Beacon getAverage() {
        Beacon location = null;
        int averageRssi = 0;
        if (!beacons.isEmpty()) {
            int count = 0;
            for (Beacon beacon : beacons) {
                count = count + beacon.rssi;
            }
            averageRssi = count / beacons.size();
            location = beacons.get(0);
            location.rssi = averageRssi;
            //清空List，重置缓存
            beacons.clear();
        }
        System.out.println(beaconMac+" : "+location.rssi);
        return location;
    }

    //Looper循环处理数据
    class  Looper {
        int interval;
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                doTask();
            }
        };

        public Looper(int interval) {
            this.interval = interval;
            timer.schedule(timerTask, interval, interval);
        }

        private synchronized void doTask() {
            Beacon location = null;
            int averageRssi = 0;
            if (!beacons.isEmpty()) {
                int count = 0;
                for (Beacon beacon : beacons) {
                    count = count + beacon.rssi;
                }
                averageRssi = count / beacons.size();
                location = beacons.get(0);
                location.rssi = averageRssi;
                //清空List，重置缓存
                beacons.clear();
            }
            result = location;
            System.out.println("已处理缓冲数据rssi:"+result.rssi);
        }
    }
}
