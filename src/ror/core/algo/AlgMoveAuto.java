package ror.core.algo;

import java.util.ArrayList;

import ror.core.Map;
import ror.core.Rail;
import ror.core.Robot;
import ror.core.actions.Action;
import ror.core.actions.DestockingAction;
import ror.core.actions.MoveAction;
import ror.core.actions.StoreAction;

/**
 * AlgMoveAuto class : Move algorithm that implements the IAlgMove interface.
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-18
 */
public class AlgMoveAuto implements IAlgMove {
    
    /**
     * Updates the robot's actions : add list of MoveAction, DestockingAction, StoreAction, InputAction and OutputAction to the robots depending on a list of destocking actions and a list of store actions to do.
     * 
     */
    public void updateRobotsActions(ArrayList<DestockingAction> newDestockActions, ArrayList<StoreAction> newStoreActions, ArrayList<Robot> robots, Map map) {
	final int limit = 15;
	int robotActions = 0;

	if (newDestockActions == null) {
	    newDestockActions = new ArrayList<DestockingAction>();
	}
	if (newStoreActions == null) {
	    newStoreActions = new ArrayList<StoreAction>();
	}

	// Count robot actions

	for (Robot robot : robots) {
	    ArrayList<Action> copyActions;

	    synchronized (robot.getActions()) {
		copyActions = new ArrayList<Action>(robot.getActions());
	    }

	    for (Action act : copyActions) {
		if (act instanceof DestockingAction || act instanceof StoreAction) {
		    robotActions++;
		}
	    }
	}
	// Fast
	if ((newDestockActions.size() + newStoreActions.size() + robotActions) >= limit) {
	    new AlgMoveFast().updateRobotsActions(newDestockActions, newStoreActions, robots, map);
	}
	// Eco
	else {
	    new AlgMoveEco().updateRobotsActions(newDestockActions, newStoreActions, robots, map);
	}
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
}
