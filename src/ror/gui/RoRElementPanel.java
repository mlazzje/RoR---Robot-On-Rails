package ror.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class RoRElementPanel extends JPanel {
    public RoRElementPanel() {
	super();
	Dimension dim = new Dimension(16, 16);
	this.setSize(dim);
	this.setMaximumSize(dim);
	this.setMinimumSize(dim);
	this.setPreferredSize(dim);
	this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }
}
