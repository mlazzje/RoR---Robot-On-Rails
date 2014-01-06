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
public class AlgStoreOrder implements IAlgStore {

    /**
     * Generate an ArrayList of StoreAction and book all drawers needed for each orders ready to store
     * 
     * @return an ArrayList of StoreAction depending of the list of inputProducts
     */
    public ArrayList<StoreAction> getActions(ArrayList<Product> inputProducts, ArrayList<Order> orders, Map map) {

	if (inputProducts == null)
	    return null;

	ArrayList<StoreAction> actions = new ArrayList<StoreAction>(); // init
	Drawer drawer = null;

	Iterator<Product> itProduct = inputProducts.iterator();
	while (itProduct.hasNext()) { // Parcours les produits à stocker
	    Product currentProduct = itProduct.next();
	    if (currentProduct.getStatus() == Product.FREE) { // Si produit PAS déjà réservé à une action
		Iterator<Order> itOrder = orders.iterator();
		while (itOrder.hasNext()) { // Parcours les commandes
		    Order currentOrder = itOrder.next(); // Commande actuelle
		    if (currentOrder.wantsProduct(currentProduct.getName())) {
			// On cherche où on doit mettre le produit
			drawer = this.getDrawerFree(currentOrder, map);
			if (drawer != null) {
			    drawer.setStatus(Drawer.BOOKED);
			    StoreAction currentAction = new StoreAction(null, null, drawer, currentProduct);
			    actions.add(currentAction);
			    currentProduct.setStatus(Product.BEING_STORED); // on met à jour le inputProducts !
			    currentOrder.addProductStored(currentProduct); // For algo store Order
			    break; // On passe au produit suivant !
			} else {
			    // No Drawer free or product already being stored
			}
		    }
		}
	    }
	}
	return actions;
    }

    /**
     * @param order
     * @param map
     * @return drawer free / null
     */
    public Drawer getDrawerFree(Order order, Map map) {
	Drawer drawer = null;
	if (order.getDrawers().size() == 0) { // Si aucun drawers réservé on va le faire
	    if (!this.bookDrawers(order, map)) { // Si on peut réserver des armoires
		// Plus de drawers dispos
		return null;
	    }
	}
	if (order.getDrawers().size() != order.getProductsName().size()) {
	    // System.out.println("Error Algo Store Order : Order ID " + order.getIdOrder() + " | nb Drawers réservé " + order.getDrawers().size() + " & nb products Name " + order.getProductsName().size() + " ");
	}
	// On parcours les drawers de la commande
	// System.out.println("Search in drawers already booked... of Order "+order.getIdOrder());
	for (Drawer drawerTest : order.getDrawers()) {
	    // System.out.println("Drawer = "+drawerTest+" | status : "+drawerTest.getStatus()+ " product : "+drawerTest.getProduct());
	    if (drawerTest.getStatus() == Drawer.BOOKED_FOR_ORDER && drawerTest.getStatus() != Drawer.BOOKED && drawerTest.getProduct() == null) { // Si drawer booked et n'as pas de produit
		drawer = drawerTest;
		break;
	    }
	}
	// System.out.println("Order id "+order.getIdOrder()+"| Drawer booked in column : "+drawer.getColumn());
	return drawer;
    }

