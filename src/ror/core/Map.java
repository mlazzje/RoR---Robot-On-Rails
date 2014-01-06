package ror.core;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import ror.core.algo.Dijkstra;

/**
 * Map class : Core class that represents the map of the warehouse
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-29
 */
public class Map {

    /**
     * ArrayList of columns
     */
    private ArrayList<Column> columns;
    /**
     * ArrayList of rails
     */
    private ArrayList<Rail> rails;
    /**
     * Two dimensionnals array of RoRElement
     */
    private RoRElement[][] map;
    /**
     * The input spot
     */
    private Input input;
    /**
     * The output spot
     */
    private Output output;
    /**
     * Algorithm Djikstra for moves
     */
    private Dijkstra djikstra;

    /**
     * Constructor of the map
     */
    public Map() {
	this.map = new RoRElement[1][1]; // Taille par defaut
	this.input = null;
	this.output = null;
	this.columns = new ArrayList<Column>();
	this.rails = new ArrayList<Rail>();
	File file = new File("xml/warehouse.xml");
	this.setWareHouse(file);
	this.djikstra = new Dijkstra(this.rails);
    }

    /**
     * @param start
     * @param end
     * @return the path from a Rail to another rail
     */
    public ArrayList<Rail> getPath(Rail start, Rail end) {
	synchronized (this.djikstra) {
	    return (ArrayList<Rail>) djikstra.getPath(start, end);
	}
    }/*
      * public ArrayList<Rail> getPath(Rail start, Rail end) { ArrayList<Rail> path = (ArrayList<Rail>) djikstra.getPath(start, end); if (path.size() > 0 && path.get(0) == start) path.remove(0); return path; }
      */

    /**
     * @return Arraylist of Rails in the map
     */
    public ArrayList<Rail> getRails() {
	return rails;
    }

    /**
     * Set the rails in the map
     * 
     * @param rails
     */
    public void setRails(ArrayList<Rail> rails) {
	this.rails = rails;
    }

    /**
     * @return ArrayList of columns in the map
     */
    public ArrayList<Column> getColumns() {
	return columns;
    }

    /**
     * Set the columns of the map
     * 
     * @param columns
     */
    public void setColumns(ArrayList<Column> columns) {
	this.columns = columns;
    }

    /**
     * @return the map in two dimensional array of RoRElement
     */
    public RoRElement[][] getMap() {
	return map;
    }

    /**
     * Set the map with a two dimensional array of RoRElement
     * 
     * @param two
     *            dimensional array of RoRElement
     */
    public void setMap(RoRElement[][] map) {
	this.map = map;
    }

    /**
     * @return the input
     */
    public Input getInput() {
	return input;
    }

    /**
     * Set the input spot
     * 
     * @param input
     */
    public void setInput(Input input) {
	this.input = input;
    }

    /**
     * @return the output
     */
    public Output getOutput() {
	return output;
    }

    /**
     * Set the output spot
     * 
     * @param output
     */
    public void setOutput(Output output) {
	this.output = output;
    }

    /**
     * @param startNode
     *            rail
     * @param endPoint
     *            rail
     * @return the best Path between two rails
     */
    public ArrayList<Rail> CalculateBestPath(Rail startNode, Rail endPoint) {

	ArrayList<Rail> open = new ArrayList<Rail>();
	ArrayList<Rail> close = new ArrayList<Rail>();

	open.add(startNode);

	while (open.size() > 0) {
	    Rail best = open.remove(0); // This is the best node
	    if (best.x == endPoint.x && best.y == endPoint.y) // We are finished
	    {
		ArrayList<Rail> sol = new ArrayList<Rail>(); // The solution
		while (best.getPreviousRail().get(0) != null) {
		    sol.add(best);
		    best = best.getPreviousRail().get(0);
		}
		return sol; // Return the solution when the parent is null (the first point)
	    }
	    close.add(best);
	    if (best.getLeftRail() != null && !open.contains(best.getLeftRail())) {
		open.add(best.getLeftRail());

		best.getLeftRail().getPreviousRail().remove(best);
	    }

	    if (best.getRightRail() != null && !open.contains(best.getRightRail())) {
		open.add(best.getRightRail());
		best.getRightRail().getPreviousRail().remove(best);

	    }
	}
	// No path found
	return null;
    }

