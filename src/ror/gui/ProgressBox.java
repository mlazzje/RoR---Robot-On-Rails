package ror.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

public class ProgressBox extends JDialog {

    private static final long serialVersionUID = 1L;
    private JProgressBar progressBar;

    public ProgressBox(RoRFrame rorFrame) {
	super(rorFrame,"Calcul en cours");
	this.setLayout(new BorderLayout());
	this.setAlwaysOnTop(true);
	this.setResizable(false);
	this.setPreferredSize(new Dimension(500, 60));
	progressBar = new JProgressBar(0, 100);
	progressBar.setValue(0);
	progressBar.setStringPainted(true);
	progressBar.setPreferredSize(new Dimension(500, 40));
	JPanel panel = new JPanel();
	panel.add(progressBar);
	this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	add(panel, BorderLayout.PAGE_START);
	rorFrame.setEnabled(false);
	this.pack();
	this.setLocationRelativeTo(null);
	this.repaint();
	this.setVisible(true);
    }

    public void setProgressValue(int value) {
	this.progressBar.setValue(value);
    }
}
