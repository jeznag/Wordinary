package net.bruncle.wordinary;

import java.util.*;

public class Category {
	
	String name;
	
	public Category(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
		
	public String toString(){
		return getName();
	}
}
	
	