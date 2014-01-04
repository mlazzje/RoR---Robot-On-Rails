package ror.gui;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBox extends JDialog {

	private static final long serialVersionUID = 1L;
	private JProgressBar progressBar;

	public ProgressBox(RoRFrame rorFrame) {
		super(rorFrame);
		this.setLayout(new BorderLayout());
		this.setTitle("Calcul en cours");
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		JPanel panel = new JPanel();
		panel.add(progressBar);

		add(panel, BorderLayout.PAGE_START);
		this.pack();
		this.repaint();
		this.setVisible(true);
	}

	public void setProgressValue(int value) {
		this.progressBar.setValue(value);
	}
}
