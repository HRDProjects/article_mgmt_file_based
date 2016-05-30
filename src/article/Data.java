package article;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import util.Config;
import util.Util;
import log.Log;
import display.Display;
import display.Pagination;

public class Data {

	/* static variable */

	// variable autoNumber use to add the auto number key to the data when insert
	
	private static boolean isFound = false;
	
	// collection for storing data
	private static ArrayList<Article> data = new ArrayList<Article>();
	// temporary collection for search
	private static ArrayList<Article> temp = new ArrayList<Article>();
	
	/**
	 * Private constructor.
	 * */
	private Data(){}
	
	/**
	 * Display all record
	 * */
	public static void display(ArrayList<Article> arr){

		// check if arr length is less than end index or not
		if(arr.size() < Config.getDisplayRow() + 1){
			Pagination.endIndex = arr.size();
		}
		
		// display header	
		Display.header();
		
		// display each record
		for(int i=Pagination.startIndex; i<Pagination.endIndex;i++){
			Display.row(arr.get(i).getId(), arr.get(i).getTitle(), arr.get(i).getAuthor(), arr.get(i).getDate());
		}
		
		// display footer
		Display.footer(arr);

	}
	
	// method for processing data
	
	/**
	 * Test Insert
	 * For testing 
	 * */
	public static void testWrite(int record) {
		data = new  ArrayList<Article>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		String d = dateFormat.format(date);
		int autoNumber = Config.getAutoNumber();
		
		for (int i = 1; i <= record; i++) {
			data.add(new Article(++autoNumber, "Title", "Author", "Content", d));
		}
		
		Collections.reverse(data);;
		Config.setAutoNumber(autoNumber);
		Config.save();
	}

	
	/*Writing Record*/
	/**
	 * Inserting new Data.
	 * The article will input in this method
	 * */
	public static void write() {
		// get date
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		String d = dateFormat.format(date);
		// get input from keyboard
		Scanner in = new Scanner(System.in);
		// array of string to store the input data
		String[] s = new String[3];
		
		System.out.println();
		System.out.println("\tID: " + (Config.getAutoNumber()+1));
		// title
		System.out.print("\tTitle > ");
		s[0] = in.nextLine();

		// author
		System.out.print("\tAuthor > ");
		s[1] = in.nextLine();

		// content
		System.out.print("\tContent");
		s[2] = getMiltiLineString("");
		// write data
		write(s[0]+"-"+s[1]+"-"+s[2]);

	}

	/**
	 * Inserting new Data.
	 * @param record is the record that pass from main form
	 * */
	public static void write(String record){
		// get date
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		String d = dateFormat.format(date);
		// split the record
		String [] arr_split = record.split("-");
			 
		//check if input is valid
		if(arr_split.length != 3){
			
			System.out.println("\n\tIncorrect syntax..." );
			
		}else{
			
			//check if content has ; will replace to \n for enter
			String enterString="";
			
			if(arr_split[2]!= null){
				enterString = arr_split[2].replace(";", "\n\t");
			}
			
			Display.enter(25);
			System.out.println("\tArticle Detail");
			System.out.println("\tInsert Date: " + d);
			System.out.println("\tArticle ID: " + (Config.getAutoNumber()+1));
			System.out.println("\tTitle: " + arr_split[0]);
			System.out.println("\tAuthor: " + arr_split[1]);
			System.out.println("\tContent: " + arr_split[2]);
			
			if(Util.confirm("Are you sure want to save this record?")){
				// add new record to first index of collection
				Article art = new Article(Config.generateAutoNumber(), arr_split[0], arr_split[1], enterString, d); 
				data.add(0, art);
				
				// operation process
				Util.operationMsg("Inserting new record");
				// change save status when data is write
				FileProcess.setSaveStatus(false);
				// write temp data to file
				TmpFileProcess.save(art, "insert");
				// add new log
				Log.setTempLog("Record\tID: " + Config.getAutoNumber() + "\thas been inserted.");
				// enter new line
				Display.enter(20);
				// display record
				display(data);

				
			}else{
				System.out.println("\n\tOperation cancelled by user...");
			}
		}
	}
	
