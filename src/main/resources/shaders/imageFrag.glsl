#version 330 core

in float height;

out vec4 out_Color;

void main(void)
{
    out_Color = vec4(height * 255, height * 255, height * 255, 1.0);
}