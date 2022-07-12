package harsh.accelerometerreadings;

import static android.content.ContentValues.TAG;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
public SensorManager sensorManager;
public Sensor sensor;
public Sensor accelerometerSensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        if (accelerometerSensor != null)
        {
            Toast.makeText(this, "You got a acclerometer", Toast.LENGTH_SHORT).show();
            // Device has Accelerometer
        }
        else{
            Toast.makeText(this, "You dont have Accelerometer", Toast.LENGTH_SHORT).show();
        }
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        ActivityCompat.requestPermissions(this,permissions,41);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 41:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    private SensorManager mSensorManager;
    private Sensor mLight;

    private final SensorEventListener mLightSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            TextView xaxis = (TextView) findViewById(R.id.Xaxisvalue);
            xaxis.setText(String.valueOf(event.values[0]));
            TextView yaxis = (TextView) findViewById(R.id.Yaxisvalue);
            yaxis.setText(String.valueOf(event.values[1]));
            TextView zaxis = (TextView) findViewById(R.id.Zaxisvalue);
            zaxis.setText(String.valueOf(event.values[2]));

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.d("MY_APP", sensor.toString() + " - " + accuracy);
        }
    };
    HashMap<String, String> data = new HashMap<>();

    public void StartReading(View view) {
        TextView xaxis = (TextView) findViewById(R.id.Xaxisvalue);
        TextView yaxis = (TextView) findViewById(R.id.Yaxisvalue);
        TextView zaxis = (TextView) findViewById(R.id.Zaxisvalue);

        xaxis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String h = DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString();

                data.put(h,xaxis.getText().toString()+" "+yaxis.getText().toString()+" "+xaxis.getText().toString()+" \n");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//createFile();
view.setEnabled(false);

    }
    public void save(View view) {
creatingFile(data);
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
public void creatingFile(HashMap enterText){
    try {
        String h = DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString();
// this will create a new name everytime and unique
        File root = new File(Environment.getExternalStorageDirectory(), "Acceleration Reading");
// if external memory exists and folder with name Notes
        if (!root.exists()) {
            root.mkdirs(); // this will create folder.
        }
        File filepath = new File(root, h + ".txt"); // file path to save
        FileWriter writer = new FileWriter(filepath);
        writer.append("Accelerometer reading \n Date followed by the values of x,y,z axis \n");
        writer.append(enterText.toString());
        writer.append("\n");
        writer.flush();
        writer.close();
        String m = "File generated with name " + h + ".txt";
        Toast.makeText(this, "sucessfully created the file with filename "+h+".txt", Toast.LENGTH_LONG).show();


    }
    catch (IOException e) {
        e.printStackTrace();
        Log.d(TAG, e.toString());
        //   result.setText(e.getMessage().toString());
    }

}
    @Override
    protected void onResume() {
        super.onResume();
        if (mLight != null) {
            mSensorManager.registerListener(mLightSensorListener, mLight,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLight != null) {
            mSensorManager.unregisterListener(mLightSensorListener);
        }
}}