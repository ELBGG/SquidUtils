#version 150
// The game's render output
uniform sampler2D DiffuseSampler;
// The texture coordinate represented as a 2D vector (x,y)
in vec2 texCoord;
// The output color of each pixel represented as a 4D vector (r,g,b,a)
out vec4 fragColor;

// Uniform para el tamaño de la textura (se pasa desde el JSON)
uniform vec2 InSize;

// Parámetros configurables (estos se pueden pasar desde el JSON después)
uniform float BorderWidth = 0.25; // Ancho del borde (0.0 a 0.5)
uniform float BlurIntensity = 1.0; // Intensidad del blur
uniform vec3 TintColor = vec3(1.0, 0.0, 1.0); // Color del tinte (por defecto rosa/magenta)

void main() {
    // Calcular la distancia desde el centro de la pantalla
    vec2 center = vec2(0.5, 0.5);
    vec2 dist = abs(texCoord - center);
    
    // Calcular qué tan cerca está el píxel del borde
    // 0.0 = centro, 1.0 = borde
    float edgeDistance = max(dist.x, dist.y) * 2.0;
    
    // Suavizar la transición del efecto
    float borderFactor = smoothstep(1.0 - BorderWidth, 1.0, edgeDistance);
    
    // Aplicar blur en los bordes
    vec4 blurredColor = vec4(0.0);
    
    if (borderFactor > 0.01) {
        // Número de muestras para el blur (más muestras = mejor calidad pero más costo)
        int samples = 9;
        float offset = BlurIntensity / 500.0;
        
        // Kernel de blur gaussiano simplificado
        for (int x = -samples/2; x <= samples/2; x++) {
            for (int y = -samples/2; y <= samples/2; y++) {
                vec2 sampleCoord = texCoord + vec2(float(x), float(y)) * offset;
                blurredColor += texture(DiffuseSampler, sampleCoord);
            }
        }
        
        blurredColor /= float((samples) * (samples));
    }
    
    // Color original sin blur
    vec4 original = texture(DiffuseSampler, texCoord);
    
    // Mezclar entre el color original y el blur según la distancia al borde
    vec4 result = mix(original, blurredColor, borderFactor);
    
    // Aplicar tinte de color en los bordes
    result.rgb = mix(result.rgb, result.rgb * TintColor, borderFactor * 0.5);
    
    // Set the fragColor output as the result
    fragColor = result;
}