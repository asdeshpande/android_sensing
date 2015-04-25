package com.example.aniruddha.hello_world;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager       sMgr;
    private Sensor              activeSensor;
    private TextView            sensor1;

    private List<Sensor>        list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        final List<String> sensorNames  = new ArrayList<>();

        list = sMgr.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor:list){
            sensorNames.add(sensor.getName());
            // Log.d("names", sensor.getName());
        }
       //   accelerometer = sMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
          sensor1 = (TextView) findViewById(R.id.textView1);
        final ListView sensorList = (ListView) findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sensorNames);
        sensorList.setAdapter(adapter);
        sensorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 deactivate();
                 activeSensor = list.get(position);
                 activate();
                 Log.d("sensor", activeSensor.getName());
            }
        });
    }

    protected void updateTxt(TextView box,String data) {
       box.setText(data);
    }

    protected void activate() {
        super.onResume();
        sMgr.registerListener(this, activeSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    protected void deactivate() {
        super.onPause();
        sMgr.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float [] value = event.values;
        String data = value[0] + " " + value[1] + " " + value[2];
        updateTxt(sensor1, data);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
}
