package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//TODO: MAKE SURE THE APP DOESN"T CRASH WHEN IT RECEIVES WRONG INPUT -> receiving 51 par example for response time causes it to crash
public class SettingsFragment extends Fragment {
    View view;
    Button defaultSettings;
    Button zeroClear;
    Spinner pressure;
    Spinner display;
    Spinner CH1LED;
    Spinner CH2LED;
    Spinner CH3LED;
    Spinner analogue;
    Spinner response;
    Spinner refresh;

    MyViewModel viewModel;
    private boolean send = false;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        send = false;
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        //viewModel.stopSendingData();

        pressure = view.findViewById(R.id.pu_spinner);
        pressure.setAdapter(ArrayAdapter.createFromResource(requireActivity(),
                R.array.pressure_settings, R.layout.spinner_item));

        display = view.findViewById(R.id.dr_spinner);
        display.setAdapter(ArrayAdapter.createFromResource(requireActivity(),
                R.array.display_resolution, R.layout.spinner_item));

        CH1LED = view.findViewById(R.id.ch1_led_spinner);
        CH1LED.setAdapter(ArrayAdapter.createFromResource(requireActivity(),
                R.array.led_settings, R.layout.spinner_item_small));

        CH2LED = view.findViewById(R.id.ch2_led_spinner);
        CH2LED.setAdapter(ArrayAdapter.createFromResource(requireActivity(),
                R.array.led_settings, R.layout.spinner_item_small));

        CH3LED = view.findViewById(R.id.ch3_led_spinner);
        CH3LED.setAdapter(ArrayAdapter.createFromResource(requireActivity(),
                R.array.led_settings, R.layout.spinner_item_small));

        analogue = view.findViewById(R.id.ao_spinner);
        analogue.setAdapter(ArrayAdapter.createFromResource(requireActivity(),
                R.array.analogue_output, R.layout.spinner_item));

        response = view.findViewById(R.id.rt_spinner);
        response.setAdapter(ArrayAdapter.createFromResource(requireActivity(),
                R.array.response_time, R.layout.spinner_item));

        refresh = view.findViewById(R.id.rr_spinner);
        refresh.setAdapter(ArrayAdapter.createFromResource(requireActivity(),
                R.array.refresh_rate, R.layout.spinner_item));

        defaultSettings = view.findViewById(R.id.default_settings_button);
        zeroClear = view.findViewById(R.id.zero_clear_button);
        spinnerSetUp();
        setUp();
        setUpObservers();
        setDefaultSettingsListener();
        setZeroClearListener();
        return view;
    }

    public void setUpObservers(){
        viewModel.getResponseTime().observe(getViewLifecycleOwner(), rt -> {
            viewModel.setLittleResponseTime(rt);
            response.setSelection(rt);
        });

        viewModel.getPressureUnit().observe(getViewLifecycleOwner(), pu -> {
            viewModel.setLittlePressure(pu);
            pressure.setSelection(pu);
        });

        viewModel.getRefreshRate().observe(getViewLifecycleOwner(), rr -> {
            viewModel.setLittleRefreshRate(rr);
            int interest = rr;
            refresh.setSelection(rr);
        });

        viewModel.getDisplayResolution().observe(getViewLifecycleOwner(), dp -> {
            viewModel.setLittleDisplayResolution(dp);
            int intererest = dp;
            display.setSelection(dp);
        });

        viewModel.getCH1LED().observe(getViewLifecycleOwner(), led -> {
            viewModel.setLittleCH1LED(led);
            int interested = led;
            CH1LED.setSelection(led);
        });

        viewModel.getCH2LED().observe(getViewLifecycleOwner(), led -> {
            viewModel.setLittleCH2LED(led);
            int interested = led;
            CH2LED.setSelection(led);
        });

        viewModel.getCH3LED().observe(getViewLifecycleOwner(), led -> {
            viewModel.setLittleCH3LED(led);
            int interested = led;
            CH3LED.setSelection(led);
        });

        viewModel.getAnalogueOutput().observe(getViewLifecycleOwner(), ao -> {
            viewModel.setLittleAnalougeOutput(ao);
            int interested = ao;
            analogue.setSelection(ao);
        });

        send = true;
    }

    public void setDefaultSettingsListener(){
        defaultSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.defaultSettings();
            }
        });
    }

    public void setZeroClearListener(){
        zeroClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.zeroClear();
            }
        });
    }

    public void setUp(){
        response.setSelection(viewModel.getLittleResponseTime());
        pressure.setSelection(viewModel.getLittlePressure());
        refresh.setSelection(viewModel.getLittleRefreshRate());
        display.setSelection(viewModel.getLittleDisplayResolution());
        CH1LED.setSelection(viewModel.getLittleCH1LED());
        CH2LED.setSelection(viewModel.getLittleCH2LED());
        CH3LED.setSelection(viewModel.getLittleCH3LED());
        analogue.setSelection(viewModel.getLittleAnalougeOutput());
    }
    public void spinnerSetUp(){
        pressure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //DO NOTHING
                        break;
                    case 1:
                        if(send){
                            viewModel.setMpa();
                        }
                        break;
                    case 2:
                        if(send){
                            viewModel.setKpa();
                        }
                        break;
                    case 3:
                        if(send){
                            viewModel.setKgf();
                        }
                        break;
                    case 4:
                        if(send){
                            viewModel.setBar();
                        }
                        break;
                    case 5:
                        if(send){
                            viewModel.setPsi();
                        }
                        break;
                    case 6:
                        if(send){
                            viewModel.setMmhg();
                        }
                        break;
                    case 7:
                        if(send){
                            viewModel.setCmhg();
                        }
                        break;
                    case 8:
                        if(send){
                            viewModel.setInchhg();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        display.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //DO NOTHING
                        break;
                    case 2:
                        if(send){
                            viewModel.set2dp();
                        }
                        break;
                    case 1:
                        if(send){
                            viewModel.set3dp();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        analogue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //DO NOTHING
                        break;
                    case 1:
                        if(send){
                            viewModel.setAO5V();
                        }
                        break;
                    case 2:
                        if(send){
                            viewModel.setAO10V();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        response.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //DO NOTHING
                        break;
                    case 1:
                        if(send){
                            viewModel.setResponse2();
                        }
                        break;
                    case 2:
                        if(send){
                            viewModel.setResponse20();
                        }
                        break;
                    case 3:
                        if(send){
                            viewModel.setResponse50();
                        }
                        break;
                    case 4:
                        if(send){
                            viewModel.setResponse100();
                        }
                        break;
                    case 5:
                        if(send){
                            viewModel.setResponse200();
                        }
                        break;
                    case 6:
                        if(send){
                            viewModel.setResponse500();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        refresh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //DO NOTHING
                        break;
                    case 1:
                        if(send){
                            viewModel.setRefresh200();
                        }
                        break;
                    case 2:
                        if(send){
                            viewModel.setRefresh500();
                        }
                        break;
                    case 3:
                        if(send){
                            viewModel.setRefresh1000();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CH1LED.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LED(1,position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        CH2LED.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LED(2,position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        CH3LED.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LED(3,position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void LED(int channel, int position){
        if(send){
            switch (position){
                case 0:
                    //DO NOTHING
                    break;
                case 1:
                    viewModel.setGreenOnRedOff(channel);
                    break;
                case 2:
                    viewModel.setRedOnGreenOff(channel);
                    break;
                case 3:
                    viewModel.setNormallyRed(channel);
                    break;
                case 4:
                    viewModel.setNormallyGreen(channel);
                    break;
            }
        }
    }
}