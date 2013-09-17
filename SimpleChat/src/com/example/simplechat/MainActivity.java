package com.example.simplechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {
    private TextView msg_area;
    private EditText send_area;
    private Button btn;
    
    private Socket socket;
    private static final int PORT = 40001;
    private static String S_IP = "10.0.2.2";
    
    /****************************************************************
	* Author: Lucas Walter						           
	* Function Name: protected void onCreate(Bundle savedInstanceState)
	* Description: Creates the Activity to show on the Screen, initializes
	* 	objects
	* Preconditions: N/A
	* Postconditions: Thread created for the client, variables initialized
	****************************************************************/
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		msg_area = (TextView)findViewById(R.id.tv_chat);
		send_area = (EditText)findViewById(R.id.tb_msg);
		btn = (Button)findViewById(R.id.btn_send);
		new Thread(new ClientThread()).start();
		
	
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//if not connected to the socket...
				if(!socket.isConnected()) {
					new Thread(new ClientThread()).start();
				}
				
				try {
					//get the message to send to the server
					String msg = send_area.getText().toString();
					//init the outstream to the socket
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					//init the input stream from the server
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					//send the string to the socket
					out.println(msg);
					//out.flush();
					//get the response from the server
					String server_msg = in.readLine();
					//Log.d("server_msg", server_msg);
					//update the text field with the response
					msg_area.append(server_msg);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//If the app is stopped, attempt to close the socket
	protected void onStop() {
		super.onStop();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Inits the socket
	public class ClientThread implements Runnable {
		public void run() {
			try {
				InetAddress server_addr = InetAddress.getByName(S_IP);
				socket = new Socket(server_addr, PORT);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
