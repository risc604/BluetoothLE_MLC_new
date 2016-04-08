package com.tomcat.mlc_rd.bluetoothle_mlc_new;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tomcat on 2016/4/7.
 */
public class BluetoothLeAdapter extends BaseAdapter
{
    private final ArrayList<BluetoothDevice>    mLeDevices;
    private final HashMap<String, Integer>      rssiMap;
    private LayoutInflater                      mInflator;
    private ViewHolder                          viewHolder;


    public BluetoothLeAdapter(Context context /*, ArrayList<BluetoothDevice> list*/)
    {
        super();
        mLeDevices = new ArrayList<BluetoothDevice>();
        rssiMap = new HashMap<String, Integer>();
        mInflator = LayoutInflater.from(context);
    }


    public void addDevice(BluetoothDevice device, int rssi)
    {
        if (!mLeDevices.contains(device))
        {
            mLeDevices.add(device);
            rssiMap.put(device.getAddress(), rssi);
        }
    }

    public BluetoothDevice getDevice(int position)
    {
        return mLeDevices.get(position);
    }

    public int getRssi(String devcieAddress)
    {
        return rssiMap.get(devcieAddress);
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount()
    {
        return mLeDevices.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position)
    {
        //return null;
        return mLeDevices.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position)
    {
        //return 0;
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder  viewHolder;

        if (convertView == null)
        {
            convertView = mInflator.inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceName = (TextView)convertView.findViewById(R.id.tvName);
            viewHolder.deviceAddress = (TextView)convertView.findViewById(R.id.tvAddress);
            viewHolder.deviceRssi = (TextView)convertView.findViewById(R.id.tvRssi);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BluetoothDevice device = mLeDevices.get(position);
        final String    deviceName = device.getName();
        if ((deviceName != null) && (deviceName.length() > 0))
        {
            viewHolder.deviceName.setText(deviceName);
        }
        else
        {
            viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceRssi.setText(rssiMap.get(device.getAddress()));
        }
        viewHolder.deviceAddress.setText(device.getAddress());

        return convertView;
        //return null;
    }

    static class ViewHolder
    {
        TextView    deviceName;
        TextView    deviceAddress;
        TextView    deviceRssi;
    }
}



