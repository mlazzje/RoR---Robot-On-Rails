package ror.core.algo;

import java.util.ArrayList;
import java.util.Iterator;
import ror.core.Column;
import ror.core.Drawer;
import ror.core.Map;
import ror.core.Order;
import ror.core.Product;
import ror.core.actions.StoreAction;

/**
 * AlgStoreFifo class : Store algorithm that implements the IAlgStore interface.
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-18
 */
public class AlgStoreFifo implements IAlgStore {

    /**
     * @return an ArrayList of StoreAction depending of the list of inputProducts
     */
    public ArrayList<StoreAction> getActions(ArrayList<Product> inputProducts, ArrayList<Order> orders, Map map) {

	ArrayList<StoreAction> actions = new ArrayList<StoreAction>();
	if (inputProducts == null)
	    return actions;
	// TODO Eventuellement prioriser les colonnes dans le XML pour qu'elle soient triées selon les souhaits du client
	Iterator<Product> itProduct = inputProducts.iterator();
	Drawer drawer = null;
	ArrayList<Drawer> bookedDrawers = new ArrayList<Drawer>();
	// Pour chaque produit libre du input on va chercher une place de libre
	synchronized (inputProducts) {
	    while (itProduct.hasNext()) {
		Product currentProduct = itProduct.next();
		if (currentProduct.getStatus() == Product.FREE) {
		    for (Column currentColumn : map.getColumns()) {
			drawer = currentColumn.getAvailableDrawer();
			if (drawer != null) {
			    currentProduct.setStatus(Product.BEING_STORED); // on passe le produit en attente de stockage
			    drawer.setStatus(Drawer.BOOKED); // on réserve le tiroir dans la colonne
			    StoreAction currentAction = new StoreAction(null, null, drawer, currentProduct);
			    actions.add(currentAction);
			    bookedDrawers.add(drawer);
			    break;
			}
		    }
		    if (drawer == null) {
			System.out.println("Erreur : pas de colonne disponible");
			// Si on arrive ici c'est qu'il n'y a plus de columns disponibles
			return actions;
		    }
		}
	    }
	}

	return actions;

    }
}
