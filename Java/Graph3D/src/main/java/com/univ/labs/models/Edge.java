package com.univ.labs.models;

/**
 * Created by anastasia on 4/21/17.
 */
public class Edge {
    private int fromVertexIndex;
    private int toVertexIndex;
    private double length;

    public Edge() {

    }

    public Edge(int fromVertexIndex, int toVertexIndex, double length) {
        this.fromVertexIndex = fromVertexIndex;
        this.toVertexIndex = toVertexIndex;
        this.length = length;
    }

    public int getFromVertexIndex() {
        return fromVertexIndex;
    }

    public void setFromVertexIndex(int fromVertexIndex) {
        this.fromVertexIndex = fromVertexIndex;
    }

    public int getToVertexIndex() {
        return toVertexIndex;
    }

    public void setToVertexIndex(int toVertexIndex) {
        this.toVertexIndex = toVertexIndex;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public boolean contains(int vertexIndex) {
        if ((fromVertexIndex == vertexIndex) || (toVertexIndex == vertexIndex))
            return true;
        return false;
    }
}
