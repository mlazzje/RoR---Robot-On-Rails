package ror.core;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import ror.core.actions.Action;
import ror.core.actions.DestockingAction;
import ror.core.actions.InputAction;
import ror.core.actions.MoveAction;
import ror.core.actions.OutputAction;
import ror.core.actions.PauseAction;
import ror.core.actions.StoreAction;
import ror.core.Order;

public class Robot extends Observable {

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
     * Timer
     */
    private Timer timer = null;
    /**
     * Timer task
     */
    private TimerTask timerTask = null;
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
     * Constructor
     * 
     * @param initRail
     * @param num
     * @param simulationManager
     */
    public Robot(Rail initRail, Integer num, SimulationManager simulationManager) {
	number = num;
	timer = new Timer();
	actions = new ArrayList<Action>();
	rail = initRail;
	products = new ArrayList<Product>();
	initRail.setRobot(this);
	this.consumption = 0;
	this.simulationManager = simulationManager;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Robot " + getNumber();
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

    /**
     * Stop schedule timer
     */
    public void stopSchedule() {
	synchronized (timer) {

	    if (timerTask != null) {
		timerTask.cancel();
	    }
	    timer.cancel();
	    timer.purge();
	    timer = new Timer();
	}
    }

    /**
     * Execute action passed in parameter
     * 
     * @param action
     */
    public void executeAction(final Action action) {
	// add action to the actions list if it no exist (only happens if we add
	// a pause action)
	synchronized (this.actions) {
	    if (this.actions.size() > 0 && action != this.actions.get(0) && action != null)
		this.actions.add(0, action);
	}
	// création d'un timer par action
	if (action instanceof MoveAction) {
	    MoveAction moveAction = (MoveAction) action;
	    if (lastMove != null) {
		// detection d'erreurs dans les deplacements
		if (!moveAction.getPrevious().getPreviousRail().contains(lastMove.getPrevious())) {
		    Integer countActions = 0;

		    synchronized (Robot.this.getActions()) {
			for (Action tmpCountAction : Robot.this.getActions()) {
			    if (!(tmpCountAction instanceof MoveAction))
				countActions++;
			}
		    }
		    System.out.println("Erreur :  " + Robot.this + " " + moveAction.getPrevious() + " <- " + lastMove.getPrevious() + " -- Nombre d'actions : " + countActions + " en cours " + Robot.this.getCurrentAction().getClass().getSimpleName());

		}
	    }
	    lastMove = moveAction;
	    this.setOrderInProgress(null);
	    synchronized (timer) {
		timerTask = new TimerTask() {
		    public void run() {
			Robot.this.rail.setRobot(null);
			if (((MoveAction) action).getNext() != null)
			    Robot.this.rail = ((MoveAction) action).getNext();
			Robot.this.rail.setRobot(Robot.this);
			Robot.this.consumption += (int) (20 / speed); // TODO vérifier le calcul
			Robot.this.traveledDistance++;
			Robot.this.setChanged();
			Robot.this.notifyObservers();
		    }
		};
		timer.schedule(timerTask, (int) (action.getDuration() * Robot.this.speed));
	    }
	} else if (action instanceof StoreAction) {
	    final StoreAction storeAction = ((StoreAction) action);
	    this.setOrderInProgress(storeAction.getProduct().getOrder());
	    synchronized (timer) {
		timerTask = new TimerTask() {
		    public void run() {
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

			Robot.this.setChanged();
			Robot.this.notifyObservers();
		    }
		};
		timer.schedule(timerTask, action.getDuration());
	    }
	} else if (action instanceof DestockingAction) {
	    final DestockingAction destockingAction = ((DestockingAction) action);
	    this.setOrderInProgress(destockingAction.getProduct().getOrder());
	    synchronized (timer) {
		timerTask = new TimerTask() {
		    public void run() {
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

			Robot.this.setChanged();
			Robot.this.notifyObservers();
		    }
		};
	    }
	    timer.schedule(timerTask, action.getDuration());

	} else if (action instanceof InputAction) {
	    final InputAction inputAction = ((InputAction) action);
	    this.setOrderInProgress(inputAction.getProduct().getOrder());
	    synchronized (timer) {

		timerTask = new TimerTask() {
		    public void run() {
			if (inputAction.getProduct() == null) {
			    System.out.println("\n Erreur input action product null " + Robot.this + "\n");
			}
			Robot.this.addProduct(inputAction.getProduct());
			inputAction.getInput().removeProduct(inputAction.getProduct());

			Robot.this.setChanged();
			Robot.this.notifyObservers();
		    }
		};
		timer.schedule(timerTask, action.getDuration());
	    }
	} else if (action instanceof OutputAction) {
	    final OutputAction outputAction = ((OutputAction) action);
	    this.setOrderInProgress(outputAction.getProduct().getOrder());
	    synchronized (timer) {

		timerTask = new TimerTask() {
		    public void run() {

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
				o.setProcessingTime(simulationManager.getUptime()-o.getProcessingTime());
			    }
			}

			Robot.this.setChanged();
			Robot.this.notifyObservers();
		    }
		};
		timer.schedule(timerTask, action.getDuration());
	    }

	} else if (action instanceof PauseAction) {
	    this.setOrderInProgress(null);
	    synchronized (timer) {

		timerTask = new TimerTask() {
		    public void run() {
			// System.out.println(Robot.this +" effectue une pause");
			Robot.this.setChanged();
			Robot.this.notifyObservers();
		    }
		};
		timer.schedule(timerTask, action.getDuration());
	    }
	}
    }

    /**
     * @return current action
     */
    public Action getCurrentAction() {
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
	synchronized (this.actions) {

	    if (this.actions.size() > 0) {
		lastAction = this.actions.get(this.actions.size() - 1);

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
	synchronized (this.actions) {
	    // Parcours des actions

	    for (Action action : this.actions) {
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

}
