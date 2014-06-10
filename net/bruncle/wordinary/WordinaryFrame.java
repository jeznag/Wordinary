package net.bruncle.wordinary;

import javax.swing.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.awt.event.*;
import com.digitprop.tonic.*;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;

import com.cloudgarden.speech.userinterface.*;
import com.cloudgarden.speech.*;

import javax.speech.*;
import javax.speech.synthesis.*;

public class WordinaryFrame {
	
	public static final double VERSION_NUMBER = 1.37;
	public static void main (String[] args){
		try{
			//Display dialog showing what it's doing in the loading process
	                loadUp();
		 }
		 catch (Exception e){
		 	e.printStackTrace();
		 }
	}
	
	static LoadingDialog loadingDialog;
	static boolean loaded;
        static boolean finished;
        static boolean done;
	private static void loadUp(){
		try {
                        Thread loadingThread = new Thread(new Runnable(){
                        	public void run(){  
		                        try{
			                        javax.swing.SwingUtilities.invokeLater(new Runnable() {
					            public void run() {
					            	try{
						                loadingDialog = new LoadingDialog(null, true, "user settings", 6);
			                        		loadingDialog.setVisible(true);
			                        		loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			                        	}
			                        	catch (Exception e){ e.printStackTrace(); }
					            }
					        });
		                        }catch (Exception e) { e.printStackTrace(); }                      		
				        while (loadingDialog == null || !loadingDialog.isVisible()){
				        	try{
				        		Thread.sleep(50);
				        	}
				        	catch (InterruptedException e){}
				        }
				        //checkForUpdates();
				        getSettings();
				        int response = JOptionPane.showConfirmDialog(loadingDialog, "Do you want to load the wordlist from "+xmlFile+"? Select no to choose a different file \n or cancel to exit the program");
		            		if (response == JOptionPane.YES_OPTION)
		            			getWordListFromFile(true);
		            		else if (response == JOptionPane.NO_OPTION)
		            			openFile(false);
                                        else
                                                System.exit(1);
					javax.swing.SwingUtilities.invokeLater(new Runnable() {
				            public void run() {
				                setUpGUI();
				            }
				        });
					getHelpFilesFromFile();
					try{
						Class temp = Class.forName("com.cloudgarden.speech.CGEngineProperties");
					}
					catch (ClassNotFoundException e){
						useSynth = false;
						saveSettings();
					}
					if (WordinaryFrame.isSpeechEnabled()){
						loadingDialog.newTask("Preparing speech engine",2);
						if (otherSynthName == null){
							JOptionPane.showMessageDialog(null,"Choose a synthesis engine for the other language");
							otherSynthName = chooseSynthModeName();
							writeWordsToNewFile();
						}
                                                if (englishSynthName == null){
							JOptionPane.showMessageDialog(null,"Choose a synthesis engine for the english voice");
							englishSynthName = chooseSynthModeName();
							writeWordsToNewFile();
						}
						synthEng = new SynthEngine(otherSynthName);
						loadingDialog.updateProgress(1);
						
						synthEngEnglish = new SynthEngine(englishSynthName);
						loadingDialog.updateProgress(2);
						
						synthEngEnglish.saySomething("Welcome to wordinary by jeremy nagel");
					}
					loadingDialog.dispose();
					done = true;
				}
			});
                        loadingThread.start();
			while(!done);
			appFrame.setVisible(true);
			appFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			changeScreen(frontScreen);

	        } catch (Exception e) { e.printStackTrace(); }
	}
	
