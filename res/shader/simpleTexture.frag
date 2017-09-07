void main (void)  {
	vec4 color = texture(uTexture[0], vTexCoord0);
	if (color.a == 0.0) {
		discard;
		return;
	}
	gl_FragColor = color;
}

