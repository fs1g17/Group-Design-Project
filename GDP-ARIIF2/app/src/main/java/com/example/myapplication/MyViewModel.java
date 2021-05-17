package com.example.myapplication;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.UUID;

public class MyViewModel extends AndroidViewModel {
    private static final String TAG = "viewmodel";
    private static final int APP_PARAMETER_SETUP = 250;
    private static final int SET_LIVE_PRESSURE_READINGS_CH1 = 1;
    private static final int SET_LIVE_PRESSURE_READINGS_CH2 = 2;
    private static final int SET_LIVE_PRESSURE_READINGS_CH3 = 3;
    private static final int SET_RESPONSE_TIME = 4;
    private static final int SET_USER_PRESSURE_UNIT = 5;
    private static final int SET_USER_REFRESH_RATE = 6;
    private static final int SET_USER_DISPLAY_RESOLUTION = 7;
    private static final int SET_ANALOGUE_OUTPUT_VOLT = 8;
    private static final int EXECUTE_ZERO_CLEAR = 9;
    private static final int EXECUTE_SET_DEFAULT = 10;
    private static final int SET_PEAK_PRESSURE_CH1 = 11;
    private static final int SET_PEAK_PRESSURE_CH2 = 12;
    private static final int SET_PEAK_PRESSURE_CH3 = 13;
    private static final int SET_BOT_PRESSURE_CH1 = 21;
    private static final int SET_BOT_PRESSURE_CH2 = 22;
    private static final int SET_BOT_PRESSURE_CH3 = 23;
    private static final int SET_SWITCH_MODE_CH1 = 31;
    private static final int SET_SWITCH_MODE_CH2 = 32;
    private static final int SET_SWITCH_MODE_CH3 = 33;
    private static final int SET_OP_MODE_CH1 = 41;
    private static final int SET_OP_MODE_CH2 = 42;
    private static final int SET_OP_MODE_CH3 = 43;
    private static final int SET_LED_STATE_CH1 = 51;
    private static final int SET_LED_STATE_CH2 = 52;
    private static final int SET_LED_STATE_CH3 = 53;
    private static final int SET_HI_SETPOINT_CH1 = 61;
    private static final int SET_HI_SETPOINT_CH2 = 62;
    private static final int SET_HI_SETPOINT_CH3 = 63;
    private static final int SET_HYSTERESIS_CH1 = 71;
    private static final int SET_HYSTERESIS_CH2 = 72;
    private static final int SET_HYSTERESIS_CH3 = 73;
    private static final int SET_LO_SETPOINT_CH1 = 81;
    private static final int SET_LO_SETPOINT_CH2 = 82;
    private static final int SET_LO_SETPOINT_CH3 = 83;
    private static final int EXECUTE_PEAK_RESET_CH1 = 91;
    private static final int EXECUTE_PEAK_RESET_CH2 = 92;
    private static final int EXECUTE_PEAK_RESET_CH3 = 93;
    private static final int EXECUTE_BOT_RESET_CH1 = 101;
    private static final int EXECUTE_BOT_RESET_CH2 = 102;
    private static final int EXECUTE_BOT_RESET_CH3 = 103;
    private static final int START_SENDING_LIVE_DATA = 200;
    private static final int STOP_SENDING_LIVE_DATA = 201;

    //TODO: delete
    private MutableLiveData<String> DEBUGGING;


    public LiveData<String> getDEBUGGING(){ return DEBUGGING; }

    private int x_axis;
    public int getXAxis(){ return x_axis; }
    public void incXAxis(){ x_axis++; }
    public void resetXAxis(){ x_axis = 1; }

    private int x_axis2;
    public int getXAxis2(){ return x_axis2; }
    public void incXAxis2(){ x_axis2++; }
    public void resetXAxis2(){ x_axis2 = 1; }

    private int x_axis3;
    public int getXAxis3(){ return x_axis3; }
    public void incXAxis3(){ x_axis3++; }
    public void resetXAxis3(){ x_axis3 = 1; }

    //RANGES
    //MPa range 0-1
    public static float MINPRESSURE = 0;
    public static float MPAMAX = 1;
    //KPa range 0-1000
    public static float KPAMAX = 1000;
    //kgfmc2 range 0-10.2
    public static float KGFCM2MAX = new Float(10.1971621297793);
    //Bar range 0-10
    public static float BARMAX = 10;
    //PSI range 0-145.038
    public static float PSIMAX = new Float(145.038);
    //mmhg range 0-7500.62
    public static float MMHGMAX = new Float(7500.62);
    //cmhg range 0-750.062
    public static float CMHGMAX = new Float(750.062);
    //inchhg range 0-295.3
    public static float INCHHGMAX = new Float(295.3);

    public void startSendingData(){ sendData(START_SENDING_LIVE_DATA, new Float(0));}
    public void stopSendingData(){ sendData(STOP_SENDING_LIVE_DATA, new Float(0));}

    private static final int mpa = 0;
    private static final int kpa = 1;
    private static final int kgf = 2;
    private static final int bar = 3;
    private static final int psi = 4;
    private static final int mmhg = 5;
    private static final int cmhg = 6;
    private static final int inchhg = 7;

