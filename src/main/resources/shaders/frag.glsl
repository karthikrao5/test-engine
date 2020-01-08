#version 330 core

in vec2 pass_textCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;


//how close the camera is to the reflected light to see a difference
uniform float shineDamper;
uniform float reflectivity;

void main(void)
{
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);

    vec3 unitToCameraVector = normalize(toCameraVector);
    vec3 lightDirection = -unitLightVector;

    vec3 reflectedLightUnitVector = reflect(lightDirection, unitNormal);
    float specularFactor = dot(reflectedLightUnitVector, unitToCameraVector);
    specularFactor = max(specularFactor, 0.2);

    float dampedFactor = pow(specularFactor, shineDamper);

    vec3 finalSpecularValue = dampedFactor * reflectivity * lightColor;

    float ndot = dot(unitNormal, unitLightVector);
    float brightness = max(ndot, 0.0);
    vec3 diffuse = brightness * lightColor;

    out_Color = vec4(diffuse, 1.0) * texture(textureSampler, pass_textCoords) + vec4(finalSpecularValue, 1.0);
}