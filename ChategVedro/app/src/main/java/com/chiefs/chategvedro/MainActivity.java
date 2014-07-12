package com.chiefs.chategvedro;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    ConnectionService connection;
    boolean bound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Permissions:
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        //Connection service starting and binding to it
        bound = false;
        Intent connectionIntent = new Intent(this, ConnectionService.class);
        ServiceConnection confusingTypeAndName = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                connection = ((ConnectionService.LocalBinder)iBinder).getService();
                bound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                bound = false;
            }
        };
        bindService(connectionIntent,confusingTypeAndName,BIND_AUTO_CREATE);
        //TODO: don't forget to start it

        //Visual
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
