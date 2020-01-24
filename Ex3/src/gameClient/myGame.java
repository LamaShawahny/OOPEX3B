package gameClient;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Server.*;
import algorithms.Graph_Algo;
import dataStructure.*;
import utils.Point3D;

/**
 * Representing the game class we work on . 
 * We get the robots(iron man) and the fruits(villains) based on the amount we need.
 * after doing that we implement the game.
 * we have two kind of implementations : 
 * the first is the AUTO - gameplay with directed scenarios.
 * The other implementation is the MANUAL-gameplay where we control the robots(iron man) which we 
 * decide where to put them and then we move them the way we want 
 * This class also has the algorithms required to play the auto player.
 * @author malik faris and fadi Aamir.
 */
public class myGame {
	public game_service game;
	public graph graph;
	public ArrayList<Robot> robo_list = new ArrayList<Robot>(); // list of robots(iron man) we have.
	public ArrayList<Fruit> fru_list = new ArrayList<Fruit>(); // list of fruits(aliens) we have.
	public String score;
	int level;
	private static KmlLogger kml=new KmlLogger();
	private int moves=0;

	public String getScore() { return score; }

	public int get_level() { return this.level; }

	public int getMoves() {return this.moves;}
	
	public myGame() {;}
	
	public myGame(graph g,game_service game, int level) {
		this.graph=g;
		this.game=game;
		this.level=level;
		fetchRobots();
		fetchFruits();
	}
	
	

	
	/**
	 * This is the main driver of the game.
	 * With this the user chooses the level and the mode he wishes to play in.
	 * GAME START.
	 * @param args
	 */


	public static void main(String[] args) {
		
		
		String ID = JOptionPane.showInputDialog("please enter your id");
		int id = Integer.parseInt(ID);
		
		int level = getLevel();
		int mode = getMode();
		
		game_service game = Game_Server.getServer(level); 
		String g = game.getGraph(); 

		DGraph gg = new DGraph();
		game.addRobot(6);
		game.addRobot(9);
		game.addRobot(6);

		gg.init(g);


		myGame theGame = new myGame(gg,game,level);
		theGame.play(mode,id);

	}
	/**
	 * This gets the user to choose which mode he wishe to play in (auto or manual).
	 * @return ModeNum.
	 */
	private static int getMode() {
		ImageIcon robo = new ImageIcon("iron.png");
		String[] Mode = {"Auto", "Manual"};
		int ModeNum = JOptionPane.showOptionDialog(null, "Choose the Mode you would like to display", "Click a button",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, robo, Mode, Mode[0]);
		if (ModeNum<0) ModeNum=0;//in case user don't pick and press x
		return ModeNum;
	}
	/**
	 * This Gets the user to choose which level he wishes to play [0-23].
	 * @return gameNum.
	 */
	private static int getLevel() {
		// Logo for options-dialog
		ImageIcon robo = new ImageIcon("iron.png");

		//Set the game Level - [0,23]
		String[] options = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
		int gameNum = JOptionPane.showOptionDialog(null, "Choose the Level you would like to display", "Click a button",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, robo, options, options[0]);
		if (gameNum<0) gameNum=0;//in case user don't pick and press x
		return gameNum;

	}
	

	/**
	 * With this the user connect to the server with his own ID .
	 * and then starts the game based on his decision .
	 * when the game is over it generates a KML file and shows the score . 
	 * @param mode
	 */
	public void play(int mode , int id) {

		myGameGui r = new myGameGui((DGraph)this.graph,game,7,this);
		r.setVisible(true);


		Game_Server.login(id);

		game.startGame();

		while(game.isRunning()) {
			this.robo_updater(mode);
			r.run();
			fru_updater();
			try {
				kml.makeKML(this,0);
			} catch (ParseException | InterruptedException e){ e.printStackTrace(); }


		}

		String res = game.toString();
		game.sendKML("kmlFile.kml");
		System.out.println(res);

		JOptionPane.showMessageDialog(null, ("           Your Score is: "+Score(this.robo_list)));


	}

