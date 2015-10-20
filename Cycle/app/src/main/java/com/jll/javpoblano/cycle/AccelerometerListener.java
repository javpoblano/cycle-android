package com.jll.javpoblano.cycle;

/**
 * Created by javpoblano on 10/10/2015.
 */
public interface AccelerometerListener {

    public void onAccelerationChanged(float x, float y, float z);

    public void onShake(float force);

}
