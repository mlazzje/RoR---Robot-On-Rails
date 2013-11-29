package ror.core.algo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ror.core.Map;
import ror.core.Order;
import ror.core.Product;
import ror.core.RoRElement;
import ror.core.actions.Action;
import ror.core.actions.DestockingAction;
import ror.core.actions.StoreAction;

public class AlgStoreOrder implements IAlgStore {
	public ArrayList<Action> getActions(ArrayList<Product> inputProducts,
			ArrayList<Order> orders, Map map) {
		
		ArrayList<Action> actions = new ArrayList<Action>();

		Iterator<Order> itOrder = orders.iterator();

		while (itOrder.hasNext()) { // Parcours les commandes
			Order currentOrder = itOrder.next();
			List<String> productsName = currentOrder.getProductsName();
			Iterator<String> itProductName = productsName.iterator();
			while (itProductName.hasNext()) { // Parcours les produits
				String currentProductName = itProductName.next();
				Iterator<Product> itTestProduct = inputProducts.iterator();
				while (itTestProduct.hasNext()) {
					Product currentProduct = itTestProduct.next();
					if (currentProduct.getStatus() == 0 && currentProduct.getName().equals(currentProductName)) {
						StoreAction currentAction = new StoreAction( 0, null, null, currentProduct);
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