	/**
	 * This function parses the Json and creates an arraylist of robots(iron man) .
	 */
	private void fetchRobots() {	

		List<String> log = game.getRobots();
		if(log!=null) {
			String robot_json = log.toString();
			try {
				JSONArray line= new JSONArray(robot_json);	
				for(int i=0; i< line.length();i++) {

					JSONObject j= line.getJSONObject(i);
					JSONObject jrobots = j.getJSONObject("Robot");
					String loc = jrobots.getString("pos");
					String[] xyz = loc.split(",");
					double x = Double.parseDouble(xyz[0]);
					double y = Double.parseDouble(xyz[1]);	
					double z = Double.parseDouble(xyz[2]);
					Point3D p = new Point3D(x,y,z);
					int rid = jrobots.getInt("id");
					int src = jrobots.getInt("src");
					int dest = jrobots.getInt("dest");
					double val = jrobots.getDouble("value");
					int speed = jrobots.getInt("speed");
					Robot r = new Robot(rid,src,dest,p,val,speed);
					robo_list.add(r);			
				}
			} catch (JSONException e) { e.printStackTrace(); }	
		}
	}
	/**
	 * This function parses the Json and creates an arraylist of Fruits(Villains) .
	 */
	private void fetchFruits() {
		List<String> log = game.getFruits();
		if(log!=null) {
			String fru_json = log.toString();
			try {
				JSONArray line= new JSONArray(fru_json);		
				for(int i =0; i<line.length();i++) {
					JSONObject j = line.getJSONObject(i);
					JSONObject fru = j.getJSONObject("Fruit");
					String loc = fru.getString("pos");
					String[] xyz = loc.split(",");
					double x = Double.parseDouble(xyz[0]);
					double y = Double.parseDouble(xyz[1]);
					double z = Double.parseDouble(xyz[2]);
					Point3D p = new Point3D(x,y,z);
					double value = fru.getDouble("value");
					int type = fru.getInt("type");
					Fruit f = new Fruit(value,type,p);
					

					fru_list.add(f);
				}
			} catch (JSONException e) { e.printStackTrace(); }	
		}
	}
	/**
	 * RANDOM WALK.
	 * @param g graph
	 * @param src src of the robot
	 * @return the node the robot will head to
	 */

	public static int nextNode(graph g, int src) {
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}

	/**
	 * Checks if our robots ( iron man ) can go to the selected node .
	 * only available in manual mode .
	 * 
	 * checks if the robot can go to the selected node
	 * used only in Manual mode
	 * @param r robot
	 * @param src node of the robot
	 * @param dest node of the robot
	 * @return
	 */

	public boolean nextNodeManual(Robot r,int src,int dest) {
		if(graph.getNode(dest)==null) return false;
		boolean ans = false;
		Collection<edge_data> ee = graph.getE(src);
		for(edge_data e:ee) {
			if(e==null) return false;
			if(e.getDest()==graph.getNode(dest).getKey()) {
				//r.setDest(nextNode(graph, r.src));
				game.chooseNextEdge(r.getID(), dest);
				return true;
			}
		}
		return ans;

	}
	/**
	 * Returns true if the game is currently running.
	 * 
	 */
	public boolean isRunning() {
		return game.isRunning();
	}

	/**
	 * 
	 * This function calculates the score of each robots and shows the total score.
	 * returns a string that shows each robot with its final score .

	 */
	public int Score(ArrayList<Robot> al){
		int ans = 0;
		int total=0;
		for(int i=0;i<al.size();i++) {
			total+=al.get(i).getV();
			ans+=al.get(i).getV();

			this.score="\n";
		}
		this.score=""+total;
		this.score+=total;
		return ans;
	}

