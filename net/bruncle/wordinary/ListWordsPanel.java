/*
 * ListWordsPanel.java
 *
 * Created on May 3, 2005, 8:22 PM
 */

package net.bruncle.wordinary;

import java.awt.event.*;
import java.util.*;
import javax.swing.event.*;
import javax.swing.table.TableModel;
import javax.swing.*;
/**
 *
 * @author  Jeremy
 */
public class ListWordsPanel extends javax.swing.JPanel implements ActionListener{
    
    public ListWordsPanel(){
    	words = WordinaryFrame.getWordList();
    	title = "Words in your wordinary:";
    	initComponents();
    }
    
    public void refresh(){
    	initComponents();
    }
    
    public void resetData(){
    	words = WordinaryFrame.getWordList();
    	title = "Words in your wordinary:";
    	initComponents();	
    }
    
    public void setData(List<Word> words, String title){
    	this.words = words;
    	this.title = title;
    	initComponents();
    }
    
    public void setData(List<Word> words) {
    	this.words = words;
    	title = "Words in your wordinary:";
        initComponents();
    }
    
    private void initComponents() {
    	removeAll();
    	setPreferredSize(new java.awt.Dimension(635,355));
        scrollList = new javax.swing.JScrollPane();
        
        tblResults = new javax.swing.JTable();
        
        lblTitle = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        btnAddWords = new javax.swing.JButton("Add words");
        btnSave = new javax.swing.JButton("Save words");
        btnEdit = new javax.swing.JButton("Edit");
        btnDel = new javax.swing.JButton("Delete");
        lblCategory = new javax.swing.JLabel("Only show this category: ");
        cmboCategory = new javax.swing.JComboBox(getCategoryData());
        btnSetCorrect = new javax.swing.JButton("Set correct");
        javax.swing.JLabel lblWords = new javax.swing.JLabel(words.size()+" words in your wordinary");
        
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setBackground(new java.awt.Color(117, 199, 83));
        tblResults.setModel(new javax.swing.table.DefaultTableModel(
            getData(),
            new String [] {
                "In English", "Other Language", "Category", "Number of times correct", "Hint"
            }
        ));
        scrollList.setViewportView(tblResults);

        add(scrollList, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 520, 200));

