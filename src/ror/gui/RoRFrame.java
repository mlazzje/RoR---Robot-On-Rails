package ror.gui;

import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

@SuppressWarnings("serial")
public class RoRFrame extends JFrame {

    	private JMenuBar menuBar;
    	private StartButton startButton;
	private AcceleratedButton acceleratedButton;
	private StopButton stopButton;
	private AlgStoreComboBox algStoreComboBox;
	private AlgDestockingComboBox algDestockingComboBox;
	private AlgMoveComboBox algMoveComboBox;
	private RobotComboBox robotComboBox;
	private RandomCheckBox randomCheckBox;
	private ImportButton importButton;
    	
	private LogList logList;
	private ImportFileChooser importFileChooser;
	private OrderList orderList;
	private SpeedSlider speedSlider;
	private UIController uiController;

	public RoRFrame(UIController uiController, String title)
			throws HeadlessException {
		super(title);
		
		// Menu
		this.menuBar = new JMenuBar();
		this.startButton = new StartButton();
		this.menuBar.add(this.startButton);
		this.acceleratedButton = new AcceleratedButton();
		this.menuBar.add(this.acceleratedButton);
		this.stopButton = new StopButton();
		this.menuBar.add(this.stopButton);
		this.menuBar.add(new JLabel("Vitesse"));
		this.speedSlider = new SpeedSlider();
		this.menuBar.add(this.speedSlider);
		this.menuBar.add(new JLabel("Stockage"));
		this.algStoreComboBox = new AlgStoreComboBox(); 
		this.menuBar.add(this.algStoreComboBox);
		this.menuBar.add(new JLabel("Destockage"));
		this.algDestockingComboBox = new AlgDestockingComboBox(); 
		this.menuBar.add(this.algDestockingComboBox);
		this.menuBar.add(new JLabel("Déplacement"));
		this.algMoveComboBox = new AlgMoveComboBox(); 
		this.menuBar.add(this.algMoveComboBox);
		this.menuBar.add(new JLabel("Nb. Robot"));
		this.robotComboBox = new RobotComboBox(); 
		this.menuBar.add(this.robotComboBox);
		this.randomCheckBox = new RandomCheckBox();
		this.menuBar.add(randomCheckBox);
		this.importButton = new ImportButton();
		this.menuBar.add(importButton);
		this.setJMenuBar(this.menuBar);
		
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