	/* Read Record */
	/**
	 * Read Record based on input id.
	 * @param id is the id of record. 
	 * Use id when searching for specific article.
	 * */
	public static void read(int id) {

		// display header
		Display.header();
		int index = searchIndex(id);
		// display record
		Display.row(data.get(index).getId(), 
				data.get(index).getTitle(), 
				data.get(index).getAuthor(), 
				data.get(index).getDate());

		// display footer
		Display.footer(data);

	}

	/**
	 * Read Record by specific range.
	 * @param firstIndex is the start index of record.
	 * @param lastIndex is the stop index of record
	 * */
	public static void read(ArrayList<Article> arr, int firstIndex, int lastIndex) {

		// display header
		Display.header();

		// get the record by specific rang
		List<Article> list = arr.subList(firstIndex, lastIndex);

		// display the record
		for (Article art : list) {
			Display.row(art.getId(), art.getTitle(), art.getAuthor(), art.getDate());
		}

		// display footer
		Display.footer(Data.getData());

	}

	/**
	 * Read the detail of record
	 * The id of record is allow user input in this method.
	 * */
	public static void readDetail() {
		// get input from keyboard
		Scanner in = new Scanner(System.in);
		// if the value input from keyboard is valid id
		boolean isInputAgain = true;
		// continue process the input if the input is invalid
		while (isInputAgain) {
			System.out.print("\n\tArticle ID > ");
			if (in.hasNext()) {
				// check the input value
				String input = in.nextLine();
				if (Util.isNumber(input)) {
					isInputAgain = false;
					readDetail(Integer.parseInt(input));
				}
			}
		}
	}

	/**
	 * Read Detail
	 * @param id is the id of record.
	 * */
	public static void readDetail(int id) {
		// operation process
		Util.operationMsg("Searching record");
		// search for index of record
		int index = searchIndex(id);
		if (index >= 0) {
			Display.enter(25);
			System.out.println("\tArticle Detail");
			System.out.println("\tLast update: " + data.get(index).getDate());
			System.out.println("\tArticle ID: " + data.get(index).getId());
			System.out.println("\tTitle: " + data.get(index).getTitle());
			System.out.println("\tAuthor: " + data.get(index).getAuthor());
			System.out.println("\tContent: " + data.get(index).getContent());
		}else{
			System.out.println("\n\tRecord not found...");
		}
	}

	/* Update Record */
	/**
	 * Update record
	 * The id of record is allow user input in this method.
	 * */
	public static boolean update() {
		// get input from keyboard
		Scanner in = new Scanner(System.in);

		boolean isUpdate = false;

		// if the value input from keyboard is valid id
		boolean isInputAgain = true;
		// continue process the input if the input is invalid
		while (isInputAgain) {
			System.out.print("\n\tArticle ID to update >");
			if (in.hasNext()) {

				// check the input value
				String input = in.nextLine();
				if (Util.isNumber(input)) {
					String updateStatus = update(Integer.parseInt(input));
					if(updateStatus == "true"){
						isInputAgain = false;
						isUpdate = true;
					}else if(updateStatus == "cancel"){
						isInputAgain = false;
						isUpdate = false;
					}
					
				} else {

					System.out.println("\n\tInput id is not valid!");
				}
			}
		}

		// return boolean value
		return isUpdate;
	}
	
