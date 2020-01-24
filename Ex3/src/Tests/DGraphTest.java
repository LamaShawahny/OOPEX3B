package Tests;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import dataStructure.DGraph;
import dataStructure.graph;
import dataStructure.*;
import utils.Point3D;

class DGraphTest {
	
	@Test
	void testAddNode() {
		Point3D p1 = new Point3D(0, 0, 0);
		Point3D p2 = new Point3D(0, 1, 2);
		Point3D p3 = new Point3D(1, 2, 0);
		nodeData n1 = new nodeData(p1);
		nodeData n2 = new nodeData(p2);
		nodeData n3 = new nodeData(p3);
		graph g = new DGraph();
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		if (g.nodeSize()!=3) { fail(); }
	}

	@Test
	void testConnect() {
		Point3D p1 = new Point3D(1, 5, 0);
		Point3D p2 = new Point3D(4, 4, 0);
		Point3D p3 = new Point3D(2, 2, 0);
		nodeData n1 = new nodeData(p1);
		nodeData n2 = new nodeData(p2);
		nodeData n3 = new nodeData(p3);
		graph g = new DGraph();
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.connect(n1.getKey(), n2.getKey(), 2);
		g.connect(n2.getKey(), n3.getKey(), 3);

		if (g.edgeSize()!=2) { fail(); }
		if (g.getEdge(n2.getKey(), n3.getKey()).getWeight()!=3) { fail(); }	
	}

	@Test
	void testRemoveNode() {
		Point3D p1 = new Point3D(1, 5, 0);
		Point3D p2 = new Point3D(4, 4, 0);
		Point3D p3 = new Point3D(2, 2, 0);
		nodeData n1 = new nodeData(p1);
		nodeData n2 = new nodeData(p2);
		nodeData n3 = new nodeData(p3);
		graph g = new DGraph();
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.connect(n1.getKey(), n2.getKey(), 2);
		g.connect(n2.getKey(), n3.getKey(), 3);
		
		g.removeNode(n2.getKey());
		
		if (g.edgeSize()!=0) { fail(); }
		try {
			g.getEdge(n2.getKey(), n3.getKey());
			fail();
		}catch (Exception e) {;}
		try {
			g.getEdge(n1.getKey(), n2.getKey());
			fail();
		}catch (Exception e) {;}
	}

	@Test
	void testRemoveEdge() {
		Point3D p1 = new Point3D(1, 5, 0);
		Point3D p2 = new Point3D(4, 4, 0);
		Point3D p3 = new Point3D(2, 2, 0);
		nodeData n1 = new nodeData(p1);
		nodeData n2 = new nodeData(p2);
		nodeData n3 = new nodeData(p3);
		graph g = new DGraph();
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.connect(n1.getKey(), n2.getKey(), 2);
		g.connect(n2.getKey(), n3.getKey(), 3);
		
		g.removeEdge(n2.getKey(), n3.getKey());
		if (g.edgeSize()!=1) { fail(); }
		if (g.getEdge(n2.getKey(), n3.getKey()) != null) { fail();}
		if (g.getEdge(n1.getKey(), n2.getKey()) == null) { fail();}
	}
}
