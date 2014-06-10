/*
 * SettingsDialog.java
 *
 * Created on June 11, 2005, 11:25 AM
 */

package net.bruncle.wordinary;

/**
 *
 * @author  Jeremy
 */
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
 
public class SettingsDialog extends javax.swing.JDialog implements ActionListener, ChangeListener{
    
    public SettingsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    private void initComponents() {
        tabPane = new javax.swing.JTabbedPane();
        pnlSpellingSkinning = new javax.swing.JPanel();
        btnBackColour = new javax.swing.JButton();
        lblBackColour = new javax.swing.JLabel();
        lblBackColourLbl = new javax.swing.JLabel();
        lblTextColour = new javax.swing.JLabel();
        lblTextColourLbl = new javax.swing.JLabel();
        lblSkin = new javax.swing.JLabel();
        btnTextColour = new javax.swing.JButton();
        lblSkin1 = new javax.swing.JLabel();
        sldAccuracy = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        pnlSymbols = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        txtShortcutKey1 = new javax.swing.JTextField();
        txtShortcutKey2 = new javax.swing.JTextField();
        txtShortcutKey3 = new javax.swing.JTextField();
        txtShortcutKey4 = new javax.swing.JTextField();
        txtShortcutKey5 = new javax.swing.JTextField();
        txtShortcutKey6 = new javax.swing.JTextField();
        txtShortcutKey7 = new javax.swing.JTextField();
        txtShortcutKey8 = new javax.swing.JTextField();
        txtShortcutKey9 = new javax.swing.JTextField();
        txtShortcutKey0 = new javax.swing.JTextField();
        lblInstruction = new javax.swing.JLabel();
        lblInstr1 = new javax.swing.JLabel();
        lblInstr2 = new javax.swing.JLabel();
        lblInstr3 = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        
        useSynth = new javax.swing.JCheckBox("Use synthesiser?",WordinaryFrame.isSpeechEnabled());
   	btnChooseEngine = new javax.swing.JButton("Change speech engine");
        
        txtChangeTitle = new javax.swing.JTextField(WordinaryFrame.getTitle(),3);
        btnChangeTitle = new javax.swing.JButton("Change title");
        
        for (int i = 0; i < txtSymbol.length; i++){
        	//System.out.println("Loaded "+WordinaryFrame.getSymbols()[i]);
        	txtSymbol[i] = new javax.swing.JTextField(((char)WordinaryFrame.getSymbols()[i])+"");
        }
        lblInstr3 = new javax.swing.JLabel();
        lblInstr4 = new javax.swing.JLabel();
        
        pnlSaving = new javax.swing.JPanel();
        chkAlwaysSave = new javax.swing.JCheckBox("Always save?",true);
        btnAlwaysSaveHelp = new javax.swing.JButton("Help?");

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Wordinary - Settings");
        setName("dlgSettings");
        pnlSpellingSkinning.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnlSaving.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblBackColour.setBackground(WordinaryFrame.getBackColour());
        lblBackColour.setForeground(WordinaryFrame.getBackColour());
        lblBackColour.setOpaque(true);
        pnlSpellingSkinning.add(lblBackColour, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 50, 40));
        
