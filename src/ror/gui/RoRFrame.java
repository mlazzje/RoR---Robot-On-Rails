package ror.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import ror.core.RoRElement;

/**
 * @author mlazzje
 *
 */
public class RoRFrame extends JFrame {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * menuBar
     */
    private JMenuBar menuBar;
    /**
     * startButton
     */
    private StartButton startButton;
    /**
     * acceleratedButton
     */
    private AcceleratedButton acceleratedButton;
    /**
     * stopButton
     */
    private StopButton stopButton;
    /**
     * speedSlider
     */
    private SpeedSlider speedSlider;
    /**
     * Algo store ComboBox
     */
    private AlgStoreComboBox algStoreComboBox;
    /**
     * Algo destocking ComboBox
     */
    private AlgDestockingComboBox algDestockingComboBox;
    /**
     * Algo move ComboBox
     */
    private AlgMoveComboBox algMoveComboBox;
    /**
     * robotComboBox
     */
    private RobotComboBox robotComboBox;
    /**
     * randomCheckBox
     */
    private RandomCheckBox randomCheckBox;
    /**
     * importButton
     */
    private ImportButton importButton;
    /**
     * orderList
     */
    private OrderList orderList;
    /**
     * logList
     */
    private LogList logList;
    /**
     * nbOrderLabel
     */
    private JLabel nbOrderLabel;
    /**
     * avgTimeOrderLabel
     */
    private JLabel avgTimeOrderLabel;
    /**
     * totalTimeOrderLabel
     */
    private JLabel totalTimeOrderLabel;
    /**
     * avgConsumptionLabel
     */
    private JLabel avgConsumptionLabel;
    /**
     * totalConsumptionLabel
     */
    private JLabel totalConsumptionLabel;
    /**
     * informationsPanel
     */
    private JPanel informationsPanel;
    /**
     * importFileChooser
     */
    private ImportFileChooser importFileChooser;
    /**
     * uiController
     */
    private UIController uiController;
    /**
     * mapPanel
     */
    private JPanel mapPanel;
    /**
     * checkedElement
     */
    private Object checkedElement;

    /**
     * @return startButton
     */
    public StartButton getStartButton() {
	return startButton;
    }

    /**
     * @return acceleratedButton
     */
    public AcceleratedButton getAcceleratedButton() {
	return acceleratedButton;
    }

    /**
     * @return stopButton
     */
    public StopButton getStopButton() {
	return stopButton;
    }

    /**
     * @return speedSlider
     */
    public SpeedSlider getSpeedSlider() {
	return speedSlider;
    }

    /**
     * @return algStoreComboBox
     */
    public AlgStoreComboBox getAlgStoreComboBox() {
	return algStoreComboBox;
    }

    /**
     * @return algDestockingComboBox
     */
    public AlgDestockingComboBox getAlgDestockingComboBox() {
	return algDestockingComboBox;
    }

    /**
     * @return algMoveComboBox
     */
    public AlgMoveComboBox getAlgMoveComboBox() {
	return algMoveComboBox;
    }

    /**
     * @return robotComboBox
     */
    public RobotComboBox getRobotComboBox() {
	return robotComboBox;
    }

    /**
     * @return randomCheckBox
     */
    public RandomCheckBox getRandomCheckBox() {
	return randomCheckBox;
    }

    /**
     * @return importButton
     */
    public ImportButton getImportButton() {
	return importButton;
    }

    /**
     * @return orderList
     */
    public OrderList getOrderList() {
	return orderList;
    }

    /**
     * @return logList
     */
    public LogList getLogList() {
	return logList;
    }

    /**
     * @return nbOrderLabel
     */
    public JLabel getNbOrderLabel() {
	return nbOrderLabel;
    }

    /**
     * @return avgTimeOrderLabel
     */
    public JLabel getAvgTimeOrderLabel() {
	return avgTimeOrderLabel;
    }

    /**
     * @return totalTimeOrderLabel
     */
    public JLabel getTotalTimeOrderLabel() {
	return totalTimeOrderLabel;
    }

    /**
     * @return avgConsumptionLabel
     */
    public JLabel getAvgConsumptionLabel() {
	return avgConsumptionLabel;
    }

    /**
     * @return totalConsumptionLabel
     */
    public JLabel getTotalConsumptionLabel() {
	return totalConsumptionLabel;
    }

    /**
     * @return informationsPanel
     */
    public JPanel getInformationsPanel() {
	return informationsPanel;
    }

