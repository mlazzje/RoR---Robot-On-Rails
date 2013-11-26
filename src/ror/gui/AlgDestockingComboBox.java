package ror.gui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class AlgDestockingComboBox extends JComboBox {

    public AlgDestockingComboBox() {
	super();
	String[] algStore = { "FIFO", "Par commande" };
	DefaultComboBoxModel algModel = new DefaultComboBoxModel(algStore);
	this.setModel(algModel);
	//TODO Faire en reflexif
    }
}
