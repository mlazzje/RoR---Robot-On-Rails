package ror.gui;

import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class RoRFrame extends JFrame {

	private LogList logList;
	private AcceleratedButton acceleratedButton;
	private ImportButton importButton;
	private ImportFileChooser importFileChooser;
	private OrderList orderList;
	private RandomCheckBox randomCheckBox;
	private RobotComboBox robotComboBox;
	private AlgDestockingComboBox algDestockingComboBox;
	private AlgMoveComboBox algMoveComboBox;
	private AlgStoreComboBox algStoreComboBox;
	private SpeedSlider speedSlider;
	private StartButton startButton;
	private StopButton stopButton;
	private UIController uiController;

	public RoRFrame(UIController uiController, String title)
			throws HeadlessException {
		super(title);
		this.uiController = uiController;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(1280, 800));
		this.setLocation(0, 0);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}

}
