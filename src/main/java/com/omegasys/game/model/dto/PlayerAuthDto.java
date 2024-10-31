package com.omegasys.game.model.dto;

import java.time.LocalDate;

public record PlayerAuthDto(String username, LocalDate birthdate) {
}
