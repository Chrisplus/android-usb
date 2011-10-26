package com.countableset.java.usbproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class Connection {

	public static void main(String[] args) throws IOException {

		System.out.println("Connection.main()");

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

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				System.in));
		// String userInput;
		System.out.println("connected!!");
		int counter = 0;

		// TODO monitor
//		while (true) {
//			counter++;
//			// out.println(counter);
//			if (counter % 1000 == 0) {
//				out.println("update" + new Date().getSeconds());
//				counter = 1;
//				System.out.println("echo: " + in.readLine());
//			}
//		}
	}
}

