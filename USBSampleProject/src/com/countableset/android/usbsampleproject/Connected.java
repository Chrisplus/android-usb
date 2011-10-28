package com.countableset.android.usbsampleproject;

import com.countableset.android.usbsampleproject.USBProject.Globals;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class Connected extends Activity {
	
	String input = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connected);
		
//		while(true) {
//			if(Globals.socketIn.hasNext()) {
//				input = Globals.socketIn.next();
//				Toast.makeText(Connected.this, input, Toast.LENGTH_SHORT).show();
//			}
//		}
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
				refresh();
				return true;
			case R.id.back:
				Intent i = new Intent(this, USBProject.class);
				startActivity(i);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	} // onOptionsItemSlected()
	
	
	// Refresh the content via usb?
	public void refresh() {
		
	}

}