	/**
	 * Calculates the edge that has the fruit on it based on the location .
	 *
	 * This is based on the Triangle Equivalence

	 * @param f - fruit that we want to determine on witch edge it sits.
	 * @param g - graph
	 * @return the edge_data that hold the fruit.
	 */
	public static edge_data fruitToEdge(Fruit f,graph g) {
		edge_data ans = null;
		Point3D f_p = f.getPos();
		Collection<node_data> nd = g.getV();
		for(node_data n:nd) {
			Point3D ns_p = n.getLocation();
			Collection<edge_data> ed = g.getE(n.getKey());
			if(ed==null) continue;
			for(edge_data e : ed) {
				Point3D nd_p = g.getNode(e.getDest()).getLocation();
				if((ns_p.distance3D(f_p)+f_p.distance3D(nd_p))-ns_p.distance3D(nd_p)<0.000001) {
					f.setFrom(e.getSrc());
					f.setTo(e.getDest());
					return e;
				}
			}
		}

		return ans;
	}



	/**
	 * 
	 * This Function while the game is running updates the data from the server .
	 * it updates the robot's pos,src,value and dest .
	 * 
	 *  mode Manual - 1 || Automate - 0
	 *  returns an arraylist of robots that has been updated
	 * 

	 */
	public ArrayList<Robot> robo_updater(int mode) {
		List<String> log = game.move();
		moves++;
		if(log!=null) {
			robo_list.clear();
			String robot_json = log.toString();
			try {

				JSONArray line= new JSONArray(robot_json);

				for(int i=0; i< line.length();i++) {

					JSONObject j= line.getJSONObject(i);
					JSONObject jrobots = j.getJSONObject("Robot");
					String loc = jrobots.getString("pos");
					String[] xyz = loc.split(",");
					double x = Double.parseDouble(xyz[0]);
					double y = Double.parseDouble(xyz[1]);	
					double z = Double.parseDouble(xyz[2]);
					Point3D p = new Point3D(x,y,z);
					int rid = jrobots.getInt("id");
					int src = jrobots.getInt("src");
					int dest = jrobots.getInt("dest");
					double val = jrobots.getDouble("value");
					int speed = jrobots.getInt("speed");
					Robot r = new Robot(rid,src,dest,p,val,speed);
					robo_list.add(r);
					r.setLast(src);
				}
			} catch (JSONException e) { e.printStackTrace(); }
		}
		for(Robot r:robo_list) {
			if(r.getDest()==-1) {
				if(mode==0) {
					int nodetoGO = getNextNode(r, graph, fru_list);
					game.chooseNextEdge(r.getID(), nodetoGO);
					//for(node_data nd:temp2) {				
					//r.setDest(nd.getKey());
					//game.chooseNextEdge(r.getID(),r.getDest());			
					//					}
				}
				else {
					ImageIcon robo = new ImageIcon("robotB.png");
					int size = this.graph.getE(r.getSrc()).size();
					int [] tem = new int[size];
					String[] options = new String[size];
					ArrayList<edge_data> temp = new ArrayList<edge_data>();
					temp.addAll(graph.getE(r.getSrc()));
					for(int i=0;i<size;i++) {
						tem[i]=temp.get(i).getDest();
						options[i]=""+temp.get(i).getDest();

					}
					int ryyy = JOptionPane.showOptionDialog(null, "Enter node to go - Robot id:"+r.getID(), "Click", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, robo, options, options[0]);
					int dest= tem[ryyy];
					nextNodeManual(r, r.getSrc(), dest);
				}
			}
		}
		try {
			Thread.sleep(sleepTime(graph, fru_list, robo_list));
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}

		return robo_list;

	}

