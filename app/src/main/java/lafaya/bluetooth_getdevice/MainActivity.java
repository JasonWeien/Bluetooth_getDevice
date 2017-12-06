package lafaya.bluetooth_getdevice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevice;
    private BluetoothDevice device;
    private BluetoothServerSocket serverSocket;

    private Button button_on,button_off,button_v,button_l;
    private ListView lv;
    List<String> device_add = new ArrayList<String>();
    List<String> device_name = new ArrayList<String>();

    String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_on = (Button)findViewById(R.id.button_on);
        button_off = (Button)findViewById(R.id.button_off);
        button_l = (Button)findViewById(R.id.button_l);
        button_v = (Button)findViewById(R.id.button_v);
        lv = (ListView)findViewById(R.id.listView1);
        BA = BluetoothAdapter.getDefaultAdapter();



        if (!BA.isEnabled()) {
            button_on.setText(R.string.bu_on);
        }
        else{
            button_on.setText(R.string.bu_off);
        //连接设备
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //获取选择的值
                    button_off.setText(device_add.get(i).toString());
                    UUID uuid = UUID.fromString(SPP_UUID);
                    //连接到设备
                    try {
                        BluetoothSocket clienSocket = device.createRfcommSocketToServiceRecord(uuid);
                        BA.cancelDiscovery();
                        clienSocket.connect();
                    }catch (IOException e){}

                }
            });

        }
    //显示设备清单
        List<String> names = new ArrayList<String>();
        for(BluetoothDevice bt:BA.getBondedDevices()){
            names.add(bt.getName());
        }
        Toast.makeText(getApplicationContext(),"Showing Paired Devices",Toast.LENGTH_LONG).show();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        lv.setAdapter(adapter);

    //
    }
//打开蓝牙设备
    public void on(View view){

        if (!BA.isEnabled()) {

            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);

            button_on.setText(R.string.bu_off);
            Toast.makeText(getApplicationContext(), "Turned on", Toast.LENGTH_LONG).show();


        } else {
            //Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
            button_on.setText(R.string.bu_on);
            BA.disable();

            Toast.makeText(getApplicationContext(),"Turned off",Toast.LENGTH_LONG).show();
        }

    }

    //获取设备列表
    public void list(View view){
        List<String> names = new ArrayList<String>();
        for(BluetoothDevice bt:BA.getBondedDevices()){
            names.add(bt.getName()+"\n"+bt.getAddress());
            device_add.add(bt.getAddress());
            device_name.add(bt.getName());
        }
        Toast.makeText(getApplicationContext(),"Showing Paired Devices",Toast.LENGTH_LONG).show();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        lv.setAdapter(adapter);
    }
//关闭蓝牙设备
    public void off(View view){
        BA.disable();
        Toast.makeText(getApplicationContext(),"Turned off",Toast.LENGTH_LONG).show();
    }

    public void visible(View view){
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible,0);
    }

    //选择蓝牙设备进行配对

}
