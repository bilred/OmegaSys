package com.omegasys.game.model.dto;

import java.time.LocalDate;

public record PlayerDto(Long id, String name, String username, LocalDate birthdate, double balance, int numBet, int numOfWin) {
}
