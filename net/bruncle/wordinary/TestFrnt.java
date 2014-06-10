/*
 * TestFrnt.java
 *
 * Created on May 3, 2005, 7:35 PM
 */

package net.bruncle.wordinary;

import java.awt.event.*;
import java.util.*;

/**
 *
 * @author  Jeremy
 */
public class TestFrnt extends javax.swing.JPanel implements ActionListener{
    
    public TestFrnt() {
        initComponents();
    }
    
    public void prepareForUse(){
    	initComponents();
    }
    
    private void initComponents() {
    	setPreferredSize(new java.awt.Dimension(350,280));
        java.awt.GridBagConstraints gridBagConstraints;

        btnStart = new javax.swing.JButton();
        
        String[] categories = getCategories();
        String[] languages = {"Any language","english","other language"};
        String[] correct = {"All words","Words not yet learnt","Revision"};
        
        cmboCategory = new javax.swing.JComboBox(categories);
        cmboLanguage = new javax.swing.JComboBox(languages);
        cmboCorrect = new javax.swing.JComboBox(correct);
        lblTitle = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        lblStopAfter = new javax.swing.JLabel();
        txtStopAfter = new javax.swing.JTextField();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setBackground(new java.awt.Color(117, 199, 83));
        btnStart.setText("Start");
        btnStart.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        add(btnStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 110, 30));
        btnStart.addActionListener(this);
        btnStart.setActionCommand("start");

        add(cmboCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 180, 20));

        add(cmboLanguage, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 180, 20));
        
        add(cmboCorrect, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 180, 20));
	
        lblTitle.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 24));
        lblTitle.setText("Test yourself");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 180, 50));
        
        lblStopAfter.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 12));
        lblStopAfter.setText("Stop after ? incorrect answers:");
        add(lblStopAfter, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, -1, -1));
        
        txtStopAfter.setText("-1");
        add(txtStopAfter, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 190, 30, -1));
        
        btnBack.setText("back");
        btnBack.setBorder(new javax.swing.border.MatteBorder(null));
        add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 50, 20));
        btnBack.addActionListener(this);
        btnBack.setActionCommand("back");

    }
    
    public void actionPerformed(ActionEvent e){
    	if (e.getActionCommand().equals("start")){
                if (isNumeric(txtStopAfter.getText()) != -3){        
                    WordinaryFrame.testPanel.startTest(
                            cmboLanguage.getSelectedItem().toString(),cmboCategory.getSelectedItem().toString(),
                            cmboCorrect.getSelectedItem().toString(), isNumeric(txtStopAfter.getText())
                    );
                    WordinaryFrame.changeScreen(WordinaryFrame.testPanel);
                }
                else{
                    javax.swing.JOptionPane.showMessageDialog(null, "The stop after field must be a number");
                    return;
                }
        }
	else
		WordinaryFrame.changeScreen(WordinaryFrame.frontScreen);
    }
    
    private String[] getCategories(){
    	List<Category> categories = WordinaryFrame.getListOfCategories();
    	String[] temp = new String[categories.size()+1];
    	for (int i = 1; i < (categories.size() +1); i++)
    		temp[i] = categories.get(i - 1).toString();
    	temp[0] = "Any category";
    	return temp;
    }
    
    public void addNotify(){
    	super.addNotify();
    	btnStart.requestFocusInWindow();
    }
    
    private static int isNumeric(String str){
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe){
            return -3;
        }
    }
    
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnStart;
    private javax.swing.JComboBox cmboCorrect;
    private javax.swing.JComboBox cmboCategory;
    private javax.swing.JComboBox cmboLanguage;
    private javax.swing.JLabel lblTitle;
    public javax.swing.JTextField txtStopAfter;
    private javax.swing.JLabel lblStopAfter;
}
