package ror.gui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import ror.core.Column;
import ror.core.Drawer;
import ror.core.ExcelExport;
import ror.core.Input;
import ror.core.Output;
import ror.core.Product;
import ror.core.Robot;
import ror.core.SimulationManager;
import ror.core.algo.AlgDestockingFifo;
import ror.core.algo.AlgDestockingOrder;
import ror.core.algo.AlgMoveAuto;
import ror.core.algo.AlgMoveEco;
import ror.core.algo.AlgMoveFast;
import ror.core.algo.AlgStoreFifo;
import ror.core.algo.AlgStoreOrder;

/**
 * UIController class Represent the UIController
 */
public class UIController implements Observer {

    /**
     * rorFrame
     */
    private RoRFrame rorFrame;
    /**
     * simulationManager
     */
    private SimulationManager simulationManager;
    /**
     * thread
     */
    private Thread thread;

    /**
     * Constructor UIController
     */
    public UIController() {
	this.simulationManager = new SimulationManager();
	this.rorFrame = new RoRFrame(this, "Robot On Rails - Simulateur automatisé des stocks");
	this.simulationManager.addObserver(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void update(Observable o, Object arg) {
	// Mise à jour de la zone d'informations
	// Input
	if (o instanceof Input) {
	    Input input = (Input) o;
	    // Si on ne suit plus l'input
	    if (!input.equals(this.rorFrame.getCheckedElement())) {
		input.deleteObserver(this);
	    } else {
		// Pour eviter les doublons
		input.deleteObservers();
		input.addObserver(this);
		Font h1Font = new Font(UIManager.getDefaults().getFont("TabbedPane.font").getFontName(), Font.BOLD, UIManager.getDefaults().getFont("TabbedPane.font").getSize() + 2);
		this.rorFrame.getInformationsPanel().removeAll();
		this.rorFrame.getInformationsPanel().setLayout(new GridLayout(13, 1));
		JLabel title = new JLabel("Détails point d'entrée");
		title.setFont(h1Font);
		title.setVerticalAlignment(JLabel.CENTER);
		title.setHorizontalAlignment(JLabel.CENTER);
		this.rorFrame.getInformationsPanel().add(title);
		// Gestion du cas où aucun produit n'est disponible
		if (input.getProductList().isEmpty()) {
		    JLabel label = new JLabel("Aucun produit n'est présent");
		    label.setVerticalAlignment(JLabel.CENTER);
		    label.setHorizontalAlignment(JLabel.CENTER);
		    this.rorFrame.getInformationsPanel().add(label);
		} else {
		    synchronized (input.getProductList()) {
			Iterator<Product> it = input.getProductList().iterator();
			int cpt = 0;
			while (it.hasNext() && cpt < 11) {
			    Product next = it.next();
			    JLabel label = new JLabel(next.getName());
			    label.setVerticalAlignment(JLabel.CENTER);
			    label.setHorizontalAlignment(JLabel.CENTER);
			    this.rorFrame.getInformationsPanel().add(label);
			    cpt++;
			}
			if (cpt >= 11) {
			    JLabel label = new JLabel("+ " + (input.getProductList().size()));
			    label.setVerticalAlignment(JLabel.CENTER);
			    label.setHorizontalAlignment(JLabel.CENTER);
			    this.rorFrame.getInformationsPanel().add(label);
			}
		    }
		}
		this.rorFrame.pack();
		this.rorFrame.repaint();
	    }
	}
	// Output
	else if (o instanceof Output) {
	    Font h1Font = new Font(UIManager.getDefaults().getFont("TabbedPane.font").getFontName(), Font.BOLD, UIManager.getDefaults().getFont("TabbedPane.font").getSize() + 2);
	    Output output = (Output) o;
	    // Si on ne suit plus l'input
	    if (!output.equals(this.rorFrame.getCheckedElement())) {
		output.deleteObserver(this);
	    } else {
		// Pour eviter les doublons
		output.deleteObservers();
		output.addObserver(this);
		this.rorFrame.getInformationsPanel().removeAll();
		this.rorFrame.getInformationsPanel().setLayout(new GridLayout(13, 1));
		JLabel title = new JLabel("Détails point de sortie");
		title.setFont(h1Font);
		title.setVerticalAlignment(JLabel.CENTER);
		title.setHorizontalAlignment(JLabel.CENTER);
		this.rorFrame.getInformationsPanel().add(title);
		// Gestion du cas où aucun produit n'est disponible
		if (output.getProductList().isEmpty()) {
		    JLabel label = new JLabel("Aucun produit n'est présent");
		    label.setVerticalAlignment(JLabel.CENTER);
		    label.setHorizontalAlignment(JLabel.CENTER);
		    this.rorFrame.getInformationsPanel().add(label);
		} else {
		    Iterator<Product> it = output.getProductList().iterator();
		    int cpt = 0;
		    while (it.hasNext() && cpt < 11) {
			Product next = it.next();
			JLabel label = new JLabel(next.getName());
			label.setVerticalAlignment(JLabel.CENTER);
			label.setHorizontalAlignment(JLabel.CENTER);
			this.rorFrame.getInformationsPanel().add(label);
			cpt++;
		    }
		    if (cpt >= 11) {
			JLabel label = new JLabel("+ " + (output.getProductList().size()));
			label.setVerticalAlignment(JLabel.CENTER);
			label.setHorizontalAlignment(JLabel.CENTER);
			this.rorFrame.getInformationsPanel().add(label);
		    }
		}
		this.rorFrame.pack();
		this.rorFrame.repaint();
	    }
	}
	// Column
	else if (o instanceof Column) {
	    Font h1Font = new Font(UIManager.getDefaults().getFont("TabbedPane.font").getFontName(), Font.BOLD, UIManager.getDefaults().getFont("TabbedPane.font").getSize() + 2);
	    Column column = (Column) o;
	    // Si on ne suit plus l'input
	    if (!column.equals(this.rorFrame.getCheckedElement())) {
		column.deleteObserver(this);
	    } else {
		// Pour eviter les doublons
		column.deleteObservers();
		column.addObserver(this);
		this.rorFrame.getInformationsPanel().removeAll();
		this.rorFrame.getInformationsPanel().setLayout(new FlowLayout());
		JLabel title = new JLabel("Détails armoire");
		title.setFont(h1Font);
		title.setVerticalAlignment(JLabel.CENTER);
		title.setHorizontalAlignment(JLabel.CENTER);

		this.rorFrame.getInformationsPanel().setLayout(new GridLayout(11, 1));
		this.rorFrame.getInformationsPanel().add(title);
		if (column.getDrawerList().isEmpty()) {
		    JLabel label = new JLabel("Aucun produit n'est présent");
		    label.setVerticalAlignment(JLabel.CENTER);
		    label.setHorizontalAlignment(JLabel.CENTER);
		    this.rorFrame.getInformationsPanel().add(label);
		} else {
		    Iterator<Drawer> it = column.getDrawerList().iterator();
		    Integer cpt = 0;
		    while (it.hasNext()) {
			Drawer next = it.next();
			if (next.getProduct() == null) {
			    JLabel label = new JLabel("Rang #" + cpt + " : Vide");
			    label.setVerticalAlignment(JLabel.CENTER);
			    label.setHorizontalAlignment(JLabel.CENTER);
			    this.rorFrame.getInformationsPanel().add(label);
			} else {
			    JLabel label = new JLabel("Rang #" + cpt + " : " + next.getProduct().getName());
			    label.setVerticalAlignment(JLabel.CENTER);
			    label.setHorizontalAlignment(JLabel.CENTER);
			    this.rorFrame.getInformationsPanel().add(label);
			}
			cpt++;
		    }
		}
	    }
	}
	// Robot
	else if (o instanceof Robot) {
	    Font h1Font = new Font(UIManager.getDefaults().getFont("TabbedPane.font").getFontName(), Font.BOLD, UIManager.getDefaults().getFont("TabbedPane.font").getSize() + 2);
	    Robot robot = (Robot) o;
	    // Si on ne suit plus l'input
	    if (!robot.equals(this.rorFrame.getCheckedElement())) {
		robot.deleteObserver(this);
	    } else {
		this.rorFrame.getInformationsPanel().removeAll();
		this.rorFrame.getInformationsPanel().setLayout(new GridLayout(13, 1));
		JLabel title = new JLabel("Détails Robot #" + robot.getNumber());
		title.setFont(h1Font);
		title.setVerticalAlignment(JLabel.CENTER);
		title.setHorizontalAlignment(JLabel.CENTER);
		this.rorFrame.getInformationsPanel().add(title);
		// Gestion du cas où aucun produit n'est disponible
		if (robot.getProducts().isEmpty()) {
		    JLabel label = new JLabel("Aucun produit n'est présent");
		    label.setVerticalAlignment(JLabel.CENTER);
		    label.setHorizontalAlignment(JLabel.CENTER);
		    this.rorFrame.getInformationsPanel().add(label);
		} else {
		    synchronized (robot.getProducts()) {

			Iterator<Product> it = robot.getProducts().iterator();
			while (it.hasNext()) {
			    Product next = it.next();
			    JLabel label = new JLabel(next.getName());
			    label.setVerticalAlignment(JLabel.CENTER);
			    label.setHorizontalAlignment(JLabel.CENTER);
			    this.rorFrame.getInformationsPanel().add(label);
			}
		    }
		}
		this.rorFrame.pack();
		this.rorFrame.repaint();
	    }

	}
	// Mise à jours de la liste des commandes
	((OrderListModel) this.rorFrame.getOrderList().getModel()).fireTableDataChanged();

	// Mise à jours de la liste des logs
	ArrayList<String> newLogs;
	synchronized (simulationManager.getNewLogs()) {
	    newLogs = new ArrayList<String>(simulationManager.getNewLogs());
	}
	
	DefaultListModel<String> model = (DefaultListModel<String>) this.rorFrame.getLogList().getModel();

	synchronized (model) {
	    for (String log : newLogs) {
		model.add(0, log);
	    }
	}

	simulationManager.setNewLogs(new ArrayList<String>());

	// update total electric consumption
	JLabel label = this.rorFrame.getTotalConsumptionLabel();
	label.setText(this.simulationManager.getTotalConsumption() + "W");
	this.rorFrame.setTotalConsumptionLabel(label);

	// update average electric consumption
	label = this.rorFrame.getAvgConsumptionLabel();
	label.setText(this.simulationManager.getAverageConsumption() + "W");
	this.rorFrame.setAverageConsumptionLabel(label);

	// update number of order done
	label = this.rorFrame.getNbOrderLabel();
	label.setText(String.valueOf(this.simulationManager.getOrdersDoneCount()));
	this.rorFrame.setNbOrderDoneLabel(label);

	// update average time
	label = this.rorFrame.getAvgTimeOrderLabel();
	Long seconds = (Long) (this.simulationManager.getAverageOrderProcessingTime() / 1000);
	Long minutes = (Long) (seconds / 60);
	seconds = seconds % 60;
	label.setText(minutes + "mn" + seconds % 60 + "s");
	this.rorFrame.setAvgTimeOrder(label);

	// update total time
	label = this.rorFrame.getTotalTimeOrderLabel();
	seconds = (Long) (this.simulationManager.getUptime() / 1000);
	minutes = (Long) (seconds / 60);
	seconds = seconds % 60;
	label.setText(minutes + "mn" + seconds + "s");
	this.rorFrame.setTotalTimeOrderLabel(label);

	// TODO Mise à jours des indicateurs
	this.rorFrame.reColor();
    }

    /**
     * Set AlgoMove with AlgoId passed in parameter
     * 
     * @param algId
     */
    public void setAlgMove(Integer algId) {
	switch (algId) {
	case 0:
	    simulationManager.setiAlgMove(new AlgMoveAuto());
	    break;
	case 1:
	    simulationManager.setiAlgMove(new AlgMoveEco());

	    break;
	case 2:
	    simulationManager.setiAlgMove(new AlgMoveFast());
	    break;
	default:
	    break;
	}
    }

    /**
     * Set AlgoStore with AlgoId passed in parameter
     * 
     * @param algId
     */
    public void setAlgStore(Integer algId) {
	switch (algId) {
	case 0:
	    simulationManager.setiAlgStore(new AlgStoreFifo());
	    break;
	case 1:
	    simulationManager.setiAlgStore(new AlgStoreOrder());
	    break;
	default:
	    break;
	}
    }

    /**
     * Set AlgoDestocking with AlgoId passed in parameter
     * 
     * @param algId
     */
    public void setAlgDestocking(Integer algId) {
	switch (algId) {
	case 0:
	    simulationManager.setiAlgDestocking(new AlgDestockingFifo());
	    break;
	case 1:
	    simulationManager.setiAlgDestocking(new AlgDestockingOrder());
	    break;
	default:
	    break;
	}
    }

    /**
     * End simulation
     */
    public void setEndSimulation() {
	simulationManager.setEndSimulation();
    }

    /**
     * Set nb Robot with nbRobot passed in parameter
     * 
     * @param nbRobot
     */
    public void setNbRobot(Integer nbRobot) {
	simulationManager.setNbRobot(nbRobot);
    }

    /**
     * //TODO What file ?
     * 
     * @param file
     */
    public void setFile(File file) {
	simulationManager.setFile(file);
	simulationManager.setSource(true); // Passage en mode scénario
	this.rorFrame.getAcceleratedButton().setEnabled(true);
	this.rorFrame.getRandomCheckBox().setEnabled(true);
    }

    /**
     * Set random mode
     */
    public void setRandomMode() {
	// Scénario
	if (simulationManager.getSource()) {
	    simulationManager.setSource(false); // Passage en mode random
	    this.rorFrame.getAcceleratedButton().setEnabled(false);
	}
    }

    /**
     * Stop simulation in progress
     */
    public void stopSimulation() {
	ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/start.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	this.rorFrame.getStartButton().setIcon(icon);
	int lastStatus = this.simulationManager.getStatus();
	simulationManager.setStop();
	this.thread = null;

	// Proposition d'export Excel
	if (lastStatus != 0) {
	    int n = JOptionPane.showConfirmDialog(this.rorFrame, "Voulez-vous exporter les résultats de la simulation?", "Export", JOptionPane.YES_NO_OPTION);
	    if (n == 0) {
		JFileChooser fc = new JFileChooser();
		int ret = fc.showSaveDialog(this.rorFrame);

		if (ret == JFileChooser.APPROVE_OPTION) {
		    File file = fc.getSelectedFile();
		    new ExcelExport(this.simulationManager, file);
		    System.out.println(file);
		}
	    }
	}
    }

    /**
     * Pause simulation
     */
    public void pauseSimulation() {
	ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/start.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	this.rorFrame.getStartButton().setIcon(icon);
	simulationManager.setPause();
    }

    /**
     * Start simulation
     */
    public void startSimulation() {
	ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/pause.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	this.rorFrame.getStartButton().setIcon(icon);
	if (thread == null) {
	    this.thread = new Thread(this.simulationManager);
	    this.thread.start();
	} else
	    this.simulationManager.setPlay();

    }

    /**
     * Set speed
     * 
     * @param speed
     */
    public void setSpeed(Float speed) {
	simulationManager.setSpeed(speed);
    }

    /**
     * @return simulationManager
     */
    public SimulationManager getSimulationManager() {
	return this.simulationManager;
    }

}
