package com.example.simplechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {
    private TextView msg_area;
    private EditText send_area;
    private Button btn;
    
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		msg_area = (TextView)findViewById(R.id.tv_chat);
		send_area = (EditText)findViewById(R.id.tb_msg);
		btn = (Button)findViewById(R.id.btn_send);
		
	
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Thread cThread = new Thread(new Client());
				cThread.start();
			}
		});
	}
	
	public class Client implements Runnable{
		public void run() {
			Socket socket;
			BufferedReader in;
			PrintWriter out;
			try {
				InetAddress server_addr = InetAddress.getByName("10.0.2.2");
				socket = new Socket(server_addr, 40001);

				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				
				String msg = send_area.getText().toString();
//				/Log.d(msg, msg);
				if(!msg.equals("exit")) {
					out.println(msg);
					out.flush();
				}
				else {
					if(in != null) {
						in.close();
					}
					if(out != null) {
						out.close();
					}
					if(socket != null) {
						socket.close();
					}
				}
			}
			catch(UnknownHostException e) {
				e.printStackTrace();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
