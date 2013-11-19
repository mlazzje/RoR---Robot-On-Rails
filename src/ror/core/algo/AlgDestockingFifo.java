package ror.core.algo;

import java.util.ArrayList;

import ror.core.Order;
import ror.core.Output;
import ror.core.Product;
import ror.core.actions.Action;
import ror.core.algo.IAlgDestocking;

public class AlgDestockingFifo implements IAlgDestocking {

	@Override
	public ArrayList<Action> getActions(ArrayList<Order> orders,
			ArrayList<Product> stockProducts, Output output) {
		// TODO implement
		return new ArrayList<Action>();
	}

}
