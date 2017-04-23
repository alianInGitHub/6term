package com.univ.labs.models;

import javafx.geometry.Point3D;
import org.jgrapht.VertexFactory;

import java.util.Random;

/**
 * Created by anastasia on 4/23/17.
 */
public class Point3DFactory<V, E> implements VertexFactory {
    private Random random;
    private final int SCALE = 6;

    public Point3DFactory() {
        random = new Random();
    }

    public Point3D createVertex() {
        return new Point3D(
                random.nextDouble() * SCALE - SCALE / 2,
                random.nextDouble() * SCALE - SCALE / 2,
                random.nextDouble() * SCALE - SCALE / 2
        );
    }
}