    private static final int twoDP = 1;
    private static final int threeDP = 0;

    private BluetoothDevice bluetoothDevice;
    private BluetoothAdapter bluetoothAdapter;
    private MutableLiveData<String> bluetoothDeviceName;

    //THIS IS MATES PHONE UUID
    //private UUID MY_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    //SWITCH UUID
    private UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    private ConnectedThread connectedThread;

    private MutableLiveData<Float> LIVE_PRESSURE_CH1;
    private MutableLiveData<Float> PEAK_PRESSURE_CH1;
    private MutableLiveData<Float> BOT_PRESSURE_CH1;
    private MutableLiveData<Float> CH1_OUT1_HI;
    private MutableLiveData<Float> CH1_OUT1_HY;
    private MutableLiveData<Float> CH1_OUT1_LO;
    private MutableLiveData<Integer> OP_MODE_CH1;
    private MutableLiveData<Integer> SWITCH_MODE_CH1;
    private MutableLiveData<Integer> LED_STATE_CH1;

    private MutableLiveData<Float> LIVE_PRESSURE_CH2;
    private MutableLiveData<Float> PEAK_PRESSURE_CH2;
    private MutableLiveData<Float> BOT_PRESSURE_CH2;
    private MutableLiveData<Float> CH2_OUT1_HI;
    private MutableLiveData<Float> CH2_OUT1_HY;
    private MutableLiveData<Float> CH2_OUT1_LO;
    private MutableLiveData<Integer> OP_MODE_CH2;
    private MutableLiveData<Integer> SWITCH_MODE_CH2;
    private MutableLiveData<Integer> LED_STATE_CH2;

    private MutableLiveData<Float> LIVE_PRESSURE_CH3;
    private MutableLiveData<Float> PEAK_PRESSURE_CH3;
    private MutableLiveData<Float> BOT_PRESSURE_CH3;
    private MutableLiveData<Float> CH3_OUT1_HI;
    private MutableLiveData<Float> CH3_OUT1_HY;
    private MutableLiveData<Float> CH3_OUT1_LO;
    private MutableLiveData<Integer> OP_MODE_CH3;
    private MutableLiveData<Integer> SWITCH_MODE_CH3;
    private MutableLiveData<Integer> LED_STATE_CH3;

    private MutableLiveData<Integer> RESPONSE_TIME;
    private MutableLiveData<Integer> PRESSURE_UNIT;
    private MutableLiveData<Integer> REFRESH_RATE;
    private MutableLiveData<Integer> DISPLAY_RESOLUTION;
    private MutableLiveData<Integer> ANALOGUE_OUTPUT;
    private int responseTime;
    private int pressureUnit;
    private int refreshRate;
    private int displayResolution;
    private int analogueOutput;
    private int CH1LED;
    private int CH2LED;
    private int CH3LED;

    private Float CH1HI;
    private Float CH2HI;
    private Float CH3HI;
    private Float CH1LO;
    private Float CH2LO;
    private Float CH3LO;
    private Float CH1HY;
    private Float CH2HY;
    private Float CH3HY;
    private int CH1OM;
    private int CH2OM;
    private int CH3OM;


    private MutableLiveData<String> readNFC;

    private int channelSelected;

    private MutableLiveData<Boolean> doneWithSetup;
    public LiveData<Boolean> isDoneWithSetup(){ return doneWithSetup; }

    //1= bottom hold, 2= live, 3 = peak hold DEFAULT: live
    private int readingSetting;

