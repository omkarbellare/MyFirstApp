package com.example.myfirstapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	public static String ip = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@SuppressLint({ "NewApi", "NewApi" })
	public void sendMessage(View view){
		String  result="";

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		try{
			for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
				NetworkInterface intf = en.nextElement();
				for(Enumeration<InetAddress> enumIPAddr = intf.getInetAddresses(); enumIPAddr.hasMoreElements();){
					InetAddress inetAddress = enumIPAddr.nextElement();
					if(!inetAddress.isLoopbackAddress()){
						ip = inetAddress.getHostAddress().toString();
						if (!ip.contains("."))
							continue;
						//Toast.makeText(getApplicationContext(), "IP Address is "+ ip, Toast.LENGTH_LONG).show();
					}
				}
			}
		}
		catch(Exception e){
			System.out.println("Exception caught ="+e.getMessage());
		}

		try{
			URL connectURL = new URL("http://192.168.1.10:8080/registerDevice?ip="+ip);
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
			result = sb.toString();
		} 
		catch(MalformedURLException e){
			System.out.println("Wrong URL!");
		} catch (IOException e) {
			System.out.println("Problem connecting to server");
			e.printStackTrace();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		Toast.makeText(getApplicationContext(), "IP Address is "+ result, Toast.LENGTH_LONG).show();

		Intent intent = new Intent(Intent.ACTION_MAIN);
		PackageManager manager = getPackageManager();
		intent = manager.getLaunchIntentForPackage("com.pas.webcam");
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		startActivity(intent);
	}
}
