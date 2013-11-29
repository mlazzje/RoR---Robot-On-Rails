package ror.core.algo;

import java.util.ArrayList;

import ror.core.Order;
import ror.core.Output;
import ror.core.Product;
import ror.core.actions.Action;

public interface IAlgDestocking {
	public ArrayList<Action> getActions(ArrayList<Order> orders,
			ArrayList<Product> stockProducts);
	
	
}

