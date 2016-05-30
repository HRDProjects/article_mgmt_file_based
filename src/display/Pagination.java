package display;

import java.util.Scanner;
import java.util.ArrayList;

import util.Config;
import util.Util;
import article.Article;
import article.Data;

public class Pagination {
	
	//Declare Data Members
	public static int startIndex = 0;
	public static int endIndex = 0;
	
	
	/**
	 * Private constructor.
	 * */
	private Pagination(){}
	
	/**
	 * Initialize Page.
	 * */
	public static void initialize(){
		try{
			startIndex = ((Config.getCurrentPage()-1) * Config.getDisplayRow());
			endIndex = startIndex + Config.getDisplayRow();
			
			Config.setCurrentPage(Config.getCurrentPage()/*, (Data.size()==0)?1:Data.size()*/);
			
			if(checkIndex(Data.getData())) Data.read(Data.getData(), startIndex, endIndex);
		}catch(ArithmeticException e){}
	}

	//int start, end;
	private static Scanner cin = new Scanner(System.in);
	//Function First
	public static void first(ArrayList<Article> arr){
		if(arr.size()==0){
			System.out.println("\n\tNo record found...");
			return;
		}
		startIndex = 0;
		endIndex = startIndex + Config.getDisplayRow();
		Config.setCurrentPage(1);
		if(checkIndex(arr)) Data.read(Data.getData(), startIndex, endIndex);
	}
	//Function Previous
	public static void previous(ArrayList<Article> arr){
		if(arr.size()==0){
			System.out.println("\n\tNo record found...");
			return;
		}
		
		if(arr.size()<Config.getDisplayRow()){
			startIndex = 0;
			endIndex = startIndex + Config.getDisplayRow();
			Config.setCurrentPage(1);
			if(checkIndex(arr)) Data.read(Data.getData(), startIndex, endIndex);
			return;
			
		}
		
		if((endIndex - startIndex)<Config.getDisplayRow()){
			startIndex -= Config.getDisplayRow() ;
			endIndex -= (arr.size() % Config.getDisplayRow());
		}else{
			startIndex -= Config.getDisplayRow();
			endIndex -= Config.getDisplayRow();
		}
		if(startIndex <=0) Config.getCurrentPage();
		else 	Config.setCurrentPage(Config.getCurrentPage()-1);
		if(checkIndex(arr)) Data.read(Data.getData(), startIndex, endIndex);
	}
	//Function Next
	public static void next(ArrayList<Article> arr){
		if(arr.size()==0){
			System.out.println("\n\tNo record found...");
			return;
		}
		
		if(arr.size()<Config.getDisplayRow()){
			startIndex = 0;
			endIndex = startIndex + Config.getDisplayRow();
			Config.setCurrentPage(1);
			if(checkIndex(arr)) Data.read(Data.getData(), startIndex, endIndex);
			return;
			
		}
		
		startIndex += Config.getDisplayRow();
		endIndex += Config.getDisplayRow();
		if(endIndex >= arr.size()) Config.setCurrentPage(page(arr));
		else Config.setCurrentPage(Config.getCurrentPage()+1);
		if(checkIndex(arr)) Data.read(Data.getData(), startIndex, endIndex);
	}
	//Function Last
	public static void last(ArrayList<Article> arr){
		if(arr.size()==0){
			System.out.println("\n\tNo record found...");
			return;
		}
		
		if(arr.size()<Config.getDisplayRow()){
			startIndex = 0;
			endIndex = startIndex + Config.getDisplayRow();
			Config.setCurrentPage(1);
			if(checkIndex(arr)) Data.read(Data.getData(), startIndex, endIndex);
			return;	
		}
		
		if((arr.size() % Config.getDisplayRow()) !=0){
			startIndex = arr.size() - (arr.size() % Config.getDisplayRow());
			endIndex = arr.size();
		}else{
			startIndex = arr.size() - Config.getDisplayRow();
			endIndex = arr.size();
		}	
		Config.setCurrentPage(page(arr));
		if(checkIndex(arr)) Data.read(Data.getData(), startIndex, endIndex);
	}
	//Function Calculate Page
	public static int page(ArrayList<Article> arr){
		
		if((arr.size() % Config.getDisplayRow()) == 0)
			return ((arr.size() / Config.getDisplayRow()));
		else
			return ((arr.size() / Config.getDisplayRow()) + 1);
	}
	//Function Check Index Array
	private static boolean checkIndex(ArrayList<Article> arr){
		boolean check = true;
		if(endIndex > arr.size()|| startIndex <=0){
			if(endIndex > arr.size()){
				startIndex = arr.size() - (arr.size() % Config.getDisplayRow());
				endIndex = arr.size();
				if(startIndex == endIndex){
					
					startIndex = endIndex - Config.getDisplayRow();
				}
			}else if(startIndex <=0){
				startIndex = 0;
				endIndex = Config.getDisplayRow();
				
			}			
		}
		return check;
	}
	//Function Goto another page
	public static void gotoPage(ArrayList<Article> arr){
		if(arr.size()==0){
			System.out.println("\n\tNo record found...");
			return;
		}
		
		System.out.print("\tEnter Page > ");
		String n = cin.nextLine();
		if(Util.isNumber(n)){
			startIndex = (Integer.parseInt(n)-1) * Config.getDisplayRow();
			endIndex = startIndex + Config.getDisplayRow();
			Config.setCurrentPage(Integer.parseInt(n));
			if(checkIndex(arr)) Data.read(Data.getData(), startIndex, endIndex);
		}else{
			System.out.println("\n\tInput page must be number...");
		}
	}
	//Function Set Row
	public static void rowSet(ArrayList<Article> arr){
		if(arr.size()==0){
			System.out.println("\n\tNo record found...");
			return;
		}
		
		String rowSet = "";
		while(true){
			System.out.print("\tEnter Row Set Number > ");
			if(cin.hasNext()){
				rowSet = cin.nextLine();
				if(Util.isNumber(rowSet)){
					if(Integer.parseInt(rowSet)>Data.size() || Integer.parseInt(rowSet) <= 0){
						System.out.println("\n\tRow must be less that or equal total records.");
						
					}else{
						Config.setDisplayRow(Integer.parseInt(rowSet));
						endIndex = startIndex + Config.getDisplayRow();
						Config.setCurrentPage(page(arr));
						//if(checkIndex(arr)) Data.read(Data.getData(), startIndex, endIndex);
						first(Data.getData());
						return;
					}
				}else{
					System.out.println("\n\tRow must be a number...");
				}
			}
		}
		
	}
}
