package ror.gui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class AlgStoreComboBox extends JComboBox {

    public AlgStoreComboBox() {
	super();
	String[] algStore = { "FIFO", "Par commande" };
	DefaultComboBoxModel algModel = new DefaultComboBoxModel(algStore);
	this.setModel(algModel);
	//TODO Faire en reflexif
    }
}
