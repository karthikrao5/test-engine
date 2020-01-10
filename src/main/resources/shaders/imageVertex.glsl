#version 330 core

layout (location = 0) in vec3 vertex_position;
in vec2 textCoords;

out vec2 pass_textCoords;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

void main(void)
{
    vec4 worldPosition = transformationMatrix * vec4(vertex_position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    pass_textCoords = textCoords;

    toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyx - worldPosition.xyz;
}