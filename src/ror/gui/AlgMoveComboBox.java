package ror.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class AlgMoveComboBox extends JComboBox implements ActionListener {

    public AlgMoveComboBox() {
	super();
	String[] algStore = { "Automatique", "Economique", "Rapidité" };
	DefaultComboBoxModel algModel = new DefaultComboBoxModel(algStore);
	this.setModel(algModel);
	this.addActionListener(this);
	//TODO Faire en reflexif
    }

    public void actionPerformed(ActionEvent e) {
	if(this.getParent().getParent().getParent().getParent() instanceof RoRFrame)
	{
	    RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent();
	    frame.getUiController().setAlgMove(this.getSelectedIndex()); //TODO Faire en reflexif
	}
	else
	{
	    System.err.println("Can't get parent RoRFrame");
	}
    }
}