	/**
	 * 
	 * This Function while the game is running updates the data from the server .
	 * it updates the fruit's pos,src,value and dest .
	 * 
	 * 
	 */
	public void fru_updater() {

		fru_list.clear();
		List<String> log = game.getFruits();


		if(log!=null) {
			String fru_json = log.toString();

			try {
				JSONArray line= new JSONArray(fru_json);		
				for(int i =0; i<line.length();i++) {
					JSONObject j = line.getJSONObject(i);
					JSONObject fru = j.getJSONObject("Fruit");
					String loc = fru.getString("pos");
					String[] xyz = loc.split(",");
					double x = Double.parseDouble(xyz[0]);
					double y = Double.parseDouble(xyz[1]);
					double z = Double.parseDouble(xyz[2]);
					Point3D p = new Point3D(x,y,z);
					double value = fru.getDouble("value");
					int type = fru.getInt("type");
					Fruit f = new Fruit(value,type,p);
					fru_list.add(f);

				}
			} catch (JSONException e) { e.printStackTrace(); }
		}
	}
	


	
	  private int sleepTime(graph g,ArrayList<Fruit> arrF,ArrayList<Robot> arrR){
	        int ans =90;
	        for (Robot rob: arrR) {
	            for (Fruit fruit: arrF) {
	                edge_data temp = fruitToEdge(fruit,g);
	                if(temp.getSrc()==rob.getSrc() || temp.getDest()==rob.getSrc()){
	                    return 65;
	                }
	            }
	        }
	        return ans;
	    }
	  
		/**
		 * 
		 * Gets the next node that robot is going to .
		 * @param r robot 
		 * @param g graph
		 * @param fru_list arraylist of fruits
		 * @return the key of the node
		 */ 
	  
	  
	  private int getNextNode(Robot r , graph g, List<Fruit> fru_list ) {
			Graph_Algo graph = new Graph_Algo(g);
			edge_data temp = null;
			double min = Integer.MAX_VALUE;
			double distancefromtherobot = 0;
			int whereTo=-1;
			int finalWhereTo =-1;
			for (Fruit fruit: fru_list) {
				if (fruit.getInfo() == 0) {
					temp = fruitToEdge(fruit,g); // return the edge that the fruit is sitting on
					if (fruit.getType() == -1) { // 
						if (temp.getDest() > temp.getSrc()) {
							distancefromtherobot = graph.shortestPathDist(r.getSrc(), temp.getDest()); //return the shortest path between robot and fruit
							whereTo = temp.getSrc();
						} else if (temp.getSrc() > temp.getDest()) {
							distancefromtherobot = graph.shortestPathDist(r.getSrc(), temp.getSrc()); //return the shortest path between robot the fruit;
							whereTo = temp.getDest();
						}
						if(r.getSrc()==temp.getSrc()) {
							fruit.setInfo(1); //visited fruits.
							return temp.getDest();
						}
						if(r.getSrc()==temp.getDest()) {
							fruit.setInfo(1); //visited fruits.
							return temp.getSrc();
						}
						if (distancefromtherobot < min) {
							min = distancefromtherobot;
							finalWhereTo = whereTo; //Where to go.
						}

					} else if (fruit.getType() == 1) {
						if (temp.getDest() < temp.getSrc()) {
							distancefromtherobot = graph.shortestPathDist(r.getSrc(), temp.getDest()); //return shortest path between robot and fruit
							whereTo = temp.getDest();
						} else if (temp.getSrc() < temp.getDest()) {
							distancefromtherobot = graph.shortestPathDist(r.getSrc(), temp.getSrc()); //return shortest path between robot and fruit
							whereTo = temp.getSrc();
						}
						if(r.getSrc()==temp.getSrc()) {
							fruit.setInfo(1); //visited fruits.
							return temp.getDest();
						}
						if(r.getSrc()==temp.getDest()) {
							fruit.setInfo(1); // visited fruits.
							return temp.getSrc();
						}
						if (distancefromtherobot < min) {
							min = distancefromtherobot;
							finalWhereTo = whereTo; // Where to go.
						}

					}

				}

			}
			List<node_data> ans = graph.shortestPath(r.getSrc(), finalWhereTo); //returns the Shortest distance between the robot and the dest he wanna go to .
			for (Fruit fruit: fru_list) {
				temp = fruitToEdge(fruit,g); //returns the edge that contains the fruit .

				if(temp.getDest()==finalWhereTo || temp.getSrc()==finalWhereTo){
					fruit.setInfo(1); //fruits that has been visited.
					break;
				}
			}
			if (ans.size() == 1) {
				List<node_data> ans2 = graph.shortestPath(r.getSrc(), (finalWhereTo + 15) % 11);
				return ans2.get(1).getKey();
			}
			return ans.get(1).getKey();



		    }
	}


