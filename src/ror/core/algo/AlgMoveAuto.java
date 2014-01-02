package ror.core.algo;

import java.util.ArrayList;

import ror.core.Map;
import ror.core.Rail;
import ror.core.Robot;
import ror.core.actions.Action;
import ror.core.actions.DestockingAction;
import ror.core.actions.MoveAction;
import ror.core.actions.StoreAction;

public class AlgMoveAuto implements IAlgMove {
	public void updateRobotsActions(ArrayList<DestockingAction> newDestockActions, ArrayList<StoreAction> newStoreActions, ArrayList<Robot> robots, Map map) {
		final int limit = 15;
		int robotActions = 0;
		// Count robot actions
		for (Robot robot : robots) {
			for (Action act : robot.getActions()) {
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
