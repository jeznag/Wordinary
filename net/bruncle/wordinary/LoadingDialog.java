/*
 * LoadingDialog.java
 *
 * Created on July 3, 2005, 9:39 PM
 */

package net.bruncle.wordinary;

/**
 *
 * @author  Jeremy
 */
public class LoadingDialog extends javax.swing.JDialog {
    
    /** Creates new form LoadingDialog */
    String task;
    int length;
    public LoadingDialog(java.awt.Frame parent, boolean modal, String task, int length) {
        super(parent, "Loading..", modal);
        this.task = task;
        this.length = length;
        initComponents();
    }
    
    private void initComponents() {
        lblTitle = new javax.swing.JLabel();
        lblAuthor = new javax.swing.JLabel();
        prgProgress = new javax.swing.JProgressBar(0,length);
        prgProgress.setStringPainted(true);

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Wordinary");
        setAlwaysOnTop(true);
        setModal(true);

        lblTitle.setFont(new java.awt.Font("Century Gothic", 0, 60));
        lblTitle.setText("Wordinary");
        getContentPane().add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 330, -1));

        lblAuthor.setFont(new java.awt.Font("Batang", 0, 30));
        lblAuthor.setText("by Jeremy Nagel");
        getContentPane().add(lblAuthor, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 300, 50));

        getContentPane().add(prgProgress, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 250, 30));

        pack();
    }
    
    public void setVisible(boolean vis){
    	super.setVisible(vis);
    }
    
    public void show(){
    	try{
    		super.show();	
    	}
    	catch (NullPointerException e){
    		System.out.println("DAFsadFDSAF");
    		e.printStackTrace();
    	}
    }
    
    public void updateProgress(int complete){
    	try{
	    	prgProgress.setValue(complete);
                this.complete = complete;
	    	prgProgress.setString("Loading "+task+": "+((int)(prgProgress.getPercentComplete()*100))+"% complete");
	    	prgProgress.update(prgProgress.getGraphics());
    	}
    	catch (Exception e){ /*e.printStackTrace();*/ }
    }
    
    public void incrementProgress(){
        updateProgress(++complete);
    }
    
    public void newTask(String task, int length){
    	try{
    		this.task = task;
    		prgProgress.setValue(0);
    		prgProgress.setMaximum(length);
	    	prgProgress.setString("Loading "+task+": "+(prgProgress.getPercentComplete()*100)+"%");
	    	prgProgress.update(prgProgress.getGraphics());
	 }
	 catch (Exception e){ /*e.printStackTrace(); */}
    }
    
    public boolean loaded;
    private int complete;
    
    private javax.swing.JLabel lblAuthor;
    private javax.swing.JLabel lblProgress;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JProgressBar prgProgress;
    
}
