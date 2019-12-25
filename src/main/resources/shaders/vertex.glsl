#version 330 core

layout (location = 0) in vec3 vertex_position;
in vec2 textCoords;

out vec2 pass_textCoords;

void main(void)
{
    gl_Position = vec4(vertex_position, 1.0);
    pass_textCoords = textCoords;
}