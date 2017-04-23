package com.univ.labs.models;

import javafx.geometry.Point3D;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BidirectionalDijkstraShortestPath;
import org.jgrapht.generate.GnmRandomGraphGenerator;

import java.util.Random;

/**
 * Created by anastasia on 4/23/17.
 */
public class GraphBuilder {

    public static Graph3D<Point3D, Edge> generate() {
        Random random = new Random();
        int amountOfVertices = random.nextInt(80) + random.nextInt(20) + 15;
        int amountOfEdges = random.nextInt(amountOfVertices / 2) + random.nextInt(amountOfVertices * amountOfVertices / 4);
        GnmRandomGraphGenerator<Point3D, Edge> generator = new GnmRandomGraphGenerator<Point3D, Edge>(amountOfVertices, amountOfEdges);
        Graph3D<Point3D, Edge> graph3D = new Graph3D<Point3D, Edge>(amountOfVertices, amountOfEdges);
        generator.generateGraph(graph3D, new Point3DFactory<Point3D, Edge>(), null);
        return graph3D;
    }

    public static void buildShortestPathBetweenRandomVertices(Graph3D<Point3D, Edge> graph3D) {
        Point3D source = graph3D.getRandomVertex();
        Point3D sink = graph3D.getRandomVertex();
        while (source.equals(sink)) {
            sink = graph3D.getRandomVertex();
        }
        graph3D.setStartPointIndex(source);
        graph3D.setEndPointIndex(sink);
        BidirectionalDijkstraShortestPath<Point3D, Edge> shortestPath = new BidirectionalDijkstraShortestPath<Point3D, Edge>(graph3D);
        GraphPath<Point3D, Edge> path = shortestPath.getPath(source, sink);
        graph3D.setPathEdges(path.getEdgeList());
        graph3D.setPathSumWeigt(path.getWeight());
    }
}
