package ror.core.algo;

import java.util.ArrayList;

import ror.core.Map;
import ror.core.Rail;
import ror.core.Robot;
import ror.core.actions.DestockingAction;
import ror.core.actions.MoveAction;
import ror.core.actions.StoreAction;

public class AlgMoveAuto implements IAlgMove {
    public void updateRobotsActions(ArrayList<DestockingAction> newDestockActions, ArrayList<StoreAction> newStoreActions, ArrayList<Robot> robots, Map map) {
	// TODO implement
    }
    public ArrayList<MoveAction> railsToMoveActions(ArrayList<Rail> rails)
    {
	return null;
    }
}
