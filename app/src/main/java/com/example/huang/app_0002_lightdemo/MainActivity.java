package com.example.huang.app_0002_lightdemo;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.app.NotificationManager;
import android.app.Notification;

import android.os.Handler;
import android.widget.SeekBar;

import java.util.logging.LogRecord;
public class MainActivity extends AppCompatActivity {

    private Button mLightButton=null;
    boolean flashing=false;
    final private int LED_NOTIFICATION_ID=123;

    private SeekBar mBlackLightSeekBar=null;

    private Handler mLightHandler=new Handler();
    private LightRunable mLightRunable=new LightRunable();
    class LightRunable implements Runnable{
        @Override
        public void run() {
            if(flashing){
                FlashingLight();
            }else{
                ClearLED();
            }
        }
    }
    private void FlashingLight()
    {
        NotificationManager nm = ( NotificationManager ) getSystemService( NOTIFICATION_SERVICE );
        Notification notif = new Notification();
        notif.ledARGB = 0xFF0000ff;
        notif.flags = Notification.FLAG_SHOW_LIGHTS;
        notif.ledOnMS = 100;
        notif.ledOffMS = 100;
        nm.notify(LED_NOTIFICATION_ID, notif);

    }
    private void ClearLED()
    {
        NotificationManager nm = ( NotificationManager ) getSystemService( NOTIFICATION_SERVICE );
        nm.cancel( LED_NOTIFICATION_ID );
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBlackLightSeekBar=(SeekBar)findViewById(R.id.seekBar);

        try {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            
            int brightness= android.provider.Settings.System.getInt(getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS);
            mBlackLightSeekBar.setProgress(brightness*100/255);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        mBlackLightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int brightness=mBlackLightSeekBar.getProgress();
                brightness=brightness*255/100;
                android.provider.Settings.System.putInt(getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS,
                        brightness);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mLightButton = (Button)findViewById(R.id.button);
        mLightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                flashing=!flashing;
                if(flashing){
                    mLightButton.setText("Stop Flashing the Light");
                }else{
                    mLightButton.setText("Flashing Light at 20s");
                }
                mLightHandler.postDelayed(mLightRunable,20000);
            }
        });
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
