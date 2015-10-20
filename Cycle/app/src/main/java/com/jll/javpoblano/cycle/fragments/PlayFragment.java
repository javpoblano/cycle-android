package com.jll.javpoblano.cycle.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jll.javpoblano.cycle.GPS.AppSettings;
import com.jll.javpoblano.cycle.GPS.Constants;
import com.jll.javpoblano.cycle.GPS.GPSCallback;
import com.jll.javpoblano.cycle.GPS.GPSManager;
import com.jll.javpoblano.cycle.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by javpoblano on 10/10/2015.
 */
public class PlayFragment extends Fragment implements GPSCallback {
    private GPSManager gpsManager = null;
    private double speed = 0.0;
    private int measurement_index = Constants.INDEX_KM;
    private AbsoluteSizeSpan sizeSpanLarge = null;
    private AbsoluteSizeSpan sizeSpanSmall = null;
    private TextView number;



    public static PlayFragment newInstance() {
        PlayFragment fragment = new PlayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PlayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_seguimiento, container, false);
        try
        {
            gpsManager = new GPSManager();

            gpsManager.startListening(rootView.getContext());
            gpsManager.setGPSCallback(this);

            number = ((TextView)rootView.findViewById(R.id.number));
            number.setText("0");

            measurement_index = AppSettings.getMeasureUnit(rootView.getContext());


        }
        catch(Exception ex)
        {
            Toast.makeText(rootView.getContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
        return rootView;
    }

        private double convertSpeed(double speed){
            return ((speed * Constants.HOUR_MULTIPLIER) * Constants.UNIT_MULTIPLIERS[measurement_index]);
        }

        private String measurementUnitString(int unitIndex){
            String string = "";

            switch(unitIndex)
            {
                case Constants.INDEX_KM:                string = "km/h";        break;
                case Constants.INDEX_MILES:     string = "mi/h";        break;
            }

            return string;
        }

        private double roundDecimal(double value, final int decimalPlace)
        {
            BigDecimal bd = new BigDecimal(value);

            bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
            value = bd.doubleValue();

            return value;
        }


        @Override
        public void onGPSUpdate(Location location) {
            location.getLatitude();
            location.getLongitude();
            speed = location.getSpeed();

            String speedString = "" + roundDecimal(convertSpeed(speed),2);
            //String unitString = measurementUnitString(measurement_index);

            setSpeedText(speedString);
        }

        private void setSpeedText(String text)
        {
            number.setText(text);
        }
}

