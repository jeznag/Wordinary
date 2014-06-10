/*
 * FileConverter.java
 *
 * Created on July 2, 2008, 9:25 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */



package net.bruncle.wordinary;
import java.io.*;
import java.util.*;
import javax.swing.*;
import org.jdom.input.SAXBuilder;
import org.jdom.*;

/**
 * Converts wordinary files from old format to new format
 * @author Jeremy
 */
public class FileConverter {
      
     public static void main (String[] args){
         openFile();
         getWordListFromFile(true);
         outputXML(getWordList());
     }
     
     static class OnlyExt extends javax.swing.filechooser.FileFilter
     {
  		String ext;
  		public OnlyExt(String ext)
		{ this.ext = "." + ext; }
		public boolean accept(File dir)
		{ 
			if (dir.isDirectory())
				return true;
			String extension = "";
			try{
				extension = dir.toString().substring(dir.toString().indexOf("."));
			}
			catch (StringIndexOutOfBoundsException e){ return false; }
			return extension.endsWith(ext); 
		}
		public String getDescription() {
		        return "XML files";
		}
     }
    
     private static void openFile(){
		JFileChooser fc = new JFileChooser("./");
		fc.addChoosableFileFilter(new OnlyExt("xml"));
		int returnVal = fc.showOpenDialog(null);
            	if (returnVal == JFileChooser.APPROVE_OPTION) {
            			xmlFile = fc.getSelectedFile().toString();
            			getWordListFromFile(true);
            	}
     }
     
     
     private static void getWordListFromFile(boolean override){
	   try {
	      if (override){
	      	wordList = new ArrayList<Word>();
	      	categories = new ArrayList<Category>();
	      }
	      SAXBuilder parser = new SAXBuilder();
	      
	      if (xmlFile.equals("null")){
	      	openFile();
	      	return;
	      }
	      
	      Document doc = parser.build(xmlFile);
	      
	      Element root = doc.getRootElement(); 
	      title = root.getChild("title",root.getNamespace()).getText();
	      
	      int numberOfWords = Integer.parseInt(root.getChild("size",root.getNamespace()).getText()); //total number of words to be loaded	   
	      
	      List categories = root.getChildren();
	      short addMode = 0; //0 nothing, 1 don't override, 2 override
	      for (Object cat : categories){ 
	      	Element elm = ((Element)cat);
	      	if (!elm.getName().equals("category"))
	      		continue;
	      	Attribute att = elm.getAttribute("name");
	      	Category cater = new Category(att.getValue());
	      	addCategory(cater,false);
	      	for (Element el : ((List<Element>)(elm.getChildren()))){
	      		String english = el.getChild("english",el.getNamespace()).getText();
	      		english = getQuotedText(english);
	      		String other = el.getChild("other",el.getNamespace()).getText();
	      		other = getQuotedText(other);
	      		String hint = el.getChild("hint",el.getNamespace()).getText();
	      		int correct = Integer.parseInt(el.getChild("correct",el.getNamespace()).getText());
	      		int id = Integer.parseInt(el.getChild("id",el.getNamespace()).getText());
	      		//id = progress;
	      		
	      		Word wrd = new Word(english,other,cater.toString(),correct);
	      		wrd.setID(id);
	      		wrd.setHint(hint);
	      		
	      		if (addMode != 1){ //not automatically adding the word
	      			boolean found = false;
	      			Word toModify = null;
		      		for (Word iterator: getWordList()){
		      			if (iterator.getID() == (wrd.getID())){	
		      				//System.out.println(iterator + ":" + wrd);
                                                
		      				if (addMode == 2){ //auto modify{
		      					System.out.print(iterator + "\t ");
	      						toModify = iterator;
	      						found = true;
	      						break;
	      					}
	      					else{
	      						Object[] options = {"Just add it on its own", "Override it", "Always just add it", "Always override it","Neither"};
	      						int response = JOptionPane.showOptionDialog(null, "Should (" + iterator + ") be overridden with (" + wrd + ")?", "Adding word..", JOptionPane.YES_NO_CANCEL_OPTION,
	      							JOptionPane.QUESTION_MESSAGE, null, options, options[0]) ;
	      						switch (response){
	      							case 0 : 
	      								break;//just add the word
	      							case 1 : 
	      								found = true;
	      								toModify = iterator;
	      								break;
	      							case 2 : 
	      								//System.out.println("Always just add it");
	      								addMode = 1; //make it always add the word without showing this dialog
	      								break;
	      							case 3: 
	      								//System.out.println("Always modify!");
	      								toModify = iterator; //make it always override the word without showing this dialog 
	      								addMode = 2;
	      								found = true;
	      								break;
                                                                case 4:
                                                                        break;
	      						}
	      					}
	      				}
	      			}
	      			if (!found){
	      				//System.out.println("adding" + wrd);
	      				getWordList().add(wrd); 
	      			}
	      			else{
	      				if (toModify != null){
	      					//System.out.println("overriding " + toModify + " with " + wrd);
	      					getWordList().set(getWordList().indexOf(toModify), wrd);
	      					//System.out.println(findIndexByID(toModify));	
	      				}
	      			}
	      		}
	      		else{ //automatically adding the word
	      			//System.out.println("adding" + wrd);
	      			getWordList().add(wrd); 
	      		}
	      		
	      		
	      	}
	      }
	      System.out.println("Loaded "+wordList.size()+" words and "+categories.size()+" categories");
	   }
	   catch (Exception e) {
	   	e.printStackTrace();
	   	JOptionPane.showMessageDialog(null, "The xml file was not valid, you can only use files created by wordinary..");
                openFile();
	   }
     }
     