	/**
	Checks if there have been any updated versions of the program uploaded to the server 
	*/
	private static final String versionPage = "http://www.bruncle.net/wordinary/updates.upd";
	private static void checkForUpdates() {
		try{
			loadingDialog.newTask("updates from website", 1); //let the user know what's going on through the loading dialog
			URL urlVersionPage = new URL(versionPage); 
			URLConnection connToVersionPage = urlVersionPage.openConnection();
			connToVersionPage.setConnectTimeout(20000);
			connToVersionPage.setReadTimeout(20000);
			BufferedReader bR = new BufferedReader(new InputStreamReader(connToVersionPage.getInputStream()));
			double version = Double.parseDouble(bR.readLine()); //file's first line is the version number as a double
			if (version > VERSION_NUMBER) //if version on site greater than this version's number
				askThenDownloadUpdates(bR.readLine()); //second line of file is the download link
			loadingDialog.updateProgress(1);
 		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	*	Asks the user whether they want to download the latest update, and if they do, download it
	*/
	private static void askThenDownloadUpdates(String downloadLink){
		try{
			URL downloadURL = new URL(downloadLink);
			URLConnection downloadConn = downloadURL.openConnection();
			int fileSize = (downloadConn.getContentLength()); //content-length header field is in bytes, so convert to kb
			int response = JOptionPane.showConfirmDialog(null, "Do you want to download the latest update for Wordinary? \n"+
				" the download is " + (fileSize/1000)+ " kb in size");
            		if (response == JOptionPane.NO_OPTION || response == JOptionPane.CANCEL_OPTION)
            			return; //if user doesn't want to download the file, exit the method
            			
            		InputStream downloadStream = downloadConn.getInputStream();
            		FileOutputStream saveStream = new FileOutputStream(System.getProperty("user.dir") + "//wordinary.jar");
            		
            		//copy file to disk
            		loadingDialog.newTask("updates from website", fileSize);
            		int progress = 0, tempProg = 0;
            		for (int nextByte = 0; nextByte != -1; nextByte = downloadStream.read()){
            			progress++;
            			tempProg++;
            			if (tempProg > 500){
            				loadingDialog.updateProgress(progress++);
            				tempProg = 0;
            			}
            			saveStream.write(nextByte);
            		}
            		downloadStream.close();
            		saveStream.close();
            		
            		JOptionPane.showMessageDialog(loadingDialog, "Update successfully downloaded. Restart the program to take advantage of the new features");
            		System.exit(0);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private static void getHelpFilesFromFile(){
	  try{	
	      loadingDialog.newTask("help files",7);
	      helpFiles = new ArrayList<HelpFile>();
	      SAXBuilder parser = new SAXBuilder();
	      Document doc = parser.build("./help/helpfile.xml");
	      
	      Element root = doc.getRootElement(); 
	      List files = root.getChildren();
	      
	      int progress = 0;
	      
	      for (Object cat : files){
	      	Element elm = ((Element)cat);
	      	HelpFile help = new HelpFile(elm.getChild("title",elm.getNamespace()).getText(),
	      		elm.getChild("details",elm.getNamespace()).getText());
	      	helpFiles.add(help);
	      	loadingDialog.updateProgress(++progress);
	      	
	      }
	   }
	   catch (JDOMException e){
	   	e.printStackTrace();
	   }
	   catch (IOException e){
	   	e.printStackTrace();
	   }
	   loaded = true;
	}
	
        private static void outputXML(List<Word> tempList){
            outputXML(tempList, xmlFile);
        }
        
        private static void outputXML(List<Word> tempList, String xmlOutputFile){
            try{                      
                    //System.out.println("Starting writing");
                    long start = System.currentTimeMillis();
                            
                    BufferedWriter out = null;
                    out = new BufferedWriter(new FileWriter(xmlOutputFile,false));
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
                    out.write("<shortcutkeys>"+shortcuts+"</shortcutkeys>");
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
                    //System.out.println("Finished writing..it took " + duration + " ms");
            }catch(Exception e){
        	e.printStackTrace();
            }

        }
        
	private static void writeWordsToNewFile(final List<Word> tempList){
                if (tempList == null){
                    System.out.println("List is null failed to write");
                    try{
                     throw new Exception();
                     }
                     catch(Exception e){
                         e.printStackTrace();
                     }
                    return;
                }
		System.out.println("Writing words to " + xmlFile);
		try {
	    	    new Thread(new Runnable(){
                        	public void run(){     
                                    outputXML(tempList);
                                }
                                }).start();
                        if (oldXMLFile != null && !oldXMLFile.equals(""))
                            xmlFile = oldXMLFile;
                        if (oldTitle != null && !oldTitle.equals(""))
                            title = oldTitle;
	        } catch (Exception e) { e.printStackTrace(); }
	}
	
	private static List<Category> getListOfCategories(List<Word> words){
		List<Category> categories = new ArrayList<Category>();
		for (Word temp : words){
			boolean done = false;
			for (Category cat : categories)
				if (cat.toString().equalsIgnoreCase(temp.getCategory()))
					done = true;
			if (!done)
				categories.add(new Category(temp.getCategory()));
		}
		return categories;
	}
		
	
	private static void writeWordsToNewFile(){               
             if (!reallyWrite && !alwaysSave)
			return;
		reallyWrite = false;
		try {	    	    
                   new Thread(new Runnable(){
                       public void run(){     
                            outputXML(wordList);
                        }
                        }).start();
	        } catch (Exception e) { e.printStackTrace(); }
	}
		    
	
	private static void addIDS(){
		for (int i = 0; i < wordList.size(); i++)
			wordList.get(i).setID(i);
		writeWordsToFile();
	}
	
	private static void getWordListFromFile(boolean override){
	   try {
	      if (override){
	      	wordList = new ArrayList<Word>();
	      	categories = new ArrayList<Category>();
	      }
	      categoryForm = new CategoriesForm();
              listWordsPanel = new ListWordsPanel();
	      SAXBuilder parser = new SAXBuilder();
	      
	      if (xmlFile.equals("null")){
	      	newFile();
	      	return;
	      }
	      
	      Document doc = parser.build(new File(xmlFile));
	      
	      Element root = doc.getRootElement(); 
	      if (override){
                title = getCDataValue(root, "title");  
              }
	      if (appFrame != null)
	      	refreshScreen();
	      
	      int numberOfWords = Integer.parseInt(root.getChild("size",root.getNamespace()).getText()); //total number of words to be loaded	   
	      loadingDialog.newTask("words",numberOfWords); //Set up loading dialog to show progress in loading wordlist
	      int progress = 0; //amount loaded so far
	      
              //load word-list specific settings:
              String[] symbols = root.getChild("symbols",root.getNamespace()).getText().split(",");
	      for (int i = 0; i < getSymbols().length; i++)
	                getSymbols()[i] = Integer.parseInt(symbols[i]);
	
	      String[] shortcuts = root.getChild("shortcutkeys",root.getNamespace()).getText().split(",");
	            for (int i = 0; i < shortcuts.length; i++)
	                getKeyShortcutSymbols()[i] = Integer.parseInt(shortcuts[i]);

	      englishSynthName = getCDataValue(root, "englishSynth");
	      otherSynthName = getCDataValue(root, "otherSynth");
              
	      List words = root.getChildren();
	      short addMode = 0; //0 nothing, 1 don't override, 2 override
	      for (Object word : words){ 
	      	Element el = ((Element)word);
	      	if (!el.getName().equals("word"))
	      		continue;
                        
	      		String english = getCDataValue(el, "english");
                        String other = getCDataValue(el, "other");
                        String hint = getCDataValue(el, "hint");
                        
	      		int correct = Integer.parseInt(el.getChild("correct",el.getNamespace()).getText());
	      		int id = Integer.parseInt(el.getChild("id",el.getNamespace()).getText());
	      		
                        Category cater = new Category(getCDataValue(el, "category"));
                        if (!getListOfCategories().contains(cater))
                            addCategory(cater,false);
                        
	      		Word wrd = new Word(english,other,cater.toString(),correct);
	      		wrd.setID(id);
	      		wrd.setHint(hint);
	      		
	      		if (addMode != 1){ //not automatically adding the word
	      			boolean found = false;
	      			Word toModify = null;
		      		for (Word iterator: getWordList()){
		      			if (iterator.getID() == (wrd.getID())){	
                                                
		      				if (addMode == 2){ //auto modify{
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
	      								
	      								addMode = 1; //make it always add the word without showing this dialog
	      								break;
	      							case 3: 
	      								
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
	      				getWordList().add(wrd); 
	      			}
	      			else{
	      				if (toModify != null){
	      					
	      					getWordList().set(getWordList().indexOf(toModify), wrd);
	      					
	      				}
	      			}
	      		}
	      		else{ //automatically adding the word
	      			getWordList().add(wrd); 
	      		}
	      		
	      		progress++;
	      		if (progress % 25 == 0)
	      			loadingDialog.updateProgress(progress);
	      		
	      }
	      System.out.println("Loaded "+wordList.size()+" words and "+categories.size()+" categories");
	      if (!xmlFile.equals(""))
	      	xmlFile = oldXMLFile;
              //writeWordsToFile(getWordList());
	   }
	   catch (Exception e) {
	   	e.printStackTrace();
	   	JOptionPane.showMessageDialog(null, "The xml file was not valid, you can only use files created by wordinary..");
                openFile(false);
	   }
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
	
	private static void setUpHelpMenu(JMenu menu){
		JMenuItem newa = new JMenuItem("Index");
		newa.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        	menu.add(newa);
        	newa.setActionCommand("newa");
        	newa.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent event) {
		        	helpDialog.setVisible(true);
		        }
	        });
	        
	        JMenuItem about = new JMenuItem("About");
		about.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        	menu.add(about);
        	about.setActionCommand("newa");
        	about.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent event) {
		        	aboutDialog.setVisible(true);
		        }
	        });
	}
	
	private static void setUpFileMenu(JMenu menu){
		JMenuItem newa = new JMenuItem("New");
		newa.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        	menu.add(newa);
        	newa.setActionCommand("newa");
        	newa.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent event) {
		        	newFile();	
		        }
	        });
		JMenuItem open = new JMenuItem("Open");
		open.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        	menu.add(open);
        	open.setActionCommand("open");
        	open.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent event) {
		        	openFile(true);	
		        }
	        });
        	JMenuItem save = new JMenuItem("Save");
		save.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        	menu.add(save);
        	save.setActionCommand("save");
        	save.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent event) {
		        	saveFile();	
		        }
	        });
	        JMenuItem settings = new JMenuItem("Settings");
		settings.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_M, ActionEvent.CTRL_MASK));
        	menu.add(settings);
        	settings.setActionCommand("settings");
        	settings.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent event) {
		        	settingsDialog.setVisible(true);
		        }
	        });
        	JMenuItem quit = new JMenuItem("Exit");
		quit.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        	menu.add(quit);
        	quit.setActionCommand("exit");
        	quit.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent event) {
		        	System.exit(0);	
		        }
	        });
	}
	
	private static void setUpViewMenu(JMenu menu){
		JMenuItem addPanel = new JMenuItem("Add words");
		addPanel.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        	menu.add(addPanel);
        	addPanel.setActionCommand("add");
        	addPanel.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent event) {
		        	if (appFrame.getContentPane() == testPanel)
		        		if (JOptionPane.showConfirmDialog(appFrame, "Are you sure you want to stop the test?") != JOptionPane.YES_OPTION)
		        			return;
		        	addWords.prepareForUse();
		        	changeScreen(addWords);	
		        }
	        });
	        
	        JMenuItem testMenuOption = new JMenuItem("Test yourself");
		testMenuOption.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        	menu.add(testMenuOption);
        	testMenuOption.setActionCommand("test");
        	testMenuOption.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent event) {
		        	if (appFrame.getContentPane() == testPanel)
		        		if (JOptionPane.showConfirmDialog(appFrame, "Are you sure you want to stop the test?") != JOptionPane.YES_OPTION)
		        			return;
		        	changeScreen(testFront);	
		        }
	        });
	        
	        JMenuItem catPanel = new JMenuItem("Edit Categories");
		catPanel.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        	menu.add(catPanel);
        	catPanel.setActionCommand("cat");
        	catPanel.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent event) {
		        	if (appFrame.getContentPane() == testPanel)
		        		if (JOptionPane.showConfirmDialog(appFrame, "Are you sure you want to stop the test?") != JOptionPane.YES_OPTION)
		        			return;
		        	categoryForm.prepareForUse();
		        	changeScreen(categoryForm);	
		        }
	        });
	        
	        JMenuItem searchPane = new JMenuItem("Search wordinary");
		searchPane.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        	menu.add(searchPane);
        	searchPane.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent event) {
		        	if (appFrame.getContentPane() == testPanel)
		        		if (JOptionPane.showConfirmDialog(appFrame, "Are you sure you want to stop the test?") != JOptionPane.YES_OPTION)
		        			return;
		        	searchPanel.prepareForUse();
		        	changeScreen(searchPanel);	
		        }
	        });
	        
	        JMenuItem listPanel = new JMenuItem("List Words");
		listPanel.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        	menu.add(listPanel);
        	listPanel.setActionCommand("list");
        	listPanel.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent event) {
		        	if (appFrame.getContentPane() == testPanel)
		        		if (JOptionPane.showConfirmDialog(appFrame, "Are you sure you want to stop the test?") != JOptionPane.YES_OPTION)
		        			return;
		        	listWordsPanel.resetData();
		        	changeScreen(listWordsPanel);	
		        }
	        });
	        
	        JMenuItem learnPanela = new JMenuItem("Learn Words");
		learnPanela.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        	menu.add(learnPanela);
        	learnPanela.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent event) {
		        	if (appFrame.getContentPane() == testPanel)
		        		if (JOptionPane.showConfirmDialog(appFrame, "Are you sure you want to stop the test?") != JOptionPane.YES_OPTION)
		        			return;
		        	learnPanel.startCardShow(getWordList());
		        	changeScreen(learnPanel);	
		        }
	        });
	}
	
	private static JMenuBar createMenu(){
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		menuBar.add(file);
		setUpFileMenu(file);
		JMenu view = new JMenu("View");
		menuBar.add(view);
		setUpViewMenu(view);
		JMenu help = new JMenu("Help");
		setUpHelpMenu(help);
		menuBar.add(help);
        	return menuBar;
        }
	
	private static void newFile(){
		int response = JOptionPane.showConfirmDialog(null, "Are you want to erase the current wordinary from memory (it will still remain on your hard disk though, "+
		"just click file->load and navigate to the file that you saved the words in (default is wordList.xml) "+
		" and start a new one?");
            	if (response == JOptionPane.YES_OPTION){
            		categoryForm = new CategoriesForm();
            		wordList = new ArrayList<Word>();
        		listWordsPanel = new ListWordsPanel();
            		categories = new ArrayList<Category>();
            		saveFile(true);
            		refreshScreen();
            	}
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
	
	private static String oldXMLFile;
	private static void openFile(boolean start){
		JFileChooser fc = new JFileChooser("./");
		fc.addChoosableFileFilter(new OnlyExt("xml"));
		int returnVal = fc.showOpenDialog(appFrame);
            	if (returnVal == JFileChooser.APPROVE_OPTION) {
            		//System.out.println("Loading from xml file: "+xmlFile);
            		int response = JOptionPane.showConfirmDialog(null, "Do you want to override the current list (yes), "+
		        	"add the words in that file to the current list (no) or do nothing (cancel)?");
            		if (response == JOptionPane.NO_OPTION){
            			oldXMLFile = xmlFile;
            			xmlFile = fc.getSelectedFile().toString();
            			getWordListFromFile(false);
            		}
            		if (response == JOptionPane.YES_OPTION){
            			xmlFile = fc.getSelectedFile().toString();
            			oldXMLFile = xmlFile;
            			getWordListFromFile(true);
            		}
            		saveSettings();
            		if (start)
            			changeScreen(frontScreen);
            	}
        }

        public static void saveFile(boolean newe){
        	if (newe)
        		title = JOptionPane.showInputDialog("What do you want the title of your new Wordinary to be?"); 
        	saveFile();
        }
        
        public static void saveFile(){
        	JFileChooser fc = new JFileChooser("./");
        	fc.addChoosableFileFilter(new OnlyExt("xml"));
		int returnVal = fc.showSaveDialog(appFrame);
            	if (returnVal == JFileChooser.APPROVE_OPTION) {
            		xmlFile = fc.getSelectedFile().toString();
            		if (xmlFile.indexOf(".xml") < 0)
            			xmlFile += ".xml";
            		oldXMLFile = xmlFile;
            		//System.out.println("Saving to xml file: "+xmlFile);
			reallyWrite = true;
            		writeWordsToFile();
            		saveSettings();
            	}
        }	
	
        
	static String oldTitle;
	public static void saveFile(List<Word> list, boolean newe){
		oldTitle = title;
		if (newe)
        		title = JOptionPane.showInputDialog("What do you want the title of your new Wordinary to be?"); 
        	JFileChooser fc = new JFileChooser("./");
        	fc.addChoosableFileFilter(new OnlyExt("xml"));
		int returnVal = fc.showSaveDialog(appFrame);
            	if (returnVal == JFileChooser.APPROVE_OPTION) {
                        String tempXMLFile = fc.getSelectedFile().toString();
                        if (tempXMLFile.indexOf(".xml") < 0)
                            tempXMLFile = fc.getSelectedFile().toString()+".xml";
            		System.out.println("Saving to xml file: "+xmlFile);
			reallyWrite = true;
            		outputXML(list, tempXMLFile);
            		//saveSettings();
            	}
        }
	
	private static void setUpGUI(){
		try{
			loadingDialog.newTask("main GUI",1);
			appFrame = new JFrame("Wordinary by Jeremy Nagel");
			try
			{
				UIManager.setLookAndFeel(new TonicLookAndFeel());
			}
			catch(UnsupportedLookAndFeelException e)
			{
				e.printStackTrace();
			}
			
			aboutDialog = new AboutDialog(appFrame, true);
			settingsDialog = new SettingsDialog(appFrame,true);
			helpDialog = new HelpDialog(appFrame, false);
	        	addWords = new AddWordPanel();
	        	categoryForm = new CategoriesForm();
	        	frontScreen = new FrntPanel();
	        	listWordsPanel = new ListWordsPanel();
	        	searchPanel = new SearchPanel();
	        	testFront = new TestFrnt();
	        	testPanel = new TestPanel();
	        	learnPanel = new LearnPanel();
			
			JFrame.setDefaultLookAndFeelDecorated(true);
			appFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			    public void windowClosing(WindowEvent winEvt) {
			        if (JOptionPane.showConfirmDialog(appFrame, "Are you sure you want to quit?") == JOptionPane.YES_OPTION)
			        	System.exit(0); 
			    }
			});
			appFrame.setJMenuBar(createMenu());
			appFrame.setSize(appFrame.getContentPane().getPreferredSize());			                		              		
			appFrame.setVisible(false);
			loadingDialog.updateProgress(1);
		}
		catch (Exception e){e.printStackTrace();}
	}
	
	public static void changeScreen(JPanel panel){
		try{
			if (panel == null)
				return;
			appFrame.setTitle("Wordinary : \""+getTitle()+"\"");
			appFrame.setContentPane(panel);
			appFrame.setSize(appFrame.getContentPane().getPreferredSize());	
			appFrame.getContentPane().setBackground(backColour);
			for (int i = 0; i < appFrame.getContentPane().getComponents().length; i++)
				appFrame.getContentPane().getComponents()[i].setForeground(textColour);
			appFrame.validate();
		}
		catch (Exception e){ 
			e.printStackTrace(); 
		}
	}
	
	public static void writeWordsToFile(List<Word> list){
		writeWordsToNewFile(list);
	}
	
	public static void writeWordsToFile(){
		writeWordsToNewFile();
	}
	
	public static List<Word> getWordList(){
		return wordList;
	}
	
	public static void setOtherLanguage(String other){
		otherLanguage = other;
	}
	
	public static String getOtherLanguage(){
		return otherLanguage;
	}
	
	private static boolean listContains(List<String> strings, String toFind){
		for (int i = 0; i < strings.size(); i++)
			if (strings.get(i).equalsIgnoreCase(toFind)){
				//System.out.println(toFind+" was inside the list");
				return true;
			}
		return false;
	}
	
	private static boolean listContains(List<String> strings, Word toFind){
		for (int i = 0; i < strings.size(); i++)
			if (strings.get(i).equalsIgnoreCase(toFind.getInEnglish()) ||
				strings.get(i).equalsIgnoreCase(toFind.getInOtherLanguage()))
				return true;
		return false;
	}
	
	public static List<Category> getListOfCategories(){
		return categories;
	}
	
	public static Word findIndexByID(Word toFind){
		for (Word temp : getWordList())
			if (temp.getID() == toFind.getID())
				return temp;
		return null;
	}
	
	public static int findIndexInList(String togind){
		Word toFind = new Word(togind,togind,"blah",0);
		for (int i = 0; i < getWordList().size(); i++)
			if (getWordList().get(i).equalsIgnoreCase(toFind))
				return i;				
		return -1;
	}
	
	public static int findIndexInList(Word toFind){
		for (int i = 0; i < getWordList().size(); i++)
			if (getWordList().get(i).equalsIgnoreCase(toFind))
				return i;				
		return -1;
	}
	
	public static int findIndexInList(List<Category> list, Category toFind){
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).toString().equalsIgnoreCase(toFind.toString()))
				return i;
		return -1;
	}
	
	public static int findIndexInList(List<String> list, String toFind){
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).equalsIgnoreCase(toFind))
				return i;
		return -1;
	}
	
	/**
	*  @return whether the list already contains the word: -1 == no, -2 == cancel add operation, -3 == append to word, else the index of the word in the list
	*/
	private static int listContainsWord(List<Word> list, Word toFind){
		if (toFind.getID() != -1){
			//System.out.println("Tofind had an id..:"+toFind.getID());
			for (int i = 0; i < list.size(); i++){
				if (list.get(i).getID() == toFind.getID()){
					//System.out.println("IDS match up: "+list.get(i).getID()+" & "+toFind.getID());
					Object[] options = { "Replace it", "Append to it", "Add this word seperately", "Do nothing" };
					int response = JOptionPane.showOptionDialog(WordinaryFrame.getJFrame(), "There is already a word similar to the one you are trying to add, what do you want to do?", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
					if (response == 0)
							return i;
					else if (response == 3)
						return -2;
					else if (response == 1)
						return -3;
					else if (response == 2)
						return -1;
				}
			}
		}
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).equalsIgnoreCase(toFind)){
				//System.out.println(list.get(i)+toFind.toString());
				Object[] options = { "Replace it", "Append to it", "Add this word seperately", "Do nothing" };
				int response = JOptionPane.showOptionDialog(WordinaryFrame.getJFrame(), "There is already a word (" + list.get(i).toString() + ") similar to the one you are trying to add, what do you want to do?", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
				if (response == 0)
						return i;
				else if (response == 3)
					return -2;
				else if (response == 1)
					return -3;
				else if (response == 2)
					return -1;
			}
		return -1;
	}
	
	/**
	*  Adds a word to the list
	*  @return  Whether the adding action was successful: 1 == edited 0 == added 2 == cancelled
	*/
	public static int addWord(Word word){
		int idx = listContainsWord(wordList,word);
		if (idx == -1){ //add seperately
			int biggestId = 0;
			for (Word iterator : wordList)
				if (iterator.getID() > biggestId)
					biggestId = (iterator.getID());
			word.setID(biggestId + 1);
			System.out.println(word);
			wordList.add(word);
                        writeWordsToFile();
			return 0;
		}
		if (idx == -2){ //cancel
			return 2;
		}
		else if (idx == -3){ //append to it
			Word original = null;
            		for (int i = 0; i < getWordList().size(); i++)
				if (getWordList().get(i).equalsIgnoreCase(word)){
					idx = i;
					original = getWordList().get(i);
					break;
				}

            		boolean isVerb = false;
            		if (word.getInEnglish().startsWith("to ")){
            			word.setInEnglish(word.getInEnglish().substring(3));
            			isVerb = true;
            		}
            		Object[] options = { "Both", "English", "Other language" };
			int response = JOptionPane.showOptionDialog(WordinaryFrame.getJFrame(), "Which part of the new word do you want to append to the old word?", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            		if (response == 0 || response == 1)
            			word.setInEnglish(original.getInEnglish() + " " + word.getInEnglish());
            		if (response == 0 || response == 2)
            			word.setInOtherLanguage(original.getInOtherLanguage() + " " + word.getInOtherLanguage());
            		word.setID(original.getID());
            		WordinaryFrame.getWordList().set(idx, word);
            		writeWordsToFile();
            		return 1;
		}
		word.setID(getWordList().get(idx).getID());
		wordList.set(idx,word);
		writeWordsToFile();
		return 1;
	}
	
	public static boolean addCategory(Category categoryName, boolean write){
		if (categoryName == null || categoryName.equals("") || categoryName.toString().length() < 1){
			//System.out.println("Invalid word");
			return false;
		}
		if (findIndexInList(categories,categoryName) == -1){
			//System.out.println("Added category: "+categoryName);
			categories.add(categoryName);
			if (write)
				writeWordsToFile();
			//System.out.println("There are now "+categories.size()+"categories");
			return true;
		}
		categories.set(findIndexInList(categories,categoryName),(categoryName));
		if (write)
			writeWordsToFile();
		//System.out.println("There are now "+categories.size()+"categories");
		return true;
	}
	
	public static boolean deleteCategory(Category categoryName){
		if (findIndexInList(categories,categoryName) == -1)
			return false;
		categories.remove(findIndexInList(categories,categoryName));
		return true;
	}
	
	public static void deleteWord(int indx){
		wordList.remove(indx);
		writeWordsToFile();
	}
	
	private static void getSettings(){
		try{
                loadingDialog.newTask("settings",7);
            
		Properties p = new Properties();
		FileInputStream fis = new FileInputStream("./settings.wrd");
	            p.load(fis);
	            //p.list(System.out);
	            xmlFile = p.getProperty("filename");
	            oldXMLFile = xmlFile;
	            loadingDialog.updateProgress(1);
	            String[] backColours = p.getProperty("backcolour").split(",");
	            backColour = new java.awt.Color(Integer.parseInt(backColours[0]),Integer.parseInt(backColours[1]),
	                Integer.parseInt(backColours[2]));
	            loadingDialog.updateProgress(2);
	
	            String[] textColours = p.getProperty("textcolour").split(",");
	            textColour = new java.awt.Color(Integer.parseInt(textColours[0]),Integer.parseInt(textColours[1]),
	                Integer.parseInt(textColours[2]));
	            loadingDialog.updateProgress(3);
	
	            spellingAcc = Integer.parseInt(p.getProperty("spellingacc"));
	            loadingDialog.updateProgress(4);
	           	
	            loadingDialog.updateProgress(5);
	
	            alwaysSave = (p.getProperty("alwaysSave").equals("true"));
	            loadingDialog.updateProgress(6);
			
		    useSynth = (p.getProperty("usesynth").equals("true"));
	            if (useSynth)
			useSynth = testIfSpeechEnabled(); 
                    
	    	   fis.close();	   		
	    	}
	    	catch (Exception e){e.printStackTrace();}
    	}
	
	private static boolean testIfSpeechEnabled(){
		try{
			Class temp = Class.forName("com.cloudgarden.speech.CGEngineProperties");
		}
		catch (ClassNotFoundException ex){
			return false;
		}
		return true;
	}
	
	public static void saveSettings(){
		try{
			Properties p = new Properties();
			FileOutputStream fout = new FileOutputStream("./settings.wrd");
			p.setProperty("filename",xmlFile);
			String backColourStr = ""+backColour.getRed()+","+backColour.getGreen()+","+backColour.getBlue();
			p.setProperty("backcolour",backColourStr);
			String textColourStr = ""+textColour.getRed()+","+textColour.getGreen()+","+textColour.getBlue();
			p.setProperty("textcolour",textColourStr);
			p.setProperty("spellingacc",""+spellingAcc);
			
			p.setProperty("alwaysSave",""+alwaysSave);
                        p.setProperty("usesynth",""+useSynth);
			p.store(fout,"Wordinary by Jeremy Nagel");
			fout.close();
			
		}
		catch (Exception e){e.printStackTrace();}
	}
	
	public static void setBackgroundColour(java.awt.Color colour){
		backColour = colour;
	}
	
	public static void setTextColour(java.awt.Color colour){
		textColour = colour;
	}
	
	public static java.awt.Color getBackColour(){
		return backColour;
	}
	
	public static java.awt.Color getTextColour(){
		return textColour;
	}
	
	public static void refreshScreen(){
		changeScreen((JPanel)appFrame.getContentPane());
	}
	
	/**For tests*/
	public static void setSpellingAccuracy(int acc){
		spellingAcc = acc;
	}
	
	public static int getSpellingAccuracy(){
		return spellingAcc;
	}
	
	public static int[] getKeyShortcutSymbols(){
                /*/System.out.print("shortcuts: ");
                for (int symb : keyshortcutsForSymbols)
                    System.out.print(symb + "-");
                System.out.println("");*/
		return keyshortcutsForSymbols;
	}
	
	public static int[] getSymbols(){
                /*System.out.print("symbols: ");
                for (int symb : symbols)
                    System.out.print(symb + "-");
                System.out.println("");*/
		return symbols;
	}
	
	public static List<HelpFile> getHelpFiles(){
		return helpFiles;
	}
	
	public static String getTitle(){
		return title;
	}
	
	public static void setAlwaysSave(boolean yes){
		alwaysSave = yes;
	}
	
	public static boolean getAlwaysSave(){
		return alwaysSave;
	}
	
	public static void addWordsToList(List<Word> wordListe){
		String fileName = "";
		JFileChooser fc = new JFileChooser("./");
		fc.addChoosableFileFilter(new OnlyExt("xml"));
		int returnVal = fc.showOpenDialog(appFrame);
            	if (returnVal == JFileChooser.APPROVE_OPTION) 
            		fileName = fc.getSelectedFile().toString();
            	try{
		      SAXBuilder parser = new SAXBuilder();
		      Document doc = parser.build(fileName);
		      
		      Element root = doc.getRootElement(); 
		      List categories = root.getChildren();
		      for (Object cat : categories){
		      	Element elm = ((Element)cat);
		      	if (!elm.getName().equals("category"))
		      		continue;
		      	Attribute att = elm.getAttribute("name");
	      		Category cater = new Category(att.getValue());
	      		boolean add = true;
	      		for (int i = 0; i < getListOfCategories().size(); i++){
	      			Category cattemp = getListOfCategories().get(i);
	      			if (cattemp.toString().equalsIgnoreCase(cater.toString()))
	      				add = false;
	      		}
	      		if (add)
	      			getListOfCategories().add(cater);
		      	for (Element el : ((List<Element>)(elm.getChildren()))){
		      		String english = getCDataValue(el, "english");
                                String other = getCDataValue(el, "other");
                                String hint = getCDataValue(el, "hint");
		      		int correct = Integer.parseInt(el.getChild("correct",el.getNamespace()).getText());
		      		int id = Integer.parseInt(el.getChild("id",el.getNamespace()).getText());
		      		
		      		Word wrd = new Word(english,other,cater.toString(),correct);
		      		wrd.setID(id);
		      		
		      		wordListe.add(wrd);
		      	}
		      }
		      System.out.println("Loaded "+wordList.size()+" words and "+categories.size()+" categories");
		   }
		   catch (Exception e) {
		   	e.printStackTrace();
		   }
	}
	
	public static void changeTitle(String titler){
		title = titler;
		writeWordsToFile();
		refreshScreen();
	}
	
	public static String chooseSynthModeName(){
	    SynthesizerModeDesc desc = null;
	    SpeechEngineChooser chooser = null;
	    while (desc == null){
		    try {
			chooser = SpeechEngineChooser.getSynthesizerDialog();
			chooser.show();
			desc = chooser.getSynthesizerModeDesc();
		    } catch(NoClassDefFoundError e) {
			System.out.println("Can't find Swing - try using Java 2 to see the SpeechEngineChooser");
		    }
	    }
	    return descToString(desc);
	}
	
	public static String descToString(SynthesizerModeDesc desc){
		return desc.getEngineName()+":"+desc.getModeName()+":"+desc.getLocale();
	}
	
	public static boolean isSpeechEnabled(){
		if (!synthAvailable)
			return false;
		try{
			Class temp = Class.forName("com.cloudgarden.speech.CGEngineProperties");
		}
		catch (ClassNotFoundException ex){
			useSynth = false;
			synthAvailable = false;
		}
		return useSynth;
	}
	
	public static void setSpeechEnabled(boolean speech){
		useSynth = speech;
                if (speech){
                    synthEng = new SynthEngine(otherSynthName);
			
                    synthEngEnglish = new SynthEngine(englishSynthName);

                    synthEngEnglish.saySomething("Welcome to wordinary by jeremy nagel");
                }
	}
		
	public static SynthEngine getSynthEngine(){
		return synthEng;
	}
	
	public static SynthEngine getEnglishSynthEngine(){
		return synthEngEnglish;
	}
	
	public static void setEnglishSynthName(String name){
		englishSynthName = name;
	}
	
	public static void setOtherSynthName(String name){
		otherSynthName = name;
	}
	
	public static String getEnglishSynthName(){
		return englishSynthName;
	}
	
	public static String getOtherSynthName(){
		return otherSynthName;
	}
	
	public static JFrame getJFrame(){
		return appFrame;
	}
	
	/**
	*  Sets the score (number of times answered correctly) of every single word in this list to supplied number
	*  @arg  score  The score that the words will be set to
	*/
	public static void resetScores(int score, List<Word> aWordList){
		for (Word word : aWordList)
			word.setNumberOfTimesCorrect(score);
		writeWordsToNewFile();
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
        
        public static void setWordList(List<Word> newList){
            wordList = newList;
        }
        
        public static void setXMLFile (String newXMLFile){
            xmlFile = newXMLFile;
        }
        
    	public static AboutDialog aboutDialog;
    	public static AddWordPanel addWords;
    	public static CategoriesForm categoryForm;
    	public static FrntPanel frontScreen;
    	public static HelpDialog helpDialog;
    	public static ListWordsPanel listWordsPanel;
    	public static SearchPanel searchPanel;
    	public static SettingsDialog settingsDialog;
    	public static TestFrnt testFront;
    	public static TestPanel testPanel;
    	public static LearnPanel learnPanel;
            	
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
	private static JFrame appFrame;
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
