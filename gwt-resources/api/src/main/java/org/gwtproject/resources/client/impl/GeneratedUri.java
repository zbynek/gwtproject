package org.gwtproject.resources.client.impl;

import org.gwtproject.safehtml.shared.SafeUri;

public class GeneratedUri implements SafeUri {

	private String uri;

	public GeneratedUri(String uri) {
		this.uri = uri;
	}
	
	@Override
	public String asString() {
		return this.uri;
	}
}
