precision mediump float;
uniform float time;
uniform vec2  mouse;
uniform vec2  resolution;

#define PI 1.0

mat2 rotate2d(float angle){
	return mat2(cos(angle), -sin(angle), sin(angle), cos(angle));
}

void main(void){
	vec2 p = (resolution-gl_FragCoord.xy *2.5) / min(resolution.x, resolution.y);
	p = rotate2d((time * 1.0) * PI) * p;
	float t = 0.03 / abs(abs(sin(500.0)) - length(p));
	gl_FragColor = vec4(vec3(t) * vec3(p.x,p.y,0.4), 6.0);
}