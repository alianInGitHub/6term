/**
 * Created by anastasia on 4/16/17.
 */
var backgroundColor = [0.0, 0.1, 0.15, 1.0];
var edgeColor = [0.02, 0.22, 0.32, 1.0];
var vertexColor = [0.94, 0.51, 0.06, 1.0];
var pathEdgeColor = [0.9, 0.87, 0.27, 1.0];

var graphReader;

var InitConnection = function() {
    var socket = new WebSocket('ws://localhost:8080/graph');
    socket.onopen = function () {
        console.log('Connected');
        graphReader = GraphDataReader;
    };

    socket.onmessage = function (evt) {
        console.log(evt.data);
        var jsonData = JSON.parse(evt.data);
        console.log(jsonData.vertices[0].x);
        graphReader.setData(JSON.parse(evt.data));
        InitWebGL();
    };
};

var InitWebGL = function () {
    console.log("This is working.");

    var canvas = document.getElementById('graph');
    var gl = canvas.getContext('webgl');
    if (!gl) {
        console.log('WebGL not supported.');
        gl = canvas.getContext('experimental webgl');
    }

    if (!gl) {
        alert('Your browser does not support WebGL.');
    }

    graphReader.readVertices();

    var vertices = [
        -1, -1, -1, // 0
        1, -1, -1, // 1
        1,  1, -1, // 2
        -1,  1, -1, // 3
        -1, -1,  1, // 4
        1, -1,  1, // 5
        1,  1,  1, // 6
        -1,  1,  1  // 7
    ];
    var edges = [
        0, 1,   0, 4,   1, 2,   1, 5,
        2, 3,   2, 6,   3, 0,   3, 7,
        4, 5,   5, 6,   6, 7,   7, 4
    ];

    var pathEdges = [
        1, 0,   0, 4,   4, 7
    ];

    // get list of vertices coordinates
    // list of edges
    // and computed path from one vertex to another

    var vertexShader = createShader(gl, gl.VERTEX_SHADER, vertexShaderText);
    if (vertexShader == null)
        return;
    var fragmentShader = createShader(gl, gl.FRAGMENT_SHADER, fragmentShaderText);
    if (fragmentShader == null)
        return;
    var vertexProgram = createProgram(gl, vertexShader, fragmentShader);
    if (vertexProgram == null)
        return;

    gl.useProgram(vertexProgram);

    var worldMatrix = createMat4Identity(16);
    var viewMatrix = new Float32Array(16);
    var projectionMatrix = new Float32Array(16);
    mat4.identity(worldMatrix);
    mat4.lookAt(viewMatrix, [3 , 2, -10], [0, 0, 0], [0, 1, 0]);
    mat4.perspective(
        projectionMatrix,
        glMatrix.toRadian(45),
        canvas.width / canvas.height,
        0.1,
        1000.0
    );

    createVertexBufferObject(gl, vertices);

    var positionAttribLocation = gl.getAttribLocation(vertexProgram, 'vertexPosition');
    gl.vertexAttribPointer(
        positionAttribLocation,
        3,
        gl.FLOAT,
        gl.FALSE,
        3 * Float32Array.BYTES_PER_ELEMENT,
        0
    );

    var edgesBufferObject = createEdgeBufferObject(gl, edges);
    var pathEdgesBufferObject = createEdgeBufferObject(gl, pathEdges);

    gl.enableVertexAttribArray(positionAttribLocation);

    //colors
    var vertexColorUniformLocation = gl.getUniformLocation(vertexProgram, 'color');
    var edgeColorUniformLocation = gl.getUniformLocation(vertexProgram, 'color');
    var pathEdgeColorUniformLocation = gl.getUniformLocation(vertexProgram, 'color');

    var vertexDisplayMatrices = makeDisplayMatrices();
    vertexDisplayMatrices.init(gl, vertexProgram);
    vertexDisplayMatrices.updateWord(gl, worldMatrix);
    vertexDisplayMatrices.updateView(gl, viewMatrix);
    vertexDisplayMatrices.updateProjection(gl, projectionMatrix);


    var xRotationMatrix = createMat4Identity(16);
    var yRotationMatrix = createMat4Identity(16);

    var angle = 0;
    var identityMatrix = createMat4Identity(16);
    var loop = function () {
        angle = performance.now() / 3000 / 6 * 2 * Math.PI;
        mat4.rotate(xRotationMatrix, identityMatrix, angle, [0, 1, 0]);
        mat4.rotate(yRotationMatrix, identityMatrix, angle / 3, [1, 0, 0]);
        mat4.mul(worldMatrix, xRotationMatrix, yRotationMatrix);
        vertexDisplayMatrices.updateWord(gl, worldMatrix);

        gl.clearColor(backgroundColor[0], backgroundColor[1], backgroundColor[2], backgroundColor[3]);
        gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

        drawEdges(gl, edgeColorUniformLocation, edges, edgesBufferObject, edgeColor);
        drawEdges(gl, pathEdgeColorUniformLocation, pathEdges, pathEdgesBufferObject, pathEdgeColor);
        drawVertexes(gl, vertexColorUniformLocation);
        requestAnimationFrame(loop);
    };
    requestAnimationFrame(loop);
};

