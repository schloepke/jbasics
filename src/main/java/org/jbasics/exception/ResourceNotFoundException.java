package org.jbasics.exception;

public class ResourceNotFoundException extends RuntimeException {
	private final String resourceName;

	public ResourceNotFoundException(String resourceName) {
		super("Resource not found; " + resourceName);
		this.resourceName = resourceName;
	}

	public String getResourceName() {
		return this.resourceName;
	}

}
