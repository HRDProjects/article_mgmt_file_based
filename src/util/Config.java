package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import article.Data;
import article.FileProcess;

public class Config {
	
	// store the real auto number value
	private static int autoNumber = 0; 
	// store the temporary auto number value that process in ram
	private static int tempAutoNumber = 0;
	// store the number of row to display in page
	private static int displayRow = 5;
	private static int tempDisplayRow = 5;
	// store the current viewing page
	private static int currentPage = 1;
	private static int tempCurrentPage = 1;
	// store the name of configuration file
	private static String fileName = "data/config/configuration.config";
	// Config object
	private static Config con = null;
	
	/**
	 * Private Constructor.
	 * Read data from file and initialize to the static field.
	 * */
	private Config(){ // initialize value
		BufferedReader br = null;
		StringTokenizer st = null;
		try {
			
			String s = "";
			String str[] = new String[3];
			br = new BufferedReader(new FileReader(fileName));
			int i = 0;
			
			while((s = br.readLine()) != null){
				// read from file and assign to array
				st = new StringTokenizer(s, ":");
				st.nextToken();
				str[i] = st.nextToken();
				i++;
			}
			
			// assign value to field
			if(str.length > 0){
				autoNumber = Integer.parseInt(str[0]);
				tempAutoNumber = autoNumber;
				displayRow = Integer.parseInt(str[1]);
				currentPage = Integer.parseInt(str[2]);
			}

		} catch (IOException e) {
			System.out.println("File " + fileName + " not found.");
		}finally{
			
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * Write config to file.
	 * */
	public static void save(){
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(fileName)));
			
			// write config
			bw.write("auto_number:" + ((FileProcess.checkSaveStatus())?tempAutoNumber:autoNumber));
			bw.newLine();
			bw.write("display_row:" + displayRow);
			bw.newLine();
			bw.write("current_page:" + currentPage);
			bw.newLine();
			bw.flush();
			
		} catch (IOException e) {
			System.out.println("\n\tFile " + fileName + " not found.");
		}finally{
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	/**
	 * @return Config object.
	 * */
	public static Config getInstance(){
		
		if(con == null){
			con =  new Config();
		}
		
		return con;
	}
	
	
	/** 
	 * @return auto number.
	 * */
	public static int getAutoNumber(){
		return tempAutoNumber;
	}
	
	/**
	 * @return display number.
	 * */
	public static int getDisplayRow(){
		if(Data.getSearchStatus()){
			return tempDisplayRow;
		}
		
		return displayRow;
	}
	
	/**
	 * @return current page.
	 * */
	public static int getCurrentPage(){
		if(Data.getSearchStatus()){
			return tempCurrentPage;
		}
		
		return currentPage;
	}
	
	/**
	 * Generate auto number when the method is invoked.
	 * */
	public static int generateAutoNumber(){
		return ++tempAutoNumber;
	}
	
	/**
	 * Set auto number.
	 * @param number is the auto number to be set.
	 * */
	public static void setAutoNumber(int number){
		if(number>0){
			autoNumber = number;
			tempAutoNumber = number;
		}
	}
	
	/**
	 * @param row is the display number.
	 * */
	public static void setDisplayRow(int row){
		if(row > 0 && row <= Data.getData().size()){
			if(Data.getSearchStatus()){
				tempDisplayRow = row; // set display temp for displaying the search collection
			}else{
				displayRow = row; // set current to for displaying main collection
				save();
			}
		}
	}
	
	/**
	 * @param page is the current display page.
	 * @param collectionSize is the total size of collection.
	 * */
	public static void setCurrentPage(int page/*, int collectionSize*/){
		
		if(page > 0 && page <= Data.size()/getDisplayRow()){
			if(Data.getSearchStatus()){
				tempCurrentPage = page; // set current temp for displaying the search collection
			}else{
				currentPage = page; // set current to for displaying main collection
				save();
			}
		}else{
			System.out.println("\tCannot go to page " + page + ". Page must be less than total pages.");
		}
	}
	
	
}