    public MyViewModel(@NonNull Application application) {
        super(application);
        //myRepository = MyRepository.getInstance();
        x_axis = 1;

        DEBUGGING = new MutableLiveData<>();
        DEBUGGING.postValue("nothing");

        doneWithSetup = new MutableLiveData<>();
        doneWithSetup.postValue(false);

        LIVE_PRESSURE_CH1 = new MutableLiveData<>();
        LIVE_PRESSURE_CH1.postValue(new Float(0.0));
        PEAK_PRESSURE_CH1 = new MutableLiveData<>();
        PEAK_PRESSURE_CH1.postValue(new Float(0.0));
        BOT_PRESSURE_CH1 = new MutableLiveData<>();
        BOT_PRESSURE_CH1.postValue(new Float(0.0));
        CH1_OUT1_HI = new MutableLiveData<>();
        CH1_OUT1_HI.postValue(new Float(0.0));
        CH1_OUT1_LO = new MutableLiveData<>();
        CH1_OUT1_LO.postValue(new Float(0.0));
        CH1_OUT1_HY = new MutableLiveData<>();
        CH1_OUT1_HY.postValue(new Float(0.0));
        OP_MODE_CH1 = new MutableLiveData<>();
        OP_MODE_CH1.postValue(0);
        SWITCH_MODE_CH1 = new MutableLiveData<>();
        SWITCH_MODE_CH1.postValue(0);

        LIVE_PRESSURE_CH2 = new MutableLiveData<>();
        LIVE_PRESSURE_CH2.postValue(new Float(0.0));
        PEAK_PRESSURE_CH2 = new MutableLiveData<>();
        PEAK_PRESSURE_CH2.postValue(new Float(0.0));
        BOT_PRESSURE_CH2 = new MutableLiveData<>();
        BOT_PRESSURE_CH2.postValue(new Float(0.0));
        CH2_OUT1_HI = new MutableLiveData<>();
        CH2_OUT1_HI.postValue(new Float(0.0));
        CH2_OUT1_LO = new MutableLiveData<>();
        CH2_OUT1_LO.postValue(new Float(0.0));
        CH2_OUT1_HY = new MutableLiveData<>();
        CH2_OUT1_HY.postValue(new Float(0.0));
        OP_MODE_CH2 = new MutableLiveData<>();
        OP_MODE_CH2.postValue(0);
        SWITCH_MODE_CH2 = new MutableLiveData<>();
        SWITCH_MODE_CH2.postValue(0);

        LIVE_PRESSURE_CH3 = new MutableLiveData<>();
        LIVE_PRESSURE_CH3.postValue(new Float(0.0));
        PEAK_PRESSURE_CH3 = new MutableLiveData<>();
        PEAK_PRESSURE_CH3.postValue(new Float(0.0));
        BOT_PRESSURE_CH3 = new MutableLiveData<>();
        BOT_PRESSURE_CH3.postValue(new Float(0.0));
        CH3_OUT1_HI = new MutableLiveData<>();
        CH3_OUT1_HI.postValue(new Float(0.0));
        CH3_OUT1_LO = new MutableLiveData<>();
        CH3_OUT1_LO.postValue(new Float(0.0));
        CH3_OUT1_HY = new MutableLiveData<>();
        CH3_OUT1_HY.postValue(new Float(0.0));
        OP_MODE_CH3 = new MutableLiveData<>();
        OP_MODE_CH3.postValue(0);
        SWITCH_MODE_CH3 = new MutableLiveData<>();
        SWITCH_MODE_CH3.postValue(0);

        RESPONSE_TIME = new MutableLiveData<>();
        PRESSURE_UNIT = new MutableLiveData<>();
        REFRESH_RATE = new MutableLiveData<>();
        DISPLAY_RESOLUTION = new MutableLiveData<>();
        ANALOGUE_OUTPUT = new MutableLiveData<>();
        LED_STATE_CH1 = new MutableLiveData<>();
        LED_STATE_CH2 = new MutableLiveData<>();
        LED_STATE_CH3 = new MutableLiveData<>();
        RESPONSE_TIME.postValue(1);
        PRESSURE_UNIT.postValue(1);
        REFRESH_RATE.postValue(1);
        DISPLAY_RESOLUTION.postValue(1);
        ANALOGUE_OUTPUT.postValue(1);
        LED_STATE_CH1.postValue(1);
        LED_STATE_CH2.postValue(1);
        LED_STATE_CH3.postValue(1);
        responseTime = 1;
        pressureUnit = 1;
        refreshRate = 1;
        displayResolution = 1;
        analogueOutput = 1;
        CH1LED = 1;
        CH2LED = 1;
        CH3LED = 1;

        readNFC = new MutableLiveData<>();
        readNFC.postValue(null);

        bluetoothDeviceName = new MutableLiveData<>();
        bluetoothDeviceName.postValue(null);

        channelSelected = 1;
        readingSetting = 2;


        CH1HI = new Float(0);
        CH2HI = new Float(0);
        CH3HI = new Float(0);
        CH1LO = new Float(0);
        CH2LO = new Float(0);
        CH3LO = new Float(0);
        CH1HY = new Float(0);
        CH2HY = new Float(0);
        CH3HY = new Float(0);
        CH1OM = 0;
        CH2OM = 0;
        CH3OM = 0;
    }

    public float getCH1HI(){ return CH1HI; }
    public float getCH2HI(){ return CH2HI; }
    public float getCH3HI(){ return CH3HI; }
    public float getCH1LO(){ return CH1LO; }
    public float getCH2LO(){ return CH2LO; }
    public float getCH3LO(){ return CH3LO; }
    public float getCH1HY(){ return CH1HY; }
    public float getCH2HY(){ return CH2HY; }
    public float getCH3HY(){ return CH3HY; }
    public int getIntCH1OM(){ return CH1OM; }
    public int getIntCH2OM(){ return CH2OM; }
    public int getIntCH3OM(){ return CH3OM; }
    public void setCH1HI(float f){ CH1HI = f; }
    public void setCH2HI(float f){ CH2HI = f; }
    public void setCH3HI(float f){ CH3HI = f; }
    public void setCH1LO(float f){ CH1LO = f; }
    public void setCH2LO(float f){ CH2LO = f; }
    public void setCH3LO(float f){ CH3LO = f; }
    public void setCH1HY(float f){ CH1HY = f; }
    public void setCH2HY(float f){ CH2HY = f; }
    public void setCH3HY(float f){ CH3HY = f; }
    public void setIntCH1OM(int i){ CH1OM = i; }
    public void setIntCH2OM(int i){ CH2OM = i; }
    public void setIntCH3OM(int i){ CH3OM = i; }


