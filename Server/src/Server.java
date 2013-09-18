import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static ServerSocket server_sock;
	private static Socket client_sock;
	private static BufferedReader in;
	private PrintWriter out;

	/****************************************************************
	* Author: Lucas Walter						           
	* Function Name: public Server(int port)
	* Description: Public constructor for a server to bind and listen to a socket
	* Preconditions: int port representing the port number to bind to
	* Postconditions:  Initializes a Server object
	****************************************************************/
	public Server(int port) {
		try {
			System.out.println("Binding to port " + port);
			//bind to the given port
			server_sock = new ServerSocket(port);
			System.out.println("Server started: " + server_sock);
			System.out.println("Waiting for a client");
			//wait for a client to connect
			client_sock = server_sock.accept();
			System.out.println("Client accepted: " + client_sock);
			//open the bufferedreader to get data in the socket
			open();
			boolean done = false;
			//while the user hasn't entered in exit to quit the connection
			while(!done) {
				try {
					//System.out.println("getting message...");
					//get the data from the socket
					String line = in.readLine();
					//print it out on the Server command line
					System.out.println("Client said: " + line);
					//generate response to send back
					String response = "You said: " + line;
					System.out.println("Sending to client: " + response);
					out.println(response);
					//out.flush();
					//update setinel
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
	
	/****************************************************************
	* Author: Lucas Walter						           
	* Function Name: public void open() throws IOException
	* Description: Initializes an InputStreamReader at the socket to read in data
	* Preconditions: N/A
	* Postconditions:  Initializes an InputStreamReader at the bound socket
	****************************************************************/
	public void open() throws IOException {
		in = new BufferedReader(new InputStreamReader(client_sock.getInputStream()));
		out = new PrintWriter(client_sock.getOutputStream(), true);
	}
	
	/****************************************************************
	* Author: Lucas Walter						           
	* Function Name: public void close() throws IOException
	* Description: Closes the socket and the InputStreamReader when the client has 
	* 	disconnected
	* Preconditions: N/A
	* Postconditions: Socket and InputStreamReader closed
	****************************************************************/
	public void close() throws IOException {
		if(client_sock != null) { client_sock.close(); }
		if(in != null) { in.close(); }
	}
	
	/****************************************************************
	* Author: Lucas Walter						           
	* Function Name: public static main(String args[])
	* Description: Creates a server object from command line
	* Preconditions: N/A
	* Postconditions: Server data member is allocated and bound to a socket
	* 	ready for a client connection
	****************************************************************/
	public static void main(String[] args) {
		Server server = null;
		if(args.length != 1) {
			System.out.println("No port supplied, using port 40001");
			server = new Server(40001);
		}
		else {
			server = new Server(Integer.parseInt(args[0]));
		}
    }
}