package dataStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;

import org.json.JSONArray;
import org.json.JSONObject;


import utils.Point3D;

public class DGraph extends Observable implements graph,Serializable{

	
	public HashMap <Integer , HashMap <Integer , edge_data>> edges  = new HashMap <Integer , HashMap <Integer , edge_data>>();
	public HashMap <Integer , node_data> nodes  = new HashMap <Integer , node_data>();
	public static int MC = 0;
	public int edgesCounter=0;

	
	public DGraph() {
		this.nodes = new HashMap<Integer, node_data>();
		this.edges = new HashMap<Integer, HashMap<Integer,edge_data>>();
		this.edgesCounter=0;
		this.MC=0;
	}
	
	public DGraph(DGraph G) {
		this.nodes.putAll(G.nodes);
		this.edges.putAll(G.edges);
		this.MC=G.MC;
		this.edgesCounter=G.edgesCounter;
	}
	
	
	public node_data getNode(int key) {
		if(nodes.get(key) == null)
			return null;
		return nodes.get(key);
	}

	
	public edge_data getEdge(int src, int dest) {
		if(edges.get(src).get(dest) == null)
			return null;
		return edges.get(src).get(dest);
	}

	@Override
	public void addNode(node_data n) {
		nodes.put(n.getKey(), n);
		MC++;
		
	}

	@Override
	public void connect(int src, int dest, double w) {
		if(nodes.containsKey(src) || nodes.containsKey(dest))
		{
			edgeData edge = new edgeData(src , dest , w);
			if(edges.containsKey(src))
			{
				edges.get(src).put(dest, edge);
				edgesCounter++;
			}
			else
			{
				edges.put(src, new HashMap<Integer,edge_data>());
				edges.get(src).put(dest, edge);
				edgesCounter++;
			}
		}
		else
			System.out.println("impossible to connect nodes");
		MC++;
	}
	

	@Override
	public Collection<node_data> getV() {
		if(this.nodes.isEmpty())
			return null;
		return nodes.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		if(this.edges.isEmpty())
			return null;
		if(this.edges.get(node_id) == null)
			return null;
		return edges.get(node_id).values();
	}

	
	@Override
	public node_data removeNode(int key) {
		if (nodes.get(key)==null) { return null; }
		
		node_data result = new nodeData((nodeData)nodes.get(key));
		ArrayList<Integer> deleteAll = new ArrayList<Integer>();
		

		this.edges.forEach((k, v) -> {
			if (v.get(key)!=null) {
				v.remove(key);
				edgesCounter--;
				MC++;
				if (v.isEmpty()) {
					deleteAll.add(k);
				}
			}
		});
		for (int i : deleteAll) {
			this.edges.remove(i);
		}
		
		edgesCounter -= this.edges.get(key).size();
		this.edges.remove(key);
		this.nodes.remove(key);
		MC++;
		return result;
	}

	
	@Override
	public edge_data removeEdge(int src, int dest) {
		if(edges.get(src).get(dest) == null)
			return null;
		MC++;
		edgesCounter--;
		return edges.get(src).remove(dest);
	}
	
	public boolean cNodes(int k) {
		if (this.nodes.containsKey(k)) { return true; }
		return false;
	}
	
	public boolean cEdges(int s, int d) {
		if (this.edges.containsKey(s)) {
			if (this.edges.get(s).containsKey(d)) { return true; }
		}
		return false;
	}

	@Override
	public int nodeSize() {
		return nodes.size();
	}

	@Override
	public int edgeSize() {
		return edgesCounter;
	}

	@Override
	public int getMC() {
		return MC;
	}

	public void init(String str) {
		try {
			JSONObject jobj = new JSONObject(str);
			JSONArray Jedges = jobj.getJSONArray("Edges");
			JSONArray Jnodes = jobj.getJSONArray("Nodes");	

			for (int i = 0; i < Jnodes.length(); i++) {
				JSONObject nody= (JSONObject) Jnodes.get(i);
				String location = (String) nody.getString("pos");
				String[] points = location.split(",");
				double x = Double.parseDouble(points[0]);
				double y = Double.parseDouble(points[1]);	
				double z = Double.parseDouble(points[2]);
				int id = nody.getInt("id");
				Point3D p = new Point3D(x,y,z);
				node_data n = new nodeData(id, p);
				this.addNode(n);
			}
			for (int i = 0; i < Jedges.length(); i++) {
				JSONObject edgeE= (JSONObject) Jedges.get(i);
				int src = edgeE.getInt("src");
				int dest = edgeE.getInt("dest");
				double weight = edgeE.getDouble("w");
				this.connect(src, dest, weight);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		} 
	}



}