    /**
     * Book a list of Drawer in the Map depending of the list of products of the order
     * 
     * @param order
     * @param map
     * @return
     */
    public boolean bookDrawers(Order order, Map map) {
	// System.out.println("Booking Drawer !");
	int nbDrawersToBook = order.getProductsName().size();
	// int nbDrawersDec = order.getProductsName().size();
	int nbDrawersBooked = 0; // Simulation
	Column firstColumn = null, secondColumn = null, thirdColumn = null; // 20 objets par commande ne peut pas être dans plus de 3 colonnes !
	/*******************************************************/
	/* Début de la simulation des réservations des drawers */
	/*******************************************************/
	for (Column column : map.getColumns()) {
	    if (column.getAvailableDrawer() != null) {
		if (nbDrawersBooked == nbDrawersToBook) {
		    // System.out.println("BREAK !");
		    break;
		}
		if (nbDrawersBooked > 0 && nbDrawersBooked < nbDrawersToBook) { // Tkt pas ça marche ici & Première colonne rempli, on rentre ici dans la deuxième colonne
		    if (nbDrawersToBook - nbDrawersBooked >= 10 && nbDrawersToBook > nbDrawersBooked && column.getNbAvailableDrawers() != 10 && firstColumn != null) { // pas assez de place dans la seconde colonne car <10
			nbDrawersBooked = 0;
			firstColumn = null;
			secondColumn = null;
			thirdColumn = null;
			// System.out.println("BREAK 2nd COL > 10 Order ID "+order.getIdOrder()+" book 1st column | nb drawer booked = "+nbDrawersBooked+"/"+nbDrawersToBook);
		    } else if (nbDrawersToBook - nbDrawersBooked < 10 && nbDrawersToBook > nbDrawersBooked && firstColumn != null && nbDrawersToBook - nbDrawersBooked > column.getNbAvailableDrawers()) { // pas assez de place dans la seconde colonne
			nbDrawersBooked = 0;
			firstColumn = null;
			secondColumn = null;
			thirdColumn = null;
			// System.out.println("BREAK 2nd COL < 10 Order ID "+order.getIdOrder()+" book 1st column | nb drawer booked = "+nbDrawersBooked+"/"+nbDrawersToBook);
		    } else if (nbDrawersToBook - nbDrawersBooked < 10 && nbDrawersToBook > nbDrawersBooked && firstColumn != null && secondColumn != null && nbDrawersToBook - nbDrawersBooked > column.getNbAvailableDrawers()) { // Pas assez de place dans la troisième colonne
			nbDrawersBooked = 0;
			firstColumn = null;
			secondColumn = null;
			thirdColumn = null;
			// System.out.println("BREAK 3rd COL Order ID "+order.getIdOrder()+" book 1st column | nb drawer booked = "+nbDrawersBooked+"/"+nbDrawersToBook);
		    } else {
			// Book drawers
			if (column.getNbAvailableDrawers() == 10) { // Si colonne peut se remplir entièrement
			    if (firstColumn == null) {
				// System.out.println("First column = "+column);
				firstColumn = column;
			    } else if (secondColumn == null) {
				// System.out.println("Second column = "+column);
				secondColumn = column;
			    } else if (thirdColumn == null) {
				// System.out.println("Third column = "+column);
				thirdColumn = column;
			    }
			    // Maj nbDrawersBooked
			    if (nbDrawersToBook - nbDrawersBooked < 10) { // Voilà
				nbDrawersBooked += nbDrawersToBook - nbDrawersBooked; // = nbDrawersBooked=nbDrawersToBook;
			    } else if (nbDrawersToBook - nbDrawersBooked >= 10) {
				nbDrawersBooked += 10;
			    }
			} else if (nbDrawersToBook - nbDrawersBooked > column.getNbAvailableDrawers()) { // On remplit la première colonne ou la dernière
			    if (firstColumn == null) {
				firstColumn = column;
			    } else if (secondColumn == null) {
				secondColumn = column;
			    } else if (thirdColumn == null) {
				thirdColumn = column;
			    }
			    // Maj nbDrawersBooked
			    nbDrawersBooked += nbDrawersToBook - nbDrawersBooked; // = nbDrawersBooked=nbDrawersToBook; // Voilà
			}
		    }
		} else if (nbDrawersBooked == 0) { // Réservation première colonne
		    // System.out.println("Book 1st column "+column);
		    if (column.getNbAvailableDrawers() > nbDrawersToBook) {
			nbDrawersBooked = nbDrawersToBook;
		    } else {
			nbDrawersBooked = column.getNbAvailableDrawers();
		    }
		    firstColumn = column;
		    // System.out.println("Order ID "+order.getIdOrder()+" book 1st column | nb drawers booked = "+nbDrawersBooked+"/"+nbDrawersToBook);
		}
	    }
	}
	// System.out.println("END simulation : Order ID "+order.getIdOrder()+" | nb drawers booked = "+nbDrawersBooked+"/"+nbDrawersToBook);
	/*******************************************************/
	/** Fin de la simulation des réservations des drawers **/
	/*******************************************************/
	/*******************************************************/
	/****** Début de la vraie réservation des drawers ******/
	/*******************************************************/
	if (nbDrawersBooked != nbDrawersToBook) {
	    // System.out.println("Return false");
	    return false;
	} else {
	    // System.out.println("Begin to Book really !");
	    // Book Drawers really here !
	    int nbDrawersBookedReally = 0; // Reality
	    int nbDrawersToBookForColumn = 0;
	    int nbDrawersAvailable;
	    if (firstColumn != null) {
		nbDrawersAvailable = firstColumn.getNbAvailableDrawers();
		if (nbDrawersToBook - nbDrawersBookedReally < nbDrawersAvailable) { // Voilà
		    nbDrawersBookedReally += nbDrawersToBookForColumn = nbDrawersToBook - nbDrawersBookedReally; // = nbDrawersBooked=nbDrawersToBook;
		} else if (nbDrawersToBook - nbDrawersBookedReally >= nbDrawersAvailable) {
		    nbDrawersBookedReally += nbDrawersToBookForColumn = nbDrawersAvailable;
		}
		firstColumn.bookNDrawersOrder(order, nbDrawersToBookForColumn);
	    }
	    if (secondColumn != null) {
		nbDrawersAvailable = secondColumn.getNbAvailableDrawers();
		if (nbDrawersToBook - nbDrawersBookedReally < nbDrawersAvailable) { // Voilà
		    nbDrawersBookedReally += nbDrawersToBookForColumn = nbDrawersToBook - nbDrawersBookedReally; // = nbDrawersBooked=nbDrawersToBook;
		} else if (nbDrawersToBook - nbDrawersBookedReally >= nbDrawersAvailable) {
		    nbDrawersBookedReally += nbDrawersToBookForColumn = nbDrawersAvailable;
		}
		secondColumn.bookNDrawersOrder(order, nbDrawersToBookForColumn);
	    }
	    if (thirdColumn != null) {
		nbDrawersAvailable = thirdColumn.getNbAvailableDrawers();
		if (nbDrawersToBook - nbDrawersBookedReally < nbDrawersAvailable) { // Voilà
		    nbDrawersBookedReally += nbDrawersToBookForColumn = nbDrawersToBook - nbDrawersBookedReally; // = nbDrawersBooked=nbDrawersToBook;
		} else if (nbDrawersToBook - nbDrawersBookedReally >= nbDrawersAvailable) {
		    nbDrawersBookedReally += nbDrawersToBookForColumn = nbDrawersAvailable;
		}
		thirdColumn.bookNDrawersOrder(order, nbDrawersToBookForColumn);
	    }
	    return true;
	}
	/*******************************************************/
	/******* Fin de la vraie réservation des drawers *******/
	/*******************************************************/
    }

}
