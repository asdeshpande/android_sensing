package com.example.aniruddha.hello_world;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager               sMgr;
    private Sensor                      activeSensor;
    private TextView                    sensor1;
    private List<Sensor>                list;
    private RecyclerView                sensorList;
    private RecyclerView.Adapter        rAdapter;
    private RecyclerView.LayoutManager  rLayoutmngr;
    private String                      data;

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

        sensorList  = (RecyclerView)findViewById(R.id.recycler_view);
        rLayoutmngr = new LinearLayoutManager(this);
        sensorList.setLayoutManager(rLayoutmngr);
        rAdapter    = new MyAdapter(sensorNames.toArray(new String[0]));
        sensorList.setAdapter(rAdapter);
        sensorList.addOnItemTouchListener(
                new MyAdapter(getBaseContext(), new MyAdapter.OnItemClickListener(){
                    @Override
                    public void onItemClick(View view, int position){
                        //deactivate();
                        activeSensor = list.get(position);
                        activate();
                        Intent intent = new Intent();
                        intent.setClass(getBaseContext(), SensorDetail.class);
                        intent.putExtra("values",data);
                        startActivity(intent);

                    }
                })
        );

    }

    protected void updateTxt(TextView box,String data) {
       box.setText(data);
    }

    protected void activate() {
        super.onResume();
        sMgr.registerListener(this, activeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sMgr.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float [] value = event.values;
        data = value[0] + " " + value[1] + " " + value[2];
        Log.d("values", data);
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
