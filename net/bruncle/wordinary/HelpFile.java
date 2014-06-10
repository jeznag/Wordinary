package net.bruncle.wordinary;

public class HelpFile {
	
	private String title;
	private String details;
	
	public HelpFile(String title, String details){
		this.title = title;
		this.details = details;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getDetails(){
		return details;
	}
}