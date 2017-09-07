package de.sscholz.iat.util;

import de.sscholz.iat.util.log.Asserter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class ResourceManager<R> implements Asserter {

	protected final Map<String, R> resources = new HashMap<>();
	protected final File defaultLocation;
	protected final String defaultFilenameEnding;

	protected ResourceManager(File defaultLocation, String defaultFilenameEnding) {
		this.defaultLocation = defaultLocation;
		this.defaultFilenameEnding = defaultFilenameEnding;
	}

	public File getFile(String simplePath) {
		return new File(defaultLocation, simplePath + defaultFilenameEnding);
	}

	public String readFile(String simplePath) {
		return FileUtil.readFile(getFile(simplePath));
	}

	protected void addResource(String resourceId, R resource) {
		if (hasResource(resourceId)) {
			log.warn("Resource '" + resourceId + "' already added!");
		}
		resources.put(resourceId, resource);
	}

	protected void removeResource(String resourceId) {
		assertTrue("Resource '" + resourceId + "' to be deleted does not exist", hasResource(resourceId));
		resources.remove(resourceId);
	}

	protected boolean hasResource(String resourceId) {
		return resources.containsKey(resourceId);
	}

	protected R getResource(String resourceId) {
		assertTrue("Resource '" + resourceId + "' not found!", hasResource(resourceId));
		return resources.get(resourceId);
	}

}
