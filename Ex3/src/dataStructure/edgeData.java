package dataStructure;

public class edgeData implements edge_data {

	private int src;
	private int dest;
	private double weight;
	private String info;
	private int tag;
	
	
	public edgeData(edgeData myEdge) {
		this.src=myEdge.src;
		this.dest=myEdge.dest;
		this.tag=myEdge.tag;
		this.weight=myEdge.weight;
		this.info=myEdge.info;
	}
	
	public edgeData(int src1,int dest1,double weight1,int tag1,String info1)
	{
		this.src = src1;
		this.dest = dest1;
		this.weight = weight1;
		this.tag = tag1;
		this.info = info1;

	}

	public edgeData (int src, int dest, double weight) {
		this.src=src;
		this.dest=dest;
		this.weight=weight;
		this.info=null;
		this.tag=0;
	}
	
	@Override
	public int getSrc()
	{
		return this.src;
	}

	@Override
	public int getDest()
	{
		return this.dest;
	}

	@Override
	public double getWeight()
	{
		return this.weight;
	}

	@Override
	public String getInfo()
	{
		return this.info;
	}

	@Override
	public void setInfo(String s)
	{
		this.info = s;
		
	}

	@Override
	public int getTag()
	{
		return this.tag;
	}

	@Override
	public void setTag(int t)
	{
		this.tag =t;
	}

}
