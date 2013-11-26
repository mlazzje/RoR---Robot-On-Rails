package ror.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import ror.core.Column;
import ror.core.Input;
import ror.core.Output;
import ror.core.Rail;
import ror.core.RoRElement;

public class RoRElementPanel extends JPanel {
    public RoRElementPanel(RoRElement element) {
	super();
	Dimension dim = new Dimension(16, 16);
	this.setSize(dim);
	this.setMaximumSize(dim);
	this.setMinimumSize(dim);
	this.setPreferredSize(dim);
	this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	if(element instanceof Rail)
	{
	    this.setBackground(Color.blue);
	}
	else if(element instanceof Output)
	{
	    this.setBackground(Color.yellow);
	}
	else if(element instanceof Input)
	{
	    this.setBackground(Color.green);
	}
	else if(element instanceof Column)
	{
	    this.setBackground(Color.red);
	}
	else
	{
	    this.setBackground(Color.white);
	}
    }
}
