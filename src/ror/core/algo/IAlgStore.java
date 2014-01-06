package ror.core.algo;

import java.util.ArrayList;

import ror.core.Map;
import ror.core.Order;
import ror.core.Product;
import ror.core.actions.StoreAction;

/**
 * IAlgDestocking interface : allowing to retrieve a list of StoreAction from a list of input products and a list of orders
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-18
 */
public interface IAlgStore {

    public ArrayList<StoreAction> getActions(ArrayList<Product> inputProducts, ArrayList<Order> orders, Map map);
}
