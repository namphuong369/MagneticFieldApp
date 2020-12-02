package com.nam.magneticfieldapp.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.nam.magneticfieldapp.R;

import java.io.IOException;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {
    private TextView tv_tesla, tv_xyz;
    private ImageView iv_phone, iv_music;
    private ProgressBar progressBar;
    private LineChart mChart;
    private Thread thread;
    private boolean plotData = true;
    private Sensor sensor;
    private SensorManager sensorManager;
    private DecimalFormat df = new DecimalFormat("#");
    private int color;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private int music, vib;
    private boolean isMusic=true;
    private boolean isVib=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBar();
        initView();
        createChart();
        feedMultiple();
    }

    private void feedMultiple() {

        if (thread != null) {
            thread.interrupt();
        }

        thread = new Thread(() -> {
            while (true) {
                plotData = true;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void initView() {
        tv_tesla = findViewById(R.id.tv_app);
        tv_xyz = findViewById(R.id.tv_xyz);
        iv_music = findViewById(R.id.iv_music);
        iv_phone = findViewById(R.id.iv_phone);
        progressBar = findViewById(R.id.progress_app);
        mChart = findViewById(R.id.chart_line);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.ting);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        iv_music.setOnClickListener(this);
        iv_phone.setOnClickListener(this);
    }

    private void createChart() {
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // set an alternative background color
        mChart.setBackgroundColor(Color.TRANSPARENT);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.YELLOW);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(12f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.setDrawBorders(false);

    }

    private void addEntry(double event) {

        LineData data = mChart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), (float) (event)), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(150);
            mChart.moveViewToX(data.getEntryCount());

        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Tín hiệu từ trường");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.RED);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (plotData) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            double m = Math.sqrt((x * x) + (y * y) + (z * z));
            tv_xyz.setText("X:" + df.format(x) + "\t" + "\t"
                    + "Y:" + df.format(y) + "\t" + "\t"
                    + "Z:" + df.format(z));
            tv_tesla.setText(df.format(m));
            progressBar.setProgress((int) m);
            //
            try {
                if (m > 9.5) {
                    if (isMusic) {
                        mediaPlayer.start();
                    } else {
                        mediaPlayer.stop();
                        mediaPlayer.prepare();
                    }
                    if(isVib){
                        vibrator.vibrate(500);
                    }else{
                        vibrator.vibrate(0);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            //0 50 100 150 200
            if (0 < m && m <= 50) {
                color = Color.GREEN;
            } else if (50 < m && m <= 100) {
                color = Color.YELLOW;
            } else if (100 < m && m <= 150) {
                color = Color.parseColor("#FF6D00");
            } else if (150 < m && m <= 200) {
                color = Color.RED;
            }
            progressBar.setProgressTintList(ColorStateList.valueOf(color));
            addEntry(m);
            plotData = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            tv_xyz.setText("Sensor không được hỗ trợ");
            tv_tesla.setText("0");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (thread != null) {
            thread.interrupt();
        }
        sensorManager.unregisterListener(this);
    }

    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }
    }

    private void showDialogExit() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_exit_app);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView oke = dialog.findViewById(R.id.tv_ok_exit);
        TextView close = dialog.findViewById(R.id.bt_close_exit);

        oke.setOnClickListener(v -> {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            finish();
            dialog.dismiss();
        });
        close.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store")));
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        showDialogExit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_music:
                music++;
                if (music % 2 == 0) {
                    iv_music.setImageResource(R.drawable.ic_sound_music);
                    isMusic = true;
                } else {
                    iv_music.setImageResource(R.drawable.ic_sound_mute);
                    isMusic = false;
                }
                break;
            case R.id.iv_phone:
                vib++;
                if (vib % 2 == 0) {
                    iv_phone.setImageResource(R.drawable.ic_phone);
                    isVib = true;
                } else {
                    iv_phone.setImageResource(R.drawable.ic_ring);
                    isVib = false;
                }
                break;
        }
    }
}
