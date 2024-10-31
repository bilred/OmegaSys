package com.omegasys.game.rest;

import com.omegasys.game.model.dto.DepositMoneyAsPlayerDto;
import com.omegasys.game.model.dto.PlayerAuthDto;
import com.omegasys.game.model.dto.PlayerDto;
import com.omegasys.game.model.dto.PlayerSummaryOfBetDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

public interface PlayerInterface {

	@Operation(summary = "Get currant Balance by Player")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Value of Balance",
			content = { @Content(mediaType = "application/json",
				schema = @Schema(implementation = double.class)) }),
	})
	double getBalance(Long idPlayer, PlayerAuthDto playerAuthDto);

	@Operation(summary = "Deposit money for player")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "PlayerDto",
			content = { @Content(mediaType = "application/json",
				schema = @Schema(implementation = double.class)) }),
	})
	PlayerDto updatePlayerBalance(Long idPlayer, DepositMoneyAsPlayerDto depositMoneyAsPlayerDto);

	@Operation(summary = "Authentication of Player")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Boolean",
			content = { @Content(mediaType = "application/json",
				schema = @Schema(implementation = double.class)) }),
	})
	boolean loginPlayer(Long idPlayer, PlayerAuthDto playerAuthDto);

	@Operation(summary = "Returns the summary of bets for players")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "List PlayerSummaryOfBetDto",
			content = { @Content(mediaType = "application/json",
				schema = @Schema(implementation = List.class)) }),
	})
	List<PlayerSummaryOfBetDto> getPlayersSummaryOfBets();

}
