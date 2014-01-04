package ror.core;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.text.DefaultEditorKit.CopyAction;

import com.sun.corba.se.spi.copyobject.CopierManager;

import ror.core.actions.Action;
import ror.core.actions.DestockingAction;
import ror.core.actions.InputAction;
import ror.core.actions.MoveAction;
import ror.core.actions.OutputAction;
import ror.core.actions.PauseAction;
import ror.core.actions.StoreAction;
import ror.core.Order;

public class Robot extends Observable implements Runnable {

    public static final int STATUS_RUNNING = 1;

    public static final int STATUS_SLEEPING = 2;

    public static final int STATUS_PAUSE = 3;

    /**
     * First speed
     */
    public static final float SPEED_1 = 1;
    /**
     * Second speed
     */
    public static final float SPEED_2 = 0.5f;
    /**
     * Third speed
     */
    public static final float SPEED_3 = 0.25f;

    /**
     * Traveled distance
     */
    private Integer traveledDistance = 0;
    /**
     * Consumption in watts
     */
    private Integer consumption = 0;
    /**
     * Actions list
     */
    private ArrayList<Action> actions = null;
    /**
     * Robot on Rail !!!
     */
    private Rail rail = null;
    /**
     * Speed default
     */
    private Float speed = SPEED_1;
    /**
     * Working or not (false by default)
     */
    private Boolean working = false;
    /**
     * Order in progress
     */
    private Order orderInProgress = null;
    /**
     * Robot status
     */
    private Integer status = 0;
    /**
     * Products list
     */
    private ArrayList<Product> products = null;

    /**
     * Last robot move
     */
    private MoveAction lastMove;
    /**
     * Robot number
     */
    private Integer number;
    /**
     * Simulation manager
     */
    private SimulationManager simulationManager;

    /**
     * Timer
     */
    private Timer timer = null;
    /**
     * Timer task
     */
    private TimerTask timerTask = null;

    /**
     * Constructor
     * 
     * @param initRail
     * @param num
     * @param simulationManager
     */
    public Robot(Rail initRail, Integer num, SimulationManager simulationManager) {
	number = num;
	actions = new ArrayList<Action>();
	rail = initRail;
	rail.setRobot(this);
	products = new ArrayList<Product>();
	this.consumption = 0;
	this.simulationManager = simulationManager;
	timer = new Timer();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Robot #" + getNumber();
    }

    /**
     * @return robot number
     */
    public Integer getNumber() {
	return number;
    }

    /**
     * Set robot number
     * 
     * @param number
     */
    public void setNumber(Integer number) {
	this.number = number;
    }

