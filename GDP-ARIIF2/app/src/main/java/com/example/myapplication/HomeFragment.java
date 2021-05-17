package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DecimalFormat;

public class HomeFragment extends Fragment {
    private EditText hi;
    private EditText lo;
    private EditText hy;
    private View view;
    private MyViewModel viewModel;
    private Button ch1;
    private Button ch2;
    private Button ch3;
    private Switch opMode;
    private Switch noncMode;
    private Button bottomHold;
    private Button live;
    private Button peakHold;
    private Button peakReset;
    private Button bottomReset;
    private Button update;
    private TextView ch1Val;
    private TextView ch2Val;
    private TextView ch3Val;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        view = inflater.inflate(R.layout.fragment_home, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);

        hi = view.findViewById(R.id.hi_output);
        lo = view.findViewById(R.id.lo_output);
        hy = view.findViewById(R.id.hyst_output);

        ch1 = view.findViewById(R.id.ch1_button);
        ch2 = view.findViewById(R.id.ch2_button);
        ch3 = view.findViewById(R.id.ch3_button);

        opMode = view.findViewById(R.id.op_mode);
        noncMode = view.findViewById(R.id.nonc_mode);
        bottomHold = view.findViewById(R.id.bottom_button);
        live = view.findViewById(R.id.live_button);
        peakHold = view.findViewById(R.id.peak_button);
        bottomReset = view.findViewById(R.id.bottom_reset_button);
        peakReset = view.findViewById(R.id.peak_reset_button);
        update = view.findViewById(R.id.update_settings);

        ch1Val = view.findViewById(R.id.ch1);
        ch2Val = view.findViewById(R.id.ch2);
        ch3Val = view.findViewById(R.id.ch3);

        viewModel.setReadingSetting(2);
        viewModel.selectChannel(1);

        setCH1Listener();
        setCH2Listener();
        setCH3Listener();
        setOMListener();
        setNONCListener();
        setBottomListener();
        setLiveListener();
        setPeakListener();
        setUpdateListener();
        setPeakResetListener();
        setBottomResetListener();

        setUp();

        //viewModel.startSendingData();

        TextView connectedTo = view.findViewById(R.id.connected_to_psd);
        viewModel.getBluetoothDeviceName().observe(getViewLifecycleOwner(), deviceName -> {
            connectedTo.setText("Connected to PSD: " + deviceName);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public String convert(int pressureUnit, Float value, int dp){
        String answer = "";
        DecimalFormat df = new DecimalFormat();
        if(dp == 1){
            df.setMaximumFractionDigits(2);
        }
        if(dp == 2){
            df.setMaximumFractionDigits(3);
        }
        switch(pressureUnit){
            case 1:
                //DO NOTHING
                answer = df.format(value);
                break;
            case 2:
                answer = df.format(value*viewModel.KPAMAX);
                break;
            case 3:
                answer = df.format(value*viewModel.KGFCM2MAX);
                break;
            case 4:
                answer = df.format(value*viewModel.BARMAX);
                break;
            case 5:
                answer = df.format(value*viewModel.PSIMAX);
                break;
            case 6:
                answer = df.format(value*viewModel.MMHGMAX);
                break;
            case 7:
                answer = df.format(value*viewModel.CMHGMAX);
                break;
            case 8:
                answer = df.format(value*viewModel.INCHHGMAX);
                break;
        }
        return answer.replace(",","");
    }

    public void flipOMSwitch(){
        opMode.setOnCheckedChangeListener(null);
        opMode.setChecked(!opMode.isChecked());
        setOMListener();
    }

    public void flipNONCSwitch(){
        noncMode.setOnCheckedChangeListener(null);
        noncMode.setChecked(!noncMode.isChecked());
        setNONCListener();
    }

    public void setUp(){
        int channelSelected = viewModel.getChannelSelected();
        switch (channelSelected){
            case 1:
                viewModel.getCH1OM().observe(getViewLifecycleOwner(), CH1OM -> {
                    viewModel.setIntCH1OM(CH1OM);
                    if(CH1OM == 0){
                        //if it is on 1 (set to comp) we want to flip it
                        if(opMode.isChecked()){
                            flipOMSwitch();
                        }
                    } else {
                        if(!opMode.isChecked()){
                            flipOMSwitch();
                        }
                    }
                });
                viewModel.getCH1NONC().observe(getViewLifecycleOwner(), NONC -> {
                    if(NONC == 0){
                        if(noncMode.isChecked()){
                            flipNONCSwitch();
                        }
                    } else {
                        if(!noncMode.isChecked()){
                            flipNONCSwitch();
                        }
                    }
                });

                ch1.setBackgroundColor(getResources().getColor(R.color.purple_200));
                readCH1Settings();
                break;
            case 2:
                viewModel.getCH2OM().observe(getViewLifecycleOwner(), CH2OM -> {
                    if(CH2OM == 0){
                        //if it is on 1 (set to comp) we want to flip it
                        if(opMode.isChecked()){
                            flipOMSwitch();
                        }
                    } else {
                        if(!opMode.isChecked()){
                            flipOMSwitch();
                        }
                    }
                });
                viewModel.getCH2NONC().observe(getViewLifecycleOwner(), NONC -> {
                    if(NONC == 0){
                        if(noncMode.isChecked()){
                            flipNONCSwitch();
                        }
                    } else {
                        if(!noncMode.isChecked()){
                            flipNONCSwitch();
                        }
                    }
                });

                ch2.setBackgroundColor(getResources().getColor(R.color.purple_200));
                readCH2Settings();
                break;
            case 3:
                viewModel.getCH3OM().observe(getViewLifecycleOwner(), CH3OM -> {
                    if(CH3OM == 0){
                        //if it is on 1 (set to comp) we want to flip it
                        if(opMode.isChecked()){
                            flipOMSwitch();
                        }
                    } else {
                        if(!opMode.isChecked()){
                            flipOMSwitch();
                        }
                    }
                });
                viewModel.getCH3NONC().observe(getViewLifecycleOwner(), NONC -> {
                    if(NONC == 0){
                        if(noncMode.isChecked()){
                            flipNONCSwitch();
                        }
                    } else {
                        if(!noncMode.isChecked()){
                            flipNONCSwitch();
                        }
                    }
                });

                ch3.setBackgroundColor(getResources().getColor(R.color.purple_200));
                readCH3Settings();
                break;
        }
    }

    public void readCH1Settings(){
        if(viewModel.getIntCH1OM() == 0){
            //if it is on 1 (set to comp) we want to flip it
            if(opMode.isChecked()){
                flipOMSwitch();
            }
        } else {
            if(!opMode.isChecked()){
                flipOMSwitch();
            }
        }

        viewModel.getCH1_OUT1_HI().observe(getViewLifecycleOwner(), hi_value -> {
            if(viewModel.getChannelSelected() == 1){
                hi.setText(convert(viewModel.getLittlePressure(),hi_value,viewModel.getLittleDisplayResolution()).toString());
            }
        });

        viewModel.getCH1_OUT1_LO().observe(getViewLifecycleOwner(), lo_value ->{
            if(viewModel.getChannelSelected() == 1){
                lo.setText(convert(viewModel.getLittlePressure(),lo_value,viewModel.getLittleDisplayResolution()).toString());
            }
        });

        viewModel.getCH1_OUT1_HY().observe(getViewLifecycleOwner(), hy_value ->{
            if(viewModel.getChannelSelected() == 1){
                hy.setText(convert(viewModel.getLittlePressure(),hy_value,viewModel.getLittleDisplayResolution()).toString());
            }
        });
    }

    public void readCH2Settings(){
        viewModel.getCH2_OUT1_HI().observe(getViewLifecycleOwner(), hi_value -> {
            if(viewModel.getChannelSelected() == 2){
                hi.setText(convert(viewModel.getLittlePressure(),hi_value,viewModel.getLittleDisplayResolution()).toString());
            }
        });

        viewModel.getCH2_OUT1_LO().observe(getViewLifecycleOwner(), lo_value ->{
            if(viewModel.getChannelSelected() == 2){
                lo.setText(convert(viewModel.getLittlePressure(),lo_value,viewModel.getLittleDisplayResolution()).toString());
            }
        });

        viewModel.getCH2_OUT1_HY().observe(getViewLifecycleOwner(), hy_value ->{
            if(viewModel.getChannelSelected() == 2){
                hy.setText(convert(viewModel.getLittlePressure(),hy_value,viewModel.getLittleDisplayResolution()).toString());
            }
        });
    }

    public void readCH3Settings(){
        viewModel.getCH3_OUT1_HI().observe(getViewLifecycleOwner(), hi_value -> {
            if(viewModel.getChannelSelected() == 3){
                hi.setText(convert(viewModel.getLittlePressure(),hi_value,viewModel.getLittleDisplayResolution()).toString());
            }
        });

        viewModel.getCH3_OUT1_LO().observe(getViewLifecycleOwner(), lo_value ->{
            if(viewModel.getChannelSelected() == 3){
                lo.setText(convert(viewModel.getLittlePressure(),lo_value,viewModel.getLittleDisplayResolution()).toString());
            }
        });

        viewModel.getCH3_OUT1_HY().observe(getViewLifecycleOwner(), hy_value ->{
            if(viewModel.getChannelSelected() == 3){
                hy.setText(convert(viewModel.getLittlePressure(),hy_value,viewModel.getLittleDisplayResolution()).toString());
            }
        });
    }

    public void setUpdateListener(){
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Float hi_value = Float.parseFloat(hi.getText().toString());
                Float lo_value = Float.parseFloat(lo.getText().toString());
                Float hy_value = Float.parseFloat(hy.getText().toString());

                hi.setText(Float.toString(hi_value));
                lo.setText(Float.toString(lo_value));
                hy.setText(Float.toString(hy_value));

                int channelSelected = viewModel.getChannelSelected();

                switch (channelSelected){
                    case 1:
                        viewModel.getCH1OM().observe(getViewLifecycleOwner(), OM -> {
                            viewModel.update(channelSelected,hi_value,lo_value,hy_value, OM);
                        });
                        break;
                    case 2:
                        viewModel.getCH2OM().observe(getViewLifecycleOwner(), OM -> {
                            viewModel.update(channelSelected,hi_value,lo_value,hy_value, OM);
                        });
                        break;
                    case 3:
                        viewModel.getCH3OM().observe(getViewLifecycleOwner(), OM -> {
                            viewModel.update(channelSelected,hi_value,lo_value,hy_value, OM);
                        });
                        break;
                }

            }
        });
    }

    public void setCH1Listener(){
        ch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.selectChannel(1);
                ch1.setBackgroundColor(getResources().getColor(R.color.purple_200));
                ch2.setBackgroundColor(getResources().getColor(R.color.purple_700));
                ch3.setBackgroundColor(getResources().getColor(R.color.purple_700));
                readCH1Settings();

                viewModel.getCH1OM().observe(getViewLifecycleOwner(), CH1OM -> {
                    if(CH1OM == 0){
                        if(opMode.isChecked()){
                            flipOMSwitch();
                        }
                    } else {
                        if(!opMode.isChecked()){
                            flipOMSwitch();
                        }
                    }
                });

                viewModel.getCH1NONC().observe(getViewLifecycleOwner(), NONC -> {
                    if(NONC == 0){
                        if(noncMode.isChecked()){
                            flipNONCSwitch();
                        }
                    } else {
                        if(!noncMode.isChecked()){
                            flipNONCSwitch();
                        }
                    }
                });
            }
        });
    }

    public void setCH2Listener(){
        ch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.selectChannel(2);
                ch1.setBackgroundColor(getResources().getColor(R.color.purple_700));
                ch2.setBackgroundColor(getResources().getColor(R.color.purple_200));
                ch3.setBackgroundColor(getResources().getColor(R.color.purple_700));
                readCH2Settings();

                viewModel.getCH2OM().observe(getViewLifecycleOwner(), CH2OM -> {
                    if(CH2OM == 0){
                        if(opMode.isChecked()){
                            flipOMSwitch();
                        }
                    } else {
                        if(!opMode.isChecked()){
                            flipOMSwitch();
                        }
                    }
                });

                viewModel.getCH2NONC().observe(getViewLifecycleOwner(), NONC -> {
                    if(NONC == 0){
                        if(noncMode.isChecked()){
                            flipNONCSwitch();
                        }
                    } else {
                        if(!noncMode.isChecked()){
                            flipNONCSwitch();
                        }
                    }
                });
            }
        });
    }

    public void setCH3Listener(){
        ch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.selectChannel(3);
                ch1.setBackgroundColor(getResources().getColor(R.color.purple_700));
                ch2.setBackgroundColor(getResources().getColor(R.color.purple_700));
                ch3.setBackgroundColor(getResources().getColor(R.color.purple_200));
                readCH3Settings();

                viewModel.getCH3OM().observe(getViewLifecycleOwner(), CH3OM -> {
                    if(CH3OM == 0){
                        if(opMode.isChecked()){
                            flipOMSwitch();
                        }
                    } else {
                        if(!opMode.isChecked()){
                            flipOMSwitch();
                        }
                    }
                });

                viewModel.getCH3NONC().observe(getViewLifecycleOwner(), NONC -> {
                    if(NONC == 0){
                        if(noncMode.isChecked()){
                            flipNONCSwitch();
                        }
                    } else {
                        if(!noncMode.isChecked()){
                            flipNONCSwitch();
                        }
                    }
                });
            }
        });
    }

    //starts at off position
    public void setOMListener(){
        opMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int opmode;
                if(isChecked){
                    //on -> comp
                    opmode = 1;
                } else {
                    //off -> hysteresis
                    opmode = 0;
                }

                switch (viewModel.getChannelSelected()){
                    case 1:
                        viewModel.setCH1OM(opmode);
                        break;
                    case 2:
                        viewModel.setCH2OM(opmode);
                        break;
                    case 3:
                        viewModel.setCH3OM(opmode);
                        break;
                }
            }
        });
    }

    public void setNONCListener(){
        noncMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int noncmode;
                if(isChecked){
                    //on -> NC
                    noncmode = 1;
                } else {
                    //off -> NO
                    noncmode = 0;
                }

                switch (viewModel.getChannelSelected()){
                    case 1:
                        viewModel.setCH1NONC(noncmode);
                        break;
                    case 2:
                        viewModel.setCH2NONC(noncmode);
                        break;
                    case 3:
                        viewModel.setCH3NONC(noncmode);
                        break;
                }
            }
        });
    }

    public void setPeakResetListener(){
        peakReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int channelSelected = viewModel.getChannelSelected();
                switch(channelSelected){
                    case 1:
                        viewModel.peakReset(1);
                        break;
                    case 2:
                        viewModel.peakReset(2);
                        break;
                    case 3:
                        viewModel.peakReset(3);
                        break;
                }
            }
        });
    }

    public void setBottomResetListener(){
        bottomReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int channelSelected = viewModel.getChannelSelected();
                switch(channelSelected){
                    case 1:
                        viewModel.bottomReset(1);
                        break;
                    case 2:
                        viewModel.bottomReset(2);
                        break;
                    case 3:
                        viewModel.bottomReset(3);
                        break;
                }
            }
        });
    }

    //TODO: convert
    public void setBottomListener(){
        bottomHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setReadingSetting(1);
                bottomHold.setBackgroundColor(getResources().getColor(R.color.purple_200));
                live.setBackgroundColor(getResources().getColor(R.color.purple_700));
                peakHold.setBackgroundColor(getResources().getColor(R.color.purple_700));

                viewModel.getBotPressureCH1().observe(getViewLifecycleOwner(), bot -> {
                    if(viewModel.getReadingSetting() == 1){
                        ch1Val.setText((convert(viewModel.getLittlePressure(),bot,viewModel.getLittleDisplayResolution())).toString());
                    }
                });
                viewModel.getBotPressureCH2().observe(getViewLifecycleOwner(), bot -> {
                    if(viewModel.getReadingSetting() == 1){
                        ch2Val.setText((convert(viewModel.getLittlePressure(),bot,viewModel.getLittleDisplayResolution())).toString());
                    }
                });
                viewModel.getBotPressureCH3().observe(getViewLifecycleOwner(), bot -> {
                    if(viewModel.getReadingSetting() == 1){
                        ch3Val.setText((convert(viewModel.getLittlePressure(),bot,viewModel.getLittleDisplayResolution())).toString());
                    }
                });
            }
        });
    }

    //TODO: convert
    public void setLiveListener(){
        live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setReadingSetting(2);
                bottomHold.setBackgroundColor(getResources().getColor(R.color.purple_700));
                live.setBackgroundColor(getResources().getColor(R.color.purple_200));
                peakHold.setBackgroundColor(getResources().getColor(R.color.purple_700));

                viewModel.getLiveReadCH1().observe(getViewLifecycleOwner(), ch1Value -> {
                    if(viewModel.getReadingSetting() == 2){
                        ch1Val.setText(convert(viewModel.getLittlePressure(),ch1Value,viewModel.getLittleDisplayResolution()).toString());
                    }
                });
                viewModel.getLiveReadCH2().observe(getViewLifecycleOwner(), ch2Value -> {
                    if(viewModel.getReadingSetting() == 2){
                        ch2Val.setText(convert(viewModel.getLittlePressure(),ch2Value,viewModel.getLittleDisplayResolution()).toString());
                    }
                });
                viewModel.getLiveReadCH3().observe(getViewLifecycleOwner(), ch3Value -> {
                    if(viewModel.getReadingSetting() == 2){
                        ch3Val.setText(convert(viewModel.getLittlePressure(),ch3Value,viewModel.getLittleDisplayResolution()).toString());
                    }
                });
            }
        });
    }


    //TODO: convert
    public void setPeakListener(){
        peakHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setReadingSetting(3);
                bottomHold.setBackgroundColor(getResources().getColor(R.color.purple_700));
                live.setBackgroundColor(getResources().getColor(R.color.purple_700));
                peakHold.setBackgroundColor(getResources().getColor(R.color.purple_200));

                viewModel.getPeakPressureCH1().observe(getViewLifecycleOwner(), peak -> {
                    if(viewModel.getReadingSetting() == 3){
                        ch1Val.setText(convert(viewModel.getLittlePressure(),peak,viewModel.getLittleDisplayResolution()).toString());
                    }
                });
                viewModel.getPeakPressureCH2().observe(getViewLifecycleOwner(), peak -> {
                    if(viewModel.getReadingSetting() == 3){
                        ch2Val.setText(convert(viewModel.getLittlePressure(),peak,viewModel.getLittleDisplayResolution()).toString());
                    }
                });
                viewModel.getPeakPressureCH3().observe(getViewLifecycleOwner(), peak -> {
                    if(viewModel.getReadingSetting() == 3){
                        ch3Val.setText(convert(viewModel.getLittlePressure(),peak,viewModel.getLittleDisplayResolution()).toString());
                    }
                });
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        //viewModel.stopSendingData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //viewModel.stopSendingData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //viewModel.stopSendingData();
    }
}