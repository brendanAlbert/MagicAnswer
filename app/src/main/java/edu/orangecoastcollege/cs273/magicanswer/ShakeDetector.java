package edu.orangecoastcollege.cs273.magicanswer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by balbert on 10/31/2017.
 *
 * ShakeDetector implements the SensorEventListener interface.
 *
 * We use this class and instances of it to utilize the various sensors available to
 * the Android OS.
 *
 * In this case we want to listen for only accelerometer sensor events
 * to detect when the User shakes the device, like they would a Magic 8 ball to
 * trigger a response to their question.
 *
 * We tune the sensitivity using the THRESHOLD constant so we can detect when the user
 * is shaking the device intentionally versus simply moving it slightly.
 *
 * We also make sure a certain specified time has elapsed between shakes.  This
 * is to prevent the detector from being too sensitive; triggering several times
 * during what is supposed to be a single shake.
 */

public class ShakeDetector implements SensorEventListener {

    private static final long ELAPSED_TIME = 1000L;
    // Accelerometer data uses float
    private static final float THRESHOLD = 17;

    private long previousShake;

    private OnShakeListener mListener;

    /**
     * ShakeDetector is a parameterized constructor that accepts a listener to initialize
     * the OnShakeListener.
     * @param listener
     */
    public ShakeDetector(OnShakeListener listener)
    {
        mListener = listener;
    }

    /**
     * onSensorChanged will detect changes to its sensors all the time, so
     * we have an if statement to check that the sensor type that is firing
     * is indeed from the accelerometer.
     *
     * We then check if the force of the shake meets our specified THRESHOLD constant.
     *
     * If these two conditions are met, we finally check that enough time has
     * elapsed since the last shake.
     *
     * If all three conditions are met, we register a shake event.
     *
     * @param sensorEvent when a sensor event is detected and passed in.
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // Ignore all other events, except ACCELEROMETERS
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            // Neutralize the effect of gravity (subtract it from each value)
            float gForceX = x - SensorManager.GRAVITY_EARTH;
            float gForceY = y - SensorManager.GRAVITY_EARTH;
            float gForceZ = z - SensorManager.GRAVITY_EARTH;

            float netForce = (float) Math.sqrt(Math.pow(gForceX, 2) + Math.pow(gForceY, 2) + Math.pow(gForceZ, 2));

            if (netForce >= THRESHOLD)
            {
                // Get current time
                long currentTime = System.currentTimeMillis();
                if (currentTime > previousShake + ELAPSED_TIME)
                {
                    // Reset the previous shake to current time
                    previousShake = currentTime;

                    // Register a shake event (it qualifies)
                    mListener.onShake();
                }
            }
        }
    }

    /**
     * onAccuracyChanged must be implemented because ShakeDetector implements the
     * SensorEventListener interface.
     *
     * @param sensor
     * @param i
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Do nothing, not being used, but must be defined.
    }



    /**
     * Define an interface for others to implement whenever a
     * true shake occurs. Interface = contract (method declarations WITHOUT implementation)
     * Some other class has to implement the method.  In the case of this app,
     * in onCreate in MagicAnswerActivity.  onShake is implemented there
     * and it calls the displayMagicAnswer method.
     */
    public interface OnShakeListener
    {
        void onShake();
    }

}
