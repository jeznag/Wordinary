/*
 * CategoriesForm.java
 *
 * Created on May 3, 2005, 8:01 PM
 */

package net.bruncle.wordinary;

/**
 *
 * @author  Jeremy
 */

import java.awt.event.*; 
import java.util.*;

public class CategoriesForm extends javax.swing.JPanel implements ActionListener{
    
    public CategoriesForm() {
        initComponents();
    }
    
    public void prepareForUse(){
    	initComponents();
    }
    
    private void initComponents() {
    	removeAll();
        setPreferredSize(new java.awt.Dimension(500,300));
        lblTitle = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        txtNewName = new javax.swing.JTextField();
        lblNewName = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setBackground(new java.awt.Color(117, 199, 83));
        lblTitle.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 24));
        lblTitle.setText("Edit Categories:");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 190, 50));
	lstCategories = new javax.swing.JList(getCategories());
	scrollPaneForList = new javax.swing.JScrollPane(lstCategories);
	
	MouseListener mouseListener = new MouseAdapter() {
	     public void mouseClicked(MouseEvent e) {
	         if (e.getClickCount() == 1) {
	             int index = lstCategories.locationToIndex(e.getPoint());
	             if (index == 0)
	             	return;
		     txtNewName.setText(WordinaryFrame.getListOfCategories().get(index-1).toString());
	          }
	     }
	};
	
	lstCategories.addMouseListener(mouseListener);
	
        add(scrollPaneForList, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 350, 140));

        btnDelete.setText("Delete");
        add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, -1));
        btnDelete.addActionListener(this);
        btnDelete.setActionCommand("del");

        add(txtNewName, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 210, 70, 20));

        lblNewName.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14));
        lblNewName.setText("New name:");
        add(lblNewName, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, 80, -1));

        btnEdit.setText("Edit");
        add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 210, -1, -1));
        btnEdit.addActionListener(this);
        btnEdit.setActionCommand("edit");

        btnAdd.setText("Add");
        add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 210, -1, -1));
        btnAdd.addActionListener(this);
        btnAdd.setActionCommand("add");

        btnBack.setText("back");
        btnBack.setBorder(new javax.swing.border.MatteBorder(null));
        add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 0, 50, 20));
        btnBack.addActionListener(this);
        btnBack.setActionCommand("back");

    }
    
    public void actionPerformed(ActionEvent e){
    	if (!go)
    		return;
    	if (e.getActionCommand().equals("back"))
    		WordinaryFrame.changeScreen(WordinaryFrame.frontScreen);
    	else if (e.getActionCommand().equals("add")){
    		if (!WordinaryFrame.addCategory(new Category(txtNewName.getText()),true)){
    			lblTitle.setText("This category already exists");
    			return;
    		}
    		else{
    			lblTitle.setText("Category added");
    			lstCategories.setListData(getCategories());
    			WordinaryFrame.addWords.prepareForUse();
    			WordinaryFrame.listWordsPanel.resetData();
    			WordinaryFrame.searchPanel.prepareForUse();
    			WordinaryFrame.testFront.prepareForUse();
    			return;
    		}
    	}
    	else if (e.getActionCommand().equals("edit")){
    		WordinaryFrame.getListOfCategories().set(lstCategories.getSelectedIndex() - 1, new Category(txtNewName.getText()));
    		lblTitle.setText("Category edited");
    		lstCategories.setListData(getCategories());
    		WordinaryFrame.writeWordsToFile();
    		return;
    	}
    	if (lstCategories.getSelectedValue() == null){
    		lblTitle.setText("You must select a category first before you can do anything to it");
    		return;
    	}
    	if (e.getActionCommand().equals("del")){
    		if (!WordinaryFrame.deleteCategory(new Category(lstCategories.getSelectedValue().toString()))){
    			lblTitle.setText("Couldn't delete the category for some reason");
    			return;
    		}
    		else{
    			lblTitle.setText("Category deleted");
    			lstCategories.setListData(getCategories());
    			WordinaryFrame.writeWordsToFile();
    			return;
    		}
    	}
    }
    
    private Object[] getCategories(){
    	List<Category> categories = WordinaryFrame.getListOfCategories();
    	//System.out.println("categories == "+categories.size()+" units large");
    	Object[] temp = new String[categories.size()+1];
    	for (int i = 1; i < (categories.size() + 1); i++){
    		temp[i] = categories.get(i - 1).getName();
    		//System.out.println("Adding "+temp[i]);
    	}
    	temp[0] = "Choose a category to edit or create a new one";
    	return temp;
    }
    
    public void addNotify(){
    	super.addNotify();
    	btnBack.requestFocusInWindow();
    }    
    
    private boolean go = true;
    
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JLabel lblNewName;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JList lstCategories;
    private javax.swing.JScrollPane scrollPaneForList;
    private javax.swing.JTextField txtNewName;
    
}
