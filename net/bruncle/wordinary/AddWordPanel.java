/*
 * AddWordPanel.java
 *
 * Created on May 3, 2005, 7:54 PM
 */

package net.bruncle.wordinary;

/**
 *
 * @author  Jeremy
 */
import java.awt.event.*; 
import java.util.*;
import javax.swing.*;
 
public class AddWordPanel extends javax.swing.JPanel implements ActionListener, KeyListener, FocusListener, Runnable{
    
    public AddWordPanel() {
    	go = true;
    	Thread testThread = new Thread(this,"keyThread");
    	testThread.start();
    	
    	final String enterPressed = "ENTER"; // modifier required!
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(enterPressed), enterPressed);
        this.getActionMap().put(enterPressed, new AbstractAction() {
            public void actionPerformed(ActionEvent ignored) {
            	if (go)
                	addWord();
            }
        });
    	removeAll();
        initComponents();
    }
    
    public void prepareForUse(){
    	edit = false;
    	go = true;
    	change = false;
    	lblTitle.setText("Add a word to the wordinary");
    	cmboCategories.setModel(new DefaultComboBoxModel(getCategories()));
    	id = -1;
        removeAll();
        initComponents();
    }
    
    public void setEditMode(Word toEdit){
    	go = true;
    	removeAll();
    	edit = true;
    	initComponents();
    	txtEnglishMeaning.setText(toEdit.getInEnglish());
    	txtOtherLanguageMeaning.setText(toEdit.getInOtherLanguage());
    	txtHint.setText(toEdit.getHint());
    	cmboCategories.setSelectedItem(toEdit.getCategory());
    	id = toEdit.getID();
    	correct = toEdit.getNumberOfTimesCorrect();
    }
    
    private void initComponents() {
         
        setPreferredSize(new java.awt.Dimension(500,300));
        javax.swing.JLabel lblEnglishMeaning;
	
	lblHint = new JLabel("Type in a hint for this word; eg. a sentence including it");
	txtHint = new JTextField();
	
        lblTitle = new javax.swing.JLabel();
        txtEnglishMeaning = new javax.swing.JTextField();
        lblEnglishMeaning = new javax.swing.JLabel();
        txtOtherLanguageMeaning = new javax.swing.JTextField();
        lblOtherLanguageMeaning = new javax.swing.JLabel();
        cmboCategories = new javax.swing.JComboBox(getCategories());
        lblCategories = new javax.swing.JLabel();
        btnEditCategories = new javax.swing.JButton();
        btnDone = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setBackground(new java.awt.Color(117, 199, 83));
        lblTitle.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14));
        lblTitle.setText("Add a word to the wordinary");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 400, 40));

        add(txtEnglishMeaning, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, 240, -1));
        txtEnglishMeaning.addFocusListener(this);
        txtEnglishMeaning.addKeyListener(this);
        txtEnglishMeaning.setRequestFocusEnabled(true);

        lblEnglishMeaning.setText("English meaning:");
        add(lblEnglishMeaning, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 120, 20));

        add(txtOtherLanguageMeaning, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 90, 220, -1));
	txtOtherLanguageMeaning.addKeyListener(this);
	
        lblOtherLanguageMeaning.setText("Meaning in other language:");
        add(lblOtherLanguageMeaning, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 180, 20));
	
	add(lblHint, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, 20));
	add(txtHint, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 400, 20));
	txtHint.addKeyListener(this);
	
        add(cmboCategories, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 180, 120, -1));

        lblCategories.setText("Category: ");
        add(lblCategories, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 60, 20));

        btnEditCategories.setText("edit categories");
        add(btnEditCategories, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 180, 80, -1));
        btnEditCategories.addActionListener(this);
        btnEditCategories.setActionCommand("cat");

        btnDone.setText("DONE");
        add(btnDone, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, -1, -1));
        btnDone.addActionListener(this);
        btnDone.setActionCommand("done");

        btnBack.setText("back");
        btnBack.setBorder(new javax.swing.border.MatteBorder(null));
        add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 0, 50, 20));
	btnBack.addActionListener(this);
        btnBack.setActionCommand("back");
        txtEnglishMeaning.setRequestFocusEnabled(true);
    }
    
    public void focusGained(FocusEvent e){
    }
    
    public void focusLost(FocusEvent e){
    
    }
    
    private void addWord(){
	String english = getQuotedText(txtEnglishMeaning.getText());
	if (english == null){
		lblTitle.setText("You didn't format the \"s correctly..");
		return;
	}
	String other = getQuotedText(txtOtherLanguageMeaning.getText());
	if (other == null){ //There weren't an even number of quotation marks
		lblTitle.setText("You didn't format the \"s correctly..");
		return;
	}
	if (english.length() <= 2 || other.length() <= 2 ){
		if (!(JOptionPane.showConfirmDialog(null, "Do you really want to add this word? It's pretty short") == JOptionPane.YES_OPTION)){
                	lblTitle.setText("Word was too short, probable error");
                        return;
                }                    
	}
	String hint = txtHint.getText();
	String category = (String)cmboCategories.getSelectedItem();
	if (category.equals("Any category")){
		category = "misc";
		if (contains(cmboCategories, "misc") == -1){
			WordinaryFrame.addCategory(new Category("misc"), true);
			cmboCategories.setModel(new javax.swing.DefaultComboBoxModel(getCategories()));
		}
	}
	Word temp = new Word(english, other, category, correct);
	temp.setID(id); //set the id of the word to the id of the word that was passed to the screen, or if there was no
		//word passed, set it to -1: wordinaryframe will give it a real id.
	temp.setHint(hint);
	int response = WordinaryFrame.addWord(temp); //1 == edited 0 == added 2 == cancelled
	if (response == 1)
		lblTitle.setText(txtEnglishMeaning.getText()+"'s definition changed");
	else if (response == 0)
		lblTitle.setText("\""+txtEnglishMeaning.getText()+"\" has been added to your wordinary");
	else if (response == 2)
		lblTitle.setText("Nothing happened");
		
	correct = 0; //Number of times the word that was passed to the screen was correct
	id = -1; //id of word that was passed to the screen
	txtEnglishMeaning.setText("");
	txtOtherLanguageMeaning.setText("");
	txtHint.setText("");
	txtEnglishMeaning.requestFocus();
    }
    
    public void actionPerformed(ActionEvent e){
    	if (!go)
    		return;
    	if (e.getActionCommand().equals("back"))
    		WordinaryFrame.changeScreen(WordinaryFrame.frontScreen);
    	else if (e.getActionCommand().equals("cat"))
    		WordinaryFrame.changeScreen(WordinaryFrame.categoryForm);
    	else{
    		addWord();
    	}
    }
    
    public static String getQuotedText(String original){
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
    		//System.out.println("odd number of \"s");
    		return null;
    	}	
    	//System.out.println("Correct number of \"s : "+(matches/2));
    	ArrayList<String> result = new ArrayList<String>();
    	for (int i = 0; i < matches; i+= 2){
    		String temp = original.substring(positions.get(i) + 1, positions.get(i+1));
    		temp = temp.replace(" ","-");
    		result.add(temp);
    		//System.out.println(i+": "+temp);
    	}
    	String resultString = original.substring(0,original.indexOf("\""));
    	for (int i = 0; i < result.size(); i++){
    		String space = "";
    		if (i < (result.size() - 1))
    			space = " ";
    		resultString += result.get(i)+space;
    	}
    	if (original.lastIndexOf("\"") < original.length() - 1)
    		resultString+= original.substring(original.lastIndexOf("\"") + 1, original.length());
    	return resultString;
    }
    		
    private String[] getCategories(){
    	List<Category> categories = WordinaryFrame.getListOfCategories();
    	String[] temp = new String[categories.size()+1];
    	for (int i = 1; i < (categories.size() + 1); i++)
    		temp[i] = categories.get(i - 1).getName();
    	temp[0] = "Any category";
    	return temp;
    }
    
    public void keyTyped(KeyEvent e) {
    	if (!go){
                return;
        }
    	if (!change && e.getKeyChar() == '\"'){
    		keyv = e;
		change = true;
    	}
    	if (e.getSource() == txtOtherLanguageMeaning && (txtOtherLanguageMeaning.getText().indexOf("der ") > -1 || txtOtherLanguageMeaning.getText().indexOf("die ") > -1  ||
    			txtOtherLanguageMeaning.getText().indexOf("das ") > -1 || txtOtherLanguageMeaning.getText().indexOf("\"der ") > -1 || txtOtherLanguageMeaning.getText().indexOf("\"die ") > -1  ||
    			txtOtherLanguageMeaning.getText().indexOf("\"das ") > -1  && contains(cmboCategories,"nouns") != -1)){
    		cmboCategories.setSelectedIndex(contains(cmboCategories,"nouns"));
    	}
    	else if (e.getSource() == txtEnglishMeaning && txtEnglishMeaning.getText().indexOf("to ") > -1 && contains(cmboCategories,"verbs") != -1){
    		cmboCategories.setSelectedIndex(contains(cmboCategories,"verbs"));
    	}
    	else if (((JTextField)e.getSource()).getText().indexOf("(adj") > -1 && contains(cmboCategories,"adjectives") != -1){
    		cmboCategories.setSelectedIndex(contains(cmboCategories,"adjectives"));
    	}
    	else if (((JTextField)e.getSource()).getText().indexOf("lich") > -1 && contains(cmboCategories,"adjectives") != -1){
    		cmboCategories.setSelectedIndex(contains(cmboCategories,"adjectives"));
    	}
    }
    
    private int contains (JComboBox cmbo, String entry){
    	for (int i = 0; i < cmbo.getItemCount(); i++){
    		Object obj = cmbo.getItemAt(i);
    		if (((String)obj).equalsIgnoreCase(entry)){
    			return i;
    		}
    	}
    	return -1;
    }
    
    public void keyPressed(KeyEvent e) {
    	if (!go)
    		return;
	char c = e.getKeyChar();
	int i = (int)c;
	int modifiers = e.getModifiersEx();
	if (modifiers == KeyEvent.ALT_DOWN_MASK){ //ctrl
		for (int j = 0; j < WordinaryFrame.getKeyShortcutSymbols().length; j++)
			if (i == WordinaryFrame.getKeyShortcutSymbols()[j]){
				String txt = ((JTextField)(e.getSource())).getText();
				int caret = (((JTextField)e.getSource()).getCaretPosition());
				txt = (txt.substring(0,((JTextField)e.getSource()).getCaretPosition())) + ((char)WordinaryFrame.getSymbols()[j]) + (txt.substring(((JTextField)e.getSource()).getCaretPosition()));
				((JTextField)(e.getSource())).setText(txt);
				((JTextField)(e.getSource())).setCaretPosition(caret+1);
			}
	}
    }

    /** Handle the key released event from the text field. */
    public void keyReleased(KeyEvent e) {
    }
    
    public void run(){
    	while (true){
    		if (!go){
    			try{
		    		Thread.sleep(20);
		    	}
		    	catch (InterruptedException e){}
		    	continue;
		}
	    	if (change){
	    		change = false;
	    		((JTextField)keyv.getSource()).setText(((JTextField)keyv.getSource()).getText()+"\"");
	    		((JTextField)keyv.getSource()).setCaretPosition(((JTextField)keyv.getSource()).getText().length()-1);
	    	}
	        try{
	    		Thread.sleep(20);
	    	}
	    	catch (InterruptedException e){}
	}
    }
    
    public void removeNotify(){
    	go = false;
    }
    
    public void addNotify(){
    	super.addNotify();
    	go = true;
    	txtEnglishMeaning.requestFocusInWindow();
    }
    
    private boolean go = true; //Toggle for the thread
    private KeyEvent keyv;     
    private boolean change; //When the user presses quotation marks it is triggered - seperate thread will put matching quote at the end of the text
    private int id = -1; //id of the word that is being edited (if there is one)
    private int correct; //number of times the word that is being edited (if there is one) has been answered correctly
    private boolean edit; //is a word being edited?
    
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDone;
    private javax.swing.JButton btnEditCategories;
    private javax.swing.JComboBox cmboCategories;
    private javax.swing.JLabel lblCategories;
    private javax.swing.JLabel lblOtherLanguageMeaning;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblHint;
    private javax.swing.JTextField txtEnglishMeaning;
    private javax.swing.JTextField txtOtherLanguageMeaning;
    private javax.swing.JTextField txtHint;
    
}
