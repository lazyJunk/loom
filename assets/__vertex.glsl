#version 330 core
layout (location = 0) in uint aData;
layout (location = 1) in uint aFace;
layout (location = 2) in vec2 aTexCoords;

out vec2 fTexCoords;
flat out uint fFace;
out vec3 fFragPosition;

uniform mat4 uProjView;
uniform ivec2 uChunkPos;

#define POSITION_INDEX_BITMASK uint(0x1FFFF)
#define BASE_17_WIDTH uint(17)
#define BASE_17_DEPTH uint(17)
#define BASE_17_HEIGHT uint(289)

void extractPosition(in uint data, out vec3 position)
{
    uint positionIndex = data & POSITION_INDEX_BITMASK;
    uint z = positionIndex % BASE_17_WIDTH;
    uint x = (positionIndex % BASE_17_HEIGHT) / BASE_17_DEPTH;
    uint y = (positionIndex - (x * BASE_17_DEPTH) - z) / BASE_17_HEIGHT;
    position = vec3(float(x), float(y), float(z));
}

void main()
{
    vec3 position;
    extractPosition(aData, position);
    fFace = aFace;

    // Convert from local Chunk Coords to world Coords
    position.x += float(uChunkPos.x) * 16.0;
    position.z += float(uChunkPos.y) * 16.0;

    fTexCoords = aTexCoords;
    fFragPosition = position;
    gl_Position = uProjView * vec4(position, 1.0);
}