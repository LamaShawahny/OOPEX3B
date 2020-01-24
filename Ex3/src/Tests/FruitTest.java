package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import gameClient.Fruit;
import gameClient.myGame;

class FruitTest {

	@Test
	void testFruitCon() {
		game_service game = Game_Server.getServer(0);
		String str = game.getGraph();
		DGraph gr = new DGraph();
		gr.init(str);
		myGame myGame = new myGame(gr, game,1);
		ArrayList<Fruit> r = myGame.fru_list;
		System.out.println(r.get(0));
	}

}
