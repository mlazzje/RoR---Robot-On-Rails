package ror.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import ror.core.RoRElement;

@SuppressWarnings("serial")
public class RoRFrame extends JFrame {

    private JMenuBar menuBar;
    private StartButton startButton;
    private AcceleratedButton acceleratedButton;
    private StopButton stopButton;
    private SpeedSlider speedSlider;
    private AlgStoreComboBox algStoreComboBox;
    private AlgDestockingComboBox algDestockingComboBox;
    private AlgMoveComboBox algMoveComboBox;
    private RobotComboBox robotComboBox;
    private RandomCheckBox randomCheckBox;
    private ImportButton importButton;
    private OrderList orderList;
    private LogList logList;
    private JLabel nbOrderLabel;
    private JLabel avgTimeOrderLabel;
    private JLabel totalTimeOrderLabel;
    private JLabel avgConsumptionLabel;
    private JLabel totalConsumptionLabel;
    private JPanel informationsPanel;
    private ImportFileChooser importFileChooser;
    private UIController uiController;
    private JPanel mapPanel;

    public StartButton getStartButton() {
	return startButton;
    }

    public AcceleratedButton getAcceleratedButton() {
	return acceleratedButton;
    }

    public StopButton getStopButton() {
	return stopButton;
    }

    public SpeedSlider getSpeedSlider() {
	return speedSlider;
    }

    public AlgStoreComboBox getAlgStoreComboBox() {
	return algStoreComboBox;
    }

    public AlgDestockingComboBox getAlgDestockingComboBox() {
	return algDestockingComboBox;
    }

    public AlgMoveComboBox getAlgMoveComboBox() {
	return algMoveComboBox;
    }

    public RobotComboBox getRobotComboBox() {
	return robotComboBox;
    }

    public RandomCheckBox getRandomCheckBox() {
	return randomCheckBox;
    }

    public ImportButton getImportButton() {
	return importButton;
    }

    public OrderList getOrderList() {
	return orderList;
    }

    public LogList getLogList() {
	return logList;
    }

    public JLabel getNbOrderLabel() {
	return nbOrderLabel;
    }

    public JLabel getAvgTimeOrderLabel() {
	return avgTimeOrderLabel;
    }

    public JLabel getTotalTimeOrderLabel() {
	return totalTimeOrderLabel;
    }

    public JLabel getAvgConsumptionLabel() {
	return avgConsumptionLabel;
    }

    public JLabel getTotalConsumptionLabel() {
	return totalConsumptionLabel;
    }

    public JPanel getInformationsPanel() {
	return informationsPanel;
    }

    public RoRFrame(UIController uiController, String title) throws HeadlessException {
	super(title);
	this.uiController = uiController;
	this.importFileChooser = new ImportFileChooser();

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
	this.setLayout(new FlowLayout());

	// Map
	mapPanel = new JPanel();
	RoRElement[][] map = uiController.getSimulationManager().getMap();
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
	bottomPanel.add(this.logList);

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
    }

    public void reColor() {
	for (Component jPanel : mapPanel.getComponents()) {
	    RoRElementPanel r = (RoRElementPanel) jPanel;
	    r.reColor();
	}
    }

    public ImportFileChooser getImportFileChooser() {
	return importFileChooser;
    }

    public UIController getUiController() {
	return uiController;
    }
}
