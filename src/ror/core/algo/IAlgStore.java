package ror.core.algo;

import java.util.ArrayList;

import ror.core.Order;
import ror.core.Product;
import ror.core.RoRElement;
import ror.core.actions.Action;

public interface IAlgStore {

	public ArrayList<Action> getActions(ArrayList<Product> inputProducts,
			ArrayList<Order> orders, RoRElement[][] map);
}
