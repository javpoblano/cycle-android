package com.jll.javpoblano.cycle.GPS;

import android.location.Location;

/**
 * Created by javpoblano on 2/27/15.
 */
public interface GPSCallback
{
    public abstract void onGPSUpdate(Location location);
}