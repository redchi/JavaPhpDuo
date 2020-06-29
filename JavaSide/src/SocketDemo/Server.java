package SocketDemo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;

public class Server {

	   public static void main(String[] args) {
	       int port = 20222;
	       ServerSocket listenSock = null; //the listening server socket
	        Socket sock = null;             //the socket that will actually be used for communication

	       try {

	           listenSock = new ServerSocket(port);

	           while (true) {       //we want the server to run till the end of times

	               sock = listenSock.accept();             //will block until connection recieved
	               System.out.println("recieved a client at " + new Timestamp(System.currentTimeMillis()));
	               Thread.sleep(2000);
	               BufferedReader br =    new BufferedReader(new InputStreamReader(sock.getInputStream()));
	               BufferedWriter bw =    new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
	               String line = "";
	               while ((line = br.readLine()) != null) {
	                   bw.write("PHP said: " + "yee yee 123" + "\n");
	                   bw.flush();
	               }

	               //Closing streams and the current socket (not the listening socket!)
	                bw.close();
	               br.close();
	               sock.close();
	               System.out.println("request dealt with " +new Timestamp(System.currentTimeMillis())+"\n");
	               
	           }
	       } catch (Exception ex) {
	           ex.printStackTrace();
	       }
	   }



	
	
	
	
	
	
}
