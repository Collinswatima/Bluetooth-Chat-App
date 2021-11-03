package com.example.bluetoothchat2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Set;

public class DeviceListActivity<getActiionBar> extends AppCompatActivity {
    private ListView ListPairedDevices, ListAvailableDevices;
    private ProgressBar progressScanDevice;

    private ArrayAdapter<String> AdapterPairedDevices, adapterAvailableDevices;
    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    private Object IntentFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        context= this;

        init();
    }
    private void init(){
        ListPairedDevices=findViewById(R.id.List_paired_devices);
        ListAvailableDevices=findViewById(R.id.List_available_devices);
        progressScanDevice= findViewById(R.id.progress_scan_devices);


        AdapterPairedDevices= new ArrayAdapter<String>(context, R.layout.device_list_item);
        adapterAvailableDevices= new ArrayAdapter<String>(context, R.layout.device_list_item);

        ListPairedDevices.setAdapter(AdapterPairedDevices);
        ListAvailableDevices.setAdapter(adapterAvailableDevices);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices =  bluetoothAdapter.getBondedDevices();

        if (pairedDevices != null && pairedDevices.size() >0 );{
            for (BluetoothDevice device : pairedDevices){
                AdapterPairedDevices.add(device.getName() + "\n" + device.getAddress());
            }
        }
        IntentFilter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothDevicesListener, (android.content.IntentFilter) IntentFilter);
        IntentFilter=new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(bluetoothDevicesListener, (android.content.IntentFilter) IntentFilter);

    }

    private final BroadcastReceiver bluetoothDevicesListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();


            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) ;
                adapterAvailableDevices.add(device.getName() + "\n" + device.getAddress());
            }

        }
        if else(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(getActiionBar))

        {

            progressScanDevice.setVisibility(View.GONE);
            if (adapterAvailableDevices.getCount() == 0) {
                Toast.makeText(context, "no new device found", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "click on the device to start the chat", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_device_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_scan_devices:
                scanDevices();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @SuppressLint("MissingPermission")
    private void scanDevices(){
        progressScanDevice.setVisibility(View.VISIBLE);
        adapterAvailableDevices.clear();
        Toast.makeText(context, "scan started", Toast.LENGTH_SHORT).show();

        if (bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();

        }
        bluetoothAdapter.startDiscovery();

    }
}
