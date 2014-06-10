/*
 * LearnPanel.java
 *
 * Created on July 17, 2005, 1:17 PM
 */

package net.bruncle.wordinary;

import java.awt.event.*;
import java.util.*;
/**
 *
 * @author  Jeremy
 */
public class LearnPanel extends javax.swing.JPanel implements ActionListener{
    
    /** Creates new form LearnPanel */
    public LearnPanel() {
        initComponents();
    }
    
    private String category;
    private String wordType;
    private String questLang;
    private List<Word> wordList;	
    private Word currentWord;
    private Random rando = new Random(4);
    private boolean english; //is the question in english?
    
    private static final int LEARNT_WORD = 5;
    
    public void startCardShow(List<Word> wordList){
     	this.wordList = new ArrayList(wordList);
     	category = (String)cmbCategory.getSelectedItem();
     	wordType = (String)cmbWordType.getSelectedItem();
     	questLang = (String)cmboQuestLang.getSelectedItem();
     	currentWord = getNextWord(wordList);
     	if (questLang.equals("English")){
     		english = true;
     		lblCard.setText(currentWord.getInEnglish());
     	}
     	else if (questLang.equals("Other")){
     		english = false;
     		lblCard.setText(currentWord.getInOtherLanguage());
     	}
     	else{
     		english = rando.nextBoolean();
     		if (english)
     			lblCard.setText(currentWord.getInEnglish());
     		else
     			lblCard.setText(currentWord.getInOtherLanguage());
     	}
     	lblCard.setHorizontalTextPosition(javax.swing.JLabel.CENTER);
     	if (!currentWord.getHint().equals("") && currentWord.getHint().length() > 2){
     		System.out.println("Hint: "+currentWord.getHint());
     		lblHint.setText(currentWord.getHint());
     	}
     	else{
     		lblHint.setText("You haven't supplied a hint yet...");
     	}
     	lblHint.setVisible(false);
    }
    
