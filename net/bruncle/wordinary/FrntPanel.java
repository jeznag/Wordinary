/*
 * FrntPanel.java
 *
 * Created on May 3, 2005, 6:45 PM
 */

package net.bruncle.wordinary;

import java.awt.event.*;
/**
 *
 * @author  Jeremy
 */
public class FrntPanel extends javax.swing.JPanel implements ActionListener{
    
    public FrntPanel() {
        initComponents();
    }
    
    public void prepareForUse(){
    	initComponents();
    }
    
    private void initComponents() {
    	setPreferredSize(new java.awt.Dimension(345,225));
        java.awt.GridBagConstraints gridBagConstraints;

        lblTitle = new javax.swing.JLabel();
        btnTest = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnFind = new javax.swing.JButton();
        btnList = new javax.swing.JButton();
        btnCat = new javax.swing.JButton();
        btnLearn = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        //setBackground(new java.awt.Color(117, 199, 83));
        lblTitle.setBackground(new java.awt.Color(255, 204, 0));
        lblTitle.setFont(new java.awt.Font("Century", 0, 36));
        lblTitle.setText("Wordinary");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 189, -1));

        //btnTest.setBackground(WordinaryFrame.getBackColour());
        btnTest.setText("Test");
        btnTest.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        add(btnTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 40, 30));
        btnTest.addActionListener(this);
        btnTest.setActionCommand("test");

        //btnAdd.setBackground(WordinaryFrame.getBackColour());
        btnAdd.setText("Add words");
        btnAdd.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 90, 30));
        btnAdd.addActionListener(this);
        btnAdd.setActionCommand("add");

        //btnFind.setBackground(WordinaryFrame.getBackColour());
        btnFind.setText("Find word");
        btnFind.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        add(btnFind, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, 70, 30));
        btnFind.addActionListener(this);
        btnFind.setActionCommand("find");

        //btnList.setBackground(WordinaryFrame.getBackColour());
        btnList.setText("List words");
        btnList.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        add(btnList, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, 80, 30));
        btnList.addActionListener(this);
        btnList.setActionCommand("list");

        //btnCat.setBackground(WordinaryFrame.getBackColour());
        btnCat.setText("Edit categories");
        btnCat.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        add(btnCat, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, 90, 30));
	btnCat.addActionListener(this);
        btnCat.setActionCommand("cat");
        
        btnLearn.setText("Learn words");
        btnLearn.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        add(btnLearn, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 100, 80, 30));
        btnLearn.addActionListener(this);
        btnLearn.setActionCommand("learn");
    }
    
    public void actionPerformed(ActionEvent e){
    	if (e.getActionCommand().equals("test"))
    		WordinaryFrame.changeScreen(WordinaryFrame.testFront);
    	else if (e.getActionCommand().equals("add")){
    		WordinaryFrame.addWords.prepareForUse();
    		WordinaryFrame.changeScreen(WordinaryFrame.addWords);
    	}
    	else if (e.getActionCommand().equals("find")){
    		WordinaryFrame.searchPanel.prepareForUse();
    		WordinaryFrame.changeScreen(WordinaryFrame.searchPanel);
        }      
    	else if (e.getActionCommand().equals("list")){
    		WordinaryFrame.listWordsPanel.resetData();
    		WordinaryFrame.changeScreen(WordinaryFrame.listWordsPanel);
    	}
    	else if (e.getActionCommand().equals("cat"))
    		WordinaryFrame.changeScreen(WordinaryFrame.categoryForm);
    	else if (e.getActionCommand().equals("learn")){
    		WordinaryFrame.learnPanel.startCardShow(WordinaryFrame.getWordList());
		WordinaryFrame.changeScreen(WordinaryFrame.learnPanel);
	}
    }
    
    public void addNotify(){
    	super.addNotify();
    	btnTest.requestFocusInWindow();
    }
    
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCat;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnList;
    private javax.swing.JButton btnTest;
    private javax.swing.JButton btnLearn;
    private javax.swing.JLabel lblTitle;
    
}