    /**
     * Generate weight of path between two rails
     * 
     * @param start
     *            rail
     * @param end
     *            rail
     */
    public void generateWeightTable(Rail start, Rail end) {

	Object[][] weights = new Object[rails.size()][3];

	int i = 0;
	for (Object[] o : weights) {
	    Rail r = rails.get(i);
	    o[0] = r;
	    if (r == start)
		o[1] = 0;
	    else
		o[1] = 1; // poid égal à un (rail à rail = 1)
	    o[2] = false;
	    i++;
	}
	/*
	 * int totalWeight = 0; for (Object[] o : weights) { totalWeight+=(Integer)o[1]; if ((Boolean) o[2] == false) {
	 * 
	 * } }
	 */

    }

    /**
     * Generate the WareHouse with an XML File
     * 
     * @param file
     */
    public void setWareHouse(File file) {

	Document document = null;
	Element racine;

	SAXBuilder sxb = new SAXBuilder();
	try {
	    document = sxb.build(file);
	} catch (Exception e) {
	}

	racine = document.getRootElement();

	// creations du plan
	int height = Integer.parseInt(racine.getAttributeValue("height"));
	int width = Integer.parseInt(racine.getAttributeValue("width"));
	this.map = new RoRElement[height][width];

	// Creation des rails
	List<Element> railRowList = racine.getChildren("rail_row");
	Iterator<Element> it = railRowList.iterator();
	while (it.hasNext()) {

	    Element currentRail = it.next();

	    // start point
	    Element start = currentRail.getChild("start");
	    Point startPoint = new Point();
	    startPoint.x = Integer.parseInt(start.getChild("x").getValue().trim());
	    startPoint.y = Integer.parseInt(start.getChild("y").getValue().trim());

	    // right rail
	    Element end = currentRail.getChild("end");
	    Point endPoint = new Point();
	    endPoint.x = Integer.parseInt(end.getChild("x").getValue().trim());
	    endPoint.y = Integer.parseInt(end.getChild("y").getValue().trim());

	    if (startPoint.x == endPoint.x) {
		int x = startPoint.x;

		// verticale de haut en bas 0 -> Y
		if (startPoint.y < endPoint.y) {
		    for (int y = startPoint.y; y <= endPoint.y; y++) {
			Rail r = new Rail(x, y, null, null, new ArrayList<Rail>(), null);
			this.map[y][x] = r;
			this.rails.add(r);
		    }
		}
		// verticale de bas en haut Y -> 0
		else {
		    for (int y = startPoint.y; y >= endPoint.y; y--) {
			Rail r = new Rail(x, y, null, null, new ArrayList<Rail>(), null);
			this.map[y][x] = r;
			this.rails.add(r);
		    }
		}
	    } else if (startPoint.y == endPoint.y) {
		int y = startPoint.y;
		// ligne de gauche à droite 0 -> X
		if (startPoint.x < endPoint.x) {
		    for (int x = startPoint.x; x <= endPoint.x; x++) {
			Rail r = new Rail(x, y, null, null, new ArrayList<Rail>(), null);
			this.map[y][x] = r;
			this.rails.add(r);
		    }
		}
		// ligne de droite à gauche X -> 0
		else {
		    for (int x = startPoint.x; x >= endPoint.x; x--) {
			Rail r = new Rail(x, y, null, null, new ArrayList<Rail>(), null);
			this.map[y][x] = r;
			this.rails.add(r);
		    }
		}
	    }
	}

	// Creation des rails
	it = railRowList.iterator();
	while (it.hasNext()) {

	    Element currentRail = (Element) it.next();

	    // start point
	    Element start = currentRail.getChild("start");
	    Point startPoint = new Point();
	    startPoint.x = Integer.parseInt(start.getChild("x").getValue().trim());
	    startPoint.y = Integer.parseInt(start.getChild("y").getValue().trim());

	    // end rail
	    Element end = currentRail.getChild("end");
	    Point endPoint = new Point();
	    endPoint.x = Integer.parseInt(end.getChild("x").getValue().trim());
	    endPoint.y = Integer.parseInt(end.getChild("y").getValue().trim());

	    // left rail
	    Element left = currentRail.getChild("left");
	    Point leftPoint = null;
	    if (left != null) {
		leftPoint = new Point();
		leftPoint.x = Integer.parseInt(left.getChild("x").getValue().trim());
		leftPoint.y = Integer.parseInt(left.getChild("y").getValue().trim());
	    }

	    // right rail
	    Element right = currentRail.getChild("right");
	    Point rightPoint = null;
	    if (right != null) {
		rightPoint = new Point();
		rightPoint.x = Integer.parseInt(right.getChild("x").getValue().trim());
		rightPoint.y = Integer.parseInt(right.getChild("y").getValue().trim());
	    }

	    if (startPoint.x == endPoint.x) {
		int x = startPoint.x;

		// verticale de haut en bas 0 -> Y
		if (startPoint.y < endPoint.y) {
		    for (int y = startPoint.y; y <= endPoint.y; y++) {
			Rail r = (Rail) this.map[y][x];
			// si rail de debut
			if (y == startPoint.y) {
			    if (rightPoint != null) {
				r.setRightRail((Rail) this.map[y + 1][x]);
			    }
			}
			// si rail de fin
			else if (y == endPoint.y) {
			    if (leftPoint != null) {
				r.setLeftRail((Rail) this.map[leftPoint.y][leftPoint.x]);
			    }
			    if (rightPoint != null) {
				r.setRightRail((Rail) this.map[rightPoint.y][rightPoint.x]);
			    }
			}
			// sinon rail du milieu
			else {
			    r.setRightRail((Rail) this.map[y + 1][x]);
			}
			if (r.getLeftRail() != null)
			    r.getLeftRail().addPreviousRail(r);
			if (r.getRightRail() != null) {
			    r.getRightRail().addPreviousRail(r);
			    r.setNextRail(r.getRightRail());
			}
		    }
		}
		// verticale de bas en haut Y -> 0
		else {
		    for (int y = startPoint.y; y >= endPoint.y; y--) {
			Rail r = (Rail) this.map[y][x];

			// si rail de debut
			if (y == startPoint.y) {
			    if (rightPoint != null) {
				r.setRightRail((Rail) this.map[y - 1][x]);
			    }
			}
			// si rail de fin
			else if (y == endPoint.y) {
			    if (leftPoint != null) {
				r.setLeftRail((Rail) this.map[leftPoint.y][leftPoint.x]);
			    }
			    if (rightPoint != null) {
				r.setRightRail((Rail) this.map[rightPoint.y][rightPoint.x]);
			    }
			}
			// sinon rail du milieu
			else {
			    r.setRightRail((Rail) this.map[y - 1][x]);
			}

			if (r.getLeftRail() != null)
			    r.getLeftRail().addPreviousRail(r);
			if (r.getRightRail() != null) {
			    r.getRightRail().addPreviousRail(r);
			    r.setNextRail(r.getRightRail());
			}
		    }
		}
	    } else if (startPoint.y == endPoint.y) {
		int y = startPoint.y;
		// ligne de gauche à droite 0 -> X
		if (startPoint.x < endPoint.x) {
		    for (int x = startPoint.x; x <= endPoint.x; x++) {
			Rail r = (Rail) this.map[y][x];

			// si rail de debut
			if (x == startPoint.x) {
			    if (rightPoint != null) {
				r.setRightRail((Rail) this.map[y][x + 1]);
			    }
			}
			// si rail de fin
			else if (x == endPoint.x) {
			    if (leftPoint != null) {
				r.setLeftRail((Rail) this.map[leftPoint.y][leftPoint.x]);
			    }
			    if (rightPoint != null) {
				r.setRightRail((Rail) this.map[rightPoint.y][rightPoint.x]);
			    }
			}
			// sinon rail du milieu
			else {
			    r.setRightRail((Rail) this.map[y][x + 1]);
			}
			if (r.getLeftRail() != null)
			    r.getLeftRail().addPreviousRail(r);
			if (r.getRightRail() != null) {
			    r.getRightRail().addPreviousRail(r);
			    r.setNextRail(r.getRightRail());
			}
		    }
		}
		// ligne de droite à gauche X -> 0
		else {
		    for (int x = startPoint.x; x >= endPoint.x; x--) {
			Rail r = (Rail) this.map[y][x];

			// si rail de debut
			if (x == startPoint.x) {
			    if (rightPoint != null) {
				r.setRightRail((Rail) this.map[y][x - 1]);
			    }
			}
			// si rail de fin
			else if (x == endPoint.x) {
			    if (leftPoint != null) {
				r.setLeftRail((Rail) this.map[leftPoint.y][leftPoint.x]);
			    }
			    if (rightPoint != null) {
				r.setRightRail((Rail) this.map[rightPoint.y][rightPoint.x]);
			    }
			}
			// sinon rail du milieu
			else {
			    r.setRightRail((Rail) this.map[y][x - 1]);
			}

			if (r.getLeftRail() != null)
			    r.getLeftRail().addPreviousRail(r);
			if (r.getRightRail() != null) {
			    r.getRightRail().addPreviousRail(r);
			    r.setNextRail(r.getRightRail());
			}
		    }
		}
	    }
	}

	// Creation des colonnes
	List<Element> columnsRowList = racine.getChildren("column_row");
	Iterator<Element> itc = columnsRowList.iterator();
	while (itc.hasNext()) {

	    Element currentColumnRail = itc.next();

	    // start point
	    Element start = currentColumnRail.getChild("start");
	    Point startPoint = new Point();
	    startPoint.x = Integer.parseInt(start.getChild("x").getValue().trim());
	    startPoint.y = Integer.parseInt(start.getChild("y").getValue().trim());

	    // right rail
	    Element end = currentColumnRail.getChild("end");
	    Point endPoint = new Point();
	    endPoint.x = Integer.parseInt(end.getChild("x").getValue().trim());
	    endPoint.y = Integer.parseInt(end.getChild("y").getValue().trim());

	    // robot access point
	    Element robotAccess = currentColumnRail.getChild("robot_access");

	    int distance = Integer.parseInt(robotAccess.getValue().trim());

	    if (startPoint.x == endPoint.x) {
		int x = startPoint.x;

		// verticale de haut en bas 0 -> Y
		if (startPoint.y < endPoint.y) {
		    for (int y = startPoint.y; y <= endPoint.y; y++) {
			Column c = new Column(null, x, y, null, null);
			c.setAccess((Rail) this.map[y][x + distance]);
			columns.add(c);
			this.map[y][x] = c;
		    }
		}
		// verticale de bas en haut Y -> 0
		else {
		    for (int y = startPoint.y; y >= endPoint.y; y--) {

			Column c = new Column(null, x, y, null, null);
			c.setAccess((Rail) this.map[y][x + distance]);
			columns.add(c);
			this.map[y][x] = c;
		    }
		}
	    } else if (startPoint.y == endPoint.y) {
		int y = startPoint.y;
		// ligne de gauche à droite 0 -> X
		if (startPoint.x < endPoint.x) {
		    for (int x = startPoint.x; x <= endPoint.x; x++) {
			Column c = new Column(null, x, y, null, null);
			c.setAccess((Rail) this.map[y + distance][x]);
			columns.add(c);
			this.map[y][x] = c;
		    }
		}
		// ligne de droite à gauche X -> 0
		else {
		    for (int x = startPoint.x; x >= endPoint.x; x--) {
			Column c = new Column(null, x, y, null, null);
			c.setAccess((Rail) this.map[y + distance][x]);
			columns.add(c);
			this.map[y][x] = c;
		    }
		}
	    }
	}

	// creation de l'input
	Element elInput = racine.getChild("input");
	if (elInput != null) {
	    int xAccess = Integer.parseInt(elInput.getChild("robot_access").getChild("x").getValue().trim());
	    int yAccess = Integer.parseInt(elInput.getChild("robot_access").getChild("y").getValue().trim());

	    this.input = new Input(Integer.parseInt(elInput.getChild("x").getValue().trim()), Integer.parseInt(elInput.getChild("y").getValue().trim()), null);
	    this.input.setAccess((Rail) this.map[yAccess][xAccess]);
	    this.map[Integer.parseInt(elInput.getChild("y").getValue().trim())][Integer.parseInt(elInput.getChild("x").getValue().trim())] = input;
	}

	// creation de l'output
	Element elOutput = racine.getChild("output");
	if (elOutput != null) {
	    int xAccess = Integer.parseInt(elOutput.getChild("robot_access").getChild("x").getValue().trim());
	    int yAccess = Integer.parseInt(elOutput.getChild("robot_access").getChild("y").getValue().trim());

	    this.output = new Output(Integer.parseInt(elOutput.getChild("x").getValue().trim()), Integer.parseInt(elOutput.getChild("y").getValue().trim()), null);
	    this.output.setAccess((Rail) this.map[yAccess][xAccess]);
	    this.map[Integer.parseInt(elOutput.getChild("y").getValue().trim())][Integer.parseInt(elOutput.getChild("x").getValue().trim())] = output;
	}

    }

    /*
     * To sort columns
     */
    public void sortColumns() {
	for (Column col : this.getColumns()) {
	    col.setDistanceInputOutput(this.getPath(this.input.getAccess(), col.getAccess()).size() + this.getPath(this.output.getAccess(), col.getAccess()).size());
	}
	Collections.sort(this.getColumns());
    }
}
