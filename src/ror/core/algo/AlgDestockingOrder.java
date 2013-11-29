package ror.core.algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

import ror.core.Order;
import ror.core.Output;
import ror.core.Product;
import ror.core.actions.Action;
import ror.core.actions.DestockingAction;

public class AlgDestockingOrder implements IAlgDestocking {

	@Override
	public ArrayList<Action> getActions(ArrayList<Order> orders, ArrayList<Product> stockProducts) {

		ArrayList<Action> actions = new ArrayList<Action>();
		
		Collections.sort(orders); // Trie les commandes grâce au compareTo de la classe Order

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
			currentOrder.setStatus(Order.BEING_DESTOCKED);
		}
		return actions;
	}
}
