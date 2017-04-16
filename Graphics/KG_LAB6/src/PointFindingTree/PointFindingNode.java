package PointFindingTree;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by anastasia on 4/12/17.
 */
class PointFindingNode {
    private ArrayList<Point> bHull;
    private int height;
    private PointFindingNode parent;
    private PointFindingNode leftChild;
    private PointFindingNode rightChild;

    public PointFindingNode (PointFindingNode parent) {
        this.parent = parent;
        if (parent != null)
            this.height = parent.height + 1;
        else
            this.height = 0;
    }

    public PointFindingNode getLeftChild() {
        return leftChild;
    }

    public PointFindingNode getRightChild() {
        return rightChild;
    }

    public PointFindingNode getParent() {
        return parent;
    }

    public PointFindingNode getGrandparent() {
        if (parent == null)
            return null;
        return parent.getParent();
    }

    public boolean isLeaf() {
        if (bHull.size() == 1)
            return true;
        return false;
    }

    public void setLeftChild(Point point) {
        leftChild = new PointFindingNode(this);
    }

    public void setRightChild(Point point) {
        rightChild = new PointFindingNode(this);
    }

    public void addPoint(Point p) {
        bHull.add(p);
    }
}
