package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import gameClient.Robot;
import utils.Point3D;

class RobotTest {

	@Test
	void testGetID() {
		//Point3D p,int VALUE,int ID,int SRC,int DEST,int SPEED
		int value = 10; 
		int ID = 2;
		int SRC =3;
		int DEST =4;
		int speed = 1;
		Point3D p = new Point3D(10,10,0);
		Robot r = new Robot(ID , SRC ,DEST , p , value, speed);
		assertEquals(2, r.getID());
	}

	@Test
	void testSetID() {
		int value = 10; 
		int ID = 2;
		int SRC =3;
		int DEST =4;
		int speed = 1;
		Point3D p = new Point3D(10,10,0);
		Robot r = new Robot(ID , SRC ,DEST , p , value, speed);
		assertEquals(2, r.getID());
		r.setID(8);
		assertEquals(8, r.getID());
	}

	@Test
	void testGetSpeed() {
		int value = 10; 
		int ID = 2;
		int SRC =3;
		int DEST =4;
		int speed = 1;
		Point3D p = new Point3D(10,10,0);
		Robot r = new Robot(SRC , value ,ID , p , DEST, speed);
		assertEquals(1, r.getSpeed());
	}

	@Test
	void testSetSpeed() {
		int value = 10; 
		int ID = 2;
		int SRC =3;
		int DEST =4;
		int speed = 1;
		Point3D p = new Point3D(10,10,0);
		Robot r = new Robot(SRC , value ,ID , p , DEST, speed);
		assertEquals(1, r.getSpeed());
		r.setSpeed(8);
		assertEquals(8, r.getSpeed());
	}

	@Test
	void testGetPoint3D() {
		int value = 10; 
		int ID = 2;
		int SRC =3;
		int DEST =4;
		int speed = 1;
		Point3D p = new Point3D(10,10,0);
		Robot r = new Robot(SRC , value ,ID , p , DEST, speed);
		assertEquals(10, r.getPos().ix());
		assertEquals(10, r.getPos().iy());
		assertEquals(0, r.getPos().iz());
	}

	@Test
	void testSetPoint3D() {
		int value = 10; 
		int ID = 2;
		int SRC =3;
		int DEST =4;
		int speed = 1;
		Point3D p = new Point3D(10,10,0);
		Robot r = new Robot(SRC , value ,ID , p , DEST, speed);
		assertEquals(10, r.getPos().ix());
		assertEquals(10, r.getPos().iy());
		assertEquals(0, r.getPos().iz());
		Point3D po = new Point3D(11,11,9);
		r.setPos(po);
		assertEquals(11, r.getPos().ix());
		assertEquals(11, r.getPos().iy());
		assertEquals(9, r.getPos().iz());
	}

	@Test
	void testGetValue() {
		int value = 10; 
		int ID = 2;
		int SRC =3;
		int DEST =4;
		int speed = 1;
		Point3D p = new Point3D(10,10,0);
		Robot r = new Robot(ID , SRC ,DEST , p , value, speed);
		assertEquals(10, r.getV());
	}

	@Test
	void testGetSrc() {
		int value = 10; 
		int ID = 2;
		int SRC =3;
		int DEST =4;
		int speed = 1;
		Point3D p = new Point3D(10,10,0);
		Robot r = new Robot(ID , SRC ,DEST , p , value, speed);
		assertEquals(3, r.getSrc());
	}

	@Test
	void testSetSrc() {
		int value = 10; 
		int ID = 2;
		int SRC =3;
		int DEST =4;
		int speed = 1;
		Point3D p = new Point3D(10,10,0);
		Robot r = new Robot(ID , SRC ,DEST , p , value, speed);
		assertEquals(3, r.getSrc());
		r.setSrc(4);
		assertEquals(4, r.getSrc());
	}

	@Test
	void testGetDest() {
		int value = 10; 
		int ID = 2;
		int SRC =3;
		int DEST =4;
		int speed = 1;
		Point3D p = new Point3D(10,10,0);
		Robot r = new Robot(ID , SRC ,DEST , p , value, speed);
		assertEquals(4, r.getDest());
	}
	
	


	@Test
	void testSetDest() {
		int value = 10; 
		int ID = 2;
		int SRC =3;
		int DEST =4;
		int speed = 1;
		Point3D p = new Point3D(10,10,0);
		Robot r = new Robot(ID , SRC ,DEST , p , value, speed);
		assertEquals(4, r.getDest());
		r.setDest(6);
		assertEquals(6, r.getDest());
	}
	
	
}
