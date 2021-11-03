package com.example.bluetoothchat2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private BluetoothAdapter bluetoothAdapter;

    private final int LOCATION_PERMISSION_REQUEST=101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        initBluetooth();
    }
    private void initBluetooth(){
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter==null){
            Toast.makeText(context, "no bluetooth found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search_devices:
                checkPermission();
                return true;
            case R.id.menu_enable_bluetooth:
                enableBluetooth();
            default:
                return super.onOptionsItemSelected(item);
        }


    }
    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)!= getPackageManager().PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String []{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);

        }
        else {
            Intent intent= new Intent(context, DeviceListActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==LOCATION_PERMISSION_REQUEST){
            if (grantResults.length >0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED){
                Intent intent= new Intent(context, DeviceListActivity.class);
                startActivity(intent);

            }
            else {
                new AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setMessage("location permission is required .\n please grant")
                        .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                checkPermission();

                            }
                        })
                        .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.this.finish();
                            }
                        })
                        .show();
            }
        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressLint("MissingPermission")
    private void enableBluetooth(){
        if(bluetoothAdapter.isEnabled()){
                 bluetoothAdapter.enable();
            }
        if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            Intent discoveryIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoveryIntent);

        }

    }
}