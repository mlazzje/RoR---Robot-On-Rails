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
	public ArrayList<Action> getActions(ArrayList<Order> orders,
			ArrayList<Product> stockProducts, Output output) {

		ArrayList<Action> actions = new ArrayList<Action>();
		
		Collections.sort(orders); // Trie les commandes grâce au compareTo de la classe Order

		Iterator<Order> itOrder = orders.iterator();

		while (itOrder.hasNext()) { // Parcours les commandes
			Order currentOrder = itOrder.next();
			List<String> productsName = currentOrder.getProductsName();
			Iterator<String> itProductName = productsName.iterator();
			while (itProductName.hasNext()) { // Parcours les produits
				String currentProductName = itProductName.next();
				Iterator<Product> itTestProduct = stockProducts.iterator();
				while (itTestProduct.hasNext()) {
					Product currentProduct = itTestProduct.next();
					if (currentProduct.getStatus() == 0
							&& currentProduct.getName().equals(
									currentProductName)) {
						DestockingAction currentAction = new DestockingAction(
								null, null, null, currentProduct);
						actions.add(currentAction);
						currentProduct.setStatus(2); // on met à jour le
														// stockProducts, c'est
														// de la simulation !
														// C'est algoMove qui
														// mettra a jour le vrai
														// stockproducts
						break; // on sort de la boucle on a trouvé notre produit
								// pas besoin d'aller voir ailleurs
					}
				}
			}
		}
		return actions;
	}
}
