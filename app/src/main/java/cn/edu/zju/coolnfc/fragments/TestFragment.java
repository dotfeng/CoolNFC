package cn.edu.zju.coolnfc.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import cn.edu.zju.coolnfc.R;

public class TestFragment extends Fragment implements View.OnClickListener {

    private static CheckBox lcdCheck;
    private static CheckBox tempCheck;
    private static TextView texttransferDir;
    private static double voltage;
    private static double temperatureC;
    private static double temperatureF;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false);
    }


    @Override
    public void onClick(View view) {

    }
}