	/**
	 * Update data
	 * @param id is the id of record.
	 * */
	public static String update(int id) {
		// get input value from keyboard
		Scanner in = new Scanner(System.in);
		String isUpdate = "false";

		// if the value input from keyboard is valid id
		boolean isInputAgain = true;
		
		boolean isInform = false;
		
		// get the record index of the inputed id
		int index = Data.searchIndex(id);
		
		// start updating process if the record found
		if (index >= 0) {
			// show old record to user before update
			readDetail(id);
			// store oldValue
			String[] s = { data.get(index).getTitle(), data.get(index).getAuthor(), data.get(index).getContent() };
			// get new date
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date date = new Date();
			String d = dateFormat.format(date);

			System.out.print("\n\tWhat do you want to update?  ");
			
			String op = "";
						
			while (isInputAgain) {
				// display option
				System.out.print("\n\t(Al)All\t(T)Title\t(A)Author\t(C)Content\t(E)Exit\t> ");
				// if user has input
				if (in.hasNext()) {
					op = in.nextLine();
					isInputAgain = false;
					// switch the input option
					switch (op.toLowerCase()) {
						case "al": // case update all, allow user to input new data one by one
							// replace old record
							System.out.print("\tTitle > ");
							s[0] = in.nextLine();
							// replace old author
							System.out.print("\tAuthor > ");
							s[1] = in.nextLine();
							// replace old content
							System.out.print("\tContent");
							s[2] = getMiltiLineString("");
							isInform = true;
							break;
						case "t": // replace only title
							// replace old author
							System.out.print("\tTitle > ");
							s[0] = in.nextLine();
							isInform = true;
							break;
						case "a": // replace only author
							// replace old author
							System.out.print("\tAuthor > ");
							s[1] = in.nextLine();
							isInform = true;
							break;
						case "c": // replace only content
							// replace old content
							System.out.print("\tContent");
							s[2] = getMiltiLineString("");
							isInform = true;
							break;
						case "e": // cancel the operation
							System.out.println("\n\tOperation cancelled by user...");
							display(data);
							isInputAgain = false;
							isUpdate = "cancel";
							break;
						default:
							System.out.println("\n\tInvalid option...");
							isInputAgain = true;
							break;
					}

				}

				/* display data before update to make sure */
				if(isInform){
					// enter new line
					Display.enter(25);
					// display record before save
					System.out.println("\tArticle Detail");
					System.out.println("\tLast update: " + d);
					System.out.println("\tArticle ID: " + id);
					System.out.println("\tTitle: " + s[0]);
					System.out.println("\tAuthor: " + s[1]);
					System.out.println("\tContent: " + s[2]);
					// confirm user before update
					if (Util.confirm("\n\tAre you sure want to update?")) {
	
						// remove old data
						data.remove(index);
						Article art = new Article(id, s[0], s[1], s[2], d);
						// add record to collection
						data.add(index, art);
						// write temp data to file
						TmpFileProcess.save(art, "update");
						// operation process
						Util.operationMsg("Updating record");
						// change save status when data is write
						FileProcess.setSaveStatus(false);
						
						display(data);
						isUpdate = "true";
					}else{
						isUpdate = "cancel";
					}
				}
			}

		} else {
			// display message to inform user that the input id doesn't exist
			System.out.println("\n\tRecord not found...");
		}
		
		// return boolean value
		return isUpdate;
	}
	
