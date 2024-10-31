package com.omegasys.game.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.stream.Stream;

public enum BetResult {

	WIN("WIN"),
	LOSE("LOSE");

	private final String result;


	BetResult(String result) {
		this.result = result;
	}

	@JsonValue
	public String getResult() {
		return result;
	}

	@JsonCreator // This is the factory method and must be static
	public static BetResult forValue(@JsonProperty("result") String result) {
		return Stream.of(BetResult.values())
			.filter(e -> Objects.equals(e.name(), result))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("value param "+result+" is not supported"));

	}

}
