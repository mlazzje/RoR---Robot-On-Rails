package ror.core.algo;

import java.util.ArrayList;

import ror.core.Order;
import ror.core.Product;
import ror.core.actions.DestockingAction;

/**
 * IAlgDestocking interface : allowing to retrieve a list of DestockingAction from a list of orders and a list of stored products
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-18
 */
public interface IAlgDestocking {
    public ArrayList<DestockingAction> getActions(ArrayList<Order> orders, ArrayList<Product> stockProducts, IAlgStore algo);

}
