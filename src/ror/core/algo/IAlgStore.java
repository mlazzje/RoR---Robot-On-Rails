package ror.core.algo;

import java.util.ArrayList;

import ror.core.Map;
import ror.core.Order;
import ror.core.Product;
import ror.core.actions.Action;
import ror.core.actions.StoreAction;

public interface IAlgStore {

    public ArrayList<StoreAction> getActions(ArrayList<Product> inputProducts, ArrayList<Order> orders, Map map);
}
