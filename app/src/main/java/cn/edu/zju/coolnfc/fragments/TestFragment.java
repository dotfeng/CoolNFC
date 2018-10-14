package cn.edu.zju.coolnfc.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
        voltage = 0;
        temperatureC = 0;
        temperatureF = 0;
        setRetainInstance(true);// what does it mean
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_test, container,
                false);
        initVariables(layout);
        refreschOption();
        return layout;
    }


    private void initVariables(View layout) {
        lcdCheck = layout.findViewById(R.id.LCD_checkbox);
        tempCheck = layout.findViewById(R.id.Temp_Sensor_checkbox);

        lcdCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("nikolajohn","lcdCheck isChecked");
                } else {
                    Log.d("nikolajohn","lcdCheck NotChecked");
                }
            }
        });

        tempCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("nikolajohn","tempCheck isChecked");
                } else {
                    Log.d("nikolajohn","tempCheck NotChecked");
                }
            }
        });

        texttransferDir = (TextView) layout.findViewById(R.id.TransferDirection);
    }


    private void refreschOption() {
    }


    @Override
    public void onClick(View view) {

    }

    public static boolean isLCDEnabled() {
        return lcdCheck.isChecked();
    }

    public static CheckBox getLcdCheck() {
        return lcdCheck;
    }

    public static void setLcdCheck(CheckBox lcdCheck) {
        TestFragment.lcdCheck = lcdCheck;
    }

    public static CheckBox getTempCheck() {
        return tempCheck;
    }

    public static void setTempCheck(CheckBox tempCheck) {
        TestFragment.tempCheck = tempCheck;
    }

    public static TextView getTexttransferDir() {
        return texttransferDir;
    }

    public static void setTexttransferDir(TextView texttransferDir) {
        TestFragment.texttransferDir = texttransferDir;
    }

    public static double getVoltage() {
        return voltage;
    }

    public static void setVoltage(double voltage) {
        TestFragment.voltage = voltage;
    }

    public static double getTemperatureC() {
        return temperatureC;
    }

    public static void setTemperatureC(double temperatureC) {
        TestFragment.temperatureC = temperatureC;
    }

    public static double getTemperatureF() {
        return temperatureF;
    }

    public static void setTemperatureF(double temperatureF) {
        TestFragment.temperatureF = temperatureF;
    }
}
