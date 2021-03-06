package ror.core.algo;

import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import ror.core.Rail;

/**
 * Vertex class : represents a vertex with some edges and a previous vertex
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-30
 */
class Vertex implements Comparable<Vertex> {
    public Rail rail;
    public Edge[] adjacencies;
    public double minDistance = Double.POSITIVE_INFINITY;
    public Vertex previous;

    public Vertex(Rail rail) {
	this.rail = rail;
    }

    public String toString() {
	return "(" + rail.getX() + "," + rail.getY() + ")";
    }

    public int compareTo(Vertex other) {
	return Double.compare(minDistance, other.minDistance);
    }

}

/**
 * Edge class : represents an edge with a weight
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-30
 */
class Edge {
    public final Vertex target;
    public final double weight;

    public Edge(Vertex argTarget, double argWeight) {
	target = argTarget;
	weight = argWeight;
    }
}


/**
 * Dijkstra class : allow to get the shortest path between two rail
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-30
 */
public class Dijkstra {

    private List<Vertex> vertices;
    private List<Rail> rails;

    public Dijkstra(List<Rail> rails) {
	super();
	this.rails = rails;

    }
    
    /**
     * process all vertex calcul (mindistance) from the source vertex
     * @param source
     */
    public void computePaths(Vertex source) {
	source.minDistance = 0.;
	PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
	vertexQueue.add(source);

	while (!vertexQueue.isEmpty()) {
	    Vertex u = vertexQueue.poll();

	    // Visit each edge exiting u
	    for (Edge e : u.adjacencies) {
		Vertex v = e.target;
		double weight = e.weight;
		double distanceThroughU = u.minDistance + weight;
		if (distanceThroughU < v.minDistance) {
		    vertexQueue.remove(v);

		    v.minDistance = distanceThroughU;
		    v.previous = u;
		    vertexQueue.add(v);
		}
	    }
	}
    }

    /**
     * 
     * @param target
     * @return return the shortest path to the target
     */
    public List<Vertex> getShortestPathTo(Vertex target) {
	List<Vertex> path = new ArrayList<Vertex>();
	for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
	    path.add(vertex);

	Collections.reverse(path);
	return path;
    }

    /**
     * 
     * @param start
     * @param end
     * @return the shortestpath between two Rail
     */
    public List<Rail> getPath(Rail start, Rail end) {

	if (start.getX() == end.getX() && start.getY() == end.getY()) {
	    return new ArrayList<Rail>();
	}

	vertices = new ArrayList<Vertex>();

	for (Rail rail : rails) {
	    Vertex v = new Vertex(rail);

	    vertices.add(v);
	}
	synchronized (vertices) {
	    synchronized (rails) {
		for (Vertex v : vertices) {
		    ArrayList<Edge> edges = new ArrayList<Edge>();
		    if (v.rail.getLeftRail() != null)
			edges.add(new Edge(vertices.get(rails.indexOf(v.rail.getLeftRail())), 1));
		    if (v.rail.getRightRail() != null)
			edges.add(new Edge(vertices.get(rails.indexOf(v.rail.getRightRail())), 1));
		    v.adjacencies = edges.toArray(new Edge[edges.size()]);
		}
	    }
	}

	this.computePaths(vertices.get(rails.indexOf(start)));
	List<Vertex> path = getShortestPathTo(vertices.get(rails.indexOf(end)));
	if (path.isEmpty() || (path.size() == 1 && path.get(0).rail == end))
	    return null;
	List<Rail> finalPath = new ArrayList<Rail>();

	for (Vertex v : path) {
	    finalPath.add(v.rail);
	}

	return finalPath;
    }
}
