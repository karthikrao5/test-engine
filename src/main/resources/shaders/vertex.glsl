#version 330 core

layout (location = 0) in vec3 vertex_position;
out vec3 color;

void main(void)
{
    gl_Position = vec4(vertex_position, 1.0);
    color = vec3(vertex_position.x + 0.5, 1.0, vertex_position.y + 0.5);
}