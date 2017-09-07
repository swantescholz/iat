
void main (void)  {
	vec4 pfl = calcPerFragmentLighting();
	vec4 fragColor = pfl;
	fragColor = clamp(fragColor, vec4(0,0,0,0), vec4(1,1,1,1));
	vec4 color = vec4(vPosition.xyz,1);
	color = vec4(fragColor.xyz, 1.0);
	//color.xyz = vNormal + vec3(.5,.5,.5);
	gl_FragColor = color;
}

