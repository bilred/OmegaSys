package com.omegasys.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.fail;

public class HelperUnitTest {

	private HelperUnitTest() {	}

	public static String asJsonString(final Object obj) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String getResourceFromPath(String resourcePath) {

		String jsonContent = null;
		try {
			ClassLoader classLoader = HelperUnitTest.class.getClassLoader();
			URL resourceUrl = classLoader.getResource(resourcePath);
			jsonContent = Files.readString(Path.of(resourceUrl.toURI()));
		} catch (IOException | URISyntaxException e) {
			fail();
		} catch (Exception ex) {
			System.err.println("Resource not found: " + resourcePath);
		}

		return jsonContent;
	}

}
