package log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import article.Data;

public class Log {
	
	
	private static String fileName = "data/log/log.txt";
	
	// array to store the transaction log
	private static ArrayList<String> txn_log = new ArrayList<String>();

	/**
	 * Set temp log.
	 * @param log is the transaction log.
	 * This method will add log to array to use for writing log to file when user save.
	 * */ 
	public static void setTempLog(String log){
		txn_log.add(formatLog(log));
	}
	
	/**
	 * Clear all temp log from memory.
	 * */
	public static void clearTempLog(){
		txn_log.clear();
	}
	
	/**
	 * Write log file.
	 * @param log is the log information.
	 * */
	public static void save(){
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			
			fw = new FileWriter(new File(fileName), true);
			bw = new BufferedWriter(fw);
			
			for(String s : txn_log){
				bw.append(s);
				bw.newLine();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			
				try {
					bw.close();
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	
	private static String formatLog(String log){
		Date d = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd h:m:s");
		return dateFormat.format(d) + "\t" + log;
	}
	
}