var createProgram = function(gl, vertexShader, fragmentShader) {
    var program = gl.createProgram();
    gl.attachShader(program, vertexShader);
    gl.attachShader(program, fragmentShader);
    gl.linkProgram(program);
    if (!gl.getProgramParameter(program, gl.LINK_STATUS)) {
        console.error('ERROR linking program:\n', gl.getProgramInfoLog(program));
        return null;
    }
    gl.validateProgram(program);
    if(!gl.getProgramParameter(program, gl.VALIDATE_STATUS)) {
        console.error("ERROR in validation:\t", gl.getProgramInfoLog(program));
        return null;
    }

    return program;
};

var createShader = function (gl, shaderType, shaderText) {
    var shader = gl.createShader(shaderType);
    gl.shaderSource(shader, shaderText);
    gl.compileShader(shader);
    if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
        console.error('ERROR in shader:\t', gl.getShaderInfoLog(shader));
        return null;
    }
    return shader;
};

var createMat4Identity = function(size) {
    var matrix = new Float32Array(size);
    mat4.identity(matrix);
    return matrix;
};

function makeDisplayMatrices() {
    return {
        'worldUniformLocation': null,
        'viewUniformLocation': null,
        'projectionUniformLocation': null,
        'init': function (gl, program) {
            this.worldUniformLocation = gl.getUniformLocation(program, 'world');
            this.viewUniformLocation = gl.getUniformLocation(program, 'view');
            this.projectionUniformLocation = gl.getUniformLocation(program, 'projection');
        },
        'updateWord': function (gl, worldMatrix) {
            gl.uniformMatrix4fv(this.worldUniformLocation, gl.FALSE, worldMatrix);
        },
        'updateView': function (gl, viewMatrix) {
            gl.uniformMatrix4fv(this.viewUniformLocation, gl.FALSE, viewMatrix);
        },
        'updateProjection': function (gl, projectionMatrix) {
            gl.uniformMatrix4fv(this.projectionUniformLocation, gl.FALSE, projectionMatrix);
        }
    };
}

var createVertexBufferObject = function (gl, vertices) {
    var vertexBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, vertexBufferObject);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);
    //return vertexBufferObject;
};

var createEdgeBufferObject = function (gl, edges) {
    var edgesBufferObject = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, edgesBufferObject);
    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(edges), gl.STATIC_DRAW);
    return edgesBufferObject;
};

drawVertexes = function(gl, vertexColorUniformLocation) {
    gl.uniform4f(vertexColorUniformLocation, vertexColor[0], vertexColor[1], vertexColor[2], 1.0);
    gl.drawArrays(gl.POINTS, 0, 8);
};

drawEdges = function(gl, colorUniformLocation, edges, edgesBufferObject, color) {
    gl.uniform4f(colorUniformLocation, color[0], color[1], color[2], 1.0);
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, edgesBufferObject);
    gl.drawElements(gl.LINES, edges.length, gl.UNSIGNED_SHORT, 0);
};

var vertexShaderText =
    [
        'precision mediump float;',
        'attribute vec3 vertexPosition;',
        'uniform mat4 world;',
        'uniform mat4 view;',
        'uniform mat4 projection;',
        'void main()',
        '{',
        'gl_Position = projection * view * world * vec4(vertexPosition, 1.0);',
        'gl_PointSize = 9.0 * 10.0 / distance( vec4(2.0, 1.0, -4.0, 1.0) * world, vec4(vertexPosition, 1.0) );',
        '}'
    ].join('\n');

var fragmentShaderText =
    [
        'precision mediump float;',
        'uniform vec4 color;',
        'void main()',
        '{',
        'gl_FragColor = color;',
        '}'
    ].join('\n');





