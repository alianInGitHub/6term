/**
 * Created by anastasia on 4/23/17.
 */

var GraphDataReader = {
    'graphData' : null,
    'setData' : function (data) {
        graphData = data;
    },
    'readVertices' : function () {
        var vertices = [];
        for (var i = 0;; i++) {
            try {
                vertices.push(graphData.vertices[i].x);
                vertices.push(graphData.vertices[i].y);
                vertices.push(graphData.vertices[i].z);
            } catch (e) {
                break;
            }
        }
        return vertices;
    },
    'readEdges' : function () {
        var edges = [];
        for (var i = 0; ; i++) {
            for (var j = 0; ; j++) {
                try {
                    edges.push(graphData.graph[i][j].fromVertexIndex);
                    edges.push(graphData.graph[i][j].toVertexIndex);
                } catch (e) {
                    if (j == 0) {
                        console.log(edges);
                        return edges;
                    }
                    break;
                }
            }
        }
        return edges;
    },
    'readPathEdges' : function () {
        var edges = [];
        for (var i = 0; ; i++) {
            try {
                edges.push(graphData.pathEdges[i].fromVertexIndex);
                edges.push(graphData.pathEdges[i].toVertexIndex);
            } catch (e) {
                break;
            }
        }
        return edges;
    },
    'readSourceIndex': function () {
        return graphData.startPointIndex;
    },
    'readSinkIndex': function () {
        return graphData.endPointIndex;
    },
    'readPathWeight': function () {
        return graphData.pathSumWeigt;
    }
};
