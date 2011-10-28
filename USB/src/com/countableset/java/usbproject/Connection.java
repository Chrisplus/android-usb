package com.countableset.java.usbproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Scanner;

public class Connection {

	public static void main(String[] args) throws IOException {

		System.out.println("Connection.main()");
		
		execAdb();
		
		Socket echoSocket = null;
		PrintStream out = null;
		BufferedReader in = null;

		try {
			echoSocket = new Socket("localhost", 38300);
			out = new PrintStream(echoSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(
					echoSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: localhost.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for "
					+ "the connection to: localhost.");
			System.exit(1);
		}

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		// String userInput;
		System.out.println("connected!!");

//		int counter = 0;
//		TODO monitor
//		while (true) {
//			counter++;
//			// out.println(counter);
//			if (counter % 1000 == 0) {
//				out.println("update" + new Date().getSeconds());
//				counter = 1;
//				System.out.println("echo: " + in.readLine());
//			}
//		}
		
		String line = null;
		while((line = in.readLine()) != null) {
			System.out.println("Got something!: " + line);
		}
		
		//out.println("testing output to input");
		
		out.close();
		in.close();
		echoSocket.close();
	} // end main()
	
	private static void execAdb() {
		// run the adb bride
		try {
			Process p = Runtime.getRuntime().exec("/Users/rachel/Documents/sdev/android-sdk/platform-tools/adb forward tcp:38300 tcp:38300");
			Scanner sc = new Scanner(p.getErrorStream());
			if(sc.hasNext()) {
				while(sc.hasNext()) System.out.println(sc.next());
				System.out.println("Cannot start the Android debug bridge");
			}
		} catch(Exception e) {
			System.out.println(e.toString());
		}
	} // end private void execAdb()
} // end class Connection

