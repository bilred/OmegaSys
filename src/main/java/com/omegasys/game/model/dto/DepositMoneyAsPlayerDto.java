package com.omegasys.game.model.dto;

public record DepositMoneyAsPlayerDto(Long playerId, double moneyToDeposit, PlayerAuthDto playerAuthDto) {
}
