package util;

import java.util.Scanner;

public class Util {
	
	/**
	 * Private constructor.
	 * */
	private Util(){} 
	
	/**
	 * valid input if it is the number
	 * @param input is the input string
	 * */
	public static boolean isNumber(String input){
		boolean i;
        i = false;
        if (!input.isEmpty()) {
            
        	i = input.matches("[0-9]+");
            
        }
        return i;
	}
	
	// display operation message
	public static void operationMsg(String msg){
		System.out.print("\n\t" + msg);
		
		for(int i = 0; i<3; i++){
			System.out.print(".");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Confirm
	 * @Confirm before process operation
	 * @return true if user press y otherwise return false.
	 * @param msg is the message to confirm
	 * */
	public static boolean confirm(String msg) {
		// get user input
		Scanner in = new Scanner(System.in);
		boolean isValid = false;
		boolean confirm = false;

		// process again if user input invalid option
		/*System.out.println("\n\t" + msg);*/
		while (!isValid) {
			
			System.out.print("\t" + msg + " [Y/N] > ");
			if (in.hasNext()) {
				String input = in.nextLine();
				switch (input.toLowerCase()) {
				case "y":
					confirm = true;
					isValid = true; // cancel input process is user input valid option
					break;
				case "n":
					confirm = false;
					isValid = true; // cancel input process is user input valid option
					break;
				default:
					System.out.println("\tInvalid option...");
				}
			}

		}
		return confirm;
	}
}