    public Float convertToMPa(Float value){
        Float answer = new Float(0);
        switch(pressureUnit){
            case 1:
                //DO NOTHING
                answer = value;
                break;
            case 2:
                answer = value/ KPAMAX;
                break;
            case 3:
                answer = value/ KGFCM2MAX;
                break;
            case 4:
                answer = value/ BARMAX;
                break;
            case 5:
                answer = value/ PSIMAX;
                break;
            case 6:
                answer = value/ MMHGMAX;
                break;
            case 7:
                answer = value/ CMHGMAX;
                break;
            case 8:
                answer = answer/ INCHHGMAX;
                break;
        }
        return answer;
    }

    public int getChannelSelected(){ return channelSelected; }
    public void selectChannel(int i){ channelSelected = i; }

    public int getReadingSetting(){ return readingSetting; }
    public void setReadingSetting(int i){ readingSetting = i; }

    public LiveData<Integer> getCH1OM(){ return OP_MODE_CH1; }
    public LiveData<Integer> getCH1NONC(){ return SWITCH_MODE_CH1; }
    public void setCH1OM(int i){
        OP_MODE_CH1.postValue(i);
        sendData(SET_OP_MODE_CH1, new Float(i));
    }
    public void setCH1NONC(int i){
        SWITCH_MODE_CH1.postValue(i);
        sendData(SET_SWITCH_MODE_CH1, new Float(i));
    }

    public LiveData<Integer> getCH2OM(){ return OP_MODE_CH2; }
    public LiveData<Integer> getCH2NONC(){ return SWITCH_MODE_CH2; }
    public void setCH2OM(int i){
        OP_MODE_CH2.postValue(i);
        sendData(SET_OP_MODE_CH2, new Float(i));
    }
    public void setCH2NONC(int i){
        SWITCH_MODE_CH2.postValue(i);
        sendData(SET_SWITCH_MODE_CH2, new Float(i));
    }

    public LiveData<Integer> getCH3OM(){ return OP_MODE_CH3; }
    public LiveData<Integer> getCH3NONC(){ return SWITCH_MODE_CH3; }
    public void setCH3OM(int i){
        OP_MODE_CH3.postValue(i);
        sendData(SET_OP_MODE_CH3, new Float(i));
    }
    public void setCH3NONC(int i){
        SWITCH_MODE_CH3.postValue(i);
        sendData(SET_SWITCH_MODE_CH3, new Float(i));
    }

    public LiveData<Float> getLiveReadCH1() { return LIVE_PRESSURE_CH1; }
    public LiveData<Float> getPeakPressureCH1(){ return PEAK_PRESSURE_CH1; }
    public LiveData<Float> getBotPressureCH1(){ return BOT_PRESSURE_CH1; }
    public LiveData<Float> getCH1_OUT1_HI() {
        return CH1_OUT1_HI;
    }
    public LiveData<Float> getCH1_OUT1_LO(){
        return CH1_OUT1_LO;
    }
    public LiveData<Float> getCH1_OUT1_HY(){
        return CH1_OUT1_HY;
    }

    public LiveData<Float> getLiveReadCH2() { return LIVE_PRESSURE_CH2; }
    public LiveData<Float> getPeakPressureCH2(){ return PEAK_PRESSURE_CH2; }
    public LiveData<Float> getBotPressureCH2(){ return BOT_PRESSURE_CH2; }
    public LiveData<Float> getCH2_OUT1_HI() {
        return CH2_OUT1_HI;
    }
    public LiveData<Float> getCH2_OUT1_LO(){
        return CH2_OUT1_LO;
    }
    public LiveData<Float> getCH2_OUT1_HY(){
        return CH2_OUT1_HY;
    }

    public LiveData<Float> getLiveReadCH3() { return LIVE_PRESSURE_CH3; }
    public LiveData<Float> getPeakPressureCH3(){ return PEAK_PRESSURE_CH3; }
    public LiveData<Float> getBotPressureCH3(){ return BOT_PRESSURE_CH3; }
    public LiveData<Float> getCH3_OUT1_HI() {
        return CH3_OUT1_HI;
    }
    public LiveData<Float> getCH3_OUT1_LO(){
        return CH3_OUT1_LO;
    }
    public LiveData<Float> getCH3_OUT1_HY(){
        return CH3_OUT1_HY;
    }

    public LiveData<Integer> getResponseTime(){ return RESPONSE_TIME; }
    public LiveData<Integer> getPressureUnit(){ return PRESSURE_UNIT; }
    public LiveData<Integer> getRefreshRate(){ return REFRESH_RATE; }
    public LiveData<Integer> getDisplayResolution(){ return DISPLAY_RESOLUTION; }
    public LiveData<Integer> getAnalogueOutput(){ return ANALOGUE_OUTPUT; }
    public LiveData<Integer> getCH1LED(){ return LED_STATE_CH1; }
    public LiveData<Integer> getCH2LED(){ return LED_STATE_CH2; }
    public LiveData<Integer> getCH3LED(){ return LED_STATE_CH3; }

