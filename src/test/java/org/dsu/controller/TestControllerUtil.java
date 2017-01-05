package org.dsu.controller;

import java.nio.charset.Charset;

import org.springframework.http.MediaType;

public final class TestControllerUtil {
	
	private TestControllerUtil() {

	}

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
}
