package com.omegasys.game.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Let's assume that the Game is a "Coin Toss Bet",
 * where the Players predict whether the coin will land on heads or tails.
 */
public enum BetValue {

	HEADS("HEADS"),
	TAILS("TAILS");


	private final String value;

	BetValue(String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}


	@JsonCreator // This is the factory method and must be static
	public static BetValue forValue(@JsonProperty("value") String value) {
		return Stream.of(BetValue.values())
			.filter(e -> Objects.equals(e.name(), value))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("value param "+value+" is not supported"));
	}

}
