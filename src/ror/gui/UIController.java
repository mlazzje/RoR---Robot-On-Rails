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
import javax.swing.JLabel;
import javax.swing.UIManager;

import ror.core.Column;
import ror.core.Drawer;
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

public class UIController implements Observer {

    private RoRFrame rorFrame;
    private SimulationManager simulationManager;
    private Thread thread;

    public UIController() {
	this.simulationManager = new SimulationManager();
	this.rorFrame = new RoRFrame(this, "Robot On Rails - Simulateur automatisé des stocks");
	this.simulationManager.addObserver(this);
    }

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
		    while (it.hasNext()) {
			Product next = it.next();
			JLabel label = new JLabel(next.getName());
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
		JLabel title = new JLabel("Détails Robot #"+robot.getNumber());
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
		    Iterator<Product> it = robot.getProducts().iterator();
		    while (it.hasNext()) {
			Product next = it.next();
			JLabel label = new JLabel(next.getName());
			label.setVerticalAlignment(JLabel.CENTER);
			label.setHorizontalAlignment(JLabel.CENTER);
			this.rorFrame.getInformationsPanel().add(label);
		    }
		}
		this.rorFrame.pack();
		this.rorFrame.repaint();
	    }

	}
	// Mise à jours de la liste des commandes
	((OrderListModel) this.rorFrame.getOrderList().getModel()).fireTableDataChanged();

	// Mise à jours de la liste des logs
	synchronized (simulationManager.getNewLogs()) {

	    ArrayList<String> newLogs = simulationManager.getNewLogs();
	    DefaultListModel<String> model = (DefaultListModel<String>) this.rorFrame.getLogList().getModel();
	    for (String log : newLogs) {
		// model.addElement(log);
		model.add(0, log);
	    }
	    this.rorFrame.getLogList().setModel(model);
	    simulationManager.setNewLogs(new ArrayList<String>());

	}
	int cons = 0;
	for (Robot robot : this.simulationManager.getRobots()) {
	    cons += robot.getConsumption();
	}
	JLabel label = this.rorFrame.getTotalConsumptionLabel();
	label.setText(cons + "W");
	this.rorFrame.setTotalConsumptionLabel(label);

	// TODO Mise à jours des indicateurs
	this.rorFrame.reColor();
    }

    public void setAlgMove(Integer algId) {
	switch (algId) {
	case 0:
	    simulationManager.setiAlgMove(new AlgMoveEco());
	    break;
	case 1:
	    simulationManager.setiAlgMove(new AlgMoveFast());
	    break;
	case 2:
	    simulationManager.setiAlgMove(new AlgMoveAuto());
	    break;
	default:
	    break;
	}
    }

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

    public void setEndSimulation() {
	simulationManager.setEndSimulation();
    }

    public void setNbRobot(Integer nbRobot) {
	simulationManager.setNbRobot(nbRobot);
    }

    public void setFile(File file) {
	simulationManager.setFile(file);
    }

    public void setRandomMode() {
	simulationManager.setRandomMode();
    }

    public void stopSimulation() {
	ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/start.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	this.rorFrame.getStartButton().setIcon(icon);
	simulationManager.setStop();
	this.thread = null;
    }

    public void pauseSimulation() {
	ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/start.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	this.rorFrame.getStartButton().setIcon(icon);
	simulationManager.setPause();
    }

    public void startSimulation() {
	ImageIcon icon = new ImageIcon(new ImageIcon(StartButton.class.getResource("/ressources/pause.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	this.rorFrame.getStartButton().setIcon(icon);
	if (thread == null) {
	    this.thread = new Thread(this.simulationManager);
	    this.thread.start();
	} else
	    this.simulationManager.setPlay();

    }

    public void setSpeed(Float speed) {
	simulationManager.setSpeed(speed);
    }

    public SimulationManager getSimulationManager() {
	return this.simulationManager;
    }

}
