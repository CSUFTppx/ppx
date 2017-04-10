package csuft.ppx.indoorlocation;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import csuft.ppx.indoorlocation.beacon.Beacon;
import csuft.ppx.indoorlocation.beacon.BeaconOperation;

public class MainActivity extends Activity {

    private static TextView tv;
    private Button btn;
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tv.setText(msg.obj.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);
        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BeaconOperation.getInstance().init(MainActivity.this);
                BeaconOperation.getInstance().startScanLe(100);
            }
        });
    }
}