    /**
     * Constructor RoRFrame
     * 
     * @param uiController
     * @param title
     * @throws HeadlessException
     */
    public RoRFrame(UIController uiController, String title) throws HeadlessException {
	super(title);
	this.uiController = uiController;
	this.importFileChooser = new ImportFileChooser();

	// Menu
	this.menuBar = new JMenuBar();
	this.startButton = new StartButton();
	this.menuBar.add(this.startButton);
	this.acceleratedButton = new AcceleratedButton();
	this.acceleratedButton.setEnabled(false);
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
	this.randomCheckBox.setSelected(true);
	this.menuBar.add(randomCheckBox);
	this.importButton = new ImportButton();
	this.menuBar.add(importButton);
	this.setJMenuBar(this.menuBar);
	this.setLayout(new FlowLayout());
	try {
	    this.setIconImage(ImageIO.read(getClass().getResource("/ressources/robot.png")));
	} catch (IOException e) {
	    e.printStackTrace();
	}

	// Map
	mapPanel = new JPanel();
	RoRElement[][] map = uiController.getSimulationManager().getMap().getMap();
	mapPanel.setLayout(new GridLayout(map.length, map[0].length));
	float coeff = (float) 1.6;
	mapPanel.setPreferredSize(new Dimension((int) (797 * coeff), (int) (213 * coeff)));
	for (int l = 0; l < map.length; l++) {
	    for (int c = 0; c < map[0].length; c++) {
		mapPanel.add(new RoRElementPanel(map[l][c]));
	    }
	}
	this.getContentPane().add(mapPanel);

	JPanel bottomPanel = new JPanel();
	bottomPanel.setLayout(new GridLayout(1, 4));
	bottomPanel.setPreferredSize(new Dimension(1280, 300));

	// Liste des commandes
	this.orderList = new OrderList(this);
	JScrollPane orderListPane = new JScrollPane(this.orderList);
	bottomPanel.add(orderListPane);

	// Traces
	this.logList = new LogList();
	bottomPanel.add(new JScrollPane(this.logList));

	// Informations
	Font bFont = new Font(UIManager.getDefaults().getFont("TabbedPane.font").getFontName(), Font.BOLD, UIManager.getDefaults().getFont("TabbedPane.font").getSize());
	Font h1Font = new Font(UIManager.getDefaults().getFont("TabbedPane.font").getFontName(), Font.BOLD, UIManager.getDefaults().getFont("TabbedPane.font").getSize() + 2);

	this.informationsPanel = new JPanel();
	this.informationsPanel.setLayout(new GridLayout(11, 1));
	JLabel titleInfo = new JLabel("Cliquez sur un élement visuel");
	titleInfo.setVerticalAlignment(JLabel.CENTER);
	titleInfo.setHorizontalAlignment(JLabel.CENTER);
	titleInfo.setFont(h1Font);
	this.informationsPanel.add(titleInfo);
	bottomPanel.add(this.informationsPanel);

	// Statistiques
	JPanel statisticsPanel = new JPanel();
	statisticsPanel.setLayout(new GridLayout(11, 1));
	JLabel statTitle = new JLabel("Statistiques");
	statTitle.setFont(h1Font);
	statisticsPanel.add(statTitle);
	JLabel nbOrderTitleLabel = new JLabel("Nombre de commande traitée");
	nbOrderTitleLabel.setFont(bFont);
	statisticsPanel.add(nbOrderTitleLabel);
	this.nbOrderLabel = new JLabel("0");
	statisticsPanel.add(this.nbOrderLabel);
	JLabel avgTimeOrderTitleLabel = new JLabel("Temps de traitement moyen");
	avgTimeOrderTitleLabel.setFont(bFont);
	statisticsPanel.add(avgTimeOrderTitleLabel);
	this.avgTimeOrderLabel = new JLabel("0 minute 0 seconde");
	statisticsPanel.add(this.avgTimeOrderLabel);
	JLabel totalTimeOrderTitleLabel = new JLabel("Temps de traitement total");
	totalTimeOrderTitleLabel.setFont(bFont);
	statisticsPanel.add(totalTimeOrderTitleLabel);
	this.totalTimeOrderLabel = new JLabel("0 minute 0 seconde");
	statisticsPanel.add(this.totalTimeOrderLabel);
	JLabel avgConsumptionTitleLabel = new JLabel("Consommation moyenne");
	avgConsumptionTitleLabel.setFont(bFont);
	statisticsPanel.add(avgConsumptionTitleLabel);
	this.avgConsumptionLabel = new JLabel("0W");
	statisticsPanel.add(this.avgConsumptionLabel);
	JLabel totalConsumptionTitleLabel = new JLabel("Consommation totale");
	totalConsumptionTitleLabel.setFont(bFont);
	statisticsPanel.add(totalConsumptionTitleLabel);
	totalConsumptionLabel = new JLabel("0W");
	statisticsPanel.add(this.totalConsumptionLabel);
	bottomPanel.add(statisticsPanel);

	this.getContentPane().add(bottomPanel);

	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setPreferredSize(new Dimension(1280, 800));
	this.pack();
	this.setLocationRelativeTo(null);
	this.setResizable(false);
	this.setVisible(true);

	this.reColor();
    }

    /**
     * reColor
     */
    public void reColor() {
	/*
	 * for (Component jPanel : mapPanel.getComponents()) { //TODO Implementer Observable/Observer sur les cases pour gagner en perf' RoRElementPanel rorElementPanel = (RoRElementPanel) jPanel; rorElementPanel.reColor(); }
	 */
    }

    /**
     * @return importFileChooser
     */
    public ImportFileChooser getImportFileChooser() {
	return importFileChooser;
    }

    /**
     * @return uiController
     */
    public UIController getUiController() {
	return uiController;
    }

    /**
     * @return checkedElement
     */
    public Object getCheckedElement() {
	return checkedElement;
    }

    /**
     * @param checkedElement
     */
    public void setCheckedElement(Object checkedElement) {
	this.checkedElement = checkedElement;
    }

    /**
     * @param label
     */
    public void setTotalConsumptionLabel(JLabel label) {
	this.totalConsumptionLabel = label;
    }
    
    /**
     * @param label
     */
    public void setAverageConsumptionLabel(JLabel label) {
	this.avgConsumptionLabel = label;
    }
    
    /**
     * @param label
     */
    public void setNbOrderDoneLabel(JLabel label) {
	this.nbOrderLabel = label;
    }
    
    /**
     * @param label
     */
    public void setAvgTimeOrder(JLabel label)
    {
	this.avgTimeOrderLabel=label;
    }
    
    /**
     * @param label
     */
    public void setTotalTimeOrderLabel(JLabel label)
    {
	this.totalTimeOrderLabel=label;
    }
}
