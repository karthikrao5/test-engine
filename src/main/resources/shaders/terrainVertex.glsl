#version 330 core

layout (location = 0) in vec3 vertex_position;
in vec2 textCoords;
in vec3 normal;

out vec2 pass_textCoords;
out vec3 surfaceNormal;
out vec3 toLightVector;

out vec3 reflectedLightVector;
out vec3 toCameraVector;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;
uniform vec3 lightPosition;

void main(void)
{
    vec4 worldPosition = transformationMatrix * vec4(vertex_position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    pass_textCoords = textCoords;

    surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
    toLightVector = lightPosition - worldPosition.xyz;

    toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyx - worldPosition.xyz;
}