package com.omegasys.game.rest;

import com.omegasys.game.model.BetResult;
import com.omegasys.game.model.dto.GameDto;
import com.omegasys.game.model.dto.NewGameDto;
import com.omegasys.game.model.dto.NewPlayerDto;
import com.omegasys.game.model.dto.PlayerAuthDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

public interface GameInterface {

	@Operation(summary = "List All Games")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "List GameDto",
			content = { @Content(mediaType = "application/json",
				schema = @Schema(implementation = List.class)) }),
	})
	List<GameDto> listAllGames();

	@Operation(summary = "Create Game")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Create GameDto",
			content = { @Content(mediaType = "application/json",
				schema = @Schema(implementation = GameDto.class)) }),
	})
	GameDto CreateGame(NewGameDto newGame);

	@Operation(summary = "Batch Create Games")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "List GameDto",
			content = { @Content(mediaType = "application/json",
				schema = @Schema(implementation = List.class)) }),
	})
	List<GameDto> createBatchOfGames(List<NewGameDto> newGames);

	@Operation(summary = "Register new Player in his Game")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "playerId game",
			content = { @Content(mediaType = "application/json",
				schema = @Schema(implementation = Long.class)) }),
	})
	Long registerPlayerToGame(Long idGame, NewPlayerDto newPlayerDto);

	@Operation(summary = "Player Pace Bet in his Game")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Bet Result",
			content = { @Content(mediaType = "application/json",
				schema = @Schema(implementation = BetResult.class)) }),
	})
	BetResult placeBet(Long idGame, Long idPlayer, String betValue, double betAmount, PlayerAuthDto playerAuthDto);

}
