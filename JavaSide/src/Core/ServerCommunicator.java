package Core;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ServerCommunicator {

	
	public ServerCommunicator() {
		
	}
	
	public void runTest() {

		try {
			
//	        HttpClient client = HttpClient.newHttpClient();
//	        HttpRequest request = HttpRequest.newBuilder()
//	                .uri(URI.create("http://localhost/api"))
//	                .build();
//	
//	        HttpResponse<String> response = client.send(request,
//	                HttpResponse.BodyHandlers.ofString());
//	
//	        System.out.println(response.body());
	      
			test2();
			
			
		}
		catch(Exception e){
			
		}
	}
	
	void test2() throws Exception{
		 HashMap<String, String> map = new HashMap<String, String>() {{
            put("name", "John Doe");
            put ("occupation", "gardener");
        }};


        String requestBody = "{"+map.entrySet().stream()
        	    .map(e -> "\""+ e.getKey() + "\"" + ":\"" + String.valueOf(e.getValue()) + "\"")
        	    .collect(Collectors.joining(", "))+"}";
        
        System.out.println(requestBody);
        requestBody = "yeeee";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost/api"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
		
		
		
	}
	
}
