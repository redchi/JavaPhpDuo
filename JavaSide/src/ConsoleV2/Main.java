package ConsoleV2;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main m = new Main();
		m.start();
	}

	void start() {
		ExternalConsoleHandler ECH = new ExternalConsoleHandler();
		ECH.startServer();
	}
	
	
	class ct1 implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("yee yee ct1");
		}
		
	}
	
	class ct2 implements Runnable{

		@Override
		public void run() {
			 System.out.println("Write something ");
			Scanner in = new Scanner(System.in);
	        String s = in.nextLine();
	        System.out.println("You entered string "+s);
			in.close();
		}
		
	}
	
	
}
