package com.countableset.android.usbsampleproject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class USBProject extends Activity implements OnClickListener {
	/**
	 * Class that deals with variables dealing with the connection process.
	 * I replaces socketIn with an BufferedReader instead of a Scanner 
	 * since it was providing results.
	 * socketOut - variable to print out to the socket
	 * socketIn - variable to receive input from socket 
	 */
	public static class Globals {
		public static boolean connected;
		public static PrintWriter socketOut;
		public static BufferedReader socketIn;
	}
	
	public static final String TAG = "USBProject";
	public static final int TIMEOUT = 10;
	private String connectionStatus = null;
	private List<String> stringList = null;
	private Handler mHandler = null;
	private ServerSocket server = null;
	private Socket client = null;
	private String selectedItem = null;
	private int portNumber = 38301;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//Set up click listeners for the buttons
		View connectButton = findViewById(R.id.connect_button);
		connectButton.setOnClickListener(this);

		mHandler = new Handler();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.connected_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.refresh:
				return true;
			case R.id.back:
				setContentView(R.layout.main);

				//Set up click listeners for the buttons
				View connectButton = findViewById(R.id.connect_button);
				connectButton.setOnClickListener(this);

				mHandler = new Handler();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	} // onOptionsItemSlected()

	/**
	 * The function that deals with the button click to connect via usb
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.connect_button:
				//initialize server socket in a new separate thread
				new Thread(initializeConnection).start();
				String msg="Attempting to connect...";
				Toast.makeText(USBProject.this, msg, msg.length()).show();
				break;
		}
	}

	/**
	 * Runs the thread to deals with input and hopefully output of the usb 
	 * connection. Make sure that ADB port forwarding is running on the
	 * machine that you want to connect to.
	 * abd forward tcp:38300 tcp:38300
	 * To call this thread, use the command:
	 * new Thread(initializeConnection).start();
	 */
	private Runnable initializeConnection = new Thread() {
		public void run() {
			// initialize server socket
			try {
				server = new ServerSocket(portNumber);
				Log.d(TAG, "waiting for connection");
				while(true) {
					//listen for incoming clients
					client = server.accept();
					try {
						Globals.socketIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
						Globals.socketOut = new PrintWriter(client.getOutputStream(), true);
						
						Globals.socketOut.println("Hello from the server!");
						
						
//						new Thread(getData).start();
						
						String line = null;
						stringList = new ArrayList<String>();
						// reading in the input from the usb socket
						while((line = Globals.socketIn.readLine()) != null) {
							stringList.add(line);
						}
						// show the input from usb as a list view in the activity
						mHandler.post(showListView);

						
					} catch (Exception e) {
						Log.d(TAG, "lost client");
						e.printStackTrace();
					} // try/catch
				} // while(true)
			} catch (Exception e) {
				connectionStatus = "error, disconnected";
				mHandler.post(showConnectionStatus);
				Log.d(TAG, "error, disconnected");
				e.printStackTrace();
			} // try/catch
		} // end run()
	};
	
	private Runnable getData = new Thread() {
		public void run() {
			try {
				String line = null;
				stringList = new ArrayList<String>();
				// reading in the input from the usb socket
				while((line = Globals.socketIn.readLine()) != null) {
					stringList.add(line);
				}
				// show the input from usb as a list view in the activity
				mHandler.post(showListView);
			} catch (Exception e) {
				Log.d(TAG, "error in getData");
			}
		}
	};
	
	private Runnable sendData = new Thread() {
		public void run() {
			Log.d(TAG, "send data " + selectedItem);
			Globals.socketOut.println(selectedItem);
		}
	};
	
	
	/**
	 * Shows a list view of of the input from socketIn
	 * called in initializeConnection thread
	 */
	private Runnable showListView = new Runnable() {
		public void run() {
			// List view stuff
			ListView lv = new ListView(USBProject.this);
			lv.setAdapter(new ArrayAdapter<String>(USBProject.this, R.layout.connected, stringList));
			setContentView(lv);
			
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// When clicked, send the text
					//Globals.socketOut.println(((TextView) view).getText());
					selectedItem = (String) ((TextView) view).getText();
					Log.d(TAG, "The selected item is (on listener): " + selectedItem);
					new Thread(sendData).start();
				}
			});
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