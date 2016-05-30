package display;

import java.util.ArrayList;
import java.util.Scanner;

import util.Config;
import util.Run;
import article.Article;

public class Display {
	
	
	/**
	 * Private constructor.
	 * */
	private Display(){}
	
	// logo
	public static void logo(){
		System.out.println();
		System.out.format("\t         <-----------***===================***=====================***----------->%n");
		System.out.printf("\t         |                                                                       |%n");
		System.out.printf("\t          >    ===>>>==>>=>     KOREA SOFTWARE HRD CENTER      <=<<==<<<===     < %n");
		System.out.printf("\t         |                                                                       |%n");
		System.out.format("\t         <-----------***===================***=====================***----------->%n");
		String title ="\t   %-20s  %-36s  %-14s %n";
		String  group="\t   %-26s  %-36s  %-14s %n";
		String  row1= "\t   %-23s  %-36s  %-14s %n";
		String  row2= "\t   %-23s  %-36s  %-14s %n";
		String  row3= "\t   %-23s  %-36s  %-14s %n";
		System.out.println();
		System.out.format(title, " ", ">> Welcome to Article Management System <<", " ");
		System.out.format(group, " ", ">>> Group 3 of Battambong <<<", " ");
		System.out.format(row1, " ", "1.Lun Sovathana |  2.Sa Sokngim", " ");
		System.out.format(row2, " ", "3.Sok Lundy     |  4.Mom Kunthy", " ");
		System.out.format(row3, " ", "5.Ly Vandan     |  6.Khut Bunthorn", " ");
	}
	
	// header
	public static void header(){
		System.out.println();
		String header[] = {"ID", "TITLE", "AUTHOR", "DATE"};
		String leftAlignFormat = "\t│ %10s        │ %12s        │ %12s        │ %12s          │%n";
		System.out.format("\t╔═══════════════════╤═════════════════════╤═════════════════════╤═══════════════════════╗%n");
		System.out.format(leftAlignFormat, header[0], header[1], header[2], header[3]);
		System.out.format("\t╠═══════════════════╧═════════════════════╧═════════════════════╧═══════════════════════╣%n");
	}
	
	/**
	 * display data with the form of table
	 * @param a is used for each record
	 * */
	public static void row(int id, String title, String author, String date){
		
		String leftAlignFormat = "\t│ %10s        │ %-15s     │ %-15s     │ %-15s       │%n";
		
		System.out.format(leftAlignFormat, id, (title.length()>10)?title.substring(0, 10)+"...":title, (author.length()>10)?author.substring(0, 10)+"...":author, date );
		System.out.format("\t╠═══════════════════════════════════════════════════════════════════════════════════════╣%n");
		
	}
	
	// footer
	public static void footer(ArrayList<Article> art){
		String leftAlignFormat = "%-10s %-10s %55s %n";
		//System.out.format("\t╠═══════════════════════════════════════════════════════════════════════════════════════╣%n");
		System.out.format(leftAlignFormat, "\t      Page: "+ Config.getCurrentPage() +" /", Pagination.page(art), "Total Record: "+ art.size());
		System.out.format("\t╚═══════════════════════════════════════════════════════════════════════════════════════╝%n");
	}
	
	// option
	public static void menu(){
		System.out.println();
		System.out.println("\t╔═══════════════════════════════════════════════════════════════════════════════════════╗");
		System.out.println("\t║ *)Display │ W)rite │ R)ead │ U)pdate │ D)elete │  F)irst │ P)revious │ N)ext │ L)ast  ║");
		System.out.println("\t║                                                                                       ║");
		System.out.println("\t║        S)earch │ G)o to │ Se)t row │ Sa)ve │ B)ackup │ Re)store │ H)elp │ E)xit       ║");
		System.out.println("\t╚═══════════════════════════════════════════════════════════════════════════════════════╝");
		
	}
	
	/*Enter*/
	public static void enter(int line){
		for(int i=0; i<=line; i++){
			System.out.println();
		}
	}

	public static void help(){
		
		Scanner s = new Scanner(System.in);
		boolean isValid = false;
		System.out.println();
		System.out.println("\t╔═══════════════════════════════════════════════════════════════════════════════════════╗");
		System.out.println("\t║	   Helps                                                                        ║");
		System.out.println("\t║	   1.Write:                                                                     ║");
		System.out.println("\t║	     - Press w and then enter to write normally.                                ║");
		System.out.println("\t║	     - Press w#title-author-content to write with shortcut use ; for enter.     ║");
		System.out.println("\t║	   2.Read:                                                                      ║");
		System.out.println("\t║	     - Press r and then enter to read normally.                                 ║");
		System.out.println("\t║	     - Press r#id and then enter to read with shortcut.                         ║");
		System.out.println("\t║	   3.Update:                                                                    ║");
		System.out.println("\t║	     - Press u and then enter to update normally.                               ║");
		System.out.println("\t║	     - Press u#id and then enter to update with shortcut.                       ║");
		System.out.println("\t║	     - Press u#id-[title]-[author]-[content]) to update with shortcut.          ║");
		System.out.println("\t║          4.Delete:                                                                    ║");
		System.out.println("\t║	     - Press d then enter to delete normally.                                   ║");
		System.out.println("\t║	     - Press d#id then enter to delete with shortcut.                           ║");
		System.out.println("\t║	     - Press d#a to delete all record.                                          ║");
		System.out.println("\t║	   5.Search:                                                                    ║");
		System.out.println("\t║	     - Press s and enter to search nornally.                                    ║");
		System.out.println("\t║	     - Press s#id then enter to search with shortcut.                           ║");
		System.out.println("\t╚═══════════════════════════════════════════════════════════════════════════════════════╝");
		System.out.println("\n\tPress b to go back to main menu.");
		
		String in = "";
		while(!isValid){
			System.out.print("\t> ");
			if(s.hasNextLine()){
				in = s.nextLine();
				switch (in) {
				case "b":
					isValid = true;
					Run.displayRecord();
					Run.start();
					break;
				default:
					System.out.println("\tInput is invalid.");
					break;
				}
			}
		}
		
		
		
	}
}
