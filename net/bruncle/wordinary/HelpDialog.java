/*
 * HelpDialog.java
 *
 * Created on June 14, 2005, 9:41 PM
 */

package net.bruncle.wordinary;

import java.util.*;
import java.awt.event.*;
/**
 *
 * @author  Jeremy
 */
public class HelpDialog extends javax.swing.JDialog {
    
    public HelpDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        resize(new java.awt.Dimension(600,500));
    }
    
    private void initComponents() {
        lblTitle = new javax.swing.JLabel();
        scrlTopics = new javax.swing.JScrollPane();
        lstTopics = new javax.swing.JList(getTitles());
        scrlShownTopic = new javax.swing.JScrollPane();
        txtpneShownTopic = new javax.swing.JTextPane();
        txtpneShownTopic.setEditable(false);
        
        MouseListener mouseListener = new MouseAdapter() {
	     public void mouseClicked(MouseEvent e) {
	         if (e.getClickCount() == 2) {
	             int index = lstTopics.locationToIndex(e.getPoint());
		     txtpneShownTopic.setText(WordinaryFrame.getHelpFiles().get(index).getDetails());	
	          }
	     }
	 };

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        lblTitle.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 24));
        lblTitle.setText("Help");
        getContentPane().add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 60, 30));
	
	lstTopics.addMouseListener(mouseListener);
        scrlTopics.setViewportView(lstTopics);

        getContentPane().add(scrlTopics, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 150, 400));
        
        scrlShownTopic.setViewportView(txtpneShownTopic);

        getContentPane().add(scrlShownTopic, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 430, 400));

        pack();
    }
    
    private Vector getTitles(){
    	Vector titles = new Vector();
    	for (HelpFile temp : WordinaryFrame.getHelpFiles())
    		titles.add(temp.getTitle());
    	return titles;
    }
    
    private javax.swing.JLabel lblTitle;
    private javax.swing.JList lstTopics;
    private javax.swing.JScrollPane scrlShownTopic;
    private javax.swing.JScrollPane scrlTopics;
    private javax.swing.JTextPane txtpneShownTopic;
    
}
