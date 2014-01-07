package ror.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import ror.core.actions.Action;
import ror.core.actions.DestockingAction;
import ror.core.actions.InputAction;
import ror.core.actions.MoveAction;
import ror.core.actions.OutputAction;
import ror.core.actions.StoreAction;
import ror.core.algo.AlgDestockingFifo;
import ror.core.algo.AlgMoveEco;
import ror.core.algo.AlgStoreFifo;
import ror.core.algo.IAlgDestocking;
import ror.core.algo.IAlgMove;
import ror.core.algo.IAlgStore;

/**
 * SimulationManager class : Core class that represents the simulationManager, the thread that permits to interact with robots, ordersource and all the algorithms.
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-18
 */
public class SimulationManager extends Observable implements Observer, Runnable {

    public static final Integer RUNNING = 1;

    public static final Integer PAUSED = 2;

    public static final Integer STOPPED = 0;

    // relationship
    /**
     * Interface store algorithm
     */
    private IAlgStore iAlgStore;
    /**
     * Interface move algorithm
     */
    private IAlgMove iAlgMove;
    /**
     * Interface destocking algorithm
     */
    private IAlgDestocking iAlgDestocking;

    /**
     * ArrayList of robots
     */
    private ArrayList<Robot> robots;
    /**
     * ArrayList of orders
     */
    private ArrayList<Order> orders;
    /**
     * Order source
     */
    private OrderSource orderSource;
    /**
     * Map
     */
    private Map map;

    // own attributes
    /**
     * Speed
     */
    private Float speed = (float) 1;
    /**
     * Number of robots
     */
    private Integer nbRobot = 3;
    /**
     * Status
     */
    private Integer status = 0;
    /**
     * Boolean was in pause (by default = false)
     */
    private boolean wasInPause = false;
    /**
     * Boolean source
     */
    private boolean source;

    /**
     * Uptime of simulation manager
     */
    private long uptime;
    /**
     * ArrayList of stockProducts
     */
    private ArrayList<Product> stockProducts;
    /**
     * ArrayList of new logs
     */
    private ArrayList<String> newLogs;

    /**
     * Data for chart
     */
    private ArrayList<HashMap<Long, Integer>> dataRobotActivity;
    private ArrayList<HashMap<Long, Integer>> dataRobotConsumption;
    private HashMap<Long, Integer> dataConsumption;
    private HashMap<Long, Integer> dataOrder;
    private HashMap<Long, Integer> dataOrderTotal;

    private ArrayList<Thread> robotThreads;

    /**
     * Constructor of simulation manager
     */
    public SimulationManager() {
	this.map = new Map();
	this.map.sortColumns();
	this.setNewLogs(new ArrayList<String>());
	this.robots = new ArrayList<Robot>();
	this.orderSource = new OrderSource();
	this.source = false;
	this.iAlgStore = new AlgStoreFifo();
	this.iAlgDestocking = new AlgDestockingFifo();
	this.iAlgMove = new AlgMoveEco();
	this.orders = new ArrayList<Order>();
	this.stockProducts = new ArrayList<Product>();
	this.dataRobotActivity = new ArrayList<HashMap<Long, Integer>>();
	this.dataRobotConsumption = new ArrayList<HashMap<Long, Integer>>();
	this.dataConsumption = new HashMap<Long, Integer>();
	this.dataOrder = new HashMap<Long, Integer>();
	this.dataOrderTotal = new HashMap<Long, Integer>();
    }

    /**
     * @return status
     */
    public Integer getStatus() {
	return this.status;
    }

    /**
     * @return source
     */
    public boolean getSource() {
	return this.source;
    }

    public OrderSource getOrderSource() {
	return orderSource;
    }

    public void setOrderSource(OrderSource orderSource) {
	this.orderSource = orderSource;
    }

    /**
     * @return speed
     */
    public Float getSpeed() {
	return this.speed;
    }

    /**
     * @return store algorithm
     */
    public IAlgStore getiAlgStore() {
	return this.iAlgStore;
    }

    /**
     * @return move algorithm
     */
    public IAlgMove getiAlgMove() {
	return this.iAlgMove;
    }

    /**
     * @return destocking algorithm
     */
    public IAlgDestocking getiAlgDestocking() {
	return this.iAlgDestocking;
    }

    /**
     * @return number of robots
     */
    public Integer getNbRobot() {
	return this.nbRobot;
    }

    /**
     * @return uptime
     */
    public Long getUptime() {
	return this.uptime;
    }