     private static void outputXML(List<Word> tempList){
            try{                      
                    System.out.println("Starting writing");
                    long start = System.currentTimeMillis();
                            
                    BufferedWriter out = null;
                    out = new BufferedWriter(new FileWriter(xmlFile,false));
                    out.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
                    out.newLine();
                    out.write("<?xml-stylesheet href=\"stylesheet.xsl\" type=\"text/xsl\"?>");
                    out.newLine();
                    out.write("<wordList xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.bruncle.net/wordinary wordList.xsd\" xmlns=\"http://www.bruncle.net/wordinary\">");
                    out.newLine();
                    out.write("<title>"+title+"</title>");
                    out.newLine();
                    out.write("<size>"+tempList.size()+"</size>");
                    out.newLine();
                    out.write("<otherSynth><![CDATA["+otherSynthName+"]]></otherSynth>");
                    out.newLine();
                    out.write("<englishSynth><![CDATA["+englishSynthName+"]]></englishSynth>");
                    out.newLine();

                    /*add symbols*/
                    String symbols = "";
                    for (int i = 0; i < getSymbols().length; i++){
                            String space = "";
                            if (i < (getSymbols().length - 1))
                                    space = ",";
                            symbols += ""+getSymbols()[i]+space;
                    }
                    out.write("<symbols>"+symbols+"</symbols>");
                    out.newLine();

                    /*add shortcuts*/
                    String shortcuts = "";
                    for (int i = 0; i < getKeyShortcutSymbols().length; i++){
                            String space = "";
                            if (i < (getKeyShortcutSymbols().length - 1))
                                    space = ",";
                            shortcuts += ""+getKeyShortcutSymbols()[i]+space;
                    }
                    out.write("<shortcutkeys>"+symbols+"</shortcutkeys>");
                    out.newLine();

                    for (Word temp : tempList){

                        //Word temp = getListOfCategories().get(i).getWords().get(i);
                        out.write("<word>");
                        out.newLine();
                        out.write("<category>" + temp.getCategory() +"</category>");
                        out.newLine();
                        out.write("<english><![CDATA[" +temp.getInEnglish()+"]]></english>");
                        out.newLine();
                        out.write("<other><![CDATA["+temp.getInOtherLanguage()+"]]></other>");
                        out.newLine();
                        out.write("<correct>"+temp.getNumberOfTimesCorrect()+"</correct>");
                        out.newLine();
                        out.write("<id>"+temp.getID()+"</id>");
                        out.newLine();
                        out.write("<hint>"+temp.getHint()+"</hint>");
                        out.newLine();
                        out.write("</word>");
                        out.newLine();
                    }
                    out.write("</wordList>");
                    out.newLine();

                    out.close();
                    
                    long duration = (System.currentTimeMillis() - start);
                    System.out.println("Finished writing..it took " + duration + " ms");
            }catch(Exception e){
        	e.printStackTrace();
            }

        }
     
