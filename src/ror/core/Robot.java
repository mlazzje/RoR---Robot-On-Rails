package ror.core;

import java.awt.List;
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

public class Robot extends Observable {

	private Integer traveledDistance = 0;
	private Integer consumption = 0;
	private ArrayList<Action> actions = null;
	private Rail rail = null;
	private Integer speed = 0;
	private Boolean working = false;
	private Order orderInProgress = null; // contient la commande en cours si il
											// y'en a une
	private Integer status = 0;
	private ArrayList<Product> products = null;
	private Timer timer = null;
	private TimerTask timerTask;

	public Robot(Rail initRail) {
		timer = new Timer();
		actions = new ArrayList<Action>();
		rail = initRail;
	}

	public void stopSchedule() {
		timerTask.cancel();
		timer.cancel();
		timer.purge();
		timer = new Timer();
	}

	public void executeAction(final Action action) {
		// ajout de l'action dans la liste dans le cas où c'est une action de
		// type PauseAction
		if (action != this.actions.get(0))
			this.actions.add(0, action);

		// création d'un timer par action
		if (action instanceof MoveAction) {
			timerTask = new TimerTask() {
				public void run() {
					Robot.this.rail = ((MoveAction) action).getNext();
					Robot.this.consumption += 20 * speed; // TODO vérifier le
															// calcul
					Robot.this.traveledDistance++;
					Robot.this.setChanged();
					Robot.this.notifyObservers();
				}
			};
			timer.schedule(timerTask, action.getDuration());

		} else if (action instanceof StoreAction) {
			final StoreAction storeAction = ((StoreAction) action);
			// TODO this.orderInProgress=storeAction.getProduct().getOrder();

			timerTask = new TimerTask() {
				public void run() {

					Drawer drawer = storeAction.getDrawer();
					Robot.this.removeProduct(storeAction.getProduct());
					// TODO drawer.setProduct(storeAction.getProduct());

					Robot.this.setChanged();
					Robot.this.notifyObservers();
				}
			};
			timer.schedule(timerTask, action.getDuration());

		} else if (action instanceof DestockingAction) {
			final DestockingAction destockingAction = ((DestockingAction) action);
			// TODO
			// this.orderInProgress=destockingAction.getProduct().getOrder();

			timerTask = new TimerTask() {
				public void run() {
					Drawer drawer = destockingAction.getDrawer();
					// TODO Robot.this.addProduct(drawer.getProduct());
					// TODO drawer.setProduct(null);

					Robot.this.setChanged();
					Robot.this.notifyObservers();
				}
			};
			timer.schedule(timerTask, action.getDuration());

		} else if (action instanceof InputAction) {
			final InputAction inputAction = ((InputAction) action);
			// TODO this.orderInProgress=inputAction.getProduct().getOrder();

			timerTask = new TimerTask() {
				public void run() {

					Robot.this.addProduct(inputAction.getProduct());
					// TODO
					// inputAction.getInput().removeProduct(inputAction.getProduct());

					Robot.this.setChanged();
					Robot.this.notifyObservers();
				}
			};
			timer.schedule(timerTask, action.getDuration());

		} else if (action instanceof OutputAction) {
			final OutputAction outputAction = ((OutputAction) action);
			// TODO this.orderInProgress=outputAction.getProduct().getOrder();
			timerTask = new TimerTask() {
				public void run() {

					Robot.this.removeProduct(outputAction.getProduct());
					// TODO
					// outputAction.getOutput().addProduct(OutputAction.getProduct());

					Robot.this.setChanged();
					Robot.this.notifyObservers();
				}
			};
			timer.schedule(timerTask, action.getDuration());

		} else if (action instanceof PauseAction) {
			final OutputAction outputAction = ((OutputAction) action);
			// TODO this.orderInProgress=outputAction.getProduct().getOrder();
			timerTask = new TimerTask() {
				public void run() {

					Robot.this.removeProduct(outputAction.getProduct());
					// TODO
					// outputAction.getOutput().addProduct(OutputAction.getProduct());

					Robot.this.setChanged();
					Robot.this.notifyObservers();
				}
			};
			timer.schedule(timerTask, action.getDuration());
		}
	}

	public Action getCurrentAction() {
		if (actions != null && actions.size() > 0)
			return actions.get(0);
		else
			return null;
	}

	public void removeCurrentAction() {
		if (actions != null && actions.size() > 0)
			this.actions.remove(0);
	}

	private void addProduct(Product product) {
		products.add(product);
	}

	private void removeProduct(Product product) {
		products.remove(product);
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public void addAction(Action action) {
		actions.add(action);
	}

	public Integer getTraveledDistance() {
		return traveledDistance;
	}

	public Integer getConsumption() {
		return consumption;
	}

	public ArrayList<Action> getActions() {
		return actions;
	}

	public Rail getRail() {
		return rail;
	}

	public Boolean getWorking() {
		return working;
	}

	public Integer getStatus() {
		return status;
	}
}
