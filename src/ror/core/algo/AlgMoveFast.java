package ror.core.algo;

import java.util.ArrayList;

import ror.core.Map;
import ror.core.Rail;
import ror.core.Robot;
import ror.core.actions.Action;
import ror.core.actions.DestockingAction;
import ror.core.actions.InputAction;
import ror.core.actions.MoveAction;
import ror.core.actions.OutputAction;
import ror.core.actions.StoreAction;

/**
 * AlgMoveFast class : Move algorithm that implements the IAlgMove interface.
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-18
 */
public class AlgMoveFast implements IAlgMove {

    /**
     * Updates the robot's actions : add list of MoveAction, DestockingAction, StoreAction, InputAction and OutputAction to the robots depending on a list of destocking actions and a list of store actions to do.
     * 
     */
    public void updateRobotsActions(ArrayList<DestockingAction> newDestockActions, ArrayList<StoreAction> newStoreActions, ArrayList<Robot> robots, Map map) {
	// si des actions input ou store sont disponibles
	if (newDestockActions != null) {
	    while (newDestockActions.size() > 0) {
		Robot robot;

		robot = getBestRobot(robots, map, newDestockActions.get(0).getDrawer().getColumn().getAccess());
		robot.setSpeed(Robot.SPEED_3);

		robot.lock.lock();
		ArrayList<Action> destockingActionToAffect = new ArrayList<Action>();
		int freeSpace = robot.getLastActionSpaceAvailabilityUnsynchronized();

		while (freeSpace > 0 && newDestockActions.size() > 0) {
		    destockingActionToAffect.add(newDestockActions.get(0));
		    newDestockActions.remove(newDestockActions.get(0));
		    freeSpace--;
		}

		// ajout des actions de mouvements et de destockage au robot
		ArrayList<Action> actionMoveAndDestock = sortActionsAndMoves(destockingActionToAffect, map, robot.getLastActionRailUnsynchronized());
		robot.getActions().addAll(actionMoveAndDestock);

		Rail start = robot.getLastActionRailUnsynchronized();
		Rail end = map.getOutput().getAccess();

		// ajout des actions des mouvements jusqu'a l'output au robot
		ArrayList<MoveAction> movesToOutput = railsToMoveActions(map.getPath(start, end));
		robot.getActions().addAll(movesToOutput);

		// ajout des actions d'output
		for (Action action : destockingActionToAffect) {
		    DestockingAction destockingAction = (DestockingAction) action;
		    OutputAction outputAction = new OutputAction(1000, robot, map.getOutput());
		    outputAction.setProduct(destockingAction.getProduct());
		    robot.getActions().add(outputAction);
		}

		robot.lock.unlock();
	    }
	}
	// si des actions input ou store sont disponibles
	if (newStoreActions != null) {
	    while (newStoreActions.size() > 0) {

		Robot robot = getBestRobot(robots, map, map.getInput().getAccess());
		robot.setSpeed(Robot.SPEED_3);
		robot.lock.lock();
		ArrayList<InputAction> inputActions = new ArrayList<InputAction>();
		for (StoreAction storeAction : newStoreActions) {
		    InputAction inputaction = new InputAction(1000, null, map.getInput());
		    inputaction.setProduct(storeAction.getProduct());
		    inputActions.add(inputaction);
		}

		Rail start = robot.getLastActionRailUnsynchronized();
		Rail end = map.getInput().getAccess();

		ArrayList<MoveAction> movesToInput = railsToMoveActions(map.getPath(start, end));
		robot.getActions().addAll(movesToInput);

		ArrayList<InputAction> affectedInputAction = new ArrayList<InputAction>();

		// ajout des inputs actions au robot
		while (robot.getLastActionSpaceAvailabilityUnsynchronized() > 0 && inputActions.size() > 0) {
		    affectedInputAction.add(inputActions.get(0));
		    robot.getActions().add(inputActions.get(0));
		    inputActions.remove(inputActions.get(0));
		}

		ArrayList<Action> storeActionsToAffect = new ArrayList<Action>();

		// on parcours les actions affectees pour recuperer les storeactions associées
		for (InputAction inputAction : affectedInputAction) {
		    for (StoreAction storeAction : newStoreActions) {
			if (inputAction.getProduct() == storeAction.getProduct()) {
			    storeActionsToAffect.add(storeAction);
			}
		    }
		}
		newStoreActions.removeAll(storeActionsToAffect);

		// ajout des actions de mouvements et de stockage au robot
		ArrayList<Action> actionMoveAndStore = sortActionsAndMoves(storeActionsToAffect, map, map.getInput().getAccess());
		robot.getActions().addAll(actionMoveAndStore);
		robot.lock.unlock();
	    }
	}

    }

