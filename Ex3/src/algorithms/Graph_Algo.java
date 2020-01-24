package algorithms;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import dataStructure.*;

/**
 * Graph Theory algorithms , Implemented as part of EX2.

 * @author malik faris and fadi Aamir.
 */
public class Graph_Algo implements graph_algorithms, Serializable{

	private static final long serialVersionUID = 1L;

	private DGraph gr;

	public Graph_Algo() {
		this.gr=new DGraph();
	}

	public Graph_Algo(graph g) {
		this.gr=(DGraph)g;
	}

	@Override
	public void init(graph g) {
		if(g instanceof DGraph) { this.gr=(DGraph)g; }
		else { throw new RuntimeException("Error initialaizing the graph"); }
	}

	@Override

	public void init(String file_name) {
		try {
			ObjectInputStream in=new ObjectInputStream(new FileInputStream(file_name));
			graph ng = (graph) in.readObject(); 
			init(ng);
			in.close();
		}
		catch(Exception e) {
			throw new RuntimeException("Can't load from file");
		}
	}

	@Override
	
	public void save(String file_name) {
		try {
			OutputStream outStream = new FileOutputStream(file_name);
			ObjectOutputStream fileObjectOut = new ObjectOutputStream(outStream);
			fileObjectOut.writeObject(gr);
			fileObjectOut.close();
			outStream.close();
		}
		catch(Exception e) {
			throw new RuntimeException("Error saving to file");
		}	
	}