        btnBackColour.setOpaque(false);
        pnlSpellingSkinning.add(btnBackColour, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 50, 40));
        btnBackColour.addActionListener(this);

        lblBackColourLbl.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 18));
        lblBackColourLbl.setText("Background Colour");
        pnlSpellingSkinning.add(lblBackColourLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 160, 40));

        lblTextColour.setBackground(WordinaryFrame.getTextColour());
        lblTextColour.setForeground(WordinaryFrame.getTextColour());
        lblTextColour.setOpaque(true);
        pnlSpellingSkinning.add(lblTextColour, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 50, 40));

        lblTextColourLbl.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 18));
        lblTextColourLbl.setText("Text Colour");
        pnlSpellingSkinning.add(lblTextColourLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, 150, 40));

        lblSkin.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 24));
        lblSkin.setText("Skin:");
        pnlSpellingSkinning.add(lblSkin, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 100, 30));

        btnTextColour.setOpaque(false);
        pnlSpellingSkinning.add(btnTextColour, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 50, 40));
        btnTextColour.addActionListener(this);

        lblSkin1.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 24));
        lblSkin1.setText("Spelling:");
        pnlSpellingSkinning.add(lblSkin1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 100, 30));

        sldAccuracy.setMajorTickSpacing(10);
        sldAccuracy.setMinorTickSpacing(5);
        sldAccuracy.setPaintLabels(true);
        sldAccuracy.setPaintTicks(true);
        sldAccuracy.setSnapToTicks(true);
        sldAccuracy.setValue(WordinaryFrame.getSpellingAccuracy());
        pnlSpellingSkinning.add(sldAccuracy, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));
        sldAccuracy.addChangeListener(this);

        jLabel1.setText("Required accuracy of spelling %");
        pnlSpellingSkinning.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 190, -1));

        tabPane.addTab("Spelling/GUI", pnlSpellingSkinning);

        pnlSymbols.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14));
        lblTitle.setText("Symbols:");
        pnlSymbols.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 80, 20));

        txtShortcutKey1.setText(""+((char)WordinaryFrame.getKeyShortcutSymbols()[0]));
        pnlSymbols.add(txtShortcutKey1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 20, -1));

        txtShortcutKey2.setText(""+((char)WordinaryFrame.getKeyShortcutSymbols()[1]));
        pnlSymbols.add(txtShortcutKey2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 20, -1));

        txtShortcutKey3.setText(""+((char)WordinaryFrame.getKeyShortcutSymbols()[2]));
        pnlSymbols.add(txtShortcutKey3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 20, -1));

        txtShortcutKey4.setText(""+((char)WordinaryFrame.getKeyShortcutSymbols()[3]));
        pnlSymbols.add(txtShortcutKey4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 20, -1));

        txtShortcutKey5.setText(""+((char)WordinaryFrame.getKeyShortcutSymbols()[4]));
        pnlSymbols.add(txtShortcutKey5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 20, -1));

        txtShortcutKey6.setText(""+((char)WordinaryFrame.getKeyShortcutSymbols()[5]));
        pnlSymbols.add(txtShortcutKey6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 20, -1));

        txtShortcutKey7.setText(""+((char)WordinaryFrame.getKeyShortcutSymbols()[6]));
        pnlSymbols.add(txtShortcutKey7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 20, -1));

        txtShortcutKey8.setText(""+((char)WordinaryFrame.getKeyShortcutSymbols()[7]));
        pnlSymbols.add(txtShortcutKey8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 20, -1));

        txtShortcutKey9.setText(""+((char)WordinaryFrame.getKeyShortcutSymbols()[8]));
        pnlSymbols.add(txtShortcutKey9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 20, -1));

        txtShortcutKey0.setText(""+((char)WordinaryFrame.getKeyShortcutSymbols()[9]));
        pnlSymbols.add(txtShortcutKey0, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 20, -1));

        lblInstruction.setText("Press alt + number/letter to insert the associated symbol");
        pnlSymbols.add(lblInstruction, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 250, 30));

        btnSave.setText("Save");
        pnlSymbols.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 240, -1));
        btnSave.addActionListener(this);
	
        lblInstr1.setText("Insert the unicode character code");
        pnlSymbols.add(lblInstr1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 170, 30));

        lblInstr2.setText("or the character itself in the text box");
        pnlSymbols.add(lblInstr2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 180, 30));
	
	lblInstr3.setText("and then press save when you're ");
        pnlSymbols.add(lblInstr3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 180, 30));

        lblInstr4.setText("<html>done. You can specify the key that you want to be used <br> as a shortcut for the symbol by <br>editing the characters in the boxes on the left hand side.</html>");
        pnlSymbols.add(lblInstr4, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 160, 90));
	
	int y = 30;
	for (int i = 0; i < txtSymbol.length; i++){
		pnlSymbols.add(txtSymbol[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(30, y, 70, -1));
		y += 20;
	}

        tabPane.addTab("Symbols", pnlSymbols);
        
        pnlSaving.add(chkAlwaysSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 30));
        pnlSaving.add(btnAlwaysSaveHelp, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 0, 80, 30));
        chkAlwaysSave.addActionListener(this);
        btnAlwaysSaveHelp.addActionListener(this);
        
        pnlSaving.add(txtChangeTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 150, 30));
        pnlSaving.add(btnChangeTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 150, 30));
        
        btnChangeTitle.addActionListener(this);
	
	pnlSaving.add(useSynth, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 150, 30));
	pnlSaving.add(btnChooseEngine, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, 90, 30));
        
        btnChooseEngine.addActionListener(this);
        useSynth.addActionListener(this);
        
        tabPane.addTab("Saving", pnlSaving);

        getContentPane().add(tabPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 340, 310));

        pack();
    }
       
    public void actionPerformed(ActionEvent e){
    	if (e.getSource() == btnBackColour){
    		java.awt.Color colour = javax.swing.JColorChooser.showDialog(
                     this,
                     "Choose Background Color",
                     WordinaryFrame.getBackColour());
    		WordinaryFrame.setBackgroundColour(colour);
                lblBackColour.setBackground(colour);
                WordinaryFrame.refreshScreen();
                WordinaryFrame.saveSettings();
        }           
        else if (e.getSource() == btnTextColour){
        	java.awt.Color colour = javax.swing.JColorChooser.showDialog(
                     this,
                     "Choose text Color",
                     WordinaryFrame.getTextColour());
        	WordinaryFrame.setTextColour(colour);
        	lblTextColour.setBackground(colour);
        	WordinaryFrame.refreshScreen();
        	WordinaryFrame.saveSettings();
        }
        else if (e.getSource() == btnSave)
        	saveSymbols();
        else if (e.getSource() == chkAlwaysSave)
        	WordinaryFrame.setAlwaysSave(!WordinaryFrame.getAlwaysSave());
        else if (e.getSource() == btnAlwaysSaveHelp)
        	javax.swing.JOptionPane.showMessageDialog((java.awt.Component)this,
                                     "If you check this box, then every time you do anything that affects your wordlist "+
                                     "\neg. add a word, your wordlist will be saved. The advantage of doing this is that "+
                                     "\nyou will never lose any changes you have made. The disadvantages are that: \n"+
                                     "\tif you have a large word list, it could take a long time to save it to a file, and you may experience some lag\n"+
                                     "\tit is sometimes not possible to undo any changes that you have made to your wordlist once they are saved to a file. \n",
                                     "What does this do?",
                                     javax.swing.JOptionPane.INFORMATION_MESSAGE);
        else if (e.getSource() == btnChangeTitle)
        	WordinaryFrame.changeTitle(txtChangeTitle.getText());
        else if (e.getSource() == useSynth){
        	try{
			Class temp = Class.forName("com.cloudgarden.speech.CGEngineProperties");
		}
		catch (ClassNotFoundException ex){
			useSynth.setSelected(false);
			JOptionPane.showMessageDialog(null, "You do not have cloudgarden speech sdk installed, so you can't enable speech");
		}	
        	WordinaryFrame.setSpeechEnabled(useSynth.isSelected());
        }
        else if (e.getSource() == btnChooseEngine){
        	try{
			Class temp = Class.forName("com.cloudgarden.speech.CGEngineProperties");
		}
		catch (ClassNotFoundException ex){
			JOptionPane.showMessageDialog(null, "You do not have cloudgarden speech sdk installed, so you can't enable speech");
			return;
		}
        	int response = JOptionPane.showConfirmDialog(null, "Click yes to modify the english voice, no to modify the other language's voice, and cancel to cancel");
            	if (response == JOptionPane.YES_OPTION){
        		if (WordinaryFrame.isSpeechEnabled()){
        			String name = WordinaryFrame.chooseSynthModeName();
        			WordinaryFrame.getEnglishSynthEngine().setEngine(name);
        			WordinaryFrame.setEnglishSynthName(name);
        		}
        		WordinaryFrame.writeWordsToFile();
        	}
        	else if (response == JOptionPane.NO_OPTION){
        		if (WordinaryFrame.isSpeechEnabled()){
        			String name = WordinaryFrame.chooseSynthModeName();
        			WordinaryFrame.getSynthEngine().setEngine(name);
        			WordinaryFrame.setOtherSynthName(name);
        		}
        		WordinaryFrame.writeWordsToFile();
        	}
        }
    } 
    
    private void saveSymbols(){
    	try{
    		for (int i = 0; i < txtSymbol.length; i++){		
	    		int code = txtSymbol[i].getText().charAt(0);
	    		if (Character.isDigit(code))
	    			code = Integer.parseInt(txtSymbol[i].getText());
	    		WordinaryFrame.getSymbols()[i] = code;
	    	}
	    	WordinaryFrame.getKeyShortcutSymbols()[0] = txtShortcutKey1.getText().charAt(0);
	    	WordinaryFrame.getKeyShortcutSymbols()[1] = txtShortcutKey2.getText().charAt(0);
	    	WordinaryFrame.getKeyShortcutSymbols()[2] = txtShortcutKey3.getText().charAt(0);
	    	WordinaryFrame.getKeyShortcutSymbols()[3] = txtShortcutKey4.getText().charAt(0);
	    	WordinaryFrame.getKeyShortcutSymbols()[4] = txtShortcutKey5.getText().charAt(0);
	    	WordinaryFrame.getKeyShortcutSymbols()[5] = txtShortcutKey6.getText().charAt(0);
	    	WordinaryFrame.getKeyShortcutSymbols()[6] = txtShortcutKey7.getText().charAt(0);
	    	WordinaryFrame.getKeyShortcutSymbols()[7] = txtShortcutKey8.getText().charAt(0);
	    	WordinaryFrame.getKeyShortcutSymbols()[8] = txtShortcutKey9.getText().charAt(0);
	    	WordinaryFrame.getKeyShortcutSymbols()[9] = txtShortcutKey0.getText().charAt(0);
	    	WordinaryFrame.writeWordsToFile();
	    	lblInstruction.setText("Symbols saved successfully");
	}
	catch (Exception e){
		e.printStackTrace();
		lblInstruction.setText("There was something wrong with the symbols you entered:|");
	}
    }
    		    
    public void stateChanged(ChangeEvent e) {
    	if (e.getSource() == sldAccuracy)
    		if (!sldAccuracy.getValueIsAdjusting()){
    			System.out.println("Spelling accuracy is now "+sldAccuracy.getValue()+"%");
    			WordinaryFrame.setSpellingAccuracy(sldAccuracy.getValue());
    			WordinaryFrame.saveSettings();
    		}
    }
    
    private javax.swing.JTextField txtChangeTitle;
    private javax.swing.JButton btnChangeTitle;
    			
    private javax.swing.JButton btnBackColour;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnTextColour;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSlider sldAccuracy;
    private javax.swing.JLabel lblBackColour;
    private javax.swing.JLabel lblBackColourLbl;
    private javax.swing.JLabel lblInstr1;
    private javax.swing.JLabel lblInstr2;
    private javax.swing.JLabel lblInstr3;
    private javax.swing.JLabel lblInstr4;
    private javax.swing.JLabel lblInstruction;
    private javax.swing.JLabel lblSkin;
    private javax.swing.JLabel lblSkin1;
    private javax.swing.JTextField txtShortcutKey0;
    private javax.swing.JTextField txtShortcutKey1;
    private javax.swing.JTextField txtShortcutKey2;
    private javax.swing.JTextField txtShortcutKey3;
    private javax.swing.JTextField txtShortcutKey4;
    private javax.swing.JTextField txtShortcutKey5;
    private javax.swing.JTextField txtShortcutKey6;
    private javax.swing.JTextField txtShortcutKey7;
    private javax.swing.JTextField txtShortcutKey8;
    private javax.swing.JTextField txtShortcutKey9;
    private javax.swing.JLabel lblTextColour;
    private javax.swing.JLabel lblTextColourLbl;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlSpellingSkinning;
    private javax.swing.JPanel pnlSymbols;
    private javax.swing.JPanel pnlSaving;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTextField[] txtSymbol = new javax.swing.JTextField[11];
    private javax.swing.JCheckBox chkAlwaysSave;
    private javax.swing.JButton btnAlwaysSaveHelp;
    private javax.swing.JCheckBox useSynth;
    private javax.swing.JButton btnChooseEngine;
    
}
