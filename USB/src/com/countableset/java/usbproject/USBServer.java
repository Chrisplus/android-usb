package com.countableset.java.usbproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class USBServer {

	public static Socket echoSocket = null;
	public static PrintStream out = null;
	public static BufferedReader in = null;
	public static String line = null;
	public static boolean send = true;
	private static int portNumber = 38301;
	
	public static void main(String[] args) throws IOException {

		System.out.println("Connection.main()");
		
		execAdb();
		
		connect();
	
		for(int i = 0; i <= 10; i++) {
			out.println("List Space: " + i);
			out.flush();
		}
		out.flush();

		
		while((line = in.readLine()) != null) {
			System.out.println("Server> " + line);
		}
		
		disconnect();
		
	} // end main()
	
	
	private static void connect() {
		try {
			echoSocket = new Socket("localhost", portNumber);
			out = new PrintStream(echoSocket.getOutputStream(), true);
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
		System.out.println("connected!");
	}
	
	private static void disconnect() throws IOException {
		out.close();
		in.close();
		echoSocket.close();
	}
	
	private static void execAdb() {
		// run the adb bride
		try {
			Process p = Runtime.getRuntime().exec("/Users/rachel/Documents/sdev/android-sdk/platform-tools/adb forward tcp:" + portNumber + " tcp:" + portNumber );
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

