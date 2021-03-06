package ConsoleV2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ExternalConsoleHandler {

	
	public int port = 1289;
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
	            System.out.println("EKEC Server Started\n");
	            System.out.println("External IP: "+ip);
	            Socket socket = new Socket();
	            socket.connect(new InetSocketAddress("google.com", 80));
	            String localip = socket.getLocalAddress().toString().replace("/", "");
	            System.out.println("Local IP: "+localip);
	            System.out.println("Local Port: "+port+"\n");
	            socket.close();
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
			 System.out.println("Error: 1");
		}
	       
	}
	

	
	public void startServer() {
		info();
	      
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
	               String ip = sock.getRemoteSocketAddress().toString().replace("/", "");
	               boolean stop = false;
	               while (stop == false && ((line = br.readLine()) != null)) {
	            	   String trueCommand = validate(line);
	                   Timestamp timestamp = new Timestamp(System.currentTimeMillis());

	            	   System.out.println("\n<Remote Command from "+ip+" at "+timestamp+">\n"+trueCommand);
	            	   String reply = processRequest(trueCommand);
	            	//   String next = br.readLine();
	            	//   System.out.println("lding next");
	            //	   System.out.println("NEXT!!! - "+next);
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
		//System.out.println("x1");
//		String breaks = "\r";
//    	int index = clientCommand.indexOf(breaks);
//    	while (index >= 0) {
//    	       System.out.println("FOUND \r at - "+index);
//    	       index = clientCommand.indexOf(breaks, index + 1);
//    	}
//		clientCommand = clientCommand.replace("\n", "");
//		clientCommand = clientCommand.replace("\r", "");
//		clientCommand = clientCommand.replace("\0", "");
	//	System.out.println("before - "+clientCommand);
		clientCommand = clientCommand.replaceAll("\\r|\\n", "");
		//System.out.println("after - "+clientCommand);


		
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
		//System.out.println(Request);
		// Put things back
		
		processExternalCommand(Request);
		
		
		
		System.out.flush();
		System.setOut(old);

		String output = baos.toString();
		// Show what happened
		//System.out.println("got - "+output);
		//output = "safksdflk;f\nasdas";
		
		System.out.println(output);
		String result  = makeJson(output);
	
		//System.out.println(result);
		//System.out.println("#1");
		
		return result;
	}
	
	private String makeJson(String responce) {
		
		String allLineBreaks = "";
		
		//find line breaks
		String breaks = "\n";
    	int index = responce.indexOf(breaks);
    	while (index >= 0) {
    	      // System.out.println("occurence at - "+index);
    	       allLineBreaks = allLineBreaks + index+",";
    	       index = responce.indexOf(breaks, index + 1);
    	}
    	String linebreaks = allLineBreaks;
    	// replace linebreaks
    	
    	//String replacedResponce = responce.replace("\n", "");
    	String replacedResponce =  responce.replaceAll("\\r", " ").replaceAll("\\n", " ");
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
			System.out.println("Experimental Kanemoke External Console EKEC");
			System.out.println("By Asim Younas");
			System.out.println("\nCommands");
			System.out.println("\ndetails - get details of connected server");
			System.out.println("clear - clear the console");
			System.out.println("list - shows all folder in root folder");
			System.out.println("mkdir [dir name] - creates a new folder in server");
		break;
		
		case "details":
			System.out.println("a machine ...........");
		break;
		
		case "list":
			listDir();
		break;
		default:
			String[] words = Request.split(" ");
			if(words[0].equals("mkdir") && words.length == 2) {
				makeDir(Request);
			}
			else {
				System.out.println("unknown command type 'help' for command list");
			}
			
			
		}

	}
	
	private void makeDir(String cmd) {
		String dirName = "F:\\ExternalDir\\";
		String[] words = cmd.split(" ");
		String name = words[1].replace(" ", "_");
		new File(dirName+name).mkdir();
		System.out.println("made new folder '"+name+"'");
	}
	
	private void listDir() {
        String dirName = "F:\\ExternalDir";

        try {
			Files.list(new File(dirName).toPath())
			        .limit(99)
			        .forEach(path -> {
			        	String pathf = path.toString().replace("F:\\ExternalDir\\", "");
			            System.out.println(pathf);
			        });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