    public void setMpa(){
        sendData(SET_USER_PRESSURE_UNIT, new Float(mpa));
        PRESSURE_UNIT.postValue(1);
        pressureUnit = 1;
    }
    public void setKpa(){
        sendData(SET_USER_PRESSURE_UNIT, new Float(kpa));
        PRESSURE_UNIT.postValue(2);
        pressureUnit = 2;
    }
    public void setKgf(){
        sendData(SET_USER_PRESSURE_UNIT, new Float(kgf));
        PRESSURE_UNIT.postValue(3);
        pressureUnit = 3;
    }
    public void setBar(){
        sendData(SET_USER_PRESSURE_UNIT, new Float(bar));
        PRESSURE_UNIT.postValue(4);
        pressureUnit = 4;
    }
    public void setPsi(){
        sendData(SET_USER_PRESSURE_UNIT, new Float(psi));
        PRESSURE_UNIT.postValue(5);
        pressureUnit = 5;
    }
    public void setMmhg(){
        sendData(SET_USER_PRESSURE_UNIT, new Float(mmhg));
        PRESSURE_UNIT.postValue(6);
        pressureUnit = 6;
    }
    public void setCmhg(){
        sendData(SET_USER_PRESSURE_UNIT, new Float(cmhg));
        PRESSURE_UNIT.postValue(7);
        pressureUnit = 7;
    }
    public void setInchhg(){
        sendData(SET_USER_PRESSURE_UNIT, new Float(inchhg));
        PRESSURE_UNIT.postValue(8);
        pressureUnit = 8;
    }

    //TODO: set dp in the app and receive the dp from the switch
    public void set2dp(){
        sendData(SET_USER_DISPLAY_RESOLUTION, new Float(twoDP));
        DISPLAY_RESOLUTION.postValue(2);
        displayResolution = 2;
    }
    public void set3dp(){
        sendData(SET_USER_DISPLAY_RESOLUTION, new Float(threeDP));
        DISPLAY_RESOLUTION.postValue(1);
        displayResolution = 1;
    }

    //TODO: recieve value from switch
    public void setAO5V(){
        sendData(SET_ANALOGUE_OUTPUT_VOLT, new Float(0));
        ANALOGUE_OUTPUT.postValue(1);
        analogueOutput = 1;
    }
    public void setAO10V(){
        sendData(SET_ANALOGUE_OUTPUT_VOLT, new Float(1));
        ANALOGUE_OUTPUT.postValue(2);
        analogueOutput = 2;
    }

    //TODO: recieve value from switch
    public void setResponse2(){
        sendData(SET_RESPONSE_TIME, new Float(0));
        RESPONSE_TIME.postValue(1);
        responseTime = 1;
    }
    public void setResponse20(){
        sendData(SET_RESPONSE_TIME, new Float(1));
        RESPONSE_TIME.postValue(2);
        responseTime = 2;
    }
    public void setResponse50(){
        sendData(SET_RESPONSE_TIME, new Float(2));
        RESPONSE_TIME.postValue(3);
        responseTime = 3;
    }
    public void setResponse100(){
        sendData(SET_RESPONSE_TIME, new Float(3));
        RESPONSE_TIME.postValue(4);
        responseTime = 4;
    }
    public void setResponse200(){
        sendData(SET_RESPONSE_TIME, new Float(4));
        RESPONSE_TIME.postValue(5);
        responseTime = 5;
    }
    public void setResponse500(){
        sendData(SET_RESPONSE_TIME, new Float(5));
        RESPONSE_TIME.postValue(6);
        responseTime = 6;
    }

    //TODO: receive value from switch
    public void setRefresh200(){
        sendData(SET_USER_REFRESH_RATE, new Float(0));
        REFRESH_RATE.postValue(1);
        refreshRate = 1;
    }
    public void setRefresh500(){
        sendData(SET_USER_REFRESH_RATE, new Float(1));
        REFRESH_RATE.postValue(2);
        refreshRate = 2;
    }
    public void setRefresh1000(){
        sendData(SET_USER_REFRESH_RATE, new Float(2));
        REFRESH_RATE.postValue(3);
        refreshRate = 3;
    }

    public void setGreenOnRedOff(int channel){
        setLEDStates(channel,1);
    }

    public void setRedOnGreenOff(int channel){
        setLEDStates(channel,2);
    }

    public void setNormallyRed(int channel){
        setLEDStates(channel,3);
    }

    public void setNormallyGreen(int channel){
        setLEDStates(channel,4);
    }

    private void setLEDStates(int channel, int localVal){
        Float switchVal = new Float(localVal-1);
        switch (channel){
            case 1:
                sendData(SET_LED_STATE_CH1, switchVal);
                LED_STATE_CH1.postValue(localVal);
                CH1LED = localVal;
                break;
            case 2:
                sendData(SET_LED_STATE_CH2, switchVal);
                LED_STATE_CH2.postValue(localVal);
                CH2LED = localVal;
                break;
            case 3:
                sendData(SET_LED_STATE_CH3, switchVal);
                LED_STATE_CH3.postValue(localVal);
                CH3LED = localVal;
                break;
        }
    }

    public void defaultSettings(){ sendData(EXECUTE_SET_DEFAULT, new Float(0));}
    public void zeroClear(){ sendData(EXECUTE_ZERO_CLEAR, new Float(0));}

