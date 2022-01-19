#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in uint aFace;
layout (location = 2) in vec2 aTexCoords;

out vec2 fTexCoords;
flat out uint fFace;
out vec3 fFragPos;

uniform mat4 uProj;
uniform mat4 uView;

void main() {
    gl_Position = uProj * uView * vec4(aPos, 1f);
    fTexCoords = aTexCoords;
    fFace = aFace;
    fFragPos = aPos;
}