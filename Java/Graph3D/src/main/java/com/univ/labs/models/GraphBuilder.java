package com.univ.labs.models;

import javafx.geometry.Point3D;
import org.jgrapht.generate.GnmRandomGraphGenerator;

import java.util.Random;

/**
 * Created by anastasia on 4/23/17.
 */
public class GraphBuilder {

    public static Graph3D<Point3D, Edge> generate() {
        //TODO: generate random 3D graph
        Random random = new Random();
        int amountOfVertices = random.nextInt(80) + random.nextInt(20) + 3;
        int amountOfEdges = random.nextInt(amountOfVertices / 2) + random.nextInt(amountOfVertices * (amountOfVertices - 2) / 2);
        GnmRandomGraphGenerator<Point3D, Edge> generator = new GnmRandomGraphGenerator<Point3D, Edge>(amountOfVertices, amountOfEdges);
        Graph3D<Point3D, Edge> graph3D = new Graph3D<Point3D, Edge>(amountOfVertices, amountOfEdges);
        generator.generateGraph(graph3D, new Point3DFactory<Point3D, Edge>(), null);
        return graph3D;
    }
}
