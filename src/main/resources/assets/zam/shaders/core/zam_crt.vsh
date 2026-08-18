#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>
#moj_import <minecraft:sample_lightmap.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV1;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D Sampler2;

out vec4 vertexColor;
out vec2 texCoord0;
out vec4 lightMapColor;

void main() {
    // ModelViewMat / ProjMat come from the DynamicTransforms / Projection UBO blocks declared
    // in the imported includes. Declaring them as plain `uniform mat4` (which I tried first)
    // does NOT bind them in MC 26.1's pipeline system — they have to be UBO-backed.
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexColor = Color;
    texCoord0 = UV0;
    lightMapColor = sample_lightmap(Sampler2, UV2);
}
