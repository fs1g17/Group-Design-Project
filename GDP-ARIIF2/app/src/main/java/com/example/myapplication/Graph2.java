package com.example.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.DataOutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Graph1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Graph2 extends Fragment {
    private View view;
    private MyViewModel viewModel;
    private LineChart graph;
    private TextView textView;

    public Graph2(){}

    public static Graph2 newInstance(){
        Graph2 graph2 = new Graph2();
        Bundle args = new Bundle();
        graph2.setArguments(args);
        return graph2;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_graph1, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        graph = view.findViewById(R.id.graph);
        textView = view.findViewById(R.id.text_view_graph);

        ArrayList<Entry> entries = new ArrayList<>();

        viewModel.getLiveReadCH2().observe(getViewLifecycleOwner(), ch1 -> {
            entries.add(new Entry(viewModel.getXAxis2(),ch1));
            viewModel.incXAxis2();

            LineDataSet dataSet = new LineDataSet(entries, "CH2");
            dataSet.setColor(Color.RED);

            LineData lineData = new LineData(dataSet);
            graph.setData(lineData);
            graph.invalidate();

            if(viewModel.getXAxis2() > 100){
                entries.remove(0);
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.resetXAxis2();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.resetXAxis2();
    }
}