	/**
	 * Update base on the input string.
	 * @param text (<tt>Id</tt>-<tt>Title</tt>-<tt>Author</tt>-<tt>Content</tt>)
	 * */
	public static boolean update(String text) {

		boolean isUpdate = true;
		
		// get the record index of the inputed id
		String [] array_hasSplit = text.split("-");
		
		// exit function if no id is input
		if(!Util.isNumber(array_hasSplit[0])){
			System.out.println("\n\tYou haven't input id...");
			return false;
		}
		
		int index = Data.searchIndex(Integer.parseInt(array_hasSplit[0]));
		
		// start updating process if the record found
		if (index >= 0) {
	
			// store oldValue
			String[] s = { data.get(index).getTitle(), data.get(index).getAuthor(), data.get(index).getContent() };
			// get new date
			DateFormat dateFormat = new SimpleDateFormat(
									"yyyy/MM/dd");
			Date date = new Date();
			String d = dateFormat.format(date);
			
			// update only title
			if(array_hasSplit.length == 2){
				s[0] = array_hasSplit[1];
			}
			
			// update title and author
			if(array_hasSplit.length == 3){
				s[0] = array_hasSplit[1];
				s[1] = array_hasSplit[2];
			}
			
			// update all
			if(array_hasSplit.length == 4){
				s[0] = array_hasSplit[1];
				s[1] = array_hasSplit[2];
				s[2] = array_hasSplit[3];
			}
			
			// enter new line
			Display.enter(25);
			// display record before save
			System.out.println("\tArticle Detail");
			System.out.println("\tLast update: " + d);
			System.out.println("\tArticle ID: " + array_hasSplit[0]);
			System.out.println("\tTitle: " + s[0]);
			System.out.println("\tAuthor: " + s[1]);
			System.out.println("\tContent: " + s[2]);
			
			// confirm user before update
			if (Util.confirm("\n\tAre you sure want to update?")) {
							
				// remove old data
				data.remove(index);
				Article art = new Article(Integer.parseInt(array_hasSplit[0]) , s[0], s[1], s[2],d);
				// add record to collection
				data.add(index, art);
				isUpdate = true;
				// write temp data to file
				TmpFileProcess.save(art, "update");
				// operation process
				Util.operationMsg("Updating record");
				// change save status when data is write
				FileProcess.setSaveStatus(false);
				// add new log
				Log.setTempLog("Record\tID: " + array_hasSplit[0] + "\thas been updated.");
			}
	
		}else{
			System.out.println("\n\tID doesn't exist, no Record to update...");
		}
		
		return isUpdate;
	}
	
	/**
	 * Delete record
	 * 
	 * @param id
	 * */
	public static boolean delete() {
		// get input from keyboard
		Scanner in = new Scanner(System.in);

		boolean isDeleted = false;
		boolean isInputAgain = true;
		// continue process the input if the input is invalid
		while (isInputAgain) {
			System.out.print("\tArticle ID to delete > ");
			if (in.hasNext()) {
				// check the input value is valid or not
				String input = in.nextLine();
				if (Util.isNumber(input)) {
					isInputAgain = false;
					delete(Integer.parseInt(input));
				} else {

					System.out.println("\tInput id is not valid!");
				}
			}
		}

		// return true if delete successfully
		return isDeleted;
	}
	
	/**
	 * Delete record
	 * @param id is the id of record.
	 * */
	public static boolean delete(int id) {
		boolean isDeleted = false;
		// get the record index of the inputed id
		int index = Data.searchIndex(id);
			
		// start to search if the collection contain key
		if (index >= 0) {
			// display record before save
			Display.enter(25);
			System.out.println("\tArticle Detail");
			System.out.println("\tLast update: " + data.get(index).getDate());
			System.out.println("\tArticle ID: " + id);
			System.out.println("\tTitle: " + data.get(index).getTitle());
			System.out.println("\tAuthor: " + data.get(index).getAuthor());
			System.out.println("\tContent: " + data.get(index).getContent());
			// remove data if id is found
			System.out.println("\n\tRecord Found...");
			if (Util.confirm("Are you sure want to delete article #" + id + "?")) {
				data.remove(index);
				isDeleted = true;
				// save temporary data to file
				TmpFileProcess.save(new Article(id, "null", "null", "null", "null"), "delete");
				// operation process
				Util.operationMsg("Deleting data");
				// change save status when data is write
				FileProcess.setSaveStatus(false);
				
				// add new log
				Log.setTempLog("Record\tID: " + id + "\thas been deleted.");
				
				Pagination.first(data);
			}else{
				System.out.println("\n\tOperation cancelled by user...");
			}
		} else {
			// display message to inform user that the input id doesn't exist
			System.out.println("\tRecord not found...");
		}
		// return true if delete successfully
		return isDeleted;
	}

	
	/**
	 * Delete All
	 * This method will remove all record from collection.
	 * */
	public static void deleteAll(){
		// delete record if record exist
		if(data.size() > 0){
			// confirm user before delete
			if(Util.confirm("Are you sure want to delete all " + data.size() + " records?")){
								
				data.clear();
				// display processing message
				Util.operationMsg("Delete all record");
				// change save status when data is write
				FileProcess.setSaveStatus(false);
				FileProcess.save();
				// add new log
				Log.setTempLog("All records \thas been deleted.");
				
				System.out.println("\n\tAll record has been deleted...");
			}
		}
	}
	