    private void initComponents() {
    	setPreferredSize(new java.awt.Dimension(370,350));
    	
        jSeparator1 = new javax.swing.JSeparator();
        lblCategory = new javax.swing.JLabel();
        cmbCategory = new javax.swing.JComboBox(getCategoryData());
        lblWordType = new javax.swing.JLabel();
        cmbWordType = new javax.swing.JComboBox();
        lblQuestionLang = new javax.swing.JLabel();
        cmboQuestLang = new javax.swing.JComboBox();
        lblTitle = new javax.swing.JLabel();
        lblCard = new javax.swing.JLabel();
        cmdTurnOver = new javax.swing.JButton();
        lblHint = new javax.swing.JLabel();
        cmdHint = new javax.swing.JButton();
        cmdNext = new javax.swing.JButton();
        cmdBack = new javax.swing.JButton();
        cmdEdit = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 60, 360, 20));

        lblCategory.setText("Category:");
        add(lblCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        add(cmbCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 100, -1));

        lblWordType.setText("Type of word:");
        add(lblWordType, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, -1, -1));

        cmbWordType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Any type", "Words not yet learnt", "Only learnt words" }));
        add(cmbWordType, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 100, -1));

        lblQuestionLang.setText("Language question is in:");
        add(lblQuestionLang, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, -1));

        cmboQuestLang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Either", "English","Other" }));
        add(cmboQuestLang, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 30, 90, -1));

        lblTitle.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 18));
        lblTitle.setText("Flip the card learning system:");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        lblCard.setText("<word>");
        lblCard.setHorizontalTextPosition(javax.swing.JLabel.CENTER);
        lblCard.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        add(lblCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 250, 60));

        cmdTurnOver.setText("Turn over");
        add(cmdTurnOver, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, -1));
        cmdTurnOver.addActionListener(this);

        lblHint.setText("<hint>");
        lblHint.setHorizontalTextPosition(javax.swing.JLabel.CENTER);
        add(lblHint, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 250, 30));

        cmdHint.setText("Hint");
        add(cmdHint, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 220, -1, -1));
        cmdHint.addActionListener(this);

        cmdNext.setText("Next word");
        add(cmdNext, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 220, -1, -1));
        cmdNext.addActionListener(this);
        
        cmdEdit.setText("Edit word");
        add(cmdEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 220, -1, -1));
        cmdEdit.addActionListener(this);
        
        cmdBack.setText("Back");
        add(cmdBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 60, -1, -1));
        cmdBack.addActionListener(this);
    }
    
    public void actionPerformed (ActionEvent e){
    	if (e.getSource() == cmdTurnOver){
    		if (english)
	     		lblCard.setText(currentWord.getInOtherLanguage());
	     	else
			lblCard.setText(currentWord.getInEnglish());	 
		english = !english;
		return;
	}
	else if (e.getSource() == cmdHint){
		lblHint.setVisible(true);
	}
	else if (e.getSource() == cmdNext){
		category = (String)cmbCategory.getSelectedItem();
	     	wordType = (String)cmbWordType.getSelectedItem();
	     	questLang = (String)cmboQuestLang.getSelectedItem();
	     	currentWord = getNextWord(wordList);
	     	if (currentWord == null){
	     		lblCard.setText("You have looked at all of the cards, go to view -> learn words to restart");
	     		cmdNext.setEnabled(false);
	     		return;
	     	}
	     	if (questLang.equals("English"))
	     		lblCard.setText(currentWord.getInEnglish());
	     	else if (questLang.equals("Other"))
	     		lblCard.setText(currentWord.getInOtherLanguage());
	     	else{
	     		english = rando.nextBoolean();
	     		if (english)
	     			lblCard.setText(currentWord.getInEnglish());
	     		else
	     			lblCard.setText(currentWord.getInOtherLanguage());
	     	}
	     	if (!currentWord.getHint().equals("") && currentWord.getHint().length() > 2){
	     		lblHint.setText(currentWord.getHint());
	     	}
	     	else{
	     		lblHint.setText("You haven't supplied a hint yet...");
	     	}
	     	lblHint.setVisible(false);
	}
	else if (e.getSource() == cmdBack)
		WordinaryFrame.changeScreen(WordinaryFrame.frontScreen);
	else if (e.getSource() == cmdEdit){
		WordinaryFrame.addWords.setEditMode(currentWord);
    		WordinaryFrame.changeScreen(WordinaryFrame.addWords);
    	}
    }
    
    //Returns list of categories in the wordinary
    private String[] getCategoryData(){
    	String[] temp = new String[WordinaryFrame.getListOfCategories().size() + 1];
    	temp[0] = "All categories";
    	for (int i = 1; i < WordinaryFrame.getListOfCategories().size() + 1; i++)
    		temp[i] = WordinaryFrame.getListOfCategories().get(i - 1).getName();
	return temp;
    }
    
    private Word getNextWord(List<Word> wordList){
    	int count = 0;
    	while (count < wordList.size()){
    		int idx = new Random().nextInt(wordList.size());
    		Word temp = wordList.get(idx);
    		if ((category.equals("All categories") || temp.getCategory().equals(category)) &&
    			(wordType.equals("Any type") || (temp.getNumberOfTimesCorrect() < LEARNT_WORD && 
    			wordType.equals("Words not yet learnt")) || (temp.getNumberOfTimesCorrect() > LEARNT_WORD && 
    			wordType.equals("Only learnt words")))
    		)
    			return wordList.remove(idx);
    		count++;
    	}
    	return null;
    }
    
    public void addNotify(){
    	super.addNotify();
    	cmdBack.requestFocusInWindow();
    }
    
    private javax.swing.JComboBox cmbCategory;
    private javax.swing.JComboBox cmboQuestLang;
    private javax.swing.JButton cmdHint;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdTurnOver;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JComboBox cmbWordType;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblCard;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblHint;
    private javax.swing.JLabel lblQuestionLang;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblWordType;
    
}
