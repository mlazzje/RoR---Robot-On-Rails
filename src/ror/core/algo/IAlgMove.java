package ror.core.algo;

import java.util.ArrayList;

import ror.core.Map;
import ror.core.Rail;
import ror.core.Robot;
import ror.core.actions.DestockingAction;
import ror.core.actions.MoveAction;
import ror.core.actions.StoreAction;

/**
 * IAlgMove interface : allow to updates all robot's actions from a list of DestockingAction or StoreAction.
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-18
 */
public interface IAlgMove {
    public void updateRobotsActions(ArrayList<DestockingAction> newDestockActions, ArrayList<StoreAction> newStoreActions, ArrayList<Robot> robots, Map map);
    public ArrayList<MoveAction> railsToMoveActions(ArrayList<Rail> rails);

}
