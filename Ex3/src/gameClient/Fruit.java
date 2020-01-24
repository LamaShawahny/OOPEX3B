package gameClient;

import utils.Point3D;

/**
 * this class represents a fruit(Villain )
 * can be a thanos or alien(-1 = thanos, 1 = alien).
 * we need to read data from json file and fetch it to the fruit.
 * such:
 * where he is, his points, and the fruit itself.
 * @author Fadi and Malik
 */
public class Fruit {
	double value;
	int type;
	Point3D pos;
	public int from;
	public int to;
	public int info;
	
	
	public Fruit(double value, int t, Point3D pos) {
		this.value=value;
		this.pos=pos;
		this.type=t;
	}
	public Fruit() {
		
	}
	public Fruit(Fruit f) {
		this.from=f.from;
		this.to=f.to;
		this.pos=f.pos;
		this.type=f.type;
		this.value=f.value; 
	}

	public int getType() { return this.type; }
	public void setPos(Point3D npn) { this.pos = npn; }
	public Point3D getPos() { return this.pos; }
	public void setV(double v) { this.value=v; }
	public double getV() { return this.value; }
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public int getFrom() {
		return from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	public int getTo() {
		return to;
	}
	public void setTo(int to) {
		this.to = to;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setInfo(int x) {this.info=x;}
	public int getInfo() { return this.info; }

}