    /**
     * Generate a list of Moveaction between the startRail and the next DestockingAction or StoreAction location then continue to add MoveAction between each DestockingAction or StoreAction location.
     * 
     * @param storeOrDestockActions
     * @param map
     * @param startRail
     * @return a list of MoveAction and DestockAction if the ArrayList is a list of DestockingActions else a list of MoveAction and StoreAction if the Arraylist is a list of StoreAction
     */
    public ArrayList<Action> sortActionsAndMoves(ArrayList<Action> storeOrDestockActions, Map map, Rail startRail) {
	ArrayList<Action> actions = new ArrayList<Action>();
	Rail firstRail = startRail;
	Action nextBestAction = null;
	ArrayList<Rail> rails = null;
	ArrayList<Action> copyActions = new ArrayList<Action>(storeOrDestockActions);
	// tant qu'il reste des actions a traiter
	while (copyActions.size() > 0) {

	    if (copyActions.get(0) instanceof StoreAction) {
		nextBestAction = (StoreAction) copyActions.get(0);
		rails = map.getPath(firstRail, ((StoreAction) nextBestAction).getDrawer().getColumn().getAccess());
	    } else if (copyActions.get(0) instanceof DestockingAction) {
		nextBestAction = (DestockingAction) copyActions.get(0);
		rails = map.getPath(firstRail, ((DestockingAction) nextBestAction).getDrawer().getColumn().getAccess());
	    }

	    for (Action action : copyActions) {
		ArrayList<Rail> tmpRails = null;
		if (copyActions.get(0) instanceof StoreAction)
		    tmpRails = map.getPath(firstRail, ((StoreAction) action).getDrawer().getColumn().getAccess());
		else if (copyActions.get(0) instanceof DestockingAction) {

		    try {
			tmpRails = map.getPath(firstRail, ((DestockingAction) action).getDrawer().getColumn().getAccess());
		    } catch (Exception e) {
			System.out.println(((DestockingAction) action));
			System.out.println(((DestockingAction) action).getDrawer());
			System.out.println(((DestockingAction) action).getDrawer().getColumn());
			System.out.println(((DestockingAction) action).getDrawer().getColumn().getAccess());
			e.printStackTrace();
		    }
		}
		if (tmpRails != null && tmpRails.size() < rails.size()) {
		    nextBestAction = action;
		    rails = tmpRails;
		}
	    }

	    // ajout des move actions au total
	    actions.addAll(railsToMoveActions(rails));

	    // ajout de l'action au total
	    actions.add(nextBestAction);

	    if (copyActions.get(0) instanceof StoreAction)
		firstRail = ((StoreAction) nextBestAction).getDrawer().getColumn().getAccess();
	    else if (copyActions.get(0) instanceof DestockingAction)
		firstRail = ((DestockingAction) nextBestAction).getDrawer().getColumn().getAccess();

	    copyActions.remove(nextBestAction);
	}
	return actions;
    }

    /**
     * Generate an ArrayList of MoveAction from a list of Rail example : ArrayList<Rail> = {Rail(0,1),Rail(0,2),Rail,(0,3)} returned list will be {MoveAction(previous=(0,1),next=(0,2)),MoveAction(previous=(0,2),next=(0,3))}
     * 
     * @return an ArrayList of MoveAction
     */
    public ArrayList<MoveAction> railsToMoveActions(ArrayList<Rail> rails) {
	if (rails.isEmpty())
	    return new ArrayList<MoveAction>();

	ArrayList<MoveAction> moves = new ArrayList<MoveAction>();
	Rail previous = rails.get(0);
	for (Rail rail : rails) {
	    if (rails.get(0) == rail)
		continue;
	    MoveAction move = new MoveAction(1000, null, previous, rail);
	    moves.add(move);
	    previous = rail;
	}
	return moves;
    }

    /**
     * Search the robot that has the less actions number.
     * 
     * @param robots
     * @param map
     * @param destination
     * @return a Robot
     */
    public Robot getBestRobot(ArrayList<Robot> robots, Map map, Rail destination) {

	Robot bestRobot = robots.get(0);

	for (Robot r : robots) {
	    r.lock.lock();
	    if (r.getActions().size() < bestRobot.getActions().size()) {
		bestRobot = r;
	    } else if (r.getActions().size() == bestRobot.getActions().size()) {
		if (map.getPath(r.getRail(), destination).size() < map.getPath(r.getRail(), destination).size())
		    bestRobot = r;
	    }
	    r.lock.unlock();
	}

	return bestRobot;

    }

    /**
     * Search the rail corresponding to a x an y coordinates in a list of rail elements.
     * 
     * @param rails
     * @param x
     * @param y
     * @return
     */
    public Rail getRail(ArrayList<Rail> rails, int x, int y) {
	for (Rail r : rails) {
	    if (r.getX() == x && r.getY() == y) {
		return r;
	    }
	}
	return null;
    }

    /**
     * Generate a copy of an ArrayList of Rails without copying references
     * 
     * @param rails
     * @return an ArrayList of Rail
     */
    public ArrayList<Rail> getCloneRail(ArrayList<Rail> rails) {
	// dupliquer la liste des rails (attention bien dupliquer la liste pour ne pas garder les references)
	ArrayList<Rail> copyRails = new ArrayList<Rail>();
	for (Rail trueRail : rails) {
	    Rail railCopy = new Rail(trueRail.getX(), trueRail.getY(), null, null, null, null);
	    copyRails.add(railCopy);
	}

	for (Rail trueRail : rails) {
	    Rail copyRail = copyRails.get(rails.indexOf(trueRail));
	    if (trueRail.getRightRail() != null)
		copyRail.setRightRail(copyRails.get(rails.indexOf(trueRail.getRightRail())));
	    else if (trueRail.getLeftRail() != null)
		copyRail.setLeftRail(copyRails.get(rails.indexOf(trueRail.getLeftRail())));
	}

	return copyRails;
    }

}
