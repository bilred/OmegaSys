package com.omegasys.game.rest;

import com.omegasys.game.model.BetResult;
import com.omegasys.game.model.BetValue;
import com.omegasys.game.model.dto.*;
import com.omegasys.game.service.BetService;
import com.omegasys.game.service.GameService;
import com.omegasys.game.service.HelperService;
import com.omegasys.game.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;

@Controller
@RestController
@RequestMapping("/games")
public class GameController implements GameInterface {

	Logger logger = LoggerFactory.getLogger(GameController.class);
    private final GameService gameService;
    private final PlayerService playerService;

	private final HelperService helperService;

	private final BetService betService;

    public GameController(GameService gameService, PlayerService playerService, HelperService helperService, BetService betService) {
        this.gameService = gameService;
		this.playerService = playerService;
        this.helperService = helperService;
        this.betService = betService;
	}

    @GetMapping(value = "/")
    @ResponseStatus(HttpStatus.OK)
	@Override
    public List<GameDto> listAllGames() {
        return gameService.getAll();
    }

    @PostMapping(value = "/")
    @ResponseStatus(HttpStatus.CREATED)
	@Override
	public GameDto CreateGame(@RequestBody NewGameDto newGame) {
		GameDto newOne = gameService.save(newGame);

		logger.info("New Game was created, Header.Location [/games/{}]", newOne.id());
        return newOne;
    }

    @PostMapping(value = "/batch")
    @ResponseStatus(HttpStatus.OK)
	@Override
	public List<GameDto> createBatchOfGames(@RequestBody List<NewGameDto> newGames) {
		//TODO add facade to extract JSON or XML Data and convert it to DTO
        return gameService.saveAll(newGames);
    }

    @PostMapping(value = "/{idGame}/players")
    @ResponseStatus(HttpStatus.CREATED)
	@Override
	public Long registerPlayerToGame(@PathVariable Long idGame, @RequestBody NewPlayerDto newPlayerDto){
		if(!helperService.isAbove18Years(newPlayerDto.birthdate())) {
			throw new IllegalArgumentException(format("The user [%s] needs to be 18 years old at least", newPlayerDto.username()));
		}

		PlayerDto playerDto = playerService.addNewPlayerAndRegisterToHisGame(idGame, newPlayerDto);

		logger.info("New Player was created and registered in Game, Header.Location [/games/{}/players/{}]", idGame, playerDto.id());
		return playerDto.id();
    }

	@PostMapping(value = "/{idGame}/players/{idPlayer}/bet")
	@ResponseStatus(HttpStatus.OK)
	@Override
	public BetResult placeBet(@PathVariable Long idGame, @PathVariable Long idPlayer,
							  @RequestParam String betValue, @RequestParam double betAmount,
							  @RequestBody PlayerAuthDto playerAuthDto) {

		if(!playerService.authenticationPlayer(idPlayer, playerAuthDto)) {
			throw new IllegalArgumentException(format("Authentication fail, username [%s] or birthdate [%s] or idPlayer [%s] Not Match",
															  playerAuthDto.username(), playerAuthDto.birthdate(), idPlayer));
		}

		GameDto gameDto = gameService.findById(idGame);
		Optional<PlayerDto> result = gameService.findByIdPlayers(gameDto.id())
			.stream()
			.filter(playerDto -> Objects.equals(playerDto.id(), idPlayer))
			.findFirst();

		if(result.isPresent()) {
			return betService.placeBet(gameDto, result.get(), BetValue.forValue(betValue), betAmount);
		}

		throw new IllegalArgumentException(format("Player not found id_player [%s], into game_id [%s]", idPlayer, idGame));
	}

}
