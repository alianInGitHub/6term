package com.univ.labs;

import com.univ.labs.models.Graph;
import org.apache.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created by anastasia on 4/16/17.
 */
@ServerEndpoint("/graph")
public class WebSocketServer {
    private static final Logger LOG = Logger.getLogger(WebSocketServer.class);

    @OnOpen
    public void open(Session session) throws IOException {
        LOG.info("connection opened");
        Graph graph = new Graph();
        graph.generate();
        graph.buildShortestPathBetween(graph.getRandomVertex(), graph.getRandomVertex());
        session.getBasicRemote().sendText(graph.toJson());
        session.close();
    }

    @OnClose
    public void close(Session session) {
        LOG.info("Connection closed");
    }

    @OnError
    public void onError(Throwable error) {
        LOG.info("Connection error");
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        LOG.info("Message: " + message);
    }

}
