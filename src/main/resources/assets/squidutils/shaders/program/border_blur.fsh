#version 150

uniform sampler2D DiffuseSampler;
in vec2 texCoord;
out vec4 fragColor;

uniform vec2 InSize;
uniform vec2 OutSize;

uniform float BorderWidth;
uniform float BlurIntensity;  // Intensidad de color
uniform vec3 TintColor;

void main() {
    vec4 original = texture(DiffuseSampler, texCoord);

    // Calcular distancia desde el centro
    vec2 center = vec2(0.5, 0.5);
    vec2 toCenter = texCoord - center;

    // Distancia radial (circular/redondeada) en lugar de cuadrada
    float distanceRadial = length(toCenter) * 2.0;

    // Ajustar aspect ratio para que sea más ovalado (matching la pantalla)
    float aspectRatio = OutSize.x / OutSize.y;
    toCenter. x *= aspectRatio;
    float distanceOval = length(toCenter) * 1.4; // Factor ajustable

    // Combinar distancia radial y ovalada para esquinas más suaves
    float distance = mix(distanceRadial, distanceOval, 0.7);

    // Crear gradiente suave y más amplio
    float vignette = smoothstep(0.5, 1.3, distance);

    // Aplicar intensidad (reducida para menos saturación)
    vignette *= BlurIntensity;

    // Mezclar con el color del tinte (reducido de 0.6 a 0.35 para más transparencia)
    vec3 tintedColor = mix(original.rgb, TintColor, vignette * 0.35);

    // Oscurecer los bordes suavemente (reducido de 0.3 a 0.15)
    float darken = 1.0 - (vignette * 0.15);
    tintedColor *= darken;

    fragColor = vec4(tintedColor, original.a);
}