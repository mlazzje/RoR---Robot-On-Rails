package ror.core;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import ror.core.actions.Action;
import ror.core.actions.MoveAction;
import ror.core.actions.PauseAction;
import ror.core.algo.AlgDestockingFifo;
import ror.core.algo.AlgMoveEco;
import ror.core.algo.AlgStoreFifo;
import ror.core.algo.IAlgDestocking;
import ror.core.algo.IAlgMove;
import ror.core.algo.IAlgStore;

public class SimulationManager extends Observable implements Observer, Runnable {

    // relationship
    private IAlgStore iAlgStore;
    private IAlgMove iAlgMove;
    private IAlgDestocking iAlgDestocking;
    private ArrayList<Cabinet> cabinets;
    private RoRElement[][] map;
    private ArrayList<Robot> robots;
    private ArrayList<Order> orders;
    private OrderSource orderSource;

    // own attributes
    private Float speed = (float) 0;
    private Integer nbRobot = 0;
    private Integer status = 0;
    private boolean source;
    private Integer coeff = 1000; // <==> 1 second
    private long startTime;

    private ArrayList<Product> stockProducts;
    private Input input;
    private Output output;

    public SimulationManager() {
	this.map = new RoRElement[1][1]; // Taille par defaut
	File file = new File("xml/warehouse.xml");
	this.setWareHouse(file);
	this.robots = new ArrayList<Robot>();
	this.orderSource = new OrderSource();
	this.source = false;
	this.iAlgStore = new AlgStoreFifo();
	this.iAlgDestocking = new AlgDestockingFifo();
	this.iAlgMove = new AlgMoveEco();
    }

    public Integer getStatus() {
	return this.status;
    }

    public boolean getSource() {
	return this.source;
    }

    public Float getSpeed() {
	return this.speed;
    }

    public IAlgStore getiAlgStore() {
	return this.iAlgStore;
    }

    public IAlgMove getiAlgMove() {
	return this.iAlgMove;
    }

    public IAlgDestocking getiAlgDestocking() {
	return this.iAlgDestocking;
    }

    public Integer getNbRobot() {
	return this.nbRobot;
    }

    public Integer getUptime() {
	return ((int) ((System.currentTimeMillis() - startTime) / 1000));
    }

