package com.omegasys.game.model.dto;

public record NewGameDto(String name,
						 double chanceOfWinning,
						 int winningMultiplier,
						 int maxBet,
						 int minBet) {
}
