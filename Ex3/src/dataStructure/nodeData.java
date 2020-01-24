package dataStructure;

import java.util.Collection;
import java.util.HashMap;
import utils.Point3D;


public class nodeData implements node_data {

	private int key;
	private Point3D location;
	private double weight;
	private String info;
	private int tag;
	private static int count = 0;
	
	
	
	public nodeData(int id,Point3D loc) {
		this.key=id;
		this.location=loc;
	}

	
	public nodeData (Point3D location )
	{
		
		this.location = location;
		this.key=count++;
		this.weight = Double.POSITIVE_INFINITY;
		this.info = "";
		this.tag = 0;
	
	}
	
	
	public nodeData(nodeData node)
	{
		this.key = node.key;
		this.location = node.location;
		this.weight = node.weight;
		this.info = node.info;
		this.tag = node.tag;
	}
	
	/*
	public void add(int dest, edge_data e) {
		myMap.put(dest, e);
	}
	
	public edge_data getEdge(int dest) {
		return myMap.get(dest);
	}
	
	public edge_data removeEdge(int dest) {
		return myMap.remove(dest);
	}
	
	public int getSize () {
		return myMap.size();
	}
	
	public Collection<edge_data> getE(){
		return edges.get
	}
	*/

	@Override
	public int getKey()
	{
		return this.key;
	}

	@Override
	public Point3D getLocation()
	{
		return this.location;
	}

	@Override
	public void setLocation(Point3D p)
	{
		this.location =p;
	}

	@Override
	public double getWeight()
	{
		return this.weight;
	}

	@Override
	public void setWeight(double w)
	{
		this.weight=w; 

	}

	@Override
	public String getInfo()
	{
		return this.info;
	}

	@Override
	public void setInfo(String s)
	{
		this.info=s;

	}

	@Override
	public int getTag()
	{
		
		return this.tag;
		
	}

	@Override
	public void setTag(int t) 
	{
		this.tag=t;

	}
	
	

}
