package com.countableset.android.usbsampleproject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class USBProject extends Activity implements OnClickListener {

	public static class Globals {
		public static boolean connected;
		public static Scanner socketIn;
		public static PrintWriter socketOut;
	}
	
	public static final String TAG="Connection";
	public static final int TIMEOUT=10;
	Intent i=null;
	TextView tv=null;
	private String connectionStatus=null;
	private Handler mHandler=null;
	ServerSocket server=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//Set up click listeners for the buttons
		View connectButton = findViewById(R.id.connect_button);
		connectButton.setOnClickListener(this);

		i = new Intent(this, Connected.class);
		mHandler=new Handler();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.connect_button:
				//tv = (TextView) findViewById(R.id.connection_text);
				//initialize server socket in a new separate thread
				new Thread(initializeConnection).start();
				String msg="Attempting to connect...";
				Toast.makeText(USBProject.this, msg, msg.length()).show();
				break;
		}
	}

	private Runnable initializeConnection = new Thread() {
		public void run() {

			Socket client=null;
			// initialize server socket
			try{
				server = new ServerSocket(38300);
				server.setSoTimeout(TIMEOUT*1000);

				//attempt to accept a connection
				client = server.accept();
				Globals.socketIn=new Scanner(client.getInputStream());
				Globals.socketOut = new PrintWriter(client.getOutputStream(), true);
			} catch (SocketTimeoutException e) {
				// print out TIMEOUT
				connectionStatus="Connection has timed out! Please try again";
				mHandler.post(showConnectionStatus);
			} catch (IOException e) {
				Log.e(TAG, ""+e);
			} finally {
				//close the server socket
				try {
					if (server!=null)
						server.close();
				} catch (IOException ec) {
					Log.e(TAG, "Cannot close server socket"+ec);
				}
			}

			if (client!=null) {
				Globals.connected=true;
				//		print out success
				connectionStatus="Connection was succesful!";
				mHandler.post(showConnectionStatus);
				
				Globals.socketOut.println("Hey you!");
				
				//startActivity(i);
			}
		}
	};

	/**
	* Pops up a "toast" to indicate the connection status
	*/
	private Runnable showConnectionStatus = new Runnable() {
		public void run() {
			Toast.makeText(USBProject.this, connectionStatus, Toast.LENGTH_SHORT).show();
		}
	};
}