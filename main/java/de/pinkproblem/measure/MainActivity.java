package de.pinkproblem.measure;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


public class MainActivity extends Activity implements SensorEventListener {

    private static final long SCAN_PERIOD = 1200;

    public static final String mac1 = "00:07:80:78:F5:88";
    public static final String mac2 = "00:07:80:78:FA:5A";
    public static final String mac3 = "00:07:80:1B:5C:7C";

    boolean running;
    MeasureStrategy measureStrategy;

    float rotation;
    SensorManager sensorManager;
    Sensor accelerometer;
    Sensor magnetometer;

    File file;
    FileOutputStream outputStream;

    BluetoothAdapter bluetoothAdapter;
    Handler handler;

    Button startButton;
    TextView rssiTextView;
    TextView rotationTextView;
    TextView resultTextView;
    TextView timeTextView;

    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        running = false;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        handler = new Handler();

        startButton = (Button) findViewById(R.id.start_button);
        rssiTextView = (TextView) findViewById(R.id.rssi_text_view);
        rotationTextView = (TextView) findViewById(R.id.rotation_text_view);
        resultTextView = (TextView) findViewById(R.id.result_text_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();

        sensorManager.unregisterListener(this);

        if (bluetoothAdapter != null) {
            bluetoothAdapter.stopLeScan(leScanCallback);
            handler.removeCallbacks(handlerAction);
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start(View view) {
        if (!running) {
            if (!isExternalStorageWritable()) {
                Toast.makeText(this, "External Storage not writeable", Toast.LENGTH_LONG).show();
                return;
            }

            //TODO
            //make new strategy instance, just in case
            measureStrategy = new MultiMagnetMeasure(this);

            File dir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS).getPath() + "/Measure/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file = new File(dir, buildFileName() + ".csv");

            try {
                outputStream = new FileOutputStream(file);
                //write header to file
                outputStream.write(measureStrategy.getHeaderLine().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("", "writing to: " + file.getAbsolutePath());

            bluetoothAdapter.startLeScan(leScanCallback);
            handler.postDelayed(handlerAction, SCAN_PERIOD);

            running = true;
            startButton.setText("Stop");
            resultTextView.setText("none");
        } else {
            bluetoothAdapter.stopLeScan(leScanCallback);
            handler.removeCallbacks(handlerAction);

            try {
                //write final lines
                outputStream.write(measureStrategy.getLines().getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            running = false;
            startButton.setText("Start");
            rssiTextView.setText("none");

            //show result
            resultTextView.setText(measureStrategy.getResult() + "");
        }
    }

    private String buildFileName() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        return year + "-" + month + "-" + day + "-" + hour + "." + minute + "-" + measureStrategy.strategyTag;
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private BluetoothAdapter.LeScanCallback leScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    final int rssi2 = rssi;

                    //check for correct device
                    if (!device.getAddress().equals(mac1) && !device.getAddress().equals(mac2) && !device.getAddress().equals(mac3)) {
                        return;
                    }

                    String line = measureStrategy.getLine(rssi);
                    if (!line.equals("")) {
                        line += "\n";
                    }
                    String line2 = measureStrategy.getLine(device.getAddress(), rssi);
                    if (!line2.equals("")) {
                        line2 += "\n";
                    }

                    try {
                        outputStream.write(line.getBytes());
                        outputStream.write(line2.getBytes());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rssiTextView.setText("" + rssi2);
                        }
                    });

                }
            };

    private Runnable handlerAction = new Runnable() {
        @Override
        public void run() {
            bluetoothAdapter.stopLeScan(leScanCallback);
            bluetoothAdapter.startLeScan(leScanCallback);
            handler.postDelayed(handlerAction, SCAN_PERIOD);
        }
    };

    float[] mGravity;
    float[] mGeomagnetic;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                rotation = orientation[0]; // orientation contains: azimut, pitch and roll
                rotationTextView.setText(rotation + "");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