    public void update(int channel, Float hi_value, Float lo_value, Float hy_value,int OM){

        Float max = new Float(0);
        Float min = new Float(0);

        switch(pressureUnit){
            case 1:
                max = MPAMAX;
                min = MINPRESSURE;
                break;
            case 2:
                max = KPAMAX;
                min = MINPRESSURE;
                break;
            case 3:
                max = KGFCM2MAX;
                min = MINPRESSURE;
                break;
            case 4:
                max = BARMAX;
                min = MINPRESSURE;
                break;
            case 5:
                max = PSIMAX;
                min = MINPRESSURE;
                break;
            case 6:
                max = MMHGMAX;
                min = MINPRESSURE;
                break;
            case 7:
                max = CMHGMAX;
                min = MINPRESSURE;
                break;
            case 8:
                max = INCHHGMAX;
                min = MINPRESSURE;
        }

        if(hi_value > max){
            hi_value = new Float(1);
        }
        else if(hi_value < min){
            hi_value = new Float(0);
        } else {
            hi_value = convertToMPa(hi_value);
        }

        if(lo_value > max){
            lo_value = new Float(1);
        }
        else if(lo_value < min){
            lo_value = new Float(0);
        } else{
            lo_value = convertToMPa(lo_value);
        }

        if(hy_value > max){
            hy_value = new Float(1);
        }
        else if(hy_value < min){
            hy_value = new Float(0);
        } else {
            hy_value = convertToMPa(hy_value);
        }

        switch (channel){
            case 1:
                CH1_OUT1_HI.postValue(hi_value);
                CH1_OUT1_LO.postValue(lo_value);
                CH1_OUT1_HY.postValue(hy_value);
                CH1HI = hi_value;
                CH1LO = lo_value;
                CH1HY = hy_value;
                sendData(SET_HI_SETPOINT_CH1, hi_value);
                sendData(SET_LO_SETPOINT_CH1, lo_value);
                sendData(SET_HYSTERESIS_CH1, hy_value);
                sendData(SET_OP_MODE_CH1, new Float(OM));
                break;
            case 2:
                CH2_OUT1_HI.postValue(hi_value);
                CH2_OUT1_LO.postValue(lo_value);
                CH2_OUT1_HY.postValue(hy_value);
                CH2HI = hi_value;
                CH2LO = lo_value;
                CH2HY = hy_value;
                sendData(SET_HI_SETPOINT_CH2, hi_value);
                sendData(SET_LO_SETPOINT_CH2, lo_value);
                sendData(SET_HYSTERESIS_CH2, hy_value);
                sendData(SET_OP_MODE_CH2, new Float(OM));
                break;
            case 3:
                CH3_OUT1_HI.postValue(hi_value);
                CH3_OUT1_LO.postValue(lo_value);
                CH3_OUT1_HY.postValue(hy_value);
                CH3HI = hi_value;
                CH3LO = lo_value;
                CH3HY = hy_value;
                sendData(SET_HI_SETPOINT_CH3, hi_value);
                sendData(SET_LO_SETPOINT_CH3, lo_value);
                sendData(SET_HYSTERESIS_CH3, hy_value);
                sendData(SET_OP_MODE_CH3, new Float(OM));
                break;
        }

    }

    public void peakReset(int channel){
        switch(channel){
            case 1:
                sendData(EXECUTE_PEAK_RESET_CH1, new Float(0.0));
                break;
            case 2:
                sendData(EXECUTE_PEAK_RESET_CH2, new Float(0.0));
                break;
            case 3:
                sendData(EXECUTE_PEAK_RESET_CH3, new Float(0.0));
        }
    }

    public void bottomReset(int channel){
        switch(channel){
            case 1:
                sendData(EXECUTE_BOT_RESET_CH1, new Float(0.0));
                break;
            case 2:
                sendData(EXECUTE_BOT_RESET_CH2, new Float(0.0));
                break;
            case 3:
                sendData(EXECUTE_BOT_RESET_CH3, new Float(0.0));
        }
    }

    private void sendData(int opcode, Float value){
        float f = value;
        switch(opcode){
            case SET_HI_SETPOINT_CH1:
            case SET_HI_SETPOINT_CH2:
            case SET_HI_SETPOINT_CH3:
            case SET_LO_SETPOINT_CH1:
            case SET_LO_SETPOINT_CH2:
            case SET_LO_SETPOINT_CH3:
            case SET_HYSTERESIS_CH1:
            case SET_HYSTERESIS_CH2:
            case SET_HYSTERESIS_CH3:
                f = value*1000;
                break;
        }
        int i = (int) f;

        BigInteger bigInt = BigInteger.valueOf(i);

        int MSB;
        int LSB;

        if(bigInt.toByteArray().length >= 2){
            MSB = bigInt.toByteArray()[0];
            LSB = bigInt.toByteArray()[1];
        } else {
            LSB = bigInt.toByteArray()[0];
            MSB = 0;
        }

        char[] packet = {(char) opcode, (char) MSB, (char) LSB};
        if(packet.length != 3){
            CH1_OUT1_HI.postValue(new Float(666));
        }

        try{
            connectedThread.writeArr(packet);
        } catch (Exception e){
            Log.println(Log.ERROR,TAG,"failed to write to connectedThread");
        }
    }

