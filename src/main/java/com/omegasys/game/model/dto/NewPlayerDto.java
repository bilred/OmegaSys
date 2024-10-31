package com.omegasys.game.model.dto;

import java.time.LocalDate;

public record NewPlayerDto(String name, String username, LocalDate birthdate) {
}
