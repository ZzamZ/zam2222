#version 330

// 1:1 port of Vista's crt_turn_on (cameramod/.../shaders/include/crt_effects.glsl).
// Drives the screen content through the same multi-phase collapse animation: vertical scanline
// shrink, horizontal pinch, central dot pulse, with a fade-to-glow tint over the top.
//
// `FadeAnimation` is passed via vertex color alpha (1.0 = fully on, 0.0 = fully off). Vertex
// color RGB stays 1.0 so the screen renders at full brightness; we encode fade into alpha so
// we don't need a custom per-draw uniform plumbing layer.

uniform sampler2D Sampler0;

in vec4 vertexColor;
in vec2 texCoord0;
in vec4 lightMapColor;

out vec4 fragColor;

const float PI = 3.14159265359;

float animate(float t, float start, float duration) {
    return clamp((t - start) / duration, 0.0, 1.0);
}

vec4 crt_turn_on(vec4 inColor, vec2 uv, float t) {
    t = 1.0 - t;

    // Parameters copied verbatim from Vista's crt_effects.glsl
    float fadeStart = 0.0;
    float fadeDuration = 0.20;

    float ryAnimStart = 0.1;
    float ryAnimDuration = 0.4;
    float ryStart = 0.25;
    float ryEnd = 0.0035;

    float rxAnimStart = 0.38;
    float rxAnimDuration = 0.62;
    float rxStart = 0.25;
    float rxEnd = 0.0;

    float dotStart = 0.49;
    float dotDuration = 0.51;
    float dotRadiusMax = 0.13;

    float r_y = mix(ryStart, ryEnd, animate(t, ryAnimStart, ryAnimDuration));
    float r_x = mix(rxStart, rxEnd, animate(t, rxAnimStart, rxAnimDuration));

    vec2 r_in = vec2(r_x, r_y);
    vec2 r_out = r_in * 10.0;

    vec2 norm = uv / r_out;
    float d = length(norm);

    vec2 norm_in = uv / r_in;
    float inside = length(norm_in) < 1.0 ? 1.0 : 0.0;
    float ellipse = max(inside, smoothstep(1.0, r_in.x / r_out.x, d));

    vec4 glow = vec4(vec3(ellipse), ellipse);

    float dotT = animate(t, dotStart, dotDuration);
    if (dotT > 0.0) {
        float s = sin(dotT * PI);
        float radius = dotRadiusMax * s;

        float dist = length(uv);
        float dotMask = smoothstep(radius, 0.0, dist);

        glow.rgb += dotMask;
        glow.a = max(glow.a, dotMask);
    }

    float fade = animate(t, fadeStart, fadeDuration);
    return mix(inColor, glow, fade);
}

void main() {
    vec4 baseColor = texture(Sampler0, texCoord0);

    // Fade value packed into vertex alpha by the BE renderer (TelevisionRenderer).
    float fade = vertexColor.a;

    // Centered UV in [-0.5, 0.5] across the cassette frame. The cassette texture for a
    // multiblock spans the same atlas region per tile; we want the ellipse mask to use
    // each tile's LOCAL UV so the collapse pivots around each tile's own center, matching
    // how Vista renders one screen per TV BE. fract handles atlased animation frames.
    vec2 maskUv = fract(texCoord0);
    maskUv = maskUv - 0.5;

    // Don't multiply baseColor by vertexColor RGB — vertex alpha carries the fade, RGB is
    // always white for the TV. Keep the texture sample at full brightness.
    vec4 result = crt_turn_on(baseColor, maskUv, fade);

    fragColor = result * lightMapColor;
}
