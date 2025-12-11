#version 150

uniform sampler2D DiffuseSampler;
in vec2 texCoord;
out vec4 fragColor;

uniform vec2 InSize;
uniform vec2 OutSize;

uniform float BorderWidth;
uniform float BlurIntensity;
uniform vec3 TintColor;

void main() {
    vec4 original = texture(DiffuseSampler, texCoord);

    vec2 center = vec2(0.5, 0.5);
    vec2 toCenter = texCoord - center;

    // Ajustar por aspect ratio
    float aspectRatio = OutSize.x / OutSize.y;
    vec2 adjustedCoord = toCenter;
    adjustedCoord.x *= aspectRatio;

    // Distancia radial
    float distance = length(adjustedCoord);

    // Expandir el área del efecto (más grande = más hacia el centro)
    // Ajustamos el rango de smoothstep para que empiece más adentro
    float vignette = smoothstep(0.55, 1.35, distance); // Antes era (0.4, 1.2)
    vignette = pow(vignette, 1.2); // Curva más suave

    // Aplicar intensidad
    vignette *= BlurIntensity * 0.8;

    // Tinte de color transparente
    vec3 tintedColor = mix(original.rgb, TintColor, vignette * 0.3);

    // Oscurecimiento sutil
    float darken = 1.0 - (vignette * 0.12);
    tintedColor *= darken;

    fragColor = vec4(tintedColor, original.a);
}