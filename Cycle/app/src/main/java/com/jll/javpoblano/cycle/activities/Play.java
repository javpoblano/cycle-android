package com.jll.javpoblano.cycle.activities;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;
import android.widget.Toast;

import com.jll.javpoblano.cycle.AccelerometerListener;
import com.jll.javpoblano.cycle.AccelerometerManager;
import com.jll.javpoblano.cycle.GPS.AppSettings;
import com.jll.javpoblano.cycle.GPS.Constants;
import com.jll.javpoblano.cycle.GPS.GPSCallback;
import com.jll.javpoblano.cycle.GPS.GPSManager;
import com.jll.javpoblano.cycle.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Play extends AppCompatActivity implements GPSCallback,AccelerometerListener{

    private GPSManager gpsManager = null;
    private double speed = 0.0;
    private int measurement_index = Constants.INDEX_KM;
    private AbsoluteSizeSpan sizeSpanLarge = null;
    private AbsoluteSizeSpan sizeSpanSmall = null;
    private TextView number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        try
        {
            gpsManager = new GPSManager();

            gpsManager.startListening(getApplicationContext());
            gpsManager.setGPSCallback(this);

            number = ((TextView)findViewById(R.id.number));
            number.setText("0");

            measurement_index = AppSettings.getMeasureUnit(getApplicationContext());

            if (AccelerometerManager.isSupported(this)) {

                //Start Accelerometer Listening
                AccelerometerManager.startListening(this);
            }
        }
        catch(Exception ex)
        {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
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
        Spannable span = new SpannableString(text);

        span.setSpan(sizeSpanLarge, 0, 0,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(sizeSpanSmall, 0, text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        number.setText(text);
    }

    @Override
    public void onAccelerationChanged(float x, float y, float z) {
        //Snackbar.make(findViewById(R.id.content), "CAMBIO LA ACELERACION", Snackbar.LENGTH_LONG)
        //        .setAction("Action", null).show();
    }

    @Override
    public void onShake(float force) {
        /*Snackbar.make(findViewById(R.id.content), "ACCIDENTE", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/
        try
        {
            Uri number = Uri.parse("tel:2221773973");
            Intent callIntent = new Intent(Intent.ACTION_CALL, number);
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(callIntent);
        }
        catch (SecurityException e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }

    }
}
