package ConsoleV2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ExternalConsoleHandler {

	private void test() {


	}
	
	private void info() {
		try {
	     URL whatismyip = new URL("http://checkip.amazonaws.com");
	        BufferedReader in = null;
	        try {
	            in = new BufferedReader(new InputStreamReader(
	                    whatismyip.openStream()));
	            String ip = in.readLine();
	            System.out.println("IP:"+ip);
	          //  return ip;
	        } finally {
	            if (in != null) {
	                try {
	                    in.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
		}
		catch(Exception e) {
			 System.out.println("Error:1");
		}
	       
	}
	

	
	public void startServer() {
		info();
	      int port = 1111;
	       ServerSocket listenSock = null; //the listening server socket
	        Socket sock = null;             //the socket that will actually be used for communication

	       try {

	           listenSock = new ServerSocket(port);

	           while (true) {       //we want the server to run till the end of times

	               sock = listenSock.accept();             //will block until connection recieved
	             //  System.out.println("recieved a client at " + new Timestamp(System.currentTimeMillis()));
	              // Thread.sleep(2000);
	               
	               BufferedReader br =    new BufferedReader(new InputStreamReader(sock.getInputStream()));
	               BufferedWriter bw =    new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
	               String line = "";
	               boolean stop = false;
	               while (stop == false && ((line = br.readLine()) != null)) {
	            	   String trueCommand = validate(line);
	            	   String reply = processRequest(trueCommand);
	            	  // System.out.println("reply - " +reply );
//	            	   String lines[] = reply.split("\\r?\\n");
//	            	   for(String line1:lines) {
//	            		   bw.write(line1+"\n");
//	            		   System.out.println("writing:"+line1);
//	            		   bw.newLine();
//	            	   }
	            	   bw.write(reply+"\n");
	                   bw.flush();
	                   stop = true;
	               }

	               //Closing streams and the current socket (not the listening socket!)
	               bw.close();
	               br.close();
	               sock.close();

	           }
	       } catch (Exception ex) {
	           ex.printStackTrace();
	       }
		
	}
	
	private String validate(String clientCommand) {
		System.out.println("x1");
//		String breaks = "\r";
//    	int index = clientCommand.indexOf(breaks);
//    	while (index >= 0) {
//    	       System.out.println("FOUND \r at - "+index);
//    	       index = clientCommand.indexOf(breaks, index + 1);
//    	}
//		clientCommand = clientCommand.replace("\n", "");
//		clientCommand = clientCommand.replace("\r", "");
//		clientCommand = clientCommand.replace("\0", "");
		System.out.println("before - "+clientCommand);
		clientCommand = clientCommand.replaceAll("\\r|\\n", "");
		System.out.println("after - "+clientCommand);


		
		return clientCommand;
	}
	
	private String processRequest(String Request) {
		//Request = "help";
		// Create a stream to hold the output
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		// IMPORTANT: Save the old System.out!
		PrintStream old = System.out;
		// Tell Java to use your special stream
		System.setOut(ps);
		
		
		
		// Print some output: goes to your special stream
		System.out.println(Request);
		// Put things back
		
		processExternalCommand(Request);
		
		
		
		System.out.flush();
		System.setOut(old);

		String output = baos.toString();
		// Show what happened
		System.out.println("got - "+output);
		//output = "safksdflk;f\nasdas";
		String result  = makeJson(output);
		
		System.out.println(result);
		System.out.println("#1");
		
		return result;
	}
	
	private String makeJson(String responce) {
		
		String allLineBreaks = "";
		
		//find line breaks
		String breaks = "\n";
    	int index = responce.indexOf(breaks);
    	while (index >= 0) {
    	       System.out.println("occurence at - "+index);
    	       allLineBreaks = allLineBreaks + index+",";
    	       index = responce.indexOf(breaks, index + 1);
    	}
    	String linebreaks = allLineBreaks;
    	// replace linebreaks
    	
    	//String replacedResponce = responce.replace("\n", "");
    	String replacedResponce =  responce.replaceAll("\\r", "").replaceAll("\\n", " ");
  
    //	System.out.println("z1"+replacedResponce);  
    	//System.out.println("z2"+replacedResponce);  
		HashMap<String, String> map = new HashMap<String, String>() {{
	            put("linebreaks", linebreaks);
	            put ("responce", replacedResponce);
	        }};


        String jsonBody = "{"+map.entrySet().stream()
        	    .map(e -> "\""+ e.getKey() + "\"" + ":\"" + String.valueOf(e.getValue()) + "\"")
        	    .collect(Collectors.joining(", "))+"}";
	        
	        return jsonBody;
	}
	
	
	
	private void processExternalCommand(String Request) {
		switch(Request) {
		case "help":
			System.out.println("K A N E M O K E   E X T E R N A L   C O N S O L E");
			System.out.println("B Y   A S I M   Y O U N A S");
			System.out.println("- - Commands - - \n details - get current details of machine");
		break;
		
		case "details":
			System.out.println("a machine ...........");
		break;
		
		default:
			System.out.println("unknown command type 'help' for command list");
			
		}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