        lblTitle.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 18));
        lblTitle.setText(title);
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 220, 40));

        btnBack.setText("Back");
        add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, 80, 25));
	btnBack.addActionListener(this);
	btnBack.setActionCommand("back");
	
	add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 10, 60, 25));
	btnEdit.addActionListener(this);
	btnEdit.setActionCommand("edit");
	
	add(btnDel, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, 80, 25));
	btnDel.addActionListener(this);
	btnDel.setActionCommand("del");
	
	add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, 80, 25));
	btnSave.addActionListener(this);
	btnSave.setActionCommand("save");
	
	add(btnSetCorrect, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 70, -1, 25));
	btnSetCorrect.addActionListener(this);
	
	add(lblCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 180, 40));
	add(cmboCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 250, 120, 40));
	cmboCategory.addActionListener(this);
	cmboCategory.setActionCommand("catego");
	
	add(lblWords, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 250, 180, 40));
	add(btnAddWords, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 250, 80, 40));
	btnAddWords.addActionListener(this);
    }
    
    private void sort(List<Word> toSort){
    	Collections.sort(toSort);
    }
    
    private String[] getCategoryData(){
    	String[] temp = new String[WordinaryFrame.getListOfCategories().size() + 1];
    	temp[0] = "All categories";
    	for (int i = 1; i < WordinaryFrame.getListOfCategories().size() + 1; i++)
    		temp[i] = WordinaryFrame.getListOfCategories().get(i - 1).getName();
	return temp;
    }
        
    private String[][] getData(){
	sort(words);
	List<Word> copy = new ArrayList<Word>();
	for (int i = 0; i < words.size(); i++){
		if (cmboCategory.getSelectedIndex() == (0) || cmboCategory.getSelectedIndex() == (-1)){	
			//System.out.println("leaving "+words.get(i)+" in because any category was selected");
			copy.add(words.get(i));
		}
		else if (!words.get(i).getCategory().equalsIgnoreCase((String)cmboCategory.getSelectedItem())){
			//System.out.println("Removing "+words.get(i)+" because it is not part of category "+cmboCategory.getSelectedItem());
		}
		else{
			//System.out.println("Adding "+words.get(i)+" because it is part of the category");
			copy.add(words.get(i));
		}		
	}
	shownWords = copy;
    	String[][] temp = new String[copy.size()][6];
    	for (int i = 0; i < copy.size(); i++)
    		for (int j = 0; j < copy.get(i).toStrings().length; j++){
    			temp[i][j] = copy.get(i).toStrings()[j];
    		}
    	return temp;
    }
    
    private int getIndex(int row){
    	String english = (String)tblResults.getValueAt(row,0);
    	List<Word> mainWordList = WordinaryFrame.getWordList();
    	for (int i = 0; i < mainWordList.size(); i++)
    		if (mainWordList.get(i).getInEnglish().equals(english))
    			return i;
    	return -1;
    }
    
    public void actionPerformed(ActionEvent e){
    	if (e.getActionCommand().equals("back"))
    		WordinaryFrame.changeScreen(WordinaryFrame.frontScreen);
    	else if (e.getActionCommand().equals("del")){
    		WordinaryFrame.getWordList().remove(getIndex(tblResults.getSelectedRow()));
    		WordinaryFrame.writeWordsToFile();
    		tblResults.setModel(new javax.swing.table.DefaultTableModel(
	            getData(),
	            new String [] {
	                "In English", "Other Language", "Category", "Number of times correct"
	            }
	        ));
    	}
    	else if (e.getActionCommand().equals("save"))
    		WordinaryFrame.saveFile(shownWords,true);
    	else if (e.getActionCommand().equals("catego"))
    		tblResults.setModel(new javax.swing.table.DefaultTableModel(
	            getData(),
	            new String [] {
	                "In English", "Other Language", "Category", "Number of times correct", "Hint"
	            }
	        ));
    	else if (e.getActionCommand().equals("edit")){ //edit
    		if (tblResults.getSelectedRow() == -1)
    			return;
    		//System.out.println(""+tblResults.getSelectedRow());
    		WordinaryFrame.addWords.setEditMode(WordinaryFrame.getWordList().get(getIndex(tblResults.getSelectedRow())));
    		WordinaryFrame.changeScreen(WordinaryFrame.addWords);
    	}
    	if (e.getSource() == btnAddWords){
    		WordinaryFrame.addWordsToList(words);
    		cmboCategory.setModel(new javax.swing.DefaultComboBoxModel(getCategoryData()));
    		tblResults.setModel(new javax.swing.table.DefaultTableModel(
	            getData(),
	            new String [] {
	                "In English", "Other Language", "Category", "Number of times correct"
	            }
	        ));
	 }
	 else if (e.getSource() == btnSetCorrect){
	 	if (javax.swing.JOptionPane.showConfirmDialog(WordinaryFrame.getJFrame(), "Are you sure you want to change the score of every single word in this list?") != javax.swing.JOptionPane.YES_OPTION)
	 		return;
	 	int score = Integer.parseInt(javax.swing.JOptionPane.showInputDialog(WordinaryFrame.getJFrame(), "What do you want to set the words' correct count to?"));
	 	WordinaryFrame.resetScores(score, shownWords);
	 	tblResults.setModel(new javax.swing.table.DefaultTableModel(
	            getData(),
	            new String [] {
	                "In English", "Other Language", "Category", "Number of times correct"
	            }
	        ));
	 }
    }
    
    public void addNotify(){
    	super.addNotify();
    	btnBack.requestFocusInWindow();
    }
    
    private String title;
    private List<Word> words;
    private List<Word> shownWords = new ArrayList<Word>();
    
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnSetCorrect;
    private javax.swing.JButton btnAddWords;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnDel;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JScrollPane scrollList;
    private javax.swing.JTable tblResults;
    private javax.swing.JComboBox cmboCategory;
    private javax.swing.JLabel lblCategory;
    
}
