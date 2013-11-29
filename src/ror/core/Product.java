package ror.core;

public class Product {
    
	public static final Integer DONE = 4;
	public static final Integer INPROGRESS = 3;
    public static final Integer BOOKED = 2;
    public static final Integer WAITING = 1;
    public static final Integer FREE = 0;
    
    private String name;
    private Integer status;
    private Order order;
    private Drawer drawer;

    public Product(String name) {
	this.name = name;
	this.status = 0;
	this.order = null;
	this.drawer = null;
    }

    public String getName() {
	return name;
    }
    
    public Drawer getDrawer() {
	return drawer;
    }

    public void setName(String name) {
	this.name = name;
    }
    
    public void setDrawer(Drawer drawer) {
	this.drawer = drawer;
    }
    
    /**
     * Return the status of products
     * 
     *  0 = Free
     *  1 = Waiting (not in stock)
     *  2 = Booked (for an order)
     *  3 = In progress (assigned to an action of destocking)
     *  4 = Done (in Ouput !)
     *
     * @return      The status 0 = Free, 1 = Waiting, 2 = Booked, 3 = In progress, 4 = Done
     * @see         Diagramme Etats-transitions "Statut product"
     */
    public Integer getStatus() {
	return status;
    }
    
    /**
     * Set the status of products
     * 
     *  0 = Free
     *  1 = Waiting (not in stock)
     *  2 = Booked (for an order)
     *  3 = In progress (assigned to an action of destocking)
     *  4 = Done (in Ouput !)
     *
     * @return      The status 0 = Free, 1 = Waiting, 2 = Booked, 3 = In progress, 4 = Done
     * @see        Diagramme Etats-transitions "Statut product"
     */
    public void setStatus(Integer status) {
	this.status = status;
    }

    public Order getOrder() {
	return order;
    }

    public void setOrder(Order order) {
	this.order = order;
    }
}