        /**
	 * Gets the CData value if available or just the normal text
	 * @param  ele  The element which contains the tag
	 * @param  tagName  The tag, whose text will be returned
	 * @return  The string value of the tag
	 */
	private static String getCDataValue(Element ele, String tagName) {
		String textVal = null;
		for(Object obj : ele.getChild(tagName,ele.getNamespace()).getContent()){
                            if (obj instanceof CDATA){
                                textVal = getQuotedText(((CDATA)obj).getText());
                                if (!textVal.equals(""))
                                    break;
                            }
                            else if (obj instanceof Text){
                                textVal = getQuotedText(((Text)obj).getText());
                                if (!textVal.equals(""))
                                    break;
                            }
                            else
                                System.out.println("Type: " + obj.toString());
                }
		return textVal;
	}
        
        private static String getQuotedText(String original){
	    	char matches = 0;
	    	char quote = '\"';
	    	ArrayList<Integer> positions = new ArrayList<Integer>();
	    	for (int i = 0; i < original.length(); i++)
	    		if (original.charAt(i) == quote){
	    			matches++;
	    			positions.add(i);
	    		}
	    	if (matches <= 0){
	    		return original;
	    	}
	    	if (matches % 2 != 0){
	    		  return null;
	    	}	
	    	
	    	ArrayList<String> result = new ArrayList<String>();
	    	for (int i = 0; i < matches; i+= 2){
	    		String temp = original.substring(positions.get(i) + 1, positions.get(i+1));
	    		temp = temp.replace(" ","-");
	    		result.add(temp);
	    	}
	    	String resultString = "";
	    	for (int i = 0; i < result.size(); i++){
	    		String space = "";
	    		if (i < (result.size() - 1))
	    			space = " ";
	    		resultString += result.get(i)+space;
	    	}
	    	return resultString;
	}
        
        public static int[] getKeyShortcutSymbols(){
		return keyshortcutsForSymbols;
	}
	
	public static int[] getSymbols(){
		return symbols;
	}
     
        public static List<Word> getWordList(){
		return wordList;
	}
        
        public static List<Category> getListOfCategories(){
		return categories;
	}
        
        public static boolean addCategory(Category categoryName, boolean write){
		if (categoryName == null || categoryName.equals("") || categoryName.toString().length() < 1){
			//System.out.println("Invalid word");
			return false;
		}
		if (findIndexInList(categories,categoryName) == -1){
			//System.out.println("Added category: "+categoryName);
			categories.add(categoryName);
			//System.out.println("There are now "+categories.size()+"categories");
			return true;
		}
		categories.set(findIndexInList(categories,categoryName),(categoryName));
		//System.out.println("There are now "+categories.size()+"categories");
		return true;
	}
        
        public static int findIndexInList(List<Category> list, Category toFind){
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).toString().equalsIgnoreCase(toFind.toString()))
				return i;
		return -1;
	}
        
        private static String temptitle = null;
	
	private static boolean alwaysSave;
	
	private static boolean reallyWrite;
	
	private static String title;
	
	private static List<HelpFile> helpFiles = new ArrayList<HelpFile>();
	
	private static int[] symbols = new int[11];
	private static final int[] keyshortcutsForSymbols = {48,49,50,51,52,97,101,105,111,117,0};
	
	private static int spellingAcc;
	
	private static java.awt.Color backColour; 
	private static java.awt.Color textColour;
			
	private static String otherLanguage;
	private static List<Word> wordList = new ArrayList<Word>();
	private static List<Category> categories = new ArrayList<Category>();
	private static String xmlFile;
	
	private static SynthEngine synthEng;
	private static SynthEngine synthEngEnglish;
	
	private static boolean useSynth;
	private static String englishSynthName = "";
	private static String otherSynthName = "";
	private static boolean synthAvailable = true;
    
}
