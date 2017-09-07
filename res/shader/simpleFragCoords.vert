void main(void) {
	vec4 vertex = uModelMatrix * gl_Vertex;
	vNormal = mat4transformNormal(gl_Normal.xyz, uNormalMatrix);
	vPosition = vertex.xyz;
	vTexCoord0 = gl_MultiTexCoord0;
	vertex = (uProjectionMatrix * (uViewMatrix * vertex));
	gl_Position = vertex;
}