    public void setReadNFC(String s){
        readNFC.postValue(s);
    }
    public LiveData<String> getReadNFC(){
        return readNFC;
    }

    public void setBluetoothDeviceName(String s) { bluetoothDeviceName.postValue(s);}
    public LiveData<String> getBluetoothDeviceName(){ return bluetoothDeviceName; }

    public void startBluetoothConnection(BluetoothAdapter bluetoothAdapter, BluetoothDevice bluetoothDevice){
        this.bluetoothAdapter = bluetoothAdapter;
        this.bluetoothDevice = bluetoothDevice;
        new ConnectThread(bluetoothDevice).start();
    }

    public int getLittleResponseTime(){ return responseTime; }
    public int getLittlePressure(){ return pressureUnit; }
    public int getLittleRefreshRate(){ return refreshRate; }
    public int getLittleDisplayResolution(){ return displayResolution; }
    public int getLittleAnalougeOutput(){ return analogueOutput; }
    public int getLittleCH1LED(){ return CH1LED; }
    public int getLittleCH2LED(){ return CH2LED; }
    public int getLittleCH3LED(){ return CH3LED; }

    public void setLittleResponseTime(int i){  responseTime = i; }
    public void setLittlePressure(int i){  pressureUnit = i; }
    public void setLittleRefreshRate(int i){  refreshRate = i; }
    public void setLittleDisplayResolution(int i){  displayResolution = i; }
    public void setLittleAnalougeOutput(int i){  analogueOutput = i; }
    public void setLittleCH1LED(int i){ CH1LED = i; }
    public void setLittleCH2LED(int i){ CH2LED = i; }
    public void setLittleCH3LED(int i){ CH3LED = i; }


    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            //manageMyConnectedSocket(mmSocket);
            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.start();
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final DataOutputStream mmDataOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            DataOutputStream tmpDataOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }
            try{
                tmpDataOut = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            mmDataOutStream = tmpDataOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            char[] init = {(char) 250, (char) 0, (char) 0};
            char[] init2= {(char) 200, (char) 0, (char) 0};
            writeArr(init);
            writeArr(init2);

            int opcode = 0;
            byte MSB = 0;
            byte LSB = 0;
            int byteNumber = 1;
            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    numBytes = mmInStream.read(mmBuffer);
                    StringBuilder sb = new StringBuilder();

                    for(int i=0; i<numBytes; i++){
                        switch (byteNumber){
                            case 1:
                                opcode = mmBuffer[i];
                                break;
                            case 2:
                                MSB = mmBuffer[i];
                                break;
                            case 3:
                                LSB = mmBuffer[i];
                                byteNumber = 0;
                                int value = (MSB & 0xFF) << 8 | (LSB & 0xFF);
                                sb.append("[" + opcode + "," + value + "]");
                                //float f = value;
                                //float ff = f/1000;

                                processInput(opcode,((float)value));
                                break;
                        }
                        byteNumber++;
                    }

                    DEBUGGING.postValue(sb.toString());
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        public void writeArr(char[] ch){
            try{
                if(ch.length != 3){
                    CH1_OUT1_HI.setValue(new Float(666));
                }
                for(int k=0; k<ch.length; k++){
                    //new DataOutputStream(mmSocket.getOutputStream()).writeByte(ch[k]);
                    mmDataOutStream.writeByte(ch[k]);
                }
            } catch(IOException e){
                e.printStackTrace();
                CH1_OUT1_HI.setValue(new Float(666));
            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

    //TODO: check PSD input works
    private void processInput(int opcode, Float val){
        switch (opcode){
            case SET_LIVE_PRESSURE_READINGS_CH1:
                LIVE_PRESSURE_CH1.postValue(val/1000);
                break;
            case SET_LIVE_PRESSURE_READINGS_CH2:
                LIVE_PRESSURE_CH2.postValue(val/1000);
                break;
            case SET_LIVE_PRESSURE_READINGS_CH3:
                LIVE_PRESSURE_CH3.postValue(val/1000);
                break;
            case SET_PEAK_PRESSURE_CH1:
                PEAK_PRESSURE_CH1.postValue(val/1000);
                break;
            case SET_PEAK_PRESSURE_CH2:
                PEAK_PRESSURE_CH2.postValue(val/1000);
                break;
            case SET_PEAK_PRESSURE_CH3:
                PEAK_PRESSURE_CH3.postValue(val/1000);
                break;
            case SET_BOT_PRESSURE_CH1:
                BOT_PRESSURE_CH1.postValue(val/1000);
                break;
            case SET_BOT_PRESSURE_CH2:
                BOT_PRESSURE_CH2.postValue(val/1000);
                break;
            case SET_BOT_PRESSURE_CH3:
                BOT_PRESSURE_CH3.postValue(val/1000);
                break;
            case SET_SWITCH_MODE_CH1:
                SWITCH_MODE_CH1.postValue(Math.round(val));
                break;
            case SET_SWITCH_MODE_CH2:
                SWITCH_MODE_CH2.postValue(Math.round(val));
                break;
            case SET_SWITCH_MODE_CH3:
                SWITCH_MODE_CH3.postValue(Math.round(val));
                break;
            case SET_OP_MODE_CH1:
                OP_MODE_CH1.postValue(Math.round(val));
                CH1OM = Math.round(val);
                break;
            case SET_OP_MODE_CH2:
                OP_MODE_CH2.postValue(Math.round(val));
                CH2OM = Math.round(val);
                break;
            case SET_OP_MODE_CH3:
                OP_MODE_CH3.postValue(Math.round(val));
                CH3OM = Math.round(val);
                break;
            case SET_LED_STATE_CH1:
                LED_STATE_CH1.postValue(Math.round(val + 1));
                CH1LED = Math.round(val + 1);
                break;
            case SET_LED_STATE_CH2:
                LED_STATE_CH2.postValue(Math.round(val + 1));
                CH2LED = Math.round(val + 1);
                break;
            case SET_LED_STATE_CH3:
                LED_STATE_CH3.postValue(Math.round(val + 1));
                CH3LED = Math.round(val + 1);
                break;
            case SET_HI_SETPOINT_CH1:
                CH1_OUT1_HI.postValue(val/1000);
                CH1HI = val/1000;
                break;
            case SET_HI_SETPOINT_CH2:
                CH2_OUT1_HI.postValue(val/1000);
                CH2HI = val/1000;
                break;
            case SET_HI_SETPOINT_CH3:
                CH3_OUT1_HI.postValue(val/1000);
                CH3HI = val/1000;
                break;
            case SET_HYSTERESIS_CH1:
                CH1_OUT1_HY.postValue(val/1000);
                CH1HY = val/1000;
                break;
            case SET_HYSTERESIS_CH2:
                CH2_OUT1_HY.postValue(val/1000);
                CH2HY = val/1000;
                break;
            case SET_HYSTERESIS_CH3:
                CH3_OUT1_HY.postValue(val/1000);
                CH3HY = val/1000;
                break;
            case SET_LO_SETPOINT_CH1:
                CH1_OUT1_LO.postValue(val/1000);
                CH1LO = val/1000;
                break;
            case SET_LO_SETPOINT_CH2:
                CH2_OUT1_LO.postValue(val/1000);
                CH2LO = val/1000;
                break;
            case SET_LO_SETPOINT_CH3:
                CH3_OUT1_LO.postValue(val/1000);
                CH3LO = val/1000;
                //TODO: put this in the corresponding case
                break;
                //might need to do +1;
            case SET_RESPONSE_TIME:
                int input = Math.round(val) + 1;
                RESPONSE_TIME.postValue(input);
                break;
            case SET_USER_PRESSURE_UNIT:
                int input2 = Math.round(val) + 1;
                PRESSURE_UNIT.postValue(input2);
                pressureUnit = Math.round(val)+1;
                break;
            case SET_USER_REFRESH_RATE:
                int input3 = Math.round(val) + 1;
                REFRESH_RATE.postValue(input3);
                break;
            case SET_USER_DISPLAY_RESOLUTION:
                int input4 = Math.round(val) + 1;
                DISPLAY_RESOLUTION.postValue(input4);
                displayResolution = Math.round(val)+1;
                break;
            case SET_ANALOGUE_OUTPUT_VOLT:
                int input5 = Math.round(val) + 1;
                ANALOGUE_OUTPUT.postValue(input5);
                doneWithSetup.postValue(true);
                break;
        }
    }
}


/*
//channel 1 sensor readings
    private double CH1_PRESSURE;
    private double CH1_PEAK;
    private double CH1_BOT;

    //channel 2 sensor readings
    private double CH2_PRESSURE;
    private double CH2_PEAK;
    private double CH2_BOT;

    //channel 3 sensor readings
    private double CH3_PRESSURE;
    private double CH3_PEAK;
    private double CH3_BOT;

    //saved posttings for channel 1
    private double CH1_OUT1_SM;
    private double CH1_OUT1_OM;
    private double CH1_OUT1_HI;
    private double CH1_OUT1_HY;
    private double CH1_OUT1_LO;
    private double CH1_OUT1_LED;

    //saved posttings for channel 2
    private double CH1_OUT2_SM;
    private double CH1_OUT2_OM;
    private double CH1_OUT2_HI;
    private double CH1_OUT2_HY;
    private double CH1_OUT2_LO;
    private double CH1_OUT2_LED;

    //saved posttings for channel 3
    private double CH1_OUT3_SM;
    private double CH1_OUT3_OM;
    private double CH1_OUT3_HI;
    private double CH1_OUT3_HY;
    private double CH1_OUT3_LO;
    private double CH1_OUT3_LED;

    //digital response time
    private double DIGITAL_RESP_TIME;

    //analogue response time
    private double ANALOG_RESP_TIME;

    //pressure units
    private double USER_PRESSURE_UNIT;

    //display resolution
    private double USER_DISP_RESO;

    //sampling rate
    private double USER_SAMP_RATE;
 */
