package com.univ.labs.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import org.jgrapht.EdgeFactory;
import org.jgrapht.UndirectedGraph;

import java.util.*;

/**
 * Created by anastasia on 4/16/17.
 */
public class Graph3D<V, E> implements UndirectedGraph<V, E> {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private ArrayList<Point3D> vertices;
    private ArrayList<ArrayList<Edge>> graph;
    private ArrayList<Point2D> pathEdges;
    private int startPointIndex;
    private int endPointIndex;
    private Edge3DFactory<V, E> edge3DFactory;

    public Graph3D(int amountOfVertices, int amountOfEdges) {
        edge3DFactory = new Edge3DFactory<V, E>();

        vertices = new ArrayList<Point3D>(amountOfVertices);
        graph = new ArrayList<ArrayList<Edge>>(amountOfVertices);
        for (int i = 0; i < amountOfVertices; i++) {
            graph.add(new ArrayList<Edge>(amountOfEdges));
        }
    }

    public void buildShortestPathBetween(int startPointIndex, int endPointIndex) {
        setStartPointIndex(startPointIndex);
        setEndPointIndex(endPointIndex);
        buildShortestPath();
    }

    private void buildShortestPath() {
        int amountOfVertices = vertices.size();
        int INF = Integer.MAX_VALUE;
        boolean[] visited = new boolean[amountOfVertices];
        double[] distances = new double[amountOfVertices];
        for (int i = 0; i < amountOfVertices; i++) {
            visited[i] = false;
            distances[i] = INF;
        }

        distances[startPointIndex] = 0;

        for (int i = 0; i < amountOfVertices; i++) {
            int currentVertexIndex = -1;
            for (int j = 0; j < amountOfVertices; j++) {
                if (!visited[j] && ((currentVertexIndex == -1) || (distances[j] < distances[currentVertexIndex]))) {
                    currentVertexIndex = j;
                }
            }
            if (distances[currentVertexIndex] == INF) {
                break;
            }
            visited[currentVertexIndex] = true;
            for (int j = 0; j < graph.get(i).size(); j++) {
                int toVertexIndex = graph.get(i).get(j).getToVertexIndex();
                double edgeWeight = graph.get(i).get(j).getLength();
                if (distances[currentVertexIndex] + edgeWeight < distances[toVertexIndex]) {
                    distances[toVertexIndex] = distances[currentVertexIndex] + edgeWeight;
                }
            }
        }
    }

    public String toJson() {
        return GSON.toJson(this, Graph3D.class);
    }

    public int getStartPointIndex() {
        return startPointIndex;
    }

    public void setStartPointIndex(int startPointIndex) {
        this.startPointIndex = startPointIndex;
    }

    public int getEndPointIndex() {
        return endPointIndex;
    }

    public void setEndPointIndex(int endPointIndex) {
        this.endPointIndex = endPointIndex;
    }

    public int getRandomVertex() {
        Random random = new Random();
        return random.nextInt(vertices.size());
    }

    public Set<E> getAllEdges(V v, V v1) {
        return null;
    }

    public E getEdge(V v, V v1) {
        int fromVertexIndex = vertices.indexOf(v);
        int toVertexIndex = vertices.indexOf(v1);
        for (Edge edge : graph.get(fromVertexIndex)) {
            if (edge.contains(toVertexIndex)) {
                return (E)edge;
            }
        }
        return null;
    }

    public EdgeFactory<V, E> getEdgeFactory() {
        return edge3DFactory;
    }

    public E addEdge(V v, V v1) {
        Edge edge = (Edge) edge3DFactory.createEdge(v, v1);
        graph.get(edge.getFromVertexIndex()).add(edge);
        graph.get(edge.getToVertexIndex()).add(edge);
        return (E) edge;
    }

    public boolean addEdge(V v, V v1, E e) {
        e = addEdge(v, v1);
        return e != null;
    }

    public boolean addVertex(V v) {
        return vertices.add((Point3D) v);
    }

    public boolean containsEdge(V v, V v1) {
        return getEdge(v, v1) != null;
    }

    public boolean containsEdge(E e) {
        int fromId = ((Edge) e).getFromVertexIndex();
        int toId = ((Edge) e).getToVertexIndex();
        return containsEdge((V) vertices.get(fromId), (V) vertices.get(toId));
    }

    public boolean containsVertex(V v) {
        return vertices.contains(v);
    }

    public Set<E> edgeSet() {
        Set<E> edgeSet = new HashSet<E>();
        for (ArrayList<Edge> edges : graph) {
            for (Edge edge : edges) {
                if (!edgeSet.contains(edge)) {
                    edgeSet.add((E) edge);
                }
            }
        }
        return edgeSet;
    }

    public Set<E> edgesOf(V v) {
        int vertexIndex = vertices.indexOf(v);
        Set<E> edges = new HashSet<E>();
        for (Edge edge : graph.get(vertexIndex)) {
            edges.add((E) edge);
        }
        return edges;
    }

    public boolean removeAllEdges(Collection<? extends E> collection) {
        return false;
    }

    public Set<E> removeAllEdges(V v, V v1) {
        return null;
    }

    public boolean removeAllVertices(Collection<? extends V> collection) {
        return false;
    }

    public E removeEdge(V v, V v1) {
        E e = getEdge(v, v1);
        if (e != null) {
            graph.get(((Edge) e).getFromVertexIndex()).remove(e);
            graph.get(((Edge) e).getToVertexIndex()).remove(e);
        }
        return e;
    }

    public boolean removeEdge(E e) {
        int fromId = ((Edge) e).getFromVertexIndex();
        int toId = ((Edge) e).getToVertexIndex();
        return removeEdge((V) vertices.get(fromId), (V) vertices.get(toId)) != null;
    }

    public boolean removeVertex(V v) {
        return vertices.remove(v);
    }

    public Set<V> vertexSet() {
        Set<V> vertexSet = new HashSet<V>(vertices.size());
        for (Point3D vertex : vertices) {
            vertexSet.add((V) vertex);
        }
        return vertexSet;
    }

    public V getEdgeSource(E e) {
        int fromId = ((Edge) e).getFromVertexIndex();
        return (V) vertices.get(fromId);
    }

    public V getEdgeTarget(E e) {
        int toId = ((Edge) e).getToVertexIndex();
        return (V) vertices.get(toId);
    }

    public double getEdgeWeight(E e) {
        return ((Edge) e).getLength();
    }

    void showData() {
        for (int i = 0; i < graph.size(); i++) {
            System.out.print(i + ":\t");
            for (int j= 0 ;j < graph.get(i).size(); j++) {
                System.out.print("(" + graph.get(i).get(j).getFromVertexIndex() +
                        graph.get(i).get(j).getToVertexIndex() + "), ");
            }
            System.out.println();
        }
    }

    public int degreeOf(V v) {
        int vertexIndex = vertices.indexOf((Point3D) v);
        return graph.get(vertexIndex).size();
    }

    class Edge3DFactory<V, E> implements EdgeFactory<V, E> {

        public E createEdge(V v, V v1) {
            int fromId = vertices.indexOf(v);
            int toId = vertices.indexOf(v1);
            return (E) new Edge(fromId, toId, ((Point3D)v).distance((Point3D)v1));
        }
    }
}
