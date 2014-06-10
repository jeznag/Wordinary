/*
 * SearchPanel.java
 *
 * Created on May 3, 2005, 8:15 PM
 */

package net.bruncle.wordinary;

import java.awt.event.*; 
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableModel;
/**
 *
 * @author  Jeremy
 */
public class SearchPanel extends javax.swing.JPanel implements ActionListener, KeyListener, FocusListener{
    
    public SearchPanel() {
        initComponents();
    }
    
    public void prepareForUse(){
       	results = new ArrayList<Word>();
    	//removeAll();
    	//initComponents();
    	txtQuery.setText("");
    	cmboLanguage.setModel(new DefaultComboBoxModel(new String[] {"Either", "English", "Other language"}));
    	cmboCategory.setModel(new DefaultComboBoxModel(getCategories()));
    	tblResults.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {
            	{"search","for","a","word","0"}
            },
            new String [] {
                "In English", "Other Language", "Category", "Number of times correct","Hint"
            }
        ));
    }
    
    private void initComponents() {
    	setPreferredSize(new java.awt.Dimension(625,300));
        lblTitle = new javax.swing.JLabel();
        txtQuery = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton("Edit");
        cmboLanguage = new javax.swing.JComboBox(new String[] {"Either", "English", "Other language"});
        cmboCategory = new javax.swing.JComboBox(getCategories());
        scrollList = new javax.swing.JScrollPane();
        tblResults = new javax.swing.JTable();
        lblResults = new javax.swing.JLabel();
        btnSearch1 = new javax.swing.JButton();
        btnDel = new javax.swing.JButton("Delete");

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setBackground(new java.awt.Color(117, 199, 83));
        lblTitle.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 30));
        lblTitle.setText("Search for a word:");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, 30));

        txtQuery.setText("Type word here");
        txtQuery.addKeyListener(this);
        txtQuery.addFocusListener(this);
        txtQuery.setRequestFocusEnabled(true);
        add(txtQuery, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 150, 20));

        btnSearch.setText("Search");
        add(btnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 60, 80, 20));
        btnSearch.addActionListener(this);
        btnSearch.setActionCommand("search");
        
        add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 60, 80, 20));
        btnEdit.addActionListener(this);
        btnEdit.setActionCommand("edit");
        
        add(btnDel, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 60, 80, 20));
        btnDel.addActionListener(this);
        btnDel.setActionCommand("del");

        tblResults.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {
            	{"search","for","a","word",""}
            },
            new String [] {
                "In English", "Other Language", "Category", "Number of times correct","Hint"
            }
        ));
        scrollList.setViewportView(tblResults);

        add(scrollList, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 600, 100));

        lblResults.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14));
        lblResults.setText("Results:");
        add(lblResults, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 60, 50));

        btnSearch1.setText("Back");
        btnSearch1.setBorder(new javax.swing.border.MatteBorder(null));
        add(btnSearch1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 15, 60, 20));
	btnSearch1.addActionListener(this);
	btnSearch1.setActionCommand("back");
	
	add(cmboCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 15, 100, 20));
	cmboCategory.addActionListener(this);
	cmboCategory.setActionCommand("cat");
	
	add(cmboLanguage, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 15, 60, 20));
	cmboLanguage.addActionListener(this);
	cmboLanguage.setActionCommand("lang");
	
	final String enterPressed = "ENTER"; // modifier required!
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(enterPressed), enterPressed);
        this.getActionMap().put(enterPressed, new AbstractAction() {
            public void actionPerformed(ActionEvent ignored) {
            	if (!go)
    			return;
    		lastQuery = txtQuery.getText();
                tblResults.setModel(new javax.swing.table.DefaultTableModel(
	            getData(txtQuery.getText()),
	            new String [] {
	                "In English", "Other Language", "Category", "Number of times correct","Hint"
	            }
	        ));
            }
        });
    }
    
    public void focusGained(FocusEvent e){
    	if (e.getSource() == txtQuery){
    		txtQuery.setText("");
    	}
    }
    
    public void focusLost(FocusEvent e){
    }
    
    public void actionPerformed(ActionEvent e){
    	String action = e.getActionCommand();
    	//System.out.println(action);
    	if (action.equals("back"))
    		WordinaryFrame.changeScreen(WordinaryFrame.frontScreen);
    	else if (action.equals("edit")){
    		if (tblResults.getSelectedRow() > -1){
    			WordinaryFrame.addWords.setEditMode(results.get(tblResults.getSelectedRow()));
    			WordinaryFrame.changeScreen(WordinaryFrame.addWords);	
    		}
    			//WordinaryFrame.changeScreen(new AddWordPanel(WordinaryFrame.getWordList().get(findIndex(tblResults.getSelectedRow()))));
    	}
    	else if (action.equalsIgnoreCase("search")){
    		//System.out.println("Searching..");
    		lastQuery = txtQuery.getText();
    		tblResults.setModel(new javax.swing.table.DefaultTableModel(
	            getData(txtQuery.getText()),
	            new String [] {
	                "In English", "Other Language", "Category", "Number of times correct","Hint"
	            }
	        ));
	}
	else if (action.equalsIgnoreCase("del")){
		try{
			WordinaryFrame.getWordList().remove(findIndex(tblResults.getSelectedRow()));
		}
		catch (Exception ex){
			JOptionPane.showMessageDialog(null,"Unknown error occured, could not delete word");
			System.out.println("Couldn't delete a word from search panel");
		}
		tblResults.setModel(new javax.swing.table.DefaultTableModel(
	            getData(txtQuery.getText()),
	            new String [] {
	                "In English", "Other Language", "Category", "Number of times correct","Hint"
	            }
	        ));
	        WordinaryFrame.writeWordsToFile();
	}
	else if (action.equals("cat") || action.equals("lang"))
		tblResults.setModel(new javax.swing.table.DefaultTableModel(
	            getData(lastQuery),
	            new String [] {
	                "In English", "Other Language", "Category", "Number of times correct","Hint"
	            }
	        ));
    }	     	
    
    private int findIndex(int row){
    	List<Word> words = WordinaryFrame.getWordList();
    	int ID = results.get(tblResults.getSelectedRow()).getID();
    	for (int i = 0; i < words.size(); i++)
    		if (words.get(i).getID() == ID)
    			return i;
    	return -1;
    }
    
    private Object[][] getData(String query){
    	query = query.toLowerCase();
    	results = new ArrayList<Word>();
    	List<Word> words = WordinaryFrame.getWordList();
    	Object[][] temp = new Object[words.size()][6];
    	int idx = 0;
    	for (int i = 0; i < words.size(); i++){
    		Word word = words.get(i);
    		if (!(word.getCategory().equals(cmboCategory.getSelectedItem()) || "Any category".equals(cmboCategory.getSelectedItem())))
    			continue;
    		String inEnglish = word.getInEnglish().toLowerCase();
    		String inOtherLanguage = word.getInOtherLanguage().toLowerCase();
    		if ((inEnglish.contains(query) && (cmboLanguage.getSelectedItem().equals("Either") || cmboLanguage.getSelectedItem().equals("English"))) ||
    			(inOtherLanguage.contains(query) && (cmboLanguage.getSelectedItem().equals("Either") || cmboLanguage.getSelectedItem().equals("Other language")))){
    				String[] subdata = words.get(i).toStrings();
    				results.add(word);
		    		for (int j = 0; j < subdata.length; j++)
		    			temp[idx][j] = subdata[j];
		    		idx++;
	    	}
    	}
    	return temp;
    }
    
    private String[] getCategories(){
    	List<Category> categories = WordinaryFrame.getListOfCategories();
    	String[] temp = new String[categories.size()+1];
    	for (int i = 1; i < (categories.size() +1); i++)
    		temp[i] = categories.get(i - 1).toString();
    	temp[0] = "Any category";
    	return temp;
    }
    
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
	char c = e.getKeyChar();
	int i = (int)c;
	int modifiers = e.getModifiersEx();
	if (modifiers == KeyEvent.ALT_DOWN_MASK){ //ctrl
		if (e.getKeyCode() == KeyEvent.VK_C)
			txtQuery.copy();
		for (int j = 0; j < WordinaryFrame.getKeyShortcutSymbols().length; j++)
			if (i == WordinaryFrame.getKeyShortcutSymbols()[j])
				txtQuery.setText(((JTextField)(e.getSource())).getText()+((char)WordinaryFrame.getSymbols()[j]));
	}
    }

    /** Handle the key released event from the text field. */
    public void keyReleased(KeyEvent e) {
    }
    
    public void addNotify(){
    	super.addNotify();
    	txtQuery.requestFocusInWindow();
    }
    
    private boolean go = true;
    
    private List<Word> results = new ArrayList<Word>();
    String lastQuery;
    
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSearch1;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnDel;
    private javax.swing.JComboBox cmboLanguage;
    private javax.swing.JComboBox cmboCategory;
    private javax.swing.JLabel lblResults;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JScrollPane scrollList;
    private javax.swing.JTable tblResults;
    private javax.swing.JTextField txtQuery;
    
}
