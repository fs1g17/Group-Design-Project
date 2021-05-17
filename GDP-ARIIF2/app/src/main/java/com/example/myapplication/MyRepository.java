package com.example.myapplication;

import androidx.lifecycle.MutableLiveData;

public class MyRepository {
    private static MyRepository myRepository;
    private MutableLiveData<Double> CH1_OUT1_HI;
    private MutableLiveData<Double> CH1_OUT1_LO;
    private MutableLiveData<Double> CH1_OUT1_HY;

    private MutableLiveData<Double> CH2_OUT1_HI;
    private MutableLiveData<Double> CH2_OUT1_LO;
    private MutableLiveData<Double> CH2_OUT1_HY;

    private MutableLiveData<Double> CH3_OUT1_HI;
    private MutableLiveData<Double> CH3_OUT1_LO;
    private MutableLiveData<Double> CH3_OUT1_HY;

    public static MyRepository getInstance(){
        if(myRepository == null){
            myRepository = new MyRepository();
        }
        return myRepository;
    }

    public MyRepository(){
        CH1_OUT1_HI = new MutableLiveData<>();
        CH1_OUT1_HI.postValue(0.0);
        CH1_OUT1_LO = new MutableLiveData<>();
        CH1_OUT1_LO.postValue(1.0);
        CH1_OUT1_HY = new MutableLiveData<>();
        CH1_OUT1_HY.postValue(2.0);

        CH2_OUT1_HI = new MutableLiveData<>();
        CH2_OUT1_HI.postValue(5.0);
        CH2_OUT1_LO = new MutableLiveData<>();
        CH2_OUT1_LO.postValue(6.0);
        CH2_OUT1_HY = new MutableLiveData<>();
        CH2_OUT1_HY.postValue(7.0);

        CH3_OUT1_HI = new MutableLiveData<>();
        CH3_OUT1_HI.postValue(8.0);
        CH3_OUT1_LO = new MutableLiveData<>();
        CH3_OUT1_LO.postValue(9.0);
        CH3_OUT1_HY = new MutableLiveData<>();
        CH3_OUT1_HY.postValue(10.0);
    }

    public MutableLiveData<Double> getCH1_OUT1_HI(){
        return CH1_OUT1_HI;
    }

    public MutableLiveData<Double> getCH1_OUT1_LO() {
        return CH1_OUT1_LO;
    }

    public MutableLiveData<Double> getCH1_OUT1_HY() {
        return CH1_OUT1_HY;
    }

    public void postCH1_OUT1_HI(double value) {
        CH1_OUT1_HI.postValue(value);
    }

    public void postCH1_OUT1_LO(double value){
        CH1_OUT1_LO.postValue(value);
    }

    public void postCH1_OUT1_HY(double value) {
        CH1_OUT1_HY.postValue(value);
    }

    public MutableLiveData<Double> getCH2_OUT1_HI(){
        return CH2_OUT1_HI;
    }

    public MutableLiveData<Double> getCH2_OUT1_LO() {
        return CH2_OUT1_LO;
    }

    public MutableLiveData<Double> getCH2_OUT1_HY() {
        return CH2_OUT1_HY;
    }

    public void postCH2_OUT1_HI(double value) {
        CH2_OUT1_HI.postValue(value);
    }

    public void postCH2_OUT1_LO(double value){
        CH2_OUT1_LO.postValue(value);
    }

    public void postCH2_OUT1_HY(double value) {
        CH2_OUT1_HY.postValue(value);
    }

    public MutableLiveData<Double> getCH3_OUT1_HI(){
        return CH3_OUT1_HI;
    }

    public MutableLiveData<Double> getCH3_OUT1_LO() {
        return CH3_OUT1_LO;
    }

    public MutableLiveData<Double> getCH3_OUT1_HY() {
        return CH3_OUT1_HY;
    }

    public void postCH3_OUT1_HI(double value) {
        CH3_OUT1_HI.postValue(value);
    }

    public void postCH3_OUT1_LO(double value){
        CH3_OUT1_LO.postValue(value);
    }

    public void postCH3_OUT1_HY(double value) {
        CH3_OUT1_HY.postValue(value);
    }
}
