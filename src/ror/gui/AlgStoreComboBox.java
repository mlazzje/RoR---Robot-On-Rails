package ror.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * @author RoR
 * 
 * Class AlgStoreComboBox
 *
 */
@SuppressWarnings("rawtypes")
public class AlgStoreComboBox extends JComboBox implements ActionListener {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor AlgStoreComboBox
	 */
	@SuppressWarnings("unchecked")
	public AlgStoreComboBox() {
		super();
		String[] algStore = { "FIFO", "Par commande" };
		DefaultComboBoxModel algModel = new DefaultComboBoxModel(algStore);
		this.setModel(algModel);
		this.setSelectedIndex(0);
		this.addActionListener(this);
		// TODO Faire en reflexif
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComboBox#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (this.getParent().getParent().getParent().getParent() instanceof RoRFrame) {
			RoRFrame frame = (RoRFrame) this.getParent().getParent().getParent().getParent();
			frame.getUiController().setAlgStore(this.getSelectedIndex()); // TODO Faire en reflexif
		} else {
			System.err.println("Can't get parent RoRFrame");
		}

	}
}
