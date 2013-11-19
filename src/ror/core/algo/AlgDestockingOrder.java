package ror.core.algo;

import java.util.ArrayList;

import ror.core.Order;
import ror.core.Output;
import ror.core.Product;
import ror.core.actions.Action;

public class AlgDestockingOrder implements IAlgDestocking {

	@Override
	public ArrayList<Action> getActions(ArrayList<Order> orders,
			ArrayList<Product> stockProducts, Output output) {
		// TODO implement
		return new ArrayList<Action>();
	}
}
