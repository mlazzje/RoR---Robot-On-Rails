package ror.core.algo;

import java.util.ArrayList;

import ror.core.Order;
import ror.core.Product;
import ror.core.actions.DestockingAction;

public interface IAlgDestocking {
    public ArrayList<DestockingAction> getActions(ArrayList<Order> orders, ArrayList<Product> stockProducts, IAlgStore algo);

}
