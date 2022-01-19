#version 330 core

out vec4 FragColor;

in vec2 fTexCoords;
flat in uint fFace;
in vec3 fFragPos;

uniform sampler2D uTexture;
uniform vec3 uSunPos;

vec3 lightColor = vec3(1, 1, 1);

void faceToNormal(in uint face, out vec3 normal)
{
    switch(face)
    {
        case uint(0):
        normal = vec3(0, 0, -1);
        break;
        case uint(1):
        normal = vec3(0, 0, 1);
        break;
        case uint(2):
        normal = vec3(0, -1, 0);
        break;
        case uint(3):
        normal = vec3(0, 1, 0);
        break;
        case uint(4):
        normal = vec3(-1, 0, 0);
        break;
        case uint(5):
        normal = vec3(1, 0, 0);
        break;
    }
}

void main() {
    //vec3 unitSunPos = normalize(uSunPos);
    //vec3 unitFragPos = normalize(fFragPos);

    float ambientStrength = .4f;
    vec3 ambient = ambientStrength * lightColor;

    vec3 lightDir = normalize(uSunPos - fFragPos);

    vec3 normal;
    faceToNormal(fFace, normal);

    float diff  = max(dot(normal, lightDir), .1f);
    vec3 diffuse = diff * lightColor;

    vec3 objectColor = texture(uTexture, fTexCoords).rgb;
    vec3 res = (diffuse + ambient) * objectColor;
    FragColor = vec4(res, 1);
    //FragColor = texture(uTexture, fTexCoords);
}