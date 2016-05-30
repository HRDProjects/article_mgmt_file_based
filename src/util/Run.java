package util;

import java.util.*;

import log.Log;
import article.*;
import display.*;

public class Run {

	
	
	public static void main(String[] args) {
		
		//Display.logo();
		
		// test writing object
		testWrite();
		
		// test reading object
		testRead();
		
		Config c = Config.getInstance();
		Config.save();
		
		TmpFileProcess.check();
		
		displayRecord();
		start();
	}
	
	public static void displayRecord(){
		// display record
		if(Data.size() == 0){
			System.out.println("\n\tNo record...");
		}else{
			Pagination.initialize();
		}
	}
	
	public static void testWrite(){
		long start = System.currentTimeMillis();
		Data.testWrite(5_000_000);
		Util.operationMsg("\tWriting record");
		FileProcess.write(Data.getData());
		long stop = System.currentTimeMillis();
		System.out.println("\tWrite Complete: " + (stop-start)/1000);
	}
	
	public static void testRead(){
		long start = System.currentTimeMillis();
		Util.operationMsg("\tLoading record");
		
		try{
			Data.setData(FileProcess.read());
		}catch(Exception e){}
		
		long stop = System.currentTimeMillis();
		System.out.println("\tRead Complete: " + (stop-start)/1000);
	}
	
	public static void start(){

		Display.menu();
		
		// get input from keyboard
		Scanner in = new Scanner(System.in);
		/*store input option*/
		String op = "";
		
		System.out.print("\n\tOption > ");
		
		if(in.hasNext()){
			op = in.nextLine();
			String[] options = op.split("#");
			if(options.length==0){
				System.out.println("\n\tIncorrect Syntax...");
				in.nextLine();
				start();
			}
			try{
				// switch option
				switch(options[0].toLowerCase()){
					case "*":
						Data.clearSearch();
						//Pagination.first(Data.getData());
						Pagination.initialize();
						start();
						break;
					case "w":
						// consider the write method
						
						if(options.length>1){ // if user input u with record
							Data.write(options[1]);
						}else{ // if user input only id
							Data.write();
						}
						start();
						break;
					case "r":
						/*consider the read method*/
						if(options.length>1){ // if user input u with id
							if(!Util.isNumber(options[1])){
								System.out.println("\n\tIncorrect Syntax...");
								in.nextLine();
								start();
								return;
							}
							Data.readDetail(Integer.parseInt(options[1]));
						}else{ // if user input only id
							Data.readDetail();
						}
						start();
						break;
					case "u":
						/*consider the update method*/
						
						if(options.length>1){ // if user input u with id
							
							if(options[1].split("-").length > 0){
								
								if(options[1].split("-").length > 1)
									Data.update(options[1]);
								else
									Data.update(Integer.parseInt(options[1]));
							}else{
								System.out.println("\n\tIncorrect Syntax, update must include record id...");
								in.nextLine();
							}
							
						}else{ // if user input only id
							Data.update();
						}
						start();
						break;
					case "d":
						/*consider the delete method*/
						if(options.length>1){ // if user input u with id
							// check if index 1 is number of not
							if(!Util.isNumber(options[1])){
								// remove all record is option is valid
								if(options[1].toLowerCase().matches("a")){
									Data.deleteAll();
									start();
								}
								else{
									throw new Exception();
								}
								
							}else{
								Data.delete(Integer.parseInt(options[1]));
							}
							
						}else{ // if user input only id
							Data.delete();
						}
						start();
						break;
						
					case "s":
						/*consider the delete method*/
						if(options.length>1){ // if user input u with id
							Data.search(options[1]);
						}else{ // if user input only id
							Data.search();
						}
						
						start();
						break;
					case "g":
						Pagination.gotoPage(Data.getData());
						start();
						break;
					case "se":
						Pagination.rowSet(Data.getData());
						start();
						break;
					case "e":
						// check if record has been save to file or not
						if(!FileProcess.checkSaveStatus()){
							if(Util.confirm("Your record has been modified.\n\tDo you want to save it?")){
								Util.operationMsg("Saving data to file");
								FileProcess.save();
								TmpFileProcess.delete();
								Config.save();
								System.out.println("\n\tYour data has been save to file.");
							}else{
								TmpFileProcess.delete();
							}
						}
						// edit application
						System.exit(0);
					case "f":
						//System.out.println(Data.getData());
						Pagination.first(Data.getData());
						start();
						break;
					case "p":
						Pagination.previous(Data.getData());
						start();
						break;
					case "n":
						Pagination.next(Data.getData());
						start();
						break;
					case "l":
						Pagination.last(Data.getData());
						start();
						break;
					case "sa":
						if(!FileProcess.checkSaveStatus()){ // check save status before save
							if(Util.confirm("Do you really want to save?")){
								Util.operationMsg("Saving data to file");
								FileProcess.save();
								TmpFileProcess.delete();
								Config.save();
								System.out.println("\n\tYour data has been save to file.");
							}
						}else{
							System.out.println("\n\tYour data is up to date. No need to save.");
						}
						displayRecord();
						start();
						break;
					case "b":
						if(Util.confirm("Are you sure want to backup?"))
							SecureData.backUp();
						displayRecord();
						start();
						break;
					case "re":
						SecureData.restore();
						displayRecord();
						start();
						break;
					case "h":
						Display.help();
						break;
					default:
						System.out.println("\n\tIncorrect Syntax...");
						in.nextLine();
						displayRecord();
						start();
				}
			}catch(Exception e){
				System.out.println("\n\tIncorrect Syntax...");
				in.nextLine();
				start();
				
			}
		}
	}

}
