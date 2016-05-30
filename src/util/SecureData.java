package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import article.Data;
import article.FileProcess;
import article.TmpFileProcess;


public class SecureData {
	
	/**
	 * Private constructor.
	 * */
	private SecureData(){}
	
	
	/**
	 * Backup to the default part.
	 * */
	public static void backUp(){
		backUp("data/db/Data.jdb","data/backup","data/backup","Backup Done.\n\tYour data is secure.", "Backing up data", true);
	}
	
	/**
	 * Back data.
	 * @param src is the source to backup from.
	 * @param des is the destination to backup.
	 * @param checkDirParent to to check the parent directory.
	 * @param msg is operation message.
	 * @param isBackup consider backup or not. <tt>True</tt> is backup. <tt>False is not backup.</tt>
	 * */
	private static void backUp(String src, String des, String checkDirParent, String msg, String process, boolean isBackup){
		File fileold, filenew = null;
		FileInputStream Fis = null;
		FileOutputStream Fos = null;
		
		//Create Data and Format it
		DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd-HH.mm.ss");
		Date date = new Date();
		String d = dateFormat.format(date);
		String direcFileBackup="";
		 
		//file for backup and directory backup file

		 if(isBackup == true){
			 direcFileBackup = "data/backup/" + d + "_backup.arc";
		 }else{
			 
			 direcFileBackup = des;
		 }
			
		 try {
			
			 fileold = new File(checkDirParent);

			if(!fileold.exists()){
				
				fileold.mkdir();
	
			}
			
			Fis = new FileInputStream(new File(src));
			Fos = new FileOutputStream(direcFileBackup);
			
			// show operation process
			Util.operationMsg(process);
			
			byte[] buffer = new byte[1024];
			int length;
			// copy original data to backup file
			while ((length= Fis.read(buffer)) > 0){
				Fos.write(buffer,0 , length);
			}
			
			Fis.close();
			Fos.close();
			// operation message
    	    System.out.println("\n\t" + msg);
    	    
		} catch (IOException e) {
			System.err.println("\n\tOperation failure.");
		}
		
	}
	
	/**
	 * Restore file from default part.
	 * */
	public static void restore(){
		restore("data/backup","data/db/");
	}
	
	/**
	 * 
	 * */
	private static void restore(String directoryBupFolder,String directoryDbFolder){
		
		File fileold, filenew= null;
		
		String backupPath = "";//Variable for Store Path
		int num = 1 ;//Variable for incre number to display file Path
		
		//for open backup folder to list
		File directory = new File(directoryBupFolder);
		
		//for open db folder to copy databasename
		File directoryDb = new File(directoryDbFolder);
			
		//get all the files from a directory
		File[] fileList = directory.listFiles();
		
		/*//Loop Path of File for display
		for (File fileloop : fileList){
			System.out.println(num++ +". "+ fileloop.getPath());
		}*/
		//Loop Path of File for display
				System.out.println("");
				System.out.println("\t╔═════════════════════════════════════════╗");
				System.out.println("\t║    No    │         Backup File          ║");
				System.out.println("\t╚═════════════════════════════════════════╝");
				String name = "";
				int countBackUpFile = 0;
				for (File fileloop : fileList){
					name = fileloop.getName();
					if(name.contains("arc")){
						
						System.out.println("\t║ "+num++ +".       │ "+ fileloop.getName()+" ║");
						System.out.println("\t╚══════════╧══════════════════════════════╝");
						
						countBackUpFile++;
					}
					
				}
		
		Scanner sc = new Scanner(System.in);
		
		
		boolean isValidInput = false;
		String numberFromSelect = "";
		// store the name of selected file
		String selectedFileName = "";
		if(countBackUpFile==0){
			System.out.println("\n\tNo backup file found.");
			return;
		}
		while(!isValidInput){
			System.out.print("\n\tPlease number of file you want to restore: ");
			//select one of backup filename in folder backup
			
			if(sc.hasNext()){
				numberFromSelect = sc.nextLine();
				if(Util.isNumber(numberFromSelect)){
					isValidInput = true;
					if(Util.confirm("Are you sure want to restore to " + selectedFileName + " ?")){
						//get Full path in backup folder atfer select
						backupPath = fileList[Integer.parseInt(numberFromSelect)-1].getPath();
						//get all the files from folder db
						File[] DbList = directoryDb.listFiles();
						//create for get full path name in folder backup to rename 
						fileold = new File(backupPath);
						
						// get the file name
						selectedFileName = fileold.getName().split("_")[0];
						
						//write Path and write new name
						filenew = new File("data/backup/Data.jdb");
						
						//rename fileold to filenew
						fileold.renameTo(filenew);
						
						//create for delete file that first index in folder db  
						filenew = new File(DbList[0].getPath());

						//delete file at data/db and check if delete then copy from backup to data/db
						if(filenew.delete()){
							// copy backup file
							SecureData.backUp("data/backup/Data.jdb", "data/db/Data.jdb", "data/db", "Restore Done.", "Restoring data",  false);
							
							//rename from Data.jdb in folder backup to old backup name
							new File("data/backup/Data.jdb").renameTo(fileold); 
							
							// load data again from collection
							Data.setData(FileProcess.read());
							// set save status
							FileProcess.setSaveStatus(true);
							// delete tmp file
							TmpFileProcess.delete();
							// write new config
							Config.setAutoNumber(Data.getData().get(0).getId());
							Config.setCurrentPage(1);
							Config.setDisplayRow(5);
							Config.save();
							Run.start();
						}
					}

				}else{
					System.err.println("\tInput number is not valid.");
				}
			}
		}
				
	}
}
	
	
	
	


