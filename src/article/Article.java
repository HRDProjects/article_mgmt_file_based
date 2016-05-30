package article;

import java.io.Serializable;

public class Article implements Serializable{
	private int id;
	private String title;
	private String author;
	private String content;
	private String date;
	
	/*public Article(int id){
		this.id = id;
	}*/
	
	public Article(int id, String title, String author, String content, String date){
		this.id = id;
		this.title = title;
		this.author = author;
		this.content = content;
		this.date = date;
	}
	
	/**
	 * @return article id.
	 * */
	public int getId(){
		return id;
	}
	
	/**
	 * @return article title.
	 * */
	public String getTitle(){
		return title;
	}
	
	/**
	 * @return article author.
	 * */
	public String getAuthor(){
		return author;
	}
	
	/**
	 * @return article content.
	 * */
	public String getContent(){
		return content;
	}
	
	/**
	 * @return article insert date.
	 * */
	public String getDate(){
		return date;
	}
	
	/**
	 * @param is the article id.
	 * */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param is the article title.
	 * */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param is the article author.
	 * */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @param is the article content.
	 * */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @param is the article date.
	 * */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return article detail.
	 * */
	public String toString(){
		return (title + " " + author + " " + content).toLowerCase();
	}
}