    @Override
    public void run() {
	status = SimulationManager.RUNNING;

	robotThreads = new ArrayList<Thread>();
	// add robots to the simulation
	if (!this.wasInPause) {
	    robots.clear();
	    dataRobotActivity.clear();
	    dataRobotConsumption.clear();
	    dataConsumption.clear();
	    dataOrder.clear();
	    dataOrderTotal.clear();
	    orders.clear();
	    for (int i = 0; i < nbRobot; i++) {
		Robot r = new Robot((Rail) getMap().getMap()[1 + i][1], i, this);
		r.addObserver(SimulationManager.this);
		robots.add(r);
		dataRobotActivity.add(new HashMap<Long, Integer>());
		dataRobotConsumption.add(new HashMap<Long, Integer>());
		Thread t = new Thread(r);
		t.setName("#"+r+"#");
		t.start();
		robotThreads.add(t);
	    }
	}

	uptime = 0;
	// on boucle tant que la simulation n'est pas arrêtée
	while (status != SimulationManager.STOPPED) {
	    Long startTime = System.currentTimeMillis();

	    if (status == SimulationManager.RUNNING) {

		// on reveille tous les robots à la sortie de la pause
		if (wasInPause) {
		    for (Robot r : this.robots) {
			synchronized (r) {
			    r.notify();
			}
		    }
		    wasInPause = false;
		}

		// on reveille les robots qui sont en veille et qui ont des actions a effectuer
		for (Robot r : this.robots) {
		    synchronized (r.getStatus()) {
			if (r.getStatus() == Robot.STATUS_SLEEPING && r.getActions().size() > 0) {
			    synchronized (r) {
				r.notify();
			    }
			}
		    }
		}

		ArrayList<Order> newOrders;
		ArrayList<Product> newProducts = null;
		if (!source) // random mode
		{
		    newOrders = SimulationManager.this.orderSource.getRandomOrders();
		    for (Order order : newOrders)
			order.setProcessingTime(this.getUptime());
		    synchronized (SimulationManager.this.orders) {
			SimulationManager.this.orders.addAll(newOrders);
		    }
		    if (this.map.getInput().getProductList().size() < 2)
			newProducts = SimulationManager.this.orderSource.getRandomProducts(this.getOrdersNotDone(), SimulationManager.this.stockProducts);
		} else // scenario mode
		{
		    newOrders = SimulationManager.this.orderSource.getScenarioOrders(SimulationManager.this.getUptime());
		    for (Order order : newOrders)
			order.setProcessingTime(this.getUptime());

		    synchronized (SimulationManager.this.orders) {
			SimulationManager.this.orders.addAll(newOrders);
		    }
		    if (this.map.getInput().getProductList().size() < 2)
			newProducts = SimulationManager.this.orderSource.getScenarioProducts(SimulationManager.this.getUptime());

		}

		if (newProducts != null) {
		    for (Product product : newProducts) {
			this.map.getInput().addProduct(product);
			this.stockProducts.add(product);
		    }
		}

		ArrayList<StoreAction> newStoreActions = SimulationManager.this.iAlgStore.getActions(newProducts, this.getOrdersNotDone(), SimulationManager.this.map);

		ArrayList<DestockingAction> newDestockActions = SimulationManager.this.iAlgDestocking.getActions(this.orders, stockProducts, this.iAlgStore);

		SimulationManager.this.iAlgMove.updateRobotsActions(newDestockActions, newStoreActions, SimulationManager.this.robots, this.map);

		// notify observers (UIController)
		if (this.status != SimulationManager.STOPPED) {
		    SimulationManager.this.setChanged();
		    SimulationManager.this.notifyObservers();
		}

		// Fill chart data (used for statistics)
		for (int cptRobot = 0; cptRobot < this.robots.size(); cptRobot++) {
		    dataRobotActivity.get(cptRobot).put(uptime, this.robots.get(cptRobot).getActions().size());
		}
		for (int cptRobot = 0; cptRobot < this.robots.size(); cptRobot++) {
		    dataRobotConsumption.get(cptRobot).put(uptime, this.robots.get(cptRobot).getConsumption());
		}
		dataConsumption.put(uptime, this.getTotalConsumption());
		dataOrder.put(uptime, this.getOrdersDoneCount());
		dataOrderTotal.put(uptime, this.orders.size());

		// si le scénario est terminé on stope la simulation
		if (orderSource.isFinished() && this.getOrdersNotDone().size() == 0) {
		    this.status = SimulationManager.STOPPED;
		    continue;
		}

		// sleep
		try {
		    long dureeTour = 1000; // 1seconde
		    long pause = (long) ((long) (dureeTour - (System.currentTimeMillis() - startTime)) / speed);
		    if (pause > 0)
			Thread.sleep(pause);

		    uptime += dureeTour;

		} catch (InterruptedException e) {
		    e.printStackTrace();
		}

	    } else if (status == SimulationManager.PAUSED) {

		// on met en pause les robots
		for (Robot r : this.robots) {
		    r.stopTimerTask();
		}

		// on met le thread du simulateur en pause
		try {
		    synchronized (this) {
			this.wait();
		    }
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}

	System.out.println("Simulation terminée");
	this.stopSimulation();
    }

    /**
     * @return map
     */
    public Map getMap() {
	return map;
    }

    /**
     * Set stop
     */
    public void setStop() {
	this.status = SimulationManager.STOPPED;
    }

    private void stopSimulation() {

	// on arrête les threads des robots
	for (Robot r : SimulationManager.this.robots) {
	    // on cancel le timer en cours
	    synchronized (r) {
		// on notifie le robot pour qu'il fasse un tour de boucle
		r.notify();
	    }
	    // et on attend que le thread du robots soient tous terminés.
	    synchronized (SimulationManager.this.robotThread(r)) {
		try {
		    if (SimulationManager.this.robotThread(r).isAlive())
			SimulationManager.this.robotThread(r).join();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}

	SimulationManager.this.map.getInput().clearProducts();
	SimulationManager.this.map.getOutput().clearProducts();
	SimulationManager.this.setNewLogs(new ArrayList<String>());
	SimulationManager.this.orderSource = new OrderSource();
	SimulationManager.this.source = false;
	SimulationManager.this.stockProducts = new ArrayList<Product>();

	// suppression des produits dans les tiroirs
	for (Column col : SimulationManager.this.map.getColumns()) {
	    for (Drawer dra : col.getDrawerList()) {
		dra.setProduct(null);
		dra.setStatus(Drawer.FREE);
	    }
	}

	Order.resetLastId();
	SimulationManager.this.setChanged();
	SimulationManager.this.notifyObservers();
	
    }

    /**
     * Set pause
     */
    public void setPause() {
	this.status = SimulationManager.PAUSED;
    }

    /**
     * Set play
     */
    public void setPlay() {
	if (status == SimulationManager.PAUSED) {
	    this.wasInPause = true;
	}
	this.status = SimulationManager.RUNNING;
	synchronized (this) {
	    this.notify();
	}
    }

    /*
     * update method called by the robots : updating the logs
     */
    @Override
    public void update(Observable o, Object arg) { // called by a robot
	if (o instanceof Robot) {
	    Robot robot = (Robot) o;

	    synchronized (this.newLogs) {

		// Traçage des actions des robots
		if (robot.getActions().get(0) instanceof InputAction) {
		    InputAction action = (InputAction) robot.getActions().get(0);
		    this.newLogs.add(robot + " prend " + action.getProduct().getName() + " au point d'entrée");
		} else if (robot.getActions().get(0) instanceof OutputAction) {
		    OutputAction action = (OutputAction) robot.getActions().get(0);
		    this.newLogs.add(robot + " dépose " + action.getProduct().getName() + " au point de sortie");
		} else if (robot.getActions().get(0) instanceof StoreAction) {
		    StoreAction action = (StoreAction) robot.getActions().get(0);
		    this.newLogs.add(robot + " dépose " + action.getProduct().getName() + " dans la colonne (" + action.getDrawer().getColumn().getX() + ", " + action.getDrawer().getColumn().getY() + ") dans le tiroir " + action.getDrawer().getPositionInColumn());
		} else if (robot.getActions().get(0) instanceof DestockingAction) {
		    DestockingAction action = (DestockingAction) robot.getActions().get(0);
		    this.newLogs.add(robot + " prend " + action.getProduct().getName() + " dans la colonne (" + action.getDrawer().getColumn().getX() + ", " + action.getDrawer().getColumn().getY() + ") dans le tiroir " + action.getDrawer().getPositionInColumn());
		}
	    }

	    if (this.status != SimulationManager.STOPPED) {
		this.setChanged();
		this.notifyObservers();
	    }
	}
    }

    /**
     * 
     * @return the ArrayList of order that have their status != Done
     */
    public ArrayList<Order> getOrdersNotDone() {
	ArrayList<Order> returnedOrders = new ArrayList<Order>();
	synchronized (this.orders) {
	    for (Order o : this.orders) {
		if (o.getStatus() != Order.DONE) {
		    returnedOrders.add(o);
		}

	    }
	}
	return returnedOrders;
    }

    /**
     * Set store algorithm
     * 
     * @param iAlgStore
     */
    public void setiAlgStore(IAlgStore iAlgStore) {
	this.iAlgStore = iAlgStore;
    }

    /**
     * Set move algorithm
     * 
     * @param iAlgMove
     */
    public void setiAlgMove(IAlgMove iAlgMove) {
	this.iAlgMove = iAlgMove;
    }

    /**
     * Set destocking algorithm
     * 
     * @param iAlgDestocking
     */
    public void setiAlgDestocking(IAlgDestocking iAlgDestocking) {
	this.iAlgDestocking = iAlgDestocking;
    }

    /**
     * Set random mode
     */
    public void setSource(boolean mode) {
	Order.resetLastId();
	if (!mode) {
	    orderSource.setScenarioFile(null);
	}
	source = mode;
    }

    /**
     * Set scenario file
     * 
     * @param file
     */
    public void setFile(File file) {
	source = true;
	orderSource.setScenarioFile(file);
    }

    /**
     * Set speed
     * 
     * @param speed
     */
    public void setSpeed(Float speed) {
	speed = (speed >= 0) ? speed : 0.0f;
	this.speed = 1 + (speed / 100);
    }

    /**
     * Set number of robots
     * 
     * @param nbRobot
     */
    public void setNbRobot(Integer nbRobot) {
	this.nbRobot = (nbRobot >= 0) ? nbRobot : 0;
    }

    /**
     * Set end of simulation
     */
    public void setEndSimulation() {
	this.setSpeed((float) 9999);
	for (Robot r : robots) {
	    r.deleteObservers();
	}
	this.setPlay();
    }

    /**
     * @return ArrayList of robots
     */
    public ArrayList<Robot> getRobots() {
	return this.robots;
    }

    /**
     * @param robot
     * @param action
     * @return nextAction of robot and action passed in parameter
     */
    public Robot checkNextAction(Robot robot, Action action) {
	if (action instanceof MoveAction) {
	    MoveAction moveAction = (MoveAction) action;
	    for (Robot r : robots) {
		if (r != robot) {
		    if (moveAction.getNext() == r.getRail()) {
			return r;
		    }
		}
	    }
	}
	return null;
    }

    /**
     * @return ArrayList of orders
     */
    public ArrayList<Order> getOrders() {
	return this.orders;
    }

    /**
     * @return ArrayList of new logs
     */
    public ArrayList<String> getNewLogs() {
	return newLogs;
    }

    /**
     * Set new logs
     * 
     * @param newLogs
     */
    public void setNewLogs(ArrayList<String> newLogs) {
	this.newLogs = newLogs;
    }

    /**
     * @return average consumption by order
     */
    public int getAverageConsumption() {
	int count = 0;
	ArrayList<Order> copyOrders;
	synchronized (this.orders) {
	    copyOrders = new ArrayList<Order>(this.orders);
	}
	for (Order order : copyOrders) {
	    if (order.getStatus() != Order.INIT)
		count++;
	}
	int totalConsumption = this.getTotalConsumption();
	if (totalConsumption != 0 && count != 0)
	    return (int) (totalConsumption / count);
	else
	    return 0;
    }

    /**
     * @return average processing time to prepare order
     */
    public long getAverageOrderProcessingTime() {
	if (orders.size() > 0) {
	    long count = 0;
	    int orderCount = 0;
	    ArrayList<Order> copyOrders;
	    synchronized (this.orders) {
		copyOrders = new ArrayList<Order>(this.orders);
	    }
	    for (Order order : copyOrders) {
		if (order.getStatus() == Order.DONE) {
		    orderCount++;
		    count += order.getProcessingTime();
		}
	    }
	    if (count != 0 && orderCount != 0)
		return (long) (count / orderCount);
	    else
		return 0;
	} else
	    return 0;
    }

    /**
     * @return total consumption of robots
     */
    public int getTotalConsumption() {
	int cons = 0;
	for (Robot robot : this.getRobots()) {
	    cons += robot.getConsumption();
	}
	return cons;
    }

    /**
     * @return numbers of orders prepared
     */
    public int getOrdersDoneCount() {
	int count = 0;

	ArrayList<Order> copyOrders;
	synchronized (this.orders) {
	    copyOrders = new ArrayList<Order>(this.orders);
	}

	for (Order order : copyOrders) {
	    if (order.getStatus() == Order.DONE)
		count++;
	}
	return count;
    }

    public ArrayList<HashMap<Long, Integer>> getDataRobotActivity() {
	return this.dataRobotActivity;
    }

    public ArrayList<HashMap<Long, Integer>> getDataRobotConsumption() {
	return this.dataRobotConsumption;
    }

    public HashMap<Long, Integer> getDataConsumption() {
	return this.dataConsumption;
    }

    public HashMap<Long, Integer> getDataOrder() {
	return this.dataOrder;
    }

    public HashMap<Long, Integer> getDataOrderTotal() {
	return this.dataOrderTotal;
    }

    public Thread robotThread(Robot r) {
	return this.robotThreads.get(SimulationManager.this.robots.indexOf(r));
    }

}
