package com.tomcat.mlc_rd.bluetoothle_mlc_new;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();

    private BluetoothLeAdapter  mLeDeviceListAdapter;
    private BluetoothAdapter    mBluetoothAdapter;
    private boolean             mScanning;
    private Handler             mHandler;

    private final String        MLC_DEVICE_NAME = "3MW1-4B";
    private static final long   SCAN_PERIOD =  10000;   //10s
    private static final int    REQUEST_ENABLE_BT = 100;

    ListView                    bleDeviceList;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
        {
            Toast.makeText(this, "BLE is not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        final BluetoothManager  bluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null)
        {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bleDeviceList = (ListView)findViewById(R.id.listView);

        //other initial
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled())
        {
            if (!mBluetoothAdapter.isEnabled())
            {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        Log.i(TAG, "onResume...");
        mLeDeviceListAdapter = new BluetoothLeAdapter(getApplicationContext());
        bleDeviceList.setAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);

    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "request Code: " + requestCode + "  result Code:" + resultCode);

        /*
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_CANCELED)
        {
            finish();
            return;
        }
        */

        switch (requestCode)
        {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_CANCELED)
                {
                    finish();
                    return;
                }
                break;

            default:
                break;
        }
    }

    private void scanLeDevice(boolean enable)
    {
        if (enable)
        {
            mHandler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }
        else
        {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

        invalidateOptionsMenu();
    }


    private BluetoothAdapter.LeScanCallback   mLeScanCallback =
            new BluetoothAdapter.LeScanCallback()
            {
                /**
                 * Callback reporting an LE device found during a device scan initiated
                 * by the {@link BluetoothAdapter#startLeScan} function.
                 *
                 * @param device     Identifies the remote device
                 * @param rssi       The RSSI value for the remote device as reported by the
                 *                   Bluetooth hardware. 0 if no RSSI value is available.
                 * @param scanRecord The content of the advertisement record offered by
                 */
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if ((device!=null) && device.equals(MLC_DEVICE_NAME))
                            {
                                mLeDeviceListAdapter.addDevice(device, rssi);
                                mLeDeviceListAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            };
}

