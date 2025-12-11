#version 150

uniform sampler2D DiffuseSampler;
in vec2 texCoord;
out vec4 fragColor;

uniform vec2 InSize;
uniform vec2 OutSize;

uniform float Progress; // 0.0 a 1.0 (progreso del fade)

void main() {
    vec4 original = texture(DiffuseSampler, texCoord);

    // Calcular el nivel de oscurecimiento basado en el progreso
    // 0.0 = normal, 1.0 = completamente negro
    float darkness = Progress;

    // Mezclar entre el color original y negro
    vec3 darkened = mix(original.rgb, vec3(0.0), darkness);

    fragColor = vec4(darkened, original.a);
}