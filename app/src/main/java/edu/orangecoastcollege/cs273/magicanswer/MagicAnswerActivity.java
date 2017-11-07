package edu.orangecoastcollege.cs273.magicanswer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * MagicAnswerActivity is the main controller for our Magic Answer app.
 *
 * A new MagicAnswer object is instantiated, View widgets are wired up,
 * and a SensorManager is obtained to gain access to the accelerometer of
 * the device running the app.
 *
 * When the user tilts the device enough to trigger the sensor, displayMagicAnswer is called
 * and a random answer is acquired from the MagicAnswer object.
 *
 * Another important method is onStop.  When the User switches contexts, or puts this app
 * in the background, we want to unregister the listener which detects shakes using the
 * accelerometer.  This saves memory and prevents unnecessary drain on the battery since the User
 * is not using it.
 *
 * If the User returns, onResume is called and a new listener is registered.
 */
public class MagicAnswerActivity extends AppCompatActivity {

    MagicAnswer magicAnswer;
    private TextView answerTextView;

    private SensorManager mSensorManager;
    private Sensor accelerometer;
    // Create a reference to our ShakeDetector
    private ShakeDetector mShakeDetector;

    /**
     * onCreate sets the content view and gets a reference to the TextView widget for displaying
     * a random answer.
     *
     * A new MagicAnswer object is instantiated.
     *
     * The SensorManager is registered and we identify that we want to use the device's
     * accelerometer sensor.
     *
     * We instantiate a ShakeDetector which if a valid shake is detected, will call
     * displayMagicAnswer().
     *
     * @param savedInstanceState is the state the app may have been in before it was sent
     *                           to the background and is being returned from.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic_answer);

        // TASK 1: SET THE REFERENCES TO THE LAYOUT ELEMENTS
        answerTextView = (TextView) findViewById(R.id.answerTextView);

        // TASK 2: CREATE A NEW MAGIC ANSWER OBJECT
        magicAnswer = new MagicAnswer(this);

        // TASK 3: REGISTER THE SENSOR MANAGER AND SETUP THE SHAKE DETECTION
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                displayMagicAnswer();
            }
        });
    }

    /**
     * displayMagicAnswer simply accesses a random answer from our MagicAnswer object.
     * The answerTextView is populated with the answer and displayed.
     */
    public void displayMagicAnswer()
    {
        String randomAnswer = magicAnswer.getRandomAnswer();
        answerTextView.setText(randomAnswer);
    }

    /**
     * onResume is called after onStart when the user first starts the app, or it can be
     * called after the user returns from a different app or activity.  We want to
     * register a listener to the accelerometer.
     */
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * onStop is called when the activity is finishing or being destroyed by the system.
     * For instance, if the user switches apps, we don't want this listener to continue listening,
     * hogging memory and draining the battery.
     */
    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(mShakeDetector, accelerometer);
    }
}
