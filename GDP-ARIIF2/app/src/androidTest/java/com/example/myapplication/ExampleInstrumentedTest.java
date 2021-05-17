package com.example.myapplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.text.DecimalFormat;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();
    private LifecycleOwner owner = mockLifecycleOwner();
    private Application application = mock(Application.class);
    private MyViewModel viewModel = new MyViewModel(application);
    private double delta = 0.0001;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.myapplication", appContext.getPackageName());
    }

    public Float convert(int pressureUnit, Float value, int dp){
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
        return Float.parseFloat(answer.replaceAll(",",""));
    }

    private static LifecycleOwner mockLifecycleOwner() {
        LifecycleOwner owner = mock(LifecycleOwner.class);
        LifecycleRegistry lifecycle = new LifecycleRegistry(owner);
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        when(owner.getLifecycle()).thenReturn(lifecycle);
        return owner;
    }

    @SuppressWarnings("unchecked")
    private static <T> Observer<T> mockObserver() {
        return (Observer<T>) Mockito.mock(Observer.class);
    }

    @Test
    public void hiTest(){
        viewModel.getCH1_OUT1_HI().observe(owner, hi_value -> {
            assertEquals(0.0, hi_value, delta);
        });
        viewModel.getCH2_OUT1_HI().observe(owner, hi_value -> {
            assertEquals(0.0, hi_value, delta);
        });
        viewModel.getCH3_OUT1_HI().observe(owner, hi_value -> {
            assertEquals(0.0, hi_value,delta);
        });
    }

    @Test
    public void loTest(){
        viewModel.getCH1_OUT1_LO().observe(owner, lo_value -> {
            assertEquals(0.0, lo_value, delta);
        });
        viewModel.getCH2_OUT1_LO().observe(owner, lo_value -> {
            assertEquals(0.0, lo_value,delta);
        });
        viewModel.getCH3_OUT1_LO().observe(owner, lo_value -> {
            assertEquals(0.0, lo_value, delta);
        });
    }

    @Test
    public void hyTest(){
        viewModel.getCH1_OUT1_HY().observe(owner, hy_value -> {
            assertEquals(0.0, hy_value,  delta);
        });
        viewModel.getCH2_OUT1_HY().observe(owner, hy_value -> {
            assertEquals(0.0, hy_value,  delta);
        });
        viewModel.getCH3_OUT1_HY().observe(owner, hy_value -> {
            assertEquals(0.0, hy_value,  delta);
        });
    }

    @Test
    public void mpaRangeTestUppper(){
        Float hi = new Float(2.0);
        Float rest = new Float(0.0);
        viewModel.update(1,hi,rest,rest,0);
        
        viewModel.getCH1_OUT1_HI().observe(owner, hi_value -> {
            assertEquals(1.0,hi_value,delta);
        });
    }

    @Test
    public void mpaRangeTestLower(){
        Float hi = new Float(-2.0);
        Float rest = new Float(0.0);
        viewModel.update(1,hi,rest,rest,0);

        viewModel.getCH1_OUT1_HI().observe(owner, hi_value -> {
            assertEquals(0.0,hi_value,delta);
        });
    }

   @Test
    public void kpaTestUpper(){
        viewModel.setKpa();
        Float hiOutOfKPaBounds = new Float(1001);
        Float rest = new Float(0);
        viewModel.update(1,hiOutOfKPaBounds,rest,rest,0);

        viewModel.getCH1_OUT1_HI().observe(owner, hi -> {
            assertEquals(1000,convert(viewModel.getLittlePressure(),hi,viewModel.getLittleDisplayResolution()),delta);
        });
   }

   @Test
    public void kpaTestLower(){
       Float hi = new Float(-2.0);
       Float rest = new Float(0.0);
       viewModel.setKpa();
       viewModel.update(1,hi,rest,rest,0);

       viewModel.getCH1_OUT1_HI().observe(owner, hi_value -> {
           assertEquals(0.0,hi_value,delta);
       });
   }

    @Test
    public void kgfcm2TestUpper(){
        viewModel.setKgf();
        Float hi = new Float(10.3);
        Float rest = new Float(0);
        viewModel.update(1,hi,rest,rest,0);

        viewModel.getCH1_OUT1_HI().observe(owner, hi_value -> {
            assertEquals(10.197,convert(viewModel.getLittlePressure(),hi_value,viewModel.getLittleDisplayResolution()),0.01);
        });
    }

    @Test
    public void kgfcm2TestLower(){
        viewModel.setKgf();
        Float hi = new Float(-2.0);
        Float rest = new Float(0.0);

        viewModel.update(1,hi,rest,rest,0);

        viewModel.getCH1_OUT1_HI().observe(owner, hi_value -> {
            assertEquals(0.0,hi_value,delta);
        });
    }

    @Test
    public void barTestUpper(){
        viewModel.setBar();
        Float hi = new Float(10.1);
        Float rest = new Float(0);
        viewModel.update(1,hi,rest,rest,0);

        viewModel.getCH1_OUT1_HI().observe(owner, hi_value -> {
            assertEquals(10,convert(viewModel.getLittlePressure(),
                    hi_value,viewModel.getLittleDisplayResolution()),delta);
        });
    }

    @Test
    public void barTestLower(){
        viewModel.setBar();
        Float hi = new Float(-2.0);
        Float rest = new Float(0.0);

        viewModel.update(1,hi,rest,rest,0);

        viewModel.getCH1_OUT1_HI().observe(owner, hi_value -> {
            assertEquals(0.0,hi_value,delta);
        });
    }

    @Test
    public void psiTestUpper(){
        viewModel.setPsi();
        Float hi = new Float(146);
        Float rest = new Float(0);
        viewModel.update(1,hi,rest,rest,0);

        viewModel.getCH1_OUT1_HI().observe(owner, hi_value -> {
            assertEquals(145.038,convert(viewModel.getLittlePressure(),hi_value,1),0.01);
        });
    }

    @Test
    public void psiTestLower(){
        viewModel.setPsi();
        Float hi = new Float(-2.0);
        Float rest = new Float(0.0);

        viewModel.update(1,hi,rest,rest,0);

        viewModel.getCH1_OUT1_HI().observe(owner, hi_value -> {
            assertEquals(0.0,hi_value,delta);
        });
    }

    @Test
    public void mmhgTestUpper(){
        viewModel.setMmhg();
        Float hi = new Float(7501);
        Float rest = new Float(0);
        viewModel.update(1,hi,rest,rest,0);

        viewModel.getCH1_OUT1_HI().observe(owner, hi_value -> {
            assertEquals(7500.62,convert(viewModel.getLittlePressure(),hi_value,1),0.01);
        });
    }

    @Test
    public void mmhgTestLower(){
        viewModel.setMmhg();
        Float hi = new Float(-2.0);
        Float rest = new Float(0.0);

        viewModel.update(1,hi,rest,rest,0);

        viewModel.getCH1_OUT1_HI().observe(owner, hi_value -> {
            assertEquals(0.0,hi_value,delta);
        });
    }

    @Test
    public void cmhgTestUpper(){
        viewModel.setCmhg();
        Float hi = new Float(750.1);
        Float rest = new Float(0);
        viewModel.update(1,hi,rest,rest,0);

        viewModel.getCH1_OUT1_HI().observe(owner, hi_value -> {
            assertEquals(750.062,convert(viewModel.getLittlePressure(),hi_value,1),0.01);
        });
    }

    @Test
    public void cmhgTestLower(){
        viewModel.setCmhg();
        Float hi = new Float(-2.0);
        Float rest = new Float(0.0);

        viewModel.update(1,hi,rest,rest,0);

        viewModel.getCH1_OUT1_HI().observe(owner, hi_value -> {
            assertEquals(0.0,hi_value,delta);
        });
    }

    @Test
    public void inchhgTestUpper(){
        viewModel.setInchhg();
        Float hi = new Float(296);
        Float rest = new Float(0);
        viewModel.update(1,hi,rest,rest,0);

        viewModel.getCH1_OUT1_HI().observe(owner, hi_value -> {
            assertEquals(295.3,convert(viewModel.getLittlePressure(),hi_value,1),0.01);
        });
    }

    @Test
    public void inchhgTestLower(){
        viewModel.setInchhg();
        Float hi = new Float(-2.0);
        Float rest = new Float(0.0);

        viewModel.update(1,hi,rest,rest,0);

        viewModel.getCH1_OUT1_HI().observe(owner, hi_value -> {
            assertEquals(0.0,hi_value,delta);
        });
    }

}