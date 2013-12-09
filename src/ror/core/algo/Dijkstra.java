package ror.core.algo;

/* The authors of this work have released all rights to it and placed it
 in the public domain under the Creative Commons CC0 1.0 waiver
 (http://creativecommons.org/publicdomain/zero/1.0/).

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 Retrieved from: http://en.literateprograms.org/Dijkstra's_algorithm_(Java)?oldid=15444
 */

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import ror.core.Rail;

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

class Edge {
    public final Vertex target;
    public final double weight;

    public Edge(Vertex argTarget, double argWeight) {
	target = argTarget;
	weight = argWeight;
    }
}

public class Dijkstra {

    private List<Vertex> vertices;
    private List<Rail> rails;
    
    public Dijkstra(List<Rail> rails) {
	super();
	this.rails = rails;
	
    }

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

    public List<Vertex> getShortestPathTo(Vertex target) {
	List<Vertex> path = new ArrayList<Vertex>();
	for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
	    path.add(vertex);

	Collections.reverse(path);
	return path;
    }

    public List<Rail> getPath(Rail start, Rail end) {
	
	vertices = new ArrayList<Vertex>();

	
	for (Rail rail : rails) {
	    Vertex v = new Vertex(rail);
	    
	    vertices.add(v);
	}
	
	
	for (Vertex v : vertices) {
	    ArrayList<Edge> edges = new ArrayList<Edge>();
	    if (v.rail.getLeftRail() != null)
		edges.add(new Edge(vertices.get(rails.indexOf(v.rail.getLeftRail())), 1));
	    if (v.rail.getRightRail() != null)
		edges.add(new Edge(vertices.get(rails.indexOf(v.rail.getRightRail())), 1));
	    v.adjacencies = edges.toArray(new Edge[edges.size()]);
	}

	computePaths(vertices.get(rails.indexOf(start)));
	List<Vertex> path = getShortestPathTo(vertices.get(rails.indexOf(end)));
	List<Rail> finalPath = new ArrayList<Rail>();

	for (Vertex v : path) {
	    finalPath.add(v.rail);
	}

	return finalPath;
    }
}