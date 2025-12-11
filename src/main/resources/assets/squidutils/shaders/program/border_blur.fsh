#version 150

uniform sampler2D DiffuseSampler;
in vec2 texCoord;
out vec4 fragColor;

// Uniforms
uniform vec2 InSize;
uniform vec2 OutSize;

// Parámetros configurables
uniform float BorderWidth;      // Ancho de la viñeta (0.0 a 1.0)
uniform float BlurIntensity;    // Intensidad del color (0.0 a 1.0)
uniform vec3 TintColor;         // Color de la viñeta

void main() {
    // Color original del pixel
    vec4 original = texture(DiffuseSampler, texCoord);

    // Calcular distancia desde el centro (0.0 en centro, 1.0 en esquinas)
    vec2 center = vec2(0.5, 0.5);
    vec2 toCenter = texCoord - center;

    // Distancia radial desde el centro
    float distance = length(toCenter);

    // Normalizar para que las esquinas también se vean afectadas
    // Ajustamos la distancia para que sea más cuadrada en lugar de circular
    float distanceSquared = max(abs(toCenter.x), abs(toCenter.y)) * 2.0;

    // Crear gradiente suave desde el borde hacia el centro
    float vignette = smoothstep(1.0 - BorderWidth, 1.0, distanceSquared);

    // Aplicar intensidad al efecto
    vignette *= BlurIntensity;

    // Mezclar el color original con el tinte de color
    vec3 tintedColor = mix(original. rgb, TintColor, vignette * 0.6);

    // Oscurecer ligeramente los bordes para efecto más dramático
    float darken = 1.0 - (vignette * 0.3);
    tintedColor *= darken;

    fragColor = vec4(tintedColor, original.a);
}