#version 150
// The game's render output
uniform sampler2D DiffuseSampler;

// The texture coordinate represented as a 2D vector (x,y)
in vec2 texCoord;

// The output color of each pixel represented as a 4D vector (r,g,b,a)
out vec4 fragColor;

// Uniforms
uniform vec2 InSize;
uniform vec2 OutSize;

// Parámetros configurables para el efecto
uniform float BorderWidth;      // Ancho del borde (0.0 a 0.5)
uniform float BlurIntensity;    // Intensidad del blur
uniform vec3 TintColor;         // Color del tinte

void main() {
    // Calcular la distancia desde el centro de la pantalla
    vec2 center = vec2(0.5, 0.5);
    vec2 dist = abs(texCoord - center);

    // Calcular qué tan cerca está el píxel del borde
    // 0.0 = centro, 1.0 = borde
    float edgeDistance = max(dist. x, dist.y) * 2.0;

    // Suavizar la transición del efecto
    float borderFactor = smoothstep(1.0 - BorderWidth, 1.0, edgeDistance);

    // Aplicar blur en los bordes
    vec4 blurredColor = vec4(0.0);

    if (borderFactor > 0.01) {
        // Número de muestras para el blur
        int samples = 9;

        // Calcular el offset basado en el tamaño de la pantalla
        vec2 pixelSize = 1.0 / InSize;
        vec2 offset = pixelSize * BlurIntensity * 2.0;

        // Kernel de blur gaussiano simplificado
        float totalWeight = 0.0;

        for (int x = -samples/2; x <= samples/2; x++) {
            for (int y = -samples/2; y <= samples/2; y++) {
                vec2 sampleCoord = texCoord + vec2(float(x), float(y)) * offset;

                // Peso gaussiano simple
                float weight = 1.0 - (length(vec2(x, y)) / float(samples));
                weight = max(weight, 0.1);

                blurredColor += texture(DiffuseSampler, sampleCoord) * weight;
                totalWeight += weight;
            }
        }

        blurredColor /= totalWeight;
    }

    // Color original sin blur
    vec4 original = texture(DiffuseSampler, texCoord);

    // Mezclar entre el color original y el blur según la distancia al borde
    vec4 result = mix(original, blurredColor, borderFactor);

    // Aplicar tinte de color en los bordes (más sutil)
    result.rgb = mix(result.rgb, result. rgb * TintColor, borderFactor * 0.3);

    // Set the fragColor output as the result
    fragColor = result;
}