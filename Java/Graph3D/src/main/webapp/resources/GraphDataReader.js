/**
 * Created by anastasia on 4/23/17.
 */

var GraphDataReader = {
    'graphData' : null,
    'setData' : function (data) {
        graphData = data;
    },
    'readVertices' : function () {
        console.log(graphData.vertices[0].x);
        for (var i = 0; i < graphData.vertices.size; i++) {
            console.log(verticesObjects[i].x);
        }
        //for (var vertex in verticesObjects) console.log(vertex.x);
    }
};
