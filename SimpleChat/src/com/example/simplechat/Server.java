package com.example.simplechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static ServerSocket server_sock;
	private static Socket client_sock;
	private static BufferedReader in;
	
	public Server(int port) {
		try {
			System.out.println("Binding to port " + port);
			server_sock = new ServerSocket(port);
			System.out.println("Server started: " + server_sock);
			System.out.println("Waiting for a client");
			client_sock = server_sock.accept();
			System.out.println("Client accepted: " + client_sock);
			open();
			boolean done = false;
			while(!done) {
				try {
					//System.out.println("getting message...");
					String line = in.readLine();
					System.out.println("Client said: " + line);
					done = line.equals("exit");
				}
				catch(IOException e) {
					done = true;
				}
			}
			close();
		}
		catch(IOException e) {
			System.out.println(e);
		}
	}
	
	public void open() throws IOException {
		in = new BufferedReader(new InputStreamReader(client_sock.getInputStream()));
	}
	
	public void close() throws IOException {
		if(client_sock != null) { client_sock.close(); }
		if(in != null) { in.close(); }
	}
	public static void main(String[] args) {
		Server server = null;
		if(args.length != 1) {
			System.out.println("No port supplied");
		}
		else {
			server = new Server(Integer.parseInt(args[0]));
		}
    }
}