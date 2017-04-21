package com.univ.labs.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by anastasia on 4/16/17.
 */
public class Graph {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOG = Logger.getLogger(Graph.class);

    private ArrayList<Point3D> vertexes;
    private ArrayList<ArrayList<Edge>> graph;
    private ArrayList<Point2D> pathEdges;
    private int startPointIndex;
    private int endPointIndex;

    public void generate() {
        //TODO: generate random 3D graph
        ArrayList<Point3D> vertexes = generatePoints();
        writeToJSON(vertexes);
    }

    private ArrayList<Point3D> generatePoints() {
        Random random = new Random();
        int amountOfVertexes = random.nextInt(80) + random.nextInt(20);
        ArrayList<Point3D> vertexes = new ArrayList<Point3D>();
        for (int i = 0; i < amountOfVertexes; i++) {
            vertexes.add(new Point3D(random.nextDouble(), random.nextDouble(), random.nextDouble()));
        }
        return vertexes;
    }

    private void writeToJSON(ArrayList<Point3D> vertexes) {
        try {
            JsonWriter jsonWriter = GSON.newJsonWriter(new FileWriter("vertexes.json"));
            jsonWriter.beginArray();
            for (Point3D vertex : vertexes) {
                jsonWriter.beginObject();
                jsonWriter.name("X");
                jsonWriter.value(vertex.getX());
                jsonWriter.name("Y");
                jsonWriter.value(vertex.getX());
                jsonWriter.name("Z");
                jsonWriter.value(vertex.getX());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();
            jsonWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readGeneratedData() {
        getVertexes();
        getEdges();
    }

    private void getVertexes() {
        vertexes = new ArrayList<Point3D>();
        try {
            JsonReader jsonReader = GSON.newJsonReader(new FileReader("vertexes.json"));
            while (jsonReader.hasNext()) {
                Point3D vertex = new Point3D(jsonReader.nextDouble(), jsonReader.nextDouble(), jsonReader.nextDouble());
                vertexes.add(vertex);
            }
            jsonReader.close();
        } catch (FileNotFoundException e) {
            LOG.info("ERROR: cannot find file vertex.json");
        } catch (IOException e) {
            LOG.info("ERROR: cannot read file vertex.json");
        }
    }

    private void getEdges() {
        graph = new ArrayList<ArrayList<Edge>>(vertexes.size());
        try {
            JsonReader jsonReader = GSON.newJsonReader(new FileReader("graph.json"));
            while (jsonReader.hasNext()) {
                Edge edge = createEdge(jsonReader);
                graph.get(edge.getFromVertexIndex()).add(edge);
            }
            jsonReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Edge createEdge(JsonReader jsonReader) throws IOException {
        Edge edge = new Edge();
        edge.setFromVertexIndex(jsonReader.nextInt());
        edge.setToVertexIndex(jsonReader.nextInt());
        Point3D fromVertex = vertexes.get(edge.getFromVertexIndex());
        Point3D toVertex = vertexes.get(edge.getToVertexIndex());
        edge.setLength(length(fromVertex, toVertex));
        return edge;
    }

    private float length(Point3D fromVertex, Point3D toVertex) {
        return (float) Math.sqrt(Math.pow(toVertex.getX() - fromVertex.getX(), 2) +
                Math.pow(toVertex.getY() - fromVertex.getY(), 2) +
                Math.pow(toVertex.getZ() - fromVertex.getZ(), 2));
    }

    public void buildShortestPathBetween(int startPointIndex, int endPointIndex) {
        setStartPointIndex(startPointIndex);
        setEndPointIndex(endPointIndex);
        buildShortestPath();
    }

    private void buildShortestPath() {
        int amountOfVertexes = vertexes.size();
        int INF = Integer.MAX_VALUE;
        boolean[] visited = new boolean[amountOfVertexes];
        float[] distances = new float[amountOfVertexes];
        for (int i = 0; i < amountOfVertexes; i++) {
            visited[i] = false;
            distances[i] = INF;
        }

        distances[startPointIndex] = 0;

        for (int i = 0; i < amountOfVertexes; i++) {
            int currentVertexIndex = -1;
            for (int j = 0; j < amountOfVertexes; j++) {
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
                float edgeValue = graph.get(i).get(j).getLength();
                if (distances[currentVertexIndex] + edgeValue < distances[toVertexIndex]) {
                    distances[toVertexIndex] = distances[currentVertexIndex] + edgeValue;
                }
            }
        }
    }

    public String toJson() {
        return GSON.toJson(this, Graph.class);
    }

    public int getStartPointIndex() {
        return startPointIndex;
    }

    public void setStartPointIndex(int startPontIndex) {
        this.startPointIndex = startPontIndex;
    }

    public int getEndPointIndex() {
        return endPointIndex;
    }

    public void setEndPointIndex(int endPointIndex) {
        this.endPointIndex = endPointIndex;
    }

    public int getRandomVertex() {
        Random random = new Random();
        return random.nextInt(vertexes.size());
    }
}
