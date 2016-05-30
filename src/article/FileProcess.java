package article;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.ArrayList;

import javax.print.attribute.standard.Finishings;

import log.Log;

public class FileProcess{
	
	/**
	 * Store process information
	 */
	// file base database
	private static String fileName = "data/db/Data.jdb";
	private static boolean isSave = true;
	
	/**
	 * Private constructor.
	 * */
	private FileProcess(){}
	
	/**
	 * Read data from file and return collection
	 * <tt>ArrayList of Article.</tt>
	 * */
	public static ArrayList<Article> read(){
		
		ArrayList<Article> article = new ArrayList<Article>();
		
		
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(new File(fileName));
			BufferedInputStream bin = new BufferedInputStream(fis);
			ois = new ObjectInputStream(bin);
			
			
			article = (ArrayList<Article>)ois.readObject();
			
			
		} catch (IOException e) {
			System.out.println("\n\tFile " + fileName + " not found.");
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found: " + e.getMessage());
		}finally{
			
			try {
				ois.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		
		return article;
	}
	
	/**
	 * Write collection 
	 * <tt>ArrayList of Article</tt> to file.
	 * */
	public static boolean write(ArrayList<Article> article){
		boolean isWrite = false;
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try {
			
			// instantiate object
			fos = new FileOutputStream(new File(fileName));
			BufferedOutputStream bout = new BufferedOutputStream(fos);
			oos = new ObjectOutputStream(bout);
			
			// write object
			oos.writeObject(article);
			isWrite = true;
			
		} catch (IOException e) {
			System.out.println("\n\tFile " + fileName + " not found.");
		}finally{
			
			try {
				oos.close();
				fos.close();
			} catch (IOException e) {
				System.out.println("\n\tCannot close file " + fileName);
			}
		}
		
		return isWrite;
		
	}
	
	/**
	 * Save collection to file
	 * */
	public static boolean save(){
		boolean save = false;
		if(write(Data.getData())){
			save = true;
			isSave = true;
			Log.save();
		}
		return save;
	}
	
	/**
	 * @return save status.
	 * */
	public static boolean checkSaveStatus(){
		return isSave;
	}
	
	/**
	 * Set save status
	 * @param status is boolean value. 
	 * <tt>True</tt> if record has been save.
	 * <tt>False</tt> if record has been modify.
	 * */
	public static void setSaveStatus(boolean status){
		isSave = status;
	}
	
}
