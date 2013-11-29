package ror.core.algo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ror.core.Order;
import ror.core.Output;
import ror.core.Product;
import ror.core.actions.Action;
import ror.core.actions.DestockingAction;
import ror.core.algo.IAlgDestocking;

public class AlgDestockingFifo implements IAlgDestocking {

	@Override
	public ArrayList<Action> getActions(ArrayList<Order> orders,
			ArrayList<Product> stockProducts) {

		ArrayList<Action> actions = new ArrayList<Action>();

		Iterator<Order> itOrder = orders.iterator();
		while (itOrder.hasNext()) { // Parcours les commandes
			Order currentOrder = itOrder.next(); // Commande actuelle
			if(currentOrder.getStatus()==Order.READY_FOR_DESTOCKING) {
				Iterator<Product> ProductsOrder = currentOrder.getProducts().iterator();
				while (ProductsOrder.hasNext()) {
					Product currentProduct = ProductsOrder.next();
						DestockingAction currentAction = new DestockingAction(0, null, currentProduct);
						actions.add(currentAction);
				}

			}
			currentOrder.setStatus(Order.ACTIONNED);
		}
		return actions;
	}
}
