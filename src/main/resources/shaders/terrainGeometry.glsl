#version 330 core

layout(triangles) in;
layout(triangle_strip, max_vertices=3) out;

in mat4 transformationMatrix[];

out vec3 normal_out;

void main(void)
{
    vec3 a = (gl_in[2].gl_Position - gl_in[0].gl_Position).xyz;
    vec3 b = (gl_in[1].gl_Position - gl_in[0].gl_Position).xyz;

    vec3 normal = normalize(cross(a, b));

    normal_out = (transformationMatrix[2] * vec4(normal, 0.0)).xyz;

    gl_Position = gl_in[0].gl_Position;
    EmitVertex();

    gl_Position = gl_in[1].gl_Position;
    EmitVertex();

    gl_Position = gl_in[2].gl_Position;
    EmitVertex();

    EndPrimitive();
}