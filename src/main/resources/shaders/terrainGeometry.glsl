#version 330 core

layout(triangles) in;
layout(triangle_strip, max_vertices=3) out;

uniform mat4 transformationMatrix;

in vec2 pass_textCoords[];
in vec3 toLightVector[];
in vec3 toCameraVector[];

out vec3 normal_out;
out vec2 pass_textCoordsFS;
out vec3 toCameraVectorFS;
out vec3 toLightVectorFS;

void main(void)
{
    vec3 a = (gl_in[2].gl_Position - gl_in[0].gl_Position).xyz;
    vec3 b = (gl_in[1].gl_Position - gl_in[0].gl_Position).xyz;

    vec3 normal = normalize(cross(a, b));

    normal_out = (transformationMatrix * vec4(normal, 0.0)).xyz;

    for (int i = 0; i < gl_in.length(); i++) {
        gl_Position = gl_in[i].gl_Position;

        pass_textCoordsFS = pass_textCoords[i];
        toLightVectorFS = toLightVector[i];
        toCameraVectorFS = toCameraVector[i];

        EmitVertex();
    }
    EndPrimitive();
}