    @Override
    public void run() {
	// add robots
	for (int i = 0; i < nbRobot; i++)
	    robots.add(new Robot(null)); // TODO ajouter le rail par defaut pour
					 // le robot

	this.stockProducts = new ArrayList<Product>();
	this.orders = new ArrayList<Order>();
	startTime = System.currentTimeMillis();
	status = 1;
	while (status != 0) {
	    System.out.println("simulation manager loop ");
	    System.out.println(status);
	    if (status == 1) // running
	    {
		ArrayList<Order> newOrders = new ArrayList<Order>();
		ArrayList<Product> newProducts = new ArrayList<Product>();
		if (!source) // random mode
		{
		    newOrders = SimulationManager.this.orderSource.getRandomOrders();
		    newProducts = SimulationManager.this.orderSource.getRandomProducts(newOrders, SimulationManager.this.stockProducts);
		} else // scenario mode
		{
		    newOrders = SimulationManager.this.orderSource.getScenarioOrders(SimulationManager.this.getUptime());
		    newProducts = SimulationManager.this.orderSource.getScenartioProducts(SimulationManager.this.getUptime());
		}
		// add new orders to orders list
		SimulationManager.this.orders.addAll(newOrders);

		// add new products to stockProducts list
		SimulationManager.this.stockProducts.addAll(newProducts);

		// TODO Implémenter méthodes algo pour pouvoir tester
		/*
		 * // get store and input actions for newProducts ArrayList<Action> newActions = SimulationManager.this.iAlgStore .getActions(newProducts, newOrders, SimulationManager.this.map);
		 * 
		 * // get destocking and output actions for stockProducts newActions.addAll(SimulationManager.this.iAlgDestocking .getActions(newOrders, stockProducts, SimulationManager.this.output));
		 * 
		 * // update robot actions lists SimulationManager.this.iAlgMove. updateRobotsActions(newActions, SimulationManager.this.robots);
		 */
		// update statistic indicators
		SimulationManager.this.updateIndicators();

		// notify observers (UIController)
		this.setChanged();
		this.notifyObservers();

		// sleep
		try {
		    Thread.sleep((int) (SimulationManager.this.coeff * SimulationManager.this.speed));
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}

	    } else if (status == 2) // pause simulation
	    {
		try {
		    synchronized (this) {
			this.wait();
		    }
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    public RoRElement[][] getMap() {
	return map;
    }

    public void updateIndicators() {
	// TODO to implement and create indicators attributes
    }

    public void setStop() {
	status = 0;
    }

    public void setPause() {
	status = 2;
    }

    public void setPlay() {
	status = 1;
	synchronized (this) {
	    this.notify();
	}
    }

    @Override
    public void update(Observable o, Object arg) { // called by a robot
	if (o instanceof Robot) {
	    Robot robot = (Robot) o;

	    // save the last action of the robot
	    Action lastRobotAction = robot.getCurrentAction();

	    // going to the next action
	    robot.removeCurrentAction();

	    // search if the next action is blocked by another robot
	    Robot blockingRobot = checkNextAction(robot, robot.getCurrentAction());

	    // if no robot on the next rail
	    if (blockingRobot == null) {

		// execute next action
		robot.executeAction(robot.getCurrentAction());

		// get all robots waiting for the current robot to move
		ArrayList<Robot> waitingRobots = getWaitingRobots(blockingRobot);

		// then unblocks all robots blocked by this robot
		for (Robot r : waitingRobots) {

		    // cancel the scheduling task of blocked robot
		    r.stopSchedule();

		    // remove the PauseAction of the blocked robot
		    r.removeCurrentAction();

		    // then launch the next action
		    r.executeAction(r.getCurrentAction());
		}
	    }
	    // else add long waiting action to robot (to be sure that the task
	    // will
	    // be cancel by the robot his waiting)
	    else {
		robot.executeAction(new PauseAction(100000, robot, blockingRobot));
	    }

	    // notify observers (UIController)
	    this.setChanged();
	    this.notifyObservers();
	}
    }

    // retourne la liste des robots dont le mouvement est bloque par le robot
    // passé paramètre
    private ArrayList<Robot> getWaitingRobots(Robot blockingRobot) {
	ArrayList<Robot> waitingRobots = new ArrayList<Robot>();

	for (Robot robot : robots) {
	    Action action = robot.getCurrentAction();
	    if (action instanceof PauseAction) {
		PauseAction pauseAction = (PauseAction) action;
		if (pauseAction.getWaitingRobot() == blockingRobot)
		    waitingRobots.add(robot);
	    }
	}
	return waitingRobots;
    }

    public void setiAlgStore(IAlgStore iAlgStore) {
	this.iAlgStore = iAlgStore;
    }

    public void setiAlgMove(IAlgMove iAlgMove) {
	this.iAlgMove = iAlgMove;
    }

    public void setiAlgDestocking(IAlgDestocking iAlgDestocking) {
	this.iAlgDestocking = iAlgDestocking;
    }

    public void setRandomMode() {
	source = false;
    }

    public void setFile(File file) {
	source = true;
	orderSource.setFile(file);
    }

    public void setSpeed(Float speed) {
	this.speed = (speed >= 0) ? speed : 0.0f;
    }

    public void setNbRobot(Integer nbRobot) {
	this.nbRobot = (nbRobot >= 0) ? nbRobot : 0;
    }

    public void setEndSimulation() {
	coeff = 0;
	this.setPlay();
    }

    public ArrayList<Robot> getRobots() {
	return this.robots;
    }

    private Robot checkNextAction(Robot robot, Action action) {
	if (action instanceof MoveAction) {
	    MoveAction moveAction = (MoveAction) action;
	    for (Robot r : robots) {
		if (r != robot) {
		    if (moveAction.getNext() == robot.getRail()) {
			return robot;
		    }
		}
	    }
	}
	return null;
    }

    public void setWareHouse(File file) {

	Document document = null;
	Element racine;

	SAXBuilder sxb = new SAXBuilder();
	try {
	    document = sxb.build(file);
	} catch (Exception e) {
	}

	racine = document.getRootElement();

	// creations du plan
	int height = Integer.parseInt(racine.getAttributeValue("height"));
	int width = Integer.parseInt(racine.getAttributeValue("width"));
	this.map = new RoRElement[height][width];

	// Creation des rails
	List railRowList = racine.getChildren("rail_row");
	Iterator it = railRowList.iterator();
	while (it.hasNext()) {

	    Element currentRail = (Element) it.next();

	    // start point
	    Element start = currentRail.getChild("start");
	    Point startPoint = new Point();
	    startPoint.x = Integer.parseInt(start.getChild("x").getValue().trim());
	    startPoint.y = Integer.parseInt(start.getChild("y").getValue().trim());

	    // right rail
	    Element end = currentRail.getChild("end");
	    Point endPoint = new Point();
	    endPoint.x = Integer.parseInt(end.getChild("x").getValue().trim());
	    endPoint.y = Integer.parseInt(end.getChild("y").getValue().trim());

	    if (startPoint.x == endPoint.x) {
		int x = startPoint.x;

		// verticale de haut en bas 0 -> Y
		if (startPoint.y < endPoint.y) {
		    for (int y = startPoint.y; y <= endPoint.y; y++)
			this.map[y][x] = new Rail(x, y, null, null, new ArrayList<Rail>(), null);
		}
		// verticale de bas en haut Y -> 0
		else {
		    for (int y = startPoint.y; y >= endPoint.y; y--)
			this.map[y][x] = new Rail(x, y, null, null, new ArrayList<Rail>(), null);
		}
	    } else if (startPoint.y == endPoint.y) {
		int y = startPoint.y;
		// ligne de gauche à droite 0 -> X
		if (startPoint.x < endPoint.x) {
		    for (int x = startPoint.x; x <= endPoint.x; x++)
			this.map[y][x] = new Rail(x, y, null, null, new ArrayList<Rail>(), null);

		}
		// ligne de droite à gauche X -> 0
		else {
		    for (int x = startPoint.x; x >= endPoint.x; x--)
			this.map[y][x] = new Rail(x, y, null, null, new ArrayList<Rail>(), null);
		}
	    }
	}

	// Creation des rails
	it = railRowList.iterator();
	while (it.hasNext()) {

	    Element currentRail = (Element) it.next();

	    // start point
	    Element start = currentRail.getChild("start");
	    Point startPoint = new Point();
	    startPoint.x = Integer.parseInt(start.getChild("x").getValue().trim());
	    startPoint.y = Integer.parseInt(start.getChild("y").getValue().trim());

	    // end rail
	    Element end = currentRail.getChild("end");
	    Point endPoint = new Point();
	    endPoint.x = Integer.parseInt(end.getChild("x").getValue().trim());
	    endPoint.y = Integer.parseInt(end.getChild("y").getValue().trim());

	    // left rail
	    Element left = currentRail.getChild("left");
	    Point leftPoint = null;
	    if (left != null) {
		leftPoint = new Point();
		leftPoint.x = Integer.parseInt(left.getChild("x").getValue().trim());
		leftPoint.y = Integer.parseInt(left.getChild("y").getValue().trim());
	    }

	    // right rail
	    Element right = currentRail.getChild("right");
	    Point rightPoint = null;
	    if (right != null) {
		rightPoint = new Point();
		rightPoint.x = Integer.parseInt(right.getChild("x").getValue().trim());
		rightPoint.y = Integer.parseInt(right.getChild("y").getValue().trim());
	    }

	    if (startPoint.x == endPoint.x) {
		int x = startPoint.x;

		// verticale de haut en bas 0 -> Y
		if (startPoint.y < endPoint.y) {
		    for (int y = startPoint.y; y <= endPoint.y; y++) {
			Rail r = (Rail) this.map[y][x];
			// si rail de debut
			if (y == startPoint.y) {
			    if (rightPoint != null) {
				r.setRightRail((Rail) this.map[y + 1][x]);
			    }
			}
			// si rail de fin
			else if (y == endPoint.y) {
			    if (leftPoint != null) {
				r.setLeftRail((Rail) this.map[leftPoint.y][leftPoint.x]);
			    }
			    if (rightPoint != null) {
				r.setRightRail((Rail) this.map[rightPoint.y][rightPoint.x]);
			    }
			}
			// sinon rail du milieu
			else {
			    r.setRightRail((Rail) this.map[y + 1][x]);
			}
			if (r.getLeftRail() != null)
			    r.getLeftRail().addPreviousRail(r);
			if (r.getRightRail() != null)
			    r.getRightRail().addPreviousRail(r);
		    }
		}
		// verticale de bas en haut Y -> 0
		else {
		    for (int y = startPoint.y; y >= endPoint.y; y--) {
			Rail r = (Rail) this.map[y][x];

			// si rail de debut
			if (y == startPoint.y) {
			    if (rightPoint != null) {
				r.setRightRail((Rail) this.map[y - 1][x]);
			    }
			}
			// si rail de fin
			else if (y == endPoint.y) {
			    if (leftPoint != null) {
				r.setLeftRail((Rail) this.map[leftPoint.y][leftPoint.x]);
			    }
			    if (rightPoint != null) {
				r.setRightRail((Rail) this.map[rightPoint.y][rightPoint.x]);
			    }
			}
			// sinon rail du milieu
			else {
			    r.setRightRail((Rail) this.map[y - 1][x]);
			}

			if (r.getLeftRail() != null)
			    r.getLeftRail().addPreviousRail(r);
			if (r.getRightRail() != null)
			    r.getRightRail().addPreviousRail(r);
		    }
		}
	    } else if (startPoint.y == endPoint.y) {
		int y = startPoint.y;
		// ligne de gauche à droite 0 -> X
		if (startPoint.x < endPoint.x) {
		    for (int x = startPoint.x; x <= endPoint.x; x++) {
			Rail r = (Rail) this.map[y][x];

			// si rail de debut
			if (x == startPoint.x) {
			    if (rightPoint != null) {
				r.setRightRail((Rail) this.map[y][x + 1]);
			    }
			}
			// si rail de fin
			else if (x == endPoint.x) {
			    if (leftPoint != null) {
				r.setLeftRail((Rail) this.map[leftPoint.y][leftPoint.x]);
			    }
			    if (rightPoint != null) {
				r.setRightRail((Rail) this.map[rightPoint.y][rightPoint.x]);
			    }
			}
			// sinon rail du milieu
			else {
			    r.setRightRail((Rail) this.map[y][x + 1]);
			}
			if (r.getLeftRail() != null)
			    r.getLeftRail().addPreviousRail(r);
			if (r.getRightRail() != null)
			    r.getRightRail().addPreviousRail(r);
		    }
		}
		// ligne de droite à gauche X -> 0
		else {
		    for (int x = startPoint.x; x >= endPoint.x; x--) {
			Rail r = (Rail) this.map[y][x];

			// si rail de debut
			if (x == startPoint.x) {
			    if (rightPoint != null) {
				r.setRightRail((Rail) this.map[y][x - 1]);
			    }
			}
			// si rail de fin
			else if (x == endPoint.x) {
			    if (leftPoint != null) {
				r.setLeftRail((Rail) this.map[leftPoint.y][leftPoint.x]);
			    }
			    if (rightPoint != null) {
				r.setRightRail((Rail) this.map[rightPoint.y][rightPoint.x]);
			    }
			}
			// sinon rail du milieu
			else {
			    r.setRightRail((Rail) this.map[y][x - 1]);
			}

			if (r.getLeftRail() != null)
			    r.getLeftRail().addPreviousRail(r);
			if (r.getRightRail() != null)
			    r.getRightRail().addPreviousRail(r);
		    }
		}
	    }
	}

	// Creation des colonnes
	List columnsRowList = racine.getChildren("column_row");
	Iterator itc = columnsRowList.iterator();
	while (itc.hasNext()) {

	    Element currentColumnRail = (Element) itc.next();

	    // start point
	    Element start = currentColumnRail.getChild("start");
	    Point startPoint = new Point();
	    startPoint.x = Integer.parseInt(start.getChild("x").getValue().trim());
	    startPoint.y = Integer.parseInt(start.getChild("y").getValue().trim());

	    // right rail
	    Element end = currentColumnRail.getChild("end");
	    Point endPoint = new Point();
	    endPoint.x = Integer.parseInt(end.getChild("x").getValue().trim());
	    endPoint.y = Integer.parseInt(end.getChild("y").getValue().trim());

	    // robot access point
	    Element robotAccess = currentColumnRail.getChild("robot_access");

	    int distance = Integer.parseInt(robotAccess.getValue().trim());

	    if (startPoint.x == endPoint.x) {
		int x = startPoint.x;

		// verticale de haut en bas 0 -> Y
		if (startPoint.y < endPoint.y) {
		    for (int y = startPoint.y; y <= endPoint.y; y++) {
			this.map[y][x] = new Column(null, x, y, null, null);
			Column c = (Column) this.map[y][x];
			c.setAccess((Rail) this.map[y][x + distance]);
		    }
		}
		// verticale de bas en haut Y -> 0
		else {
		    for (int y = startPoint.y; y >= endPoint.y; y--) {
			this.map[y][x] = new Column(null, x, y, null, null);
			Column c = (Column) this.map[y][x];
			c.setAccess((Rail) this.map[y][x + distance]);
		    }
		}
	    } else if (startPoint.y == endPoint.y) {
		int y = startPoint.y;
		// ligne de gauche à droite 0 -> X
		if (startPoint.x < endPoint.x) {
		    for (int x = startPoint.x; x <= endPoint.x; x++) {
			this.map[y][x] = new Column(null, x, y, null, null);
			Column c = (Column) this.map[y][x];
			c.setAccess((Rail) this.map[y + distance][x]);
		    }
		}
		// ligne de droite à gauche X -> 0
		else {
		    for (int x = startPoint.x; x >= endPoint.x; x--) {
			this.map[y][x] = new Column(null, x, y, null, null);
			Column c = (Column) this.map[y][x];
			c.setAccess((Rail) this.map[y + distance][x]);
		    }
		}
	    }
	}

	// creation de l'input
	Element elInput = racine.getChild("input");
	if (elInput != null) {
	    int xAccess = Integer.parseInt(elInput.getChild("robot_access").getChild("x").getValue().trim());
	    int yAccess = Integer.parseInt(elInput.getChild("robot_access").getChild("y").getValue().trim());

	    this.input = new Input(Integer.parseInt(elInput.getChild("x").getValue().trim()), Integer.parseInt(elInput.getChild("y").getValue().trim()), null);
	    this.input.setAccess((Rail) this.map[yAccess][xAccess]);
	    this.map[Integer.parseInt(elInput.getChild("y").getValue().trim())][Integer.parseInt(elInput.getChild("x").getValue().trim())] = input;
	}

	// creation de l'output
	Element elOutput = racine.getChild("output");
	if (elOutput != null) {
	    int xAccess = Integer.parseInt(elOutput.getChild("robot_access").getChild("x").getValue().trim());
	    int yAccess = Integer.parseInt(elOutput.getChild("robot_access").getChild("y").getValue().trim());

	    this.output = new Output(Integer.parseInt(elOutput.getChild("x").getValue().trim()), Integer.parseInt(elOutput.getChild("y").getValue().trim()), null);
	    this.output.setAccess((Rail) this.map[yAccess][xAccess]);
	    this.map[Integer.parseInt(elOutput.getChild("y").getValue().trim())][Integer.parseInt(elOutput.getChild("x").getValue().trim())] = output;
	}

    }
}
