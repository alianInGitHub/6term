package com.univ.labs.models;

/**
 * Created by anastasia on 4/21/17.
 */
public class Edge {
    private int fromVertexIndex;
    private int toVertexIndex;
    private float length;

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

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }
}
