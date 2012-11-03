package com.example.myfirstapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

public class DDSBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			String phoneState =  bundle.getString(TelephonyManager.EXTRA_STATE);
			if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
				Log.d("broadcast", "Incoming call received, ip:" + MainActivity.ip);
				URL connectURL;
				try {
					connectURL = new URL("http://192.168.1.10:8080/deregisterDevice?ip=" + MainActivity.ip);
					URLConnection conn = connectURL.openConnection();
					// Get the response
					BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					StringBuffer sb = new StringBuffer();
					String line;
					while ((line = rd.readLine()) != null)
					{
						sb.append(line);
					}
					rd.close();
					String result = sb.toString();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		

	}

}
