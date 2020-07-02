package ConsoleDemo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		main m = new main();
	
		m.start();
	}
	
	public main() {
		
		
	}
	
	public void start() {
		Thread t1 = new Thread(new SocketThread());
		Thread t2 = new Thread(new CountThread());
		t1.start();
		t2.start();
	}
	
	
	
	
	class SocketThread implements Runnable{
		
		public void run() {
			listen();
		}
		
		private void listen() {
		       int port = 20222;
		       ServerSocket listenSock = null; //the listening server socket
		        Socket sock = null;             //the socket that will actually be used for communication

		       try {

		           listenSock = new ServerSocket(port);

		           while (true) {       //we want the server to run till the end of times

		               sock = listenSock.accept();             //will block until connection recieved
		               System.out.println("recieved a client at " + new Timestamp(System.currentTimeMillis()));
		              // Thread.sleep(2000);
		               BufferedReader br =    new BufferedReader(new InputStreamReader(sock.getInputStream()));
		               BufferedWriter bw =    new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		               String line = "";
		               while ((line = br.readLine()) != null) {
		            	   String reply = processRequest(line);
		                   bw.write("Java Reply: " + reply + "\n");
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
		
		private String processRequest(String command) throws Exception {
			System.out.println("got command - "+ command);
			if(command.equals("give update")) {
				String num = "" +CountThread.getNum();
				return num;
			}
			else {
				throw new Exception();
			}
		}
		
		
	}
	
	
	
	static class CountThread implements Runnable{
		
		private static volatile int number = 0;
		
		
		
		public synchronized static int getNum() {
			return number;
		}
		
		public void run() {
			while(true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				number = number + 1;
			}
		}
	}
	
	
	
}
