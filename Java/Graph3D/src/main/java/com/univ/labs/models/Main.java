package com.univ.labs.models;

import javafx.geometry.Point3D;

/**
 * Created by anastasia on 4/23/17.
 */
public class Main {
    public static void main(String[] args) {
        Graph3D<Point3D, Edge> graph3D = GraphBuilder.generate();
        graph3D.showData();
    }
}