    private void waitForTimer(int duration) {

	timer = new Timer();
	timerTask = new TimerTask() {
	    public void run() {
		synchronized (Robot.this) {
		    Robot.this.notify();
		}
	    }
	};

	timer.schedule(timerTask, duration);
	synchronized (Robot.this) {
	    try {
		Robot.this.wait();
		this.stopTimerTask();
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }

    /**
     * Execute action passed in parameter
     * 
     * @param action
     */
    private void executeAction(final Action action) {

	// création d'un timer par action
	if (action instanceof MoveAction) {
	    // aguillage du prochain rail

	    MoveAction moveAction = (MoveAction) action;
	    // aiguillage du prochain rail
	    moveAction.getPrevious().setNextRail(moveAction.getNext());

	    if (lastMove != null) {
		// detection d'erreurs dans les deplacements
		if (!moveAction.getPrevious().getPreviousRail().contains(lastMove.getPrevious())) {
		    Integer countActions = 0;

		    for (Action tmpCountAction : Robot.this.getActions()) {
			if (!(tmpCountAction instanceof MoveAction))
			    countActions++;
		    }
		    System.out.println("Erreur :  " + Robot.this + " " + moveAction.getPrevious() + " <- " + lastMove.getPrevious() + " -- Nombre d'actions : " + countActions + " en cours " + Robot.this.actions.get(0).getClass().getSimpleName());
		}
	    }

	    this.waitForTimer((int) (action.getDuration() * Robot.this.speed));

	    lastMove = moveAction;

	    this.setOrderInProgress(null);
	    this.rail.setRobot(null);
	    this.rail.lock.unlock();
	    if (((MoveAction) action).getNext() != null)
		Robot.this.rail = ((MoveAction) action).getNext();
	    Robot.this.rail.setRobot(Robot.this);
	    Robot.this.consumption += (int) (20 / speed); // TODO vérifier le calcul
	    Robot.this.traveledDistance++;

	} else if (action instanceof StoreAction) {
	    final StoreAction storeAction = ((StoreAction) action);
	    this.setOrderInProgress(storeAction.getProduct().getOrder());

	    this.waitForTimer(action.getDuration());

	    Drawer drawer = storeAction.getDrawer();
	    Product product = storeAction.getProduct();

	    if (drawer.getProduct() != null)
		System.out.println("\nErreur tiroir plein\n " + drawer);

	    Robot.this.removeProduct(product); // on supprimer le produit du plateau
	    drawer.setProduct(product); // on associe le tiroir au produit
	    product.setDrawer(drawer); // on associe le produit au tiroir

	    // dans le cas du stockage par commande
	    if (product.getOrder() != null) {
		product.getOrder().addProduct(storeAction.getProduct());
	    }

	} else if (action instanceof DestockingAction) {
	    final DestockingAction destockingAction = ((DestockingAction) action);

	    this.waitForTimer(action.getDuration());

	    this.setOrderInProgress(destockingAction.getProduct().getOrder());

	    Drawer drawer = destockingAction.getDrawer();
	    Product product = destockingAction.getProduct();

	    product.getOrder().setStatus(Order.BEING_DESTOCKED);
	    drawer.getStatus();
	    if (drawer.getProduct() == null) {
		System.out.println("\n" + Robot.this + " Erreur destocking " + drawer + " : product null \n");
	    }

	    Robot.this.addProduct(product);
	    drawer.setProduct(null); // status is set to free in setProduct
	    product.setDrawer(null); // on supprime le tiroir au produit

	} else if (action instanceof InputAction) {
	    final InputAction inputAction = ((InputAction) action);
	    this.setOrderInProgress(inputAction.getProduct().getOrder());

	    this.waitForTimer(action.getDuration());

	    if (inputAction.getProduct() == null) {
		System.out.println("\n Erreur input action product null " + Robot.this + "\n");
	    }
	    Robot.this.addProduct(inputAction.getProduct());
	    inputAction.getInput().removeProduct(inputAction.getProduct());

	} else if (action instanceof OutputAction) {
	    final OutputAction outputAction = ((OutputAction) action);
	    this.setOrderInProgress(outputAction.getProduct().getOrder());

	    this.waitForTimer(action.getDuration());

	    Robot.this.removeProduct(outputAction.getProduct());
	    outputAction.getOutput().addProduct(outputAction.getProduct());
	    // TODO : check mise à jour état commande
	    Order o = outputAction.getProduct().getOrder();

	    synchronized (o.getProducts()) {

		Boolean orderDone = true;
		for (Product p : o.getProducts()) {
		    if (p.getStatus() != Product.DONE) {
			orderDone = false;
		    }
		}
		if (orderDone) {
		    synchronized (outputAction.getOutput().getProductList()) {
			outputAction.getOutput().getProductList().removeAll(o.getProducts());
		    }

		    o.setStatus(Order.DONE);
		    o.setProcessingTime(simulationManager.getUptime() - o.getProcessingTime());
		}
	    }

	} else if (action instanceof PauseAction) {
	    this.setOrderInProgress(null);
	    this.waitForTimer(action.getDuration());

	}
    }

    /**
     * @return current action
     */
    private Action getCurrentAction() {
	synchronized (this.actions) {

	    if (actions != null && actions.size() > 0)
		return actions.get(0);
	    else
		return null;
	}
    }

    /**
     * Remove current action
     */
    public void removeCurrentAction() {
	synchronized (this.actions) {
	    if (actions != null && actions.size() > 0)
		this.actions.remove(0);
	}
    }

    /**
     * Add product on robot platform
     * 
     * @param product
     */
    private void addProduct(Product product) {

	products.add(product);
    }

    /**
     * Remove product on robot platform
     * 
     * @param product
     */
    private void removeProduct(Product product) {
	synchronized (this.products) {
	    products.remove(product);
	}
    }

    /**
     * @return ArrayList of products on the platform
     */
    public ArrayList<Product> getProducts() {
	return products;
    }

    /**
     * Add an action passed in parameter
     * 
     * @param action
     */
    public void addAction(Action action) {

	synchronized (this.actions) {

	    actions.add(action);
	}
    }

    /**
     * @return Traveled distance
     */
    public Integer getTraveledDistance() {
	return traveledDistance;
    }

    /**
     * @return consumption in watts
     */
    public Integer getConsumption() {
	return consumption;
    }

    /**
     * @return ArrayList of actions
     */
    public ArrayList<Action> getActions() {
	return actions;
    }

    /**
     * @return Rail under robot
     */
    public Rail getRail() {
	return rail;
    }

    /**
     * @return Working state
     */
    public Boolean getWorking() {
	return working;
    }

    /**
     * @return robot status
     */
    public Integer getStatus() {
	return status;
    }

    /**
     * @return Order in progress
     */
    public Order getOrderInProgress() {
	return orderInProgress;
    }

    /**
     * Set an order in progress
     * 
     * @param orderInProgress
     */
    public void setOrderInProgress(Order orderInProgress) {
	this.orderInProgress = orderInProgress;
    }

    /**
     * @return Last action rail
     */
    public Rail getLastActionRail() {
	Action lastAction;
	ArrayList<Action> copyActions;
	synchronized (this.actions) {
	    copyActions = new ArrayList<Action>(this.actions);
	}

	if (copyActions.size() > 0) {
	    lastAction = copyActions.get(copyActions.size() - 1);

	    if (lastAction instanceof MoveAction) {
		return ((MoveAction) lastAction).getNext();
	    } else if (lastAction instanceof StoreAction) {
		return ((StoreAction) lastAction).getDrawer().getColumn().getAccess();
	    } else if (lastAction instanceof DestockingAction) {
		return ((DestockingAction) lastAction).getDrawer().getColumn().getAccess();
	    } else if (lastAction instanceof OutputAction) {
		return ((OutputAction) lastAction).getOutput().getAccess();
	    } else if (lastAction instanceof InputAction) {
		return ((InputAction) lastAction).getInput().getAccess();
	    } else
		return this.rail;
	} else
	    return this.rail;

    }

    /**
     * @return Number of products that robot can transport
     */
    public Integer getLastActionSpaceAvailability() {
	int count = this.products.size();

	synchronized (this.actions) {

	    for (Action action : actions) {

		if (action instanceof StoreAction) {
		    count--;
		} else if (action instanceof DestockingAction) {
		    count++;
		} else if (action instanceof OutputAction) {
		    count--;
		} else if (action instanceof InputAction) {
		    count++;
		}
	    }

	    return 10 - count;
	}
    }

    /**
     * For a robot which block
     * 
     * @return the next available rail
     */
    public Rail getOpositeRailAtNextIntersection() {
	Rail intersectionRail = null;
	MoveAction lastAction = null;

	ArrayList<Action> copyActions;
	synchronized (this.actions) {
	    copyActions = new ArrayList<Action>(this.actions);
	}
	// Parcours des actions

	for (Action action : copyActions) {
	    if (action instanceof MoveAction) {
		intersectionRail = ((MoveAction) action).getNext();

		// Le rail est une intersection
		if (intersectionRail.getLeftRail() != null && rail.getRightRail() != null) {
		    if (intersectionRail.getLeftRail() == ((MoveAction) action).getNext())
			return intersectionRail.getRightRail();
		    else
			return intersectionRail.getLeftRail();
		}
		lastAction = (MoveAction) action;
	    }
	}

	return lastAction.getNext().getRightRail();
    }

    /**
     * @return robot speed
     */
    public Float getSpeed() {
	return speed;
    }

    /**
     * Set robot speed
     * 
     * @param speed
     */
    public void setSpeed(Float speed) {
	this.speed = speed;
    }

    private void moveBlockingRobot(Robot blockingRobot) {

	System.out.println(this + " bloqué par " + blockingRobot + " qui a le status : " + blockingRobot.getStatus() + " et " + blockingRobot.getActions().size() + " actions");

	// si le robot qui bloque n'a pas prévu d'avancer
	if (blockingRobot.willMove() == false) {

	    // on fait avancer le robot jusqu'à la prochaine intersection ou jusqu'au rail suivant
	    ArrayList<MoveAction> movesBlockingRobot = this.simulationManager.getiAlgMove().railsToMoveActions(simulationManager.getMap().getPath(blockingRobot.getLastActionRail(), this.getOpositeRailAtNextIntersection()));
	    blockingRobot.getActions().addAll(movesBlockingRobot);

	}
	// si le robot dort on le reveille
	synchronized (blockingRobot.status) {

	    if (blockingRobot.getStatus() == Robot.STATUS_SLEEPING) {
		synchronized (blockingRobot) {
		    blockingRobot.notify();
		}
	    }
	}

    }

    public boolean willMove() {
	for (Action a : this.actions) {
	    if (a instanceof MoveAction) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public void run() {
	do {
	    int simulationStatus = this.simulationManager.getStatus();

	    // on lock le rail courant la premiere fois a l'initialisation de la simulation
	    if (lastMove == null && !this.rail.lock.isLocked()) {
		this.rail.lock.lock();
	    }

	    // mise en veille si plus d'actions ou si simulation en pause ou arrêtée
	    if (this.getCurrentAction() == null || simulationStatus == SimulationManager.PAUSED || simulationStatus == SimulationManager.STOPPED) {

		// maj du status si pas d'actions
		synchronized (this.status) {
		    this.status = Robot.STATUS_SLEEPING;
		}

		// mise en pause du robot
		synchronized (this) {
		    try {
			this.wait();
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}

		synchronized (this.status) {
		    this.status = Robot.STATUS_RUNNING;
		}
	    } else {
		if (this.actions.get(0) instanceof MoveAction) {
		    MoveAction move = (MoveAction) this.actions.get(0);
		    Robot blockingRobot = this.simulationManager.checkNextAction(this, move);

		    if (blockingRobot != null) {
			this.moveBlockingRobot(blockingRobot);
		    }
		    Rail r = move.getNext();
		    r.lock.lock();
		}
		// execution de la prochaine action
		Action a;
		synchronized (this.actions) {
		    a = this.actions.get(0);
		}
		a.setDuration((int) (1000 * this.simulationManager.getSpeed()));

		this.executeAction(a);

		this.setChanged();
		this.notifyObservers();
		synchronized (this.actions) {
		    this.actions.remove(0);
		}

	    }

	} while (true);
    }

    public Timer getTimer() {
	return timer;
    }

    public void setTimer(Timer timer) {
	this.timer = timer;
    }

    public TimerTask getTimerTask() {
	return timerTask;
    }

    public void setTimerTask(TimerTask timerTask) {
	this.timerTask = timerTask;
    }

    public void stopTimerTask() {
	if (this.timer != null) {
	    timer.cancel();
	    timer.purge();
	    timer = null;
	}
	if (this.timerTask != null) {
	    timerTask.cancel();
	    timerTask = null;
	}
    }

}
