package com.example.myapplication;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class Connection extends Fragment {
    MyViewModel viewModel;
    View view;
    ImageButton check;
    ImageButton cancel;
    TextView psdID;
    BluetoothAdapter bluetoothAdapter;
    String desiredDeviceName;
    BluetoothDevice desiredDevice;

    public Connection() {
        // Required empty public constructor
    }

    public static Connection newInstance() {
        Connection fragment = new Connection();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_connection, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        check = view.findViewById(R.id.tick_button);
        cancel = view.findViewById(R.id.cancel_button);
        psdID = view.findViewById(R.id.psd_id_textview);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        setCheck();
        setCancel();

        viewModel.getReadNFC().observe(getViewLifecycleOwner(), readNFC -> {
            if(readNFC != null){
                psdID.setText("looking for " + readNFC);
                desiredDeviceName = readNFC;
                if(!bluetoothAdapter.isEnabled()){
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 0);
                }

                Intent discoverableIntent =
                        new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivityForResult(discoverableIntent, 0);

                btDiscover();
            }
        });

        return view;
    }

    public void setCheck(){
        check.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(desiredDevice != null){
                    //TODO: go to home page
                    ((MainActivity) requireActivity()).navigateToHome();
                }
            }
        });
    }

    public void setCancel(){
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void btDiscover(){
        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }

        checkBTPermissions();
        bluetoothAdapter.startDiscovery();
        IntentFilter discoverDevices = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        requireActivity().registerReceiver(receiver,discoverDevices);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = requireActivity().checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += requireActivity().checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            //Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                if(desiredDevice == null){
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    //psdID.setText("found device: " + device.getName() + " desired: " + desiredDeviceName);
                    String currentDeviceName = device.getName() + "\n";
                    if(desiredDeviceName.equals(currentDeviceName)){
                        psdID.setText("connect to " + currentDeviceName + "?");
                        desiredDevice = device;
                        viewModel.setBluetoothDeviceName(device.getName());
                        viewModel.startBluetoothConnection(bluetoothAdapter,device);
                    }
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(receiver);
    }
}