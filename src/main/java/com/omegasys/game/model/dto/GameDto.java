package com.omegasys.game.model.dto;

public record GameDto(long id,
					  String name,
					  double chanceOfWinning,
					  int winningMultiplier,
					  int maxBet,
					  int minBet) {
}
