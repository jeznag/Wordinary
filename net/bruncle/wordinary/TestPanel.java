/*
 * TestPanel.java
 *
 * Created on May 3, 2005, 7:47 PM
 */

package net.bruncle.wordinary;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import org.apache.commons.codec.language.RefinedSoundex;

/**
 *
 * @author  Jeremy
 */
public class TestPanel extends javax.swing.JPanel implements ActionListener, Runnable, KeyListener{
    boolean go; //controls if panel will process events
    
    public void prepareForUse(){
    	wrongWords = new ArrayList<Word>();
    	initComponents();
    }
    
    public void startTest(String language, String category, String level, int stopAfter) {
    	this.language = language;
    	this.category = category;
    	this.level = level;
        this.stopAfter = stopAfter;
    	finished = false; //condition for testing thread
    	answered = false; //condition for thread waiting until user answers a quiz question	
	correct = 0; //total number of questions answered correctly
    	totalQuestions = 0; //total number of questions asked
    	finishedYN = false; //true when all questions have been asked
        initComponents();
        wrongWords = new ArrayList<Word>();
        startTest();
    }

    private void initComponents() {
    	removeAll(); //remove all components that exist on the jpanel's contentpane
    	setPreferredSize(new java.awt.Dimension(520,300));
        lblQuestion = new javax.swing.JLabel();
        lblWord = new javax.swing.JLabel();
        lblHint = new javax.swing.JLabel();
        txtAnswer = new javax.swing.JTextField();
        lblAnswerInstr = new javax.swing.JLabel();
        btnSubmit = new javax.swing.JButton();
        lblCorrect = new javax.swing.JLabel();
        lblScore = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setBackground(new java.awt.Color(117, 199, 83));
        lblQuestion.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14));
        lblQuestion.setText("What does the following word mean in the other language?");
        add(lblQuestion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 450, 30));

        add(lblWord, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, -1, -1));

        add(txtAnswer, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, 140, -1));
        txtAnswer.addKeyListener(this);

        lblAnswerInstr.setText("Type the meaning here -->");
        add(lblAnswerInstr, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));

        btnSubmit.setText("Am I right?");
        add(btnSubmit, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 140, -1));
        btnSubmit.addActionListener(this);
        btnSubmit.setActionCommand("answer");

        lblCorrect.setBackground(new java.awt.Color(255, 51, 0));
        lblCorrect.setForeground(new java.awt.Color(255, 0, 0));
        lblCorrect.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        add(lblCorrect, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 180, 480, 60));

        lblScore.setText("You have answered "+correct+" out of "+totalQuestions+" correctly.");
        lblScore.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        add(lblScore, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        btnBack.setText("STOP");
        btnBack.setBackground(java.awt.Color.red);
        btnBack.setBorder(new javax.swing.border.MatteBorder(null));
        add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 220, 50, 20));
        btnBack.addActionListener(this);
        btnBack.setActionCommand("back");
        
        add(lblHint, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 75, -1, 50));
	
	final String f9Pressed = "F9"; // modifier required!
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(f9Pressed), f9Pressed);
        this.getActionMap().put(f9Pressed, new AbstractAction() {
            public void actionPerformed(ActionEvent ignored) {
            	if (!go || lastWord == null)
    			return;
    		if (JOptionPane.showConfirmDialog(WordinaryFrame.getJFrame(), "Are you sure you want to add your answer (" + lastAttempt + ") to the " +
    			"word (" + lastWord.getInEnglish() + ":" + lastWord.getInOtherLanguage() + ")'s " + 
    			(!lastInEnglish ? "english " : "other language ") + "field?") == JOptionPane.YES_OPTION){
            		String formattedAttempt = AddWordPanel.getQuotedText("\"" + lastAttempt + "\"");
            		int idx = WordinaryFrame.findIndexInList(lastWord);
			lastWord.setID(WordinaryFrame.getWordList().get(idx).getID());
			lastWord.setNumberOfTimesCorrect(WordinaryFrame.getWordList().get(idx).getNumberOfTimesCorrect() + 1);
            		for (Word test : wrongWords)
            			if (test.equals(lastWord)){
            				//System.out.println("removing " + test);
            				wrongWords.remove(test);
            				break;
            			}
            		correct++;
            		if (!lastInEnglish){
            			lastWord.setInEnglish(lastWord.getInEnglish() + " " + formattedAttempt);
            			WordinaryFrame.getWordList().set(idx, lastWord);
			}
			else{
            			lastWord.setInOtherLanguage(lastWord.getInOtherLanguage() + " " + formattedAttempt);
            			WordinaryFrame.getWordList().set(idx, lastWord);
			}
            	}
            }
        });
    }
    
    private void startTest(){
    	Thread testThread = new Thread(this,"testThread");
    	testThread.start();
    }
    
    private List<Word> cullWords(List<Word> toCull){
    	List<Word> toReturn = new ArrayList<Word>();
    	for (Word temp : toCull){
    		if ((category.equals("Any category") || temp.getCategory().equals(category)) &&
    			(level.equals("All words") || (temp.getNumberOfTimesCorrect() < LEARNT_WORD && 
    			level.equals("Words not yet learnt")) || (temp.getNumberOfTimesCorrect() >= LEARNT_WORD && 
    			level.equals("Revision")))
    		)
    			toReturn.add(temp);
    	}
    	if (toReturn.size() == 0)
    		toReturn = null;
    	return toReturn;
    }
    
    boolean finished;
    List<Word> wordList;
    public void run(){
    	wordList = cullWords(new ArrayList(WordinaryFrame.getWordList()));
    	System.out.println(wordList.size() + " words to test");
    	if (wordList == null){
    		JOptionPane.showMessageDialog(null, "You have answered all the words correctly");
    		WordinaryFrame.changeScreen(WordinaryFrame.frontScreen);
    		return;
    	}
    	english = language.equals("english");
    	Random rando = new Random(4);
    	while (!finished){
    		ready = false;
    		//lblHint.setForeground(new java.awt.Color(rando.nextInt(255), rando.nextInt(255), rando.nextInt(255)));
    		//lblHint.setText("<-- Word's over there");
    		word = getNextWord(wordList);
    		percentageCorrect = 0;
    		reasonIncorrect = "";
    		if (word == null){
    			System.out.println("test over");
    			setQuestion("The test is over, you have answered all of the questions possible");
    			finishedYN = true;
    			break;
    		}
                if (wrongWords.size() >= stopAfter && stopAfter != -1){
                    for (Word temp : wrongWords){
                        temp.setNumberOfTimesCorrect(2);
                        WordinaryFrame.findIndexByID(temp).setNumberOfTimesCorrect(2);
                    }
                    javax.swing.JOptionPane.showMessageDialog(null, "You have now answered " + wrongWords.size() + " incorrectly.\n"+
                            "They will now be saved in another file to make it easier to work on the ones you have had trouble with");
                    WordinaryFrame.saveFile(wrongWords,true);
                    WordinaryFrame.setWordList(wrongWords);
                    WordinaryFrame.changeScreen(WordinaryFrame.testFront);
                    WordinaryFrame.testFront.txtStopAfter.setText("-1");
                }
                if (!word.getHint().equals("null"))
                    lblHint.setText(word.getHint());
                else    
                    lblHint.setText("");
    		if (english){
    			String chosenWord = chooseRandomWord(word.getInEnglish());
    			setWord("In english ("+word.getCategory()+"): " + chosenWord.replaceAll("-", " "));
    			correctAnswer = word.getInOtherLanguage();
    			ready = true;
    			if (WordinaryFrame.isSpeechEnabled()){
    				WordinaryFrame.getEnglishSynthEngine().saySomething("The word is ");
    				sayPhrase(ENGLISH, chosenWord);
    			}
    		}
    		else{
    			String chosenWord = chooseRandomWord(word.getInOtherLanguage());
    			setWord("In other language ("+word.getCategory()+"): " + chosenWord.replaceAll("-", " "));
    			correctAnswer = word.getInEnglish();
    			ready = true;
    			if (WordinaryFrame.isSpeechEnabled()){
    				WordinaryFrame.getEnglishSynthEngine().saySomething("The word is ");
    				sayPhrase(OTHER_LANGUAGE, chosenWord); 
    			}  
    				//System.out.println("No");           
    		}
    		while (!answered){ 
    			try { 
    				Thread.sleep(1);
    			}
    			catch (Exception e){e.printStackTrace();}
    		}
    		answered = false;
    		synonym = null;	
    		if (WordinaryFrame.isSpeechEnabled()){
    			WordinaryFrame.getEnglishSynthEngine().waitUntilSafe();
    			WordinaryFrame.getSynthEngine().waitUntilSafe();
    		}
    		lastWord = word.clone();
    		lastAttempt = new String(attempt != null ? attempt : "");
    		lastInEnglish = english;
    		
    		if (language.equals("Any language")){
    			english = rando.nextBoolean();
    		}
    	}
    	if (wrongWords.size() > 0){
	    	int response = JOptionPane.showConfirmDialog(null, "Do you want to view a list of the "+wrongWords.size()+" words you got wrong?");
	    	if (response == JOptionPane.YES_OPTION){
			ListWordsPanel listWords = WordinaryFrame.listWordsPanel;
			listWords.setData(wrongWords,"Words you got wrong");
			WordinaryFrame.changeScreen(listWords);
		}
		else
			WordinaryFrame.changeScreen(WordinaryFrame.frontScreen);	
	}
	else
		WordinaryFrame.changeScreen(WordinaryFrame.frontScreen);
    }
    
    /**
    * Chooses one of the synonyms of the word eg. word == english : "run sprint" - returns run
    */
    private String chooseRandomWord(String word){
    	boolean isVerb = (word.startsWith("to ")); //word is a verb
    	boolean bracket = false;
    	List<String> options = new ArrayList<String>();
    	for (String option : word.split(" ")){
    		if (option.startsWith("("))
    			bracket = true;
    		if (option.endsWith(")")){
    			bracket = false;
    			continue;
    		}
    		if (bracket)
    			continue;
    		if (option.equalsIgnoreCase("to"))
    			continue;
    		options.add(option);
    	}
    	String toReturn = (isVerb ? "to " : "") + options.get(random(0, options.size()));
    	if (options.size() > 1 || word.contains("("))
    		toReturn += " (more)";
	return toReturn;
    }
    
    private void setWord(String word){
    	lblWord.setText(word);
    }
    
    private void setQuestion(String text){
    	lblQuestion.setText(text);
    }
    
    private Word getNextWord(List<Word> wordList){
    	if (wordList.size() <= 0){
    		//System.out.println("last word");
    		return null;
    	}
    	int count = 0;
    	while (count < wordList.size()){
    		int idx = random(0, wordList.size());
    		Word temp = wordList.get(idx);
    		if ((category.equals("Any category") || temp.getCategory().equals(category)) &&
    			(level.equals("All words") || (temp.getNumberOfTimesCorrect() < LEARNT_WORD && 
    			level.equals("Words not yet learnt")) || (temp.getNumberOfTimesCorrect() >= LEARNT_WORD && 
    			level.equals("Revision")) && !temp.equals(lastWord))
    		)
    			return idx >= 0 ? wordList.remove(idx) : null;
    		count++;
    	}
    	return null;
    }
    
    public void actionPerformed(ActionEvent e){
    	if (!go)
    		return;
    	if (e.getActionCommand().equals("answer") && !finishedYN && ready){
    		checkAnswer();
    		answered = true;
    	}
    	else{
    		finished = true;
		if (wrongWords.size() > 0){
	    	int response = JOptionPane.showConfirmDialog(null, "Do you want to view a list of the "+wrongWords.size()+" words you got wrong?");
	    	if (response == JOptionPane.YES_OPTION){
			ListWordsPanel listWords = WordinaryFrame.listWordsPanel;
			listWords.setData(wrongWords,"Words you got wrong");
			WordinaryFrame.changeScreen(listWords);
		}
		else
			WordinaryFrame.changeScreen(WordinaryFrame.frontScreen);	
	}
	else
		WordinaryFrame.changeScreen(WordinaryFrame.frontScreen);
	}
    }
    
    private boolean correct(String answer, String attempt){
    	this.attempt = attempt;
    	if (attempt.length() <= 2){
    		if ((answer.equalsIgnoreCase(attempt) || ((answer.indexOf(attempt + " ") > 0 || answer.indexOf(" " + attempt) > 0) && !answer.equals(""))) 
    			&& !attempt.equals("") && attempt.length() == 2 && ((float)((float)attempt.length()) / ((float)(answer.contains(" ") ? answer.split(" ")[0].length() : answer.length())) > 0.5)){
    			percentageCorrect = 100;
    			return true;
    		}
    		lblHint.setText("Your answer was too short");
    		//System.out.println("too short");
    		return false;
    	}
        lblHint.setText("");
    	answer = answer.toLowerCase();
    	attempt = attempt.toLowerCase();
    	try{
	    	if (answer.startsWith("to ")){
	    		answer = answer.substring(3);
	    		//System.out.println(answer);
	    	}
	        if (attempt.startsWith("to ") && !answer.contains("to-")){
	    		attempt = attempt.substring(3);
	    		//System.out.println(attempt);
	    	}
	}
	catch (StringIndexOutOfBoundsException e){
		e.printStackTrace();
		System.out.println("error: " + answer+":"+attempt);
	}
	attempt = attempt.replace('-',' ');
        String[] answers = answer.split(" ");
        try{
	    	for (int i = 0; i < answers.length; i++){
	    		answers[i] = answers[i].replace('-', ' ');
	    		if (answers[i].startsWith("(")){
	    			//System.out.println("In brackets " + answers[i] + ":" + attempt);
	    			continue;
	    		}
	    		try{
		    		if (!attempt.startsWith(answers[i].substring(0,3))){ //wrong gender
		    			//System.out.println("Wrong gender: " + answers[i] + ":" + attempt);
		    			continue;
		    		}
		    	}
		    	catch (StringIndexOutOfBoundsException e){
		    		//word was less than 3 letters long
		    	}
	    		answers[i] = answers[i].replace('-', ' ');
	    		//System.out.println(answers[i] + ":" + attempt + ":" + getPercentage(new RefinedSoundex().difference(answers[i], attempt), new RefinedSoundex().encode(answers[i]).length()));
	    		if (getPercentage(new RefinedSoundex().difference(answers[i], attempt), new RefinedSoundex().encode(answers[i]).length()) >= WordinaryFrame.getSpellingAccuracy()){
	    			percentageCorrect = getPercentage(new RefinedSoundex().difference(answers[i], attempt), new RefinedSoundex().encode(answers[i]).length());
	    			return true;
	    		}
	    	}
	    	//System.out.println(attempt + ":" + answer);
	    	percentageCorrect = getPercentage(new RefinedSoundex().difference(answer, attempt), new RefinedSoundex().encode(answer).length());
	}
	catch (Exception e){
		e.printStackTrace();
		percentageCorrect = 0;
	}
	//System.out.println(answer+":"+attempt);
    	return false;
    }
    
    /*
    private boolean correct(String answer, String attempt){
    	if (attempt.length() <= 2){
    		if ((answer.equalsIgnoreCase(attempt) || ((answer.indexOf(attempt + " ") > 0 || answer.indexOf(" " + attempt) > 0) && !answer.equals(""))) 
    			&& !attempt.equals("") && attempt.length() == 2 && ((float)((float)attempt.length()) / ((float)(answer.contains(" ") ? answer.split(" ")[0].length() : answer.length())) > 0.5)){
    			percentageCorrect = 100;
    			return true;
    		}
    		reasonIncorrect = "answer was too short";
    		return false;
    	}
        lblHint.setText("");
    	int incorrect = 0;
    	int total = 0;
    	try{
	    	if (answer.startsWith("to ")){
	    		answer = answer.substring(3);
	    		//System.out.println(answer);
	    	}
	        if (attempt.startsWith("to ") && !answer.contains("to-")){
	    		attempt = attempt.substring(3);
	    		System.out.println(attempt);
	    	}
	}
	catch (StringIndexOutOfBoundsException e){
		e.printStackTrace();
		System.out.println(answer+":"+attempt);
	}
	attempt = attempt.replace('-',' ');
        String[] answers = answer.split(" ");
    	for (int i = 0; i < answers.length; i++){ 
    		answers[i] = answers[i].replace('-',' ');
                //System.out.println(answers[i]+":"+attempt+i+"/"+answers.length);
    		if (answers[i].length() < 3 && answer.length() > 3) {
    			reasonIncorrect = ("Words didn't start the same way");
    			//System.out.println("Answer wasn't long enough, or two words didn't start the same way");
    			continue;
    		}
    		if ((answers[i].startsWith("der ") || answers[i].startsWith("die ")
                 || answers[i].startsWith("das ")) && !startsWithIgnoreCase(attempt.substring(0,3), answers[i].substring(0,3))){
                      //System.out.println("You got the article wrong..");
                      reasonIncorrect = ("You got the article wrong");
                      continue;
                }
    		try{
    			if (!startsWithIgnoreCase(answers[i],(attempt.substring(0,2)))){
	    			reasonIncorrect = "words didn't start the same way";
	    			continue;
	    		}
	    		if (answers[i].length() - attempt.length() > 2){
	    			reasonIncorrect = ("Answer wasn't long enough");
	                        continue;
	                }
	        }
	        catch (StringIndexOutOfBoundsException e){
	        }    		
    		for (int j = 0; j < answers[i].length(); j++){
    			if (j >= attempt.length()){
    				total += (answers[i].length() - (j+1));
    				break;
    			}
    			if (Character.toLowerCase(answers[i].charAt(j)) != Character.toLowerCase(attempt.charAt(j))){
    				incorrect++;
    			}
    			total++;
    		}
    		if ((100-((float)(((float)incorrect)/((float)total))*100)) >= WordinaryFrame.getSpellingAccuracy()){
    		    percentageCorrect = (int)((100-((float)(((float)incorrect)/((float)total))*100)));
                    return true;
                }              
    		else{
    			total = 0;
    			incorrect = 0;
    		}
    	}	
        //lblHint.setText("You were "+((int)(100-((float)(((float)incorrect)/((float)total))*100)))+"% correct");
    		//System.out.println("accuracy was "+(100-((float)(((float)incorrect)/((float)total))*100)));
    		//System.out.println(" required accuracy: "+WordinaryFrame.getSpellingAccuracy());
    	percentageCorrect = (int)((100-((float)(((float)incorrect)/((float)total))*100)));
        if ((100-((float)(((float)incorrect)/((float)total))*100)) >= WordinaryFrame.getSpellingAccuracy()){
            return true;
        }     
    	return false;
    }*/
    		
    private boolean startsWithIgnoreCase(String comp1, String comp2){
    	comp1 = comp1.toLowerCase();
    	comp2 = comp2.toLowerCase();
    	return(comp1.startsWith(comp2));
    }
    
    private void checkAnswer(){
        try{
            if (finishedYN)
                    return;
            totalQuestions++;
            lblCorrect.setForeground(java.awt.Color.red);
            boolean right = false;
            //if (correct((correctAnswer),txtAnswer.getText())){
            if (correct(correctAnswer, txtAnswer.getText())){
                    correct++;
                    Word temp = null;
                    try{
                            temp = WordinaryFrame.getWordList().get(WordinaryFrame.findIndexInList(correctAnswer));
                    }
                    catch (Exception e){
                        lblCorrect.setText("Problem with finding word");
                        return;
                    }
                    int correctN = word.getNumberOfTimesCorrect();
                    //System.out.println("You have answered this word correctly "+(correctN+1)+" times");
                    temp.setWord(word.getInEnglish(), word.getInOtherLanguage(), word.getCategory(), correctN+1);
                    lblCorrect.setForeground(java.awt.Color.yellow);
                    right = true;
            }
            else{
                    //System.out.println("You got it wrong..");
                    ArrayList<String> temper = null;
                    if (english)
                            temper = getSynonyms(word.getInOtherLanguage());
                    else
                            temper = getSynonyms(word.getInEnglish());				
                    for (int i = 0; i < temper.size(); i++){
                            //System.out.println("synonym: "+temper.get(i));
                            String answer = temper.get(i);
                            if (correct(correctAnswer, txtAnswer.getText())){
                                    correct++;
                                    Word temp = WordinaryFrame.getWordList().get(WordinaryFrame.findIndexInList(correctAnswer));
                                    int correctN = word.getNumberOfTimesCorrect();
                                    //System.out.println("You have answered this word correctly "+(correctN+1)+" times");
                                    temp.setWord(word.getInEnglish(), word.getInOtherLanguage(), word.getCategory(), correctN+1);
                                    lblCorrect.setForeground(java.awt.Color.yellow);
                                    synonym = " or " +temper.get(i);
                                    right = true;
                                    break;
                            }
                    }
            }
            if (!right){
                    int indexOfWord = WordinaryFrame.findIndexInList(correctAnswer);
                    Word temp = null;
                    if (indexOfWord > -1){
                        temp = WordinaryFrame.getWordList().get(indexOfWord);
                    }
                    else{
                        System.out.println("Couldn't find word in list for some reason?: "+ correctAnswer);
                        lblCorrect.setText("Problem with finding word in wordlist, see if you can fix up the word: " + correctAnswer);
                        txtAnswer.setText("");
                        return;
                    }
                    int correctN = word.getNumberOfTimesCorrect() - 1;
                    //System.out.println("You have answered this word correctly "+(correctN+1)+" times");
                    temp.setWord(word.getInEnglish(), word.getInOtherLanguage(), word.getCategory(), correctN);
                    wrongWords.add(temp);
            }
            int indexOfWord = WordinaryFrame.findIndexInList(correctAnswer);
            Word temp = null;
            if (indexOfWord > -1){
                temp = WordinaryFrame.getWordList().get(indexOfWord);
            }
            else{
                System.out.println("Couldn't find word in list for some reason?: "+ correctAnswer);
                lblCorrect.setText("Problem with finding word in wordlist, see if you can fix up the word: " + correctAnswer);
                txtAnswer.setText("");
                return;
            }
            lblScore.setText("You have answered "+correct+" out of "+totalQuestions+" correctly. There are "+wordList.size()+
                    " words left to test.");
            lblCorrect.setText("<html>You were " + percentageCorrect + "% correct, the meaning was \""+correctAnswer + ((synonym != null) ?  (" " + synonym) : "") +"\".<br> You have answered this word correct "+temp.getNumberOfTimesCorrect()+" times</html>");
            WordinaryFrame.writeWordsToFile();
            txtAnswer.setText("");
        }
        catch (Exception e) {
            lblCorrect.setText("Sorry a serious error occured, but you can probably keep going with the test");
            e.printStackTrace();
        }
    }
    
    /**
    *  Calculates what percentage a is of b
    */    
    private int getPercentage(int a, int b){
    	//System.out.println("a = " + a + "b = " + b);
   	return (int)((((double)a) / ((double)b)) * 100);
    }
    
    private ArrayList<String> getSynonyms(String word){
    	//System.out.println("Finding synonyms for "+word);
    	ArrayList<String> temp = new ArrayList<String>();
    	for (int i = 0; i < WordinaryFrame.getWordList().size(); i++){
    		if (english){
    			if (WordinaryFrame.getWordList().get(i).getInOtherLanguage().indexOf(word) > -1)
				temp.add(WordinaryFrame.getWordList().get(i).getInOtherLanguage());
		}
		else{
			if (WordinaryFrame.getWordList().get(i).getInEnglish().indexOf(word) > -1)
				temp.add(WordinaryFrame.getWordList().get(i).getInEnglish());		
		}
    	}
    	return temp;
    }
    
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
	if (e.getKeyCode() == KeyEvent.VK_ENTER && ready && !finishedYN && ((JComponent)e.getSource()).hasFocus()){
		checkAnswer();
		answered = true;
	}
	char c = e.getKeyChar();
	int i = (int)c;
	int modifiers = e.getModifiersEx();
	if (modifiers == KeyEvent.ALT_DOWN_MASK){ //ctrl
		for (int j = 0; j < WordinaryFrame.getKeyShortcutSymbols().length; j++)
			if (i == WordinaryFrame.getKeyShortcutSymbols()[j]){
				String txt = ((JTextField)(e.getSource())).getText();
				if (txt.length() > 0 && txt.charAt(txt.length() - 1) == '\"')
					((JTextField)(e.getSource())).setText(txt.substring(0,txt.lastIndexOf("\"") - 1)+((char)WordinaryFrame.getSymbols()[j])+"\"");
				else 
					((JTextField)(e.getSource())).setText(txt+((char)WordinaryFrame.getSymbols()[j]));
			}
	}
	else if (modifiers == KeyEvent.CTRL_DOWN_MASK){
		if (e.getKeyCode() == KeyEvent.VK_R){
			if (english){
	    			String chosenWord = seperateWords(word.getInEnglish());
	    			setWord("In english ("+word.getCategory()+"): " + chosenWord.replaceAll("-", " "));
	    			correctAnswer = word.getInOtherLanguage();
	    		}
	    		else{
	    			String chosenWord = seperateWords(word.getInOtherLanguage());
	    			setWord("In other language ("+word.getCategory()+"): " + chosenWord.replaceAll("-", " "));
	    			correctAnswer = word.getInEnglish();        
	    		}
		}
	}
    }

    /** Handle the key released event from the text field. */
    public void keyReleased(KeyEvent e) {
    }
    
    public String seperateWords(String words){
    	String toReturn = "";
    	boolean bracket = false;
    	for (String word : words.split(" ")){
    		if (word.startsWith("("))
    			bracket = true;
    		else if (word.startsWith(")"))
    			bracket = true;
    		if (word.equals("to") || bracket)
    			toReturn += word + "-";
    		else
    			toReturn += word + "; ";
    	}
    	return toReturn;
    }
    
    private void sayPhrase(byte talkMode, String phrase){
    	speaking = true;
    	try{
    		boolean bracket = false;
	    	String phraseCopy = "";
	    	String[] brokenUp = phrase.split(" ");
	    	for (int i = 0; i < brokenUp.length; i++){
	    		String temp = brokenUp[i];
	    		temp = temp.replace("-", " ");
	                if (temp.charAt(0) == ((char)'(')){
	                	bracket = true;
	                	if (temp.charAt(temp.length() - 1) == ((char)')'))
	                		bracket = false;
	                        continue;
	                }
	                if (temp.charAt(temp.length() - 1) == ((char)')')){
	                	bracket = false;
	                	if (i != (brokenUp.length - 1)){
	                		if (talkMode == OTHER_LANGUAGE)
		    				phraseCopy += (temp + " oder ");
		    			else
		    				phraseCopy += (temp + " or ");
		    		}
	                	continue;
	                }
	    		if (temp.equals("to"))
	    			phraseCopy += (temp + " ");
	    		else{
	    			if (bracket)
	    				continue;
	    			if (i < (brokenUp.length - 1)){
	    				if (brokenUp[i+1].charAt(0) != '('){
		    				if (talkMode == OTHER_LANGUAGE)
		    					phraseCopy += (temp + " oder ");
		    				else
		    					phraseCopy += (temp + " or ");
		    			}
		    			else
		    				phraseCopy += temp;
	    			}
	    			else
	    				phraseCopy += (temp + ".");
	    		}
	    	}
	    	if (talkMode == OTHER_LANGUAGE){
	    		WordinaryFrame.getSynthEngine().saySomething(phraseCopy);
	    		WordinaryFrame.getSynthEngine().waitUntilSafe();
	    	}
	    	else{
	    		WordinaryFrame.getEnglishSynthEngine().saySomething(phraseCopy);
	    		WordinaryFrame.getEnglishSynthEngine().waitUntilSafe();
	    	}
    	}
    	catch (Exception e){
    		e.printStackTrace();
    	}
    	finally {
    		speaking = false;
    	}
    }
    
    public void addNotify(){
    	super.addNotify();
    	go = true;
    	txtAnswer.requestFocusInWindow();
    }
    
    public void removeNotify(){
    	super.removeNotify();
    	go = false;
    }
    
    /**
     * Generates a positive random number, using java.util.Random
     *
	 * @param min The minimum base
	 * @param max The maximum value
	 * @return The random int
	 * @see java.util.Random
	*/
    public int random(int min, int max) {
    		int toReturn = -1;
    		while (toReturn < 0){
	    		int newMin = min;
	    		int newMax = max;
	    		if (newMin < 0)
	    			newMin = 0;
	    		if (newMax < 0)
	    			newMax = 10;
			toReturn = newMin + rand.nextInt(newMax - newMin);
		}
		return toReturn;
    }
    
    private static Random rand = new Random();
    
    private boolean speaking;
    
    private static final byte ENGLISH = 1;
    private static final byte OTHER_LANGUAGE = 2;
    
    private int percentageCorrect; //percentage correct for the last word answered
    private String reasonIncorrect = ""; //reason why the last word was incorrect
    
    private List<Word> wrongWords = new ArrayList<Word>();
    
    private Word word;
    private String attempt;
    private boolean english;
    
    private Word lastWord;
    private String lastAttempt;
    private boolean lastInEnglish;
    
    private static final int LEARNT_WORD = 5;
    
    private boolean answered;	
    private boolean ready;
    
    private int correct;
    private int totalQuestions;
    private boolean finishedYN;
    
    private String correctAnswer;
    private String synonym;
    
    private String category;
    private String language;
    private String level; //All words, revision, words not yet learnt
    private int stopAfter; //how many incorrect answers to stop after
    
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JLabel lblAnswerInstr;
    private javax.swing.JLabel lblCorrect;
    private javax.swing.JLabel lblQuestion;
    private javax.swing.JLabel lblScore;
    private javax.swing.JLabel lblWord;
    private javax.swing.JLabel lblHint;
    private javax.swing.JTextField txtAnswer;
    
    
}
