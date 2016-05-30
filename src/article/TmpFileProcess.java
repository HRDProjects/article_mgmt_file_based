package article;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import util.Config;
import util.Util;
import log.Log;

public class TmpFileProcess {
	/**
	 * Store process information
	 */
	// file base database
	private static String fileName = "data/db/Data.tmp";
	
	/**
	 * Private constructor.
	 * */
	private TmpFileProcess(){}
	
	/**
	 * Check temporary data on application load.
	 * If use didn't save data before that stop app or system failure.
	 * */ 
	public static void check(){
		File f = new File(fileName);
		boolean isRecovery = false;
		if(f.exists()){ // check if file exist
			if(f.isFile()){ // if temp is a file
				
				// confirm user to recovery
				if(Util.confirm("Your have the record that you didn't save for your last session?\n\tDo you want to recovery?")){
					BufferedReader bw = null;
					try {
						bw = new BufferedReader(new FileReader(f));
						
						String s = "";
						String[] str = null;
						Article art_insert = null;

						while((s = bw.readLine()) != null){
							str = s.split("\t");
							
							// switch the process type
							switch(str[0].toLowerCase()){
								case "insert":
									// create object article
									art_insert = new Article(Config.generateAutoNumber(), str[2], str[3], str[4].replace("!@#", "\n\t"), str[5]);
									// insert new article to collection
									Data.getData().add(0, art_insert);
									// add new log
									Log.setTempLog("Record\tID: " + Config.getAutoNumber() + "\thas been inserted.");
									break;
								case "update":
									// search for article index
									int index = Data.searchIndex(Integer.parseInt(str[1].split(":")[1])); 
									// search for article before update
									if(index > -1){
										// update record
										Data.getData().get(index).setTitle(str[2]);
										Data.getData().get(index).setAuthor(str[3]);
										Data.getData().get(index).setContent(str[4].replace("!@#", "\n"));
										// add new log
										Log.setTempLog("Record\tID: " + str[1].split(":")[1] + "\thas been updated.");
									}
									break;
								case "delete":
									// search for article index
									int index_d = Data.searchIndex(Integer.parseInt(str[1].split(":")[1]));
									if(index_d > -1){
										Data.getData().remove(index_d);
										// add new log
										Log.setTempLog("Record\tID: " + str[1].split(":")[1] + "\thas been deleted.");
									}
									break;
							}
						}
 						
					} catch (IOException e) {
						
					}finally{
						try {
							bw.close();
						} catch (IOException e) {
							System.err.println("\n\tCannot close file " + fileName);
						}
					}
					
					isRecovery = true;
					
				} // end if condition
				
				// delete temp file
				f.delete();
				
				if(isRecovery){
					Util.operationMsg("Recovering data");
					// write new data to file
					FileProcess.write(Data.getData());
					Config.save();
					Log.save();
					System.out.println("\n\tRecovery successfully");
					System.out.println("\n\tYour data is up to date.");
				}else{
					System.out.println("\n\tRecovery cancelled by user.");
				}
				
			}
		}else{
			//System.out.println("\n\tYour data is up to date.");
		}
	}

	/**
	 * Save temporary data to file.
	 * @param art is the article object.
	 * @param status is the status of process.
	 * */
	public static void save(Article art, String status){
		
		BufferedWriter bw = null;
		try{
			
			// instantiate object
			bw = new BufferedWriter(new FileWriter(new File(fileName), true));
			bw.write(status.toLowerCase() + "\tID:" + art.getId() + 
					"\t" + art.getTitle() + 
					"\t" + art.getAuthor() + 
					"\t" + art.getContent().replace("\n\t", "!@#") + "\t" + art.getDate());
			bw.newLine();
			
		}catch(IOException e){
			
		}finally{
			try {
				bw.close();
			} catch (IOException e) {
				System.err.println("\n\tCannot close file " + fileName);
			}
		}
		
	}

	/**
	 * Delete temp file.
	 * */ 
	public static boolean delete(){
		boolean isDelete = false;
		File f = new File(fileName);
		
		if(f.exists()){ // check if file exist
			if(f.isFile()){ //check if a file
				if(f.delete())
					isDelete = true;
			}
		}
		
		return isDelete;
	}
}