	@Override
	/**
	 * checks if the graph is connected .
	 * returns boolean based on if its connected or not .
	 */
	public boolean isConnected() {
		DGraph temp = (DGraph) this.copy();
		if (temp.nodes.size()<2) { return false; }
		if (temp.edgeSize()<2) { return false; }
		for(int key:temp.nodes.keySet()) {
			temp.nodes.get(key).setTag(0);
		}
		Collection<node_data> ns = temp.getV();
		for(node_data t:ns) {
			DFS(t); 
			break;
		}
		for (node_data node : ns) {
			if (node.getTag() == 0) {
				return false;
			}
		}
		reverse(temp);
		for(int key:temp.nodes.keySet()) {
			temp.nodes.get(key).setTag(0);
		}
		Collection<node_data> nss = temp.getV();
		for(node_data t:nss) {
			DFS(t); 
			break;
		}
		for (node_data node1 : nss) {
			if (node1.getTag() == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * 
	 * 
	 * THE renowned DFS algorithm.
	 * traversing graph.
	 * @param node
	 */
	private void DFS(node_data node) {
		node.setTag(1);
		Collection<edge_data> t_c = this.gr.getE(node.getKey());
		if (t_c != null) {
			for (edge_data edge : t_c) {
				if (gr.getNode(edge.getDest()) != null && gr.getE(edge.getDest()) != null && gr.getNode(edge.getDest()).getTag() == 0) {
					DFS(gr.getNode(edge.getDest()));
				}
			}
		}
	}

	/**
	 * 
	 * 
	 * reverses graph's edges
	 *and replaces the dest with the source for each edge .
	 * @param g
	 */
	public void reverse (graph g) {
		Collection<node_data> n = gr.getV();
		for(node_data nodesource:n) {
			Collection<edge_data> e = gr.getE(nodesource.getKey());
			for(edge_data edge:e) {
				edgeData c = new edgeData((edgeData)edge);
				int tag = c.getTag();
				e.remove(c);
				gr.connect(edge.getDest(), edge.getSrc(), edge.getWeight());
				gr.getEdge(edge.getDest(), edge.getSrc()).setTag(tag);
			}
		}	
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		try {
			dijakstra(src);
			return gr.getNode(dest).getWeight();
		} 
		catch (NullPointerException e) {
			return -1;
		}
	}

	/**
	 * Calculates shortest path between two selected nodes.
	 * returns double.MAX_VALUE
	 */
	@Override
	public List<node_data> shortestPath(int src, int dest) {
		dijakstra(src);

		List<node_data> ans = new ArrayList<node_data>();
		node_data current = gr.getNode(dest);
		while(!current.getInfo().isEmpty()||current.getKey()==gr.getNode(src).getKey())	{
			ans.add(0, current);
			current = gr.getNode(Integer.parseInt(current.getInfo()));
			if(current.getKey()==gr.getNode(src).getKey()) break;
		}
		ans.add(0, current);
		return ans;
	}

	/**
	 * THE renowned dijakstra algorithm.
	 * calculating the paths from a selected node.
	 * to all other nodes in graph.

	 * @param src
	 */
	public void dijakstra(int src) {

		List<node_data>  unvisited = new LinkedList<node_data>();
		Collection<node_data> n = gr.getV(); 
		for(node_data nd:n) unvisited.add(nd);
		List<node_data>  visited = new LinkedList<node_data>();

		for(int key:this.gr.nodes.keySet())	{
			this.gr.nodes.get(key).setWeight(Double.MAX_VALUE);		
		}

		this.gr.nodes.get(src).setWeight(0);

		node_data current = gr.nodes.get(src); //this node

		while(!unvisited.isEmpty()||Infinity(gr)||current!=null) {

			visited.add(current);

			unvisited.remove(current);

			Collection<edge_data> e = gr.getE(current.getKey()); //all th edges starting with the current node
			if(e==null) { //case when no edges coming out of the node

				current = unvistedmin(unvisited);
				if(current==null) break;
				continue;
			}
			for(edge_data edge:e) {
				node_data destnode = gr.getNode(edge.getDest()); // the neighbors of this specific current node 
				if(current.getWeight()+edge.getWeight()<=gr.nodes.get(destnode.getKey()).getWeight()) { // checks if this node's weight and edge is starting 
					gr.nodes.get(destnode.getKey()).setWeight(edge.getWeight()+current.getWeight()); 
					destnode.setInfo(""+current.getKey());// at this node weight is less than the dest node weight
				}
			}
			current = unvistedmin(unvisited);	
			if(current==null) break;
		}
		return;
	}

	/**
	 * Creates a list of unvisited nodes.
	 * @param ar
	 * @return list of unvisited nodes
	 */
	public node_data unvistedmin(List<node_data> ar) {
		if(ar.isEmpty()) return null;
		double min = Double.MAX_VALUE;
		node_data mini=null;
		for(int i=0; i<ar.size();i++) {
			if(ar.get(i).getWeight()<min) {
				min=ar.get(i).getWeight();
				mini=ar.get(i);
			}
		}
		return mini;
	}

	/**
	 * Method that check if there are nodes that weight infinity.
	 * @param g
	 * @return true if there are, else false.
	 */
	public boolean Infinity(graph g) {
		Collection<node_data> s = g.getV();
		for(node_data n : s) {
			if(n.getWeight()==Double.MAX_VALUE) return true;
		}
		return false;
	}

	@Override
	/**
	 * Method that calculates and returns a path
	 * that is passing through every node in 
	 * the target list
	 * runtime complexity : forever.
	 */
	public List<node_data> TSP(List<Integer> targets) { 

		if(targets.size()==0) throw new RuntimeException("Empty list of targets");
		ArrayList<node_data> ans = new ArrayList<node_data>();
		if(targets.size()==1) {
			ans.add(gr.getNode(targets.get(0)));
			return ans;
		}

		Collection<node_data> all_nodes = gr.getV(); //collection of all nodes in the graph
		ArrayList<node_data> target_nodes = new ArrayList<node_data>();//array list that will contain target nodes ONLY
		ArrayList<node_data> all_nodesList = new ArrayList<node_data>();//array list that will contain ALL nodes
		for(node_data nd:all_nodes) {
			nd.setTag(0);
			all_nodesList.add(nd);
			if(targets.contains(nd.getKey())) {
				target_nodes.add(nd);
			}
		}

		for(int i=1;i<2020;i++) {
			for(int j=1;j<target_nodes.size();j++) {
				ans.addAll(isTherePath(target_nodes.get(j-1), target_nodes.get(j)));
				target_nodes.get(j-1).setTag(1);
				target_nodes.get(j).setTag(1);
			}
			if(allTargetsVisted(target_nodes)) break;

			shuffle(target_nodes);
		}
		if(ans.size()<2) return null;
		RemoveDup(ans);
		return ans;	
	}

	private void RemoveDup(ArrayList<node_data> ans) {
		for(int i=1;i<ans.size();i++) {
			if(ans.get(i-1)==ans.get(i)) {
				ans.remove(i-1);
			}
		}	
	}

	/**
	 * checks if all target nodes have been visited
	 * helper of the TSP
	 * @param list
	 * @return 
	 */
	private boolean allTargetsVisted(List<node_data> list) {
		for(node_data data:list) {
			if(data.getTag()==0) return false;
		}
		return true;
	}

	/**
	 * based on java's shuffle method
	 * @param target_nodes
	 */
	private void shuffle(ArrayList<node_data> target_nodes) {
		Collections.shuffle(target_nodes);	
	}

	/**
	 * used to check if there is a path between 2 nodes
	 * @param node_data
	 * @param node_data2
	 * @return
	 */
	private List<node_data> isTherePath(node_data node_data, node_data node_data2) {
		List<node_data> ans = shortestPath(node_data.getKey(), node_data2.getKey());
		if(ans!=null) return ans;
		return null;
	}

	@Override	
	/**
	 * deep copy
	 */
	public graph copy() {
		graph copy = new DGraph(this.gr);
		return copy;
	}

	/**
	 * @return True if Object g is a Graph_Algo that logically equals to this
	 */
	public boolean equals(Object g) {

		if(g instanceof Graph_Algo) {
			if(this.gr.getMC()==((Graph_Algo)g).gr.getMC()) {

				if(this.gr.edgeSize()==((Graph_Algo)g).gr.edgeSize()){

					for(int key:gr.nodes.keySet()) {
						if(gr.nodes.get(key).getKey()!=((Graph_Algo)g).gr.nodes.get(key).getKey()) {
							return false;
						}
						if(!(gr.edges.containsKey(key)&&((Graph_Algo)g).gr.edges.containsKey(key))) {
							return false;
						}														
					}
					return true;
				}
			}
		}
		if(g instanceof graph) {
			Graph_Algo t = new Graph_Algo((graph) g);
			return this.equals(t);
		}
		return false;
	}	
}