	/* Searching Record */
	/**
	 * Search record. 
	 * This method is allow user to input anything.
	 * It will searching from title, author, content
	 * */
	public static void search() {
		// get input from keyboard
		Scanner in = new Scanner(System.in);
		// if the value input from keyboard is valid id
		boolean isInputAgain = true;
		// continue process the input if the input is invalid
		while (isInputAgain) {
			System.out.print("\n\tSearch > ");
			if (in.hasNext()) {
				// check the input value
				String input = in.nextLine();
				
				search(input);
				isInputAgain = false;
			}
		}
	}

	/**
	 * Search record. 
	 * This method get the input string to search.
	 * */
	public static void search(String keyword) {
		
		// clear old founded record before add new
		temp.clear();
		// add found data to temp collection
		temp.addAll(searchString(keyword));
		// operation process
		Util.operationMsg("Searching data");
		
		if (temp.size() == 0) {
			System.out.println("\n\tNo record found...");
			
			return;
		}else{
			
			// change find status
			isFound = true;
			// change pagination data
			Pagination.page(temp);
			Config.setDisplayRow(temp.size());
			// display record based on the founded record
			Display.header();
			for(Article art : temp){
				Display.row(art.getId(), art.getTitle(), art.getAuthor(), art.getDate());
			}
			Display.footer(getData());
		}
	}

	
	
	/**
	 * Search index.
	 * Use this method with method delete, update need searching data before processing that operation.
	 * @param id is the key of each record.
	 * @return index of the collection that store that article.
	 * */
	public static int searchIndex(int id) {

		// search data based on id
		for (Article art : data) {
			if (art.getId() == id) {
				// if data found return index on the collection
				return data.indexOf(art);
			}
		}
		// return -1 if data is not found
		return -1;
	}

	
	/**
	 * Searching matching keyword from title, author, and content 
	 * @return ArrayList<Article>
	 * */
	private static ArrayList<Article> searchString(String s) {

		// store index
		ArrayList<Article> al = new ArrayList<Article>();

		// search data based on id
		for (Article art : data) {

			if (art.toString().contains(s.toLowerCase())) {
				al.add(art);
			}
		}
		// return null if data is not found
		return al;
	}


	/* Get Multi-line String */
	private static String getMiltiLineString(String msg) {
		Scanner input = new Scanner(System.in);
		StringBuffer sb = new StringBuffer();
		System.out.print(msg + " use ; for break point " + "> ");
		while (true) {
			String imsi = input.nextLine();
			// if(imsi != null && imsi.trim().length() == 1 && imsi.trim().charAt(0)==';') break;
			if (imsi != null && imsi.indexOf(";") > -1){
				sb.append(imsi.replace(';', ' '));
				break;
			}
			
			if (imsi == null)
				imsi = "";
			sb.append(imsi + "\n\t");
		}
		return sb.toString();
	}

	/**
	 * @return total size.
	 * */
	public static int size(){
		return data.size();
	}
	
	

	/**
	 * @return collection of data
	 * <tt>ArrayList<Article><tt> 
	 * */
	public static ArrayList<Article> getData() {
		
		if(!isFound){
			return data; // return main collection if search record is not found
		}else{
			return temp; // return found record
		}
	}
	
	/**
	 * @set_data set collection
	 * <tt>ArrayList<article></tt>
	 * */
	public static void setData(ArrayList<Article> article){
		data = article;
	}
	
	/**
	 * Clear found record
	 * */
	public static void clearSearch(){
		temp.clear();	 // clear old found data
		isFound = false; // change found status
	}
	
	/**
	 * Get search status.
	 * @return <tt>true</tt> if search found otherwise @return <tt>false</tt>
	 * */
	public static boolean getSearchStatus(){
		return isFound;
	}
	
	
	
}
