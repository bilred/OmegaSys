package com.omegasys.game.service;

import com.omegasys.game.model.Game;
import com.omegasys.game.model.Player;
import com.omegasys.game.model.dto.GameDto;
import com.omegasys.game.model.dto.NewGameDto;
import com.omegasys.game.model.dto.PlayerDto;
import com.omegasys.game.repository.GameRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameService {
	private static final List<Game> initialGames = new ArrayList<>();

	static {
		Game game1 = new Game(null, "Game 1", 0.5, 2, 10, 0, null);
		Game game2 = new Game(null, "Game 2", 0.25, 4, 5, 5, null);
		initialGames.addAll(List.of(game1, game2));
	}

	@PostConstruct
	public void initialLoad() {
		gameRepository.saveAll(initialGames);
	}

    private final GameRepository gameRepository;
	private final HelperService helperService;


	public GameService(GameRepository gameRepository, HelperService helperService) {
        this.gameRepository = gameRepository;
		this.helperService = helperService;
	}

	public List<GameDto> getAll() {
		List<Game> games = gameRepository.findAll();

		return games.stream()
					.map(game -> new GameDto(game.getGameId(), game.getGameName(),
											 game.getGameChanceOfWinning(), game.getGameWinningMultiplier(),
											 game.getGameMaxBet(), game.getGameMinBet()))
					.collect(Collectors.toList());
	}

	public GameDto findById(Long id) {
		Optional<Game> result = gameRepository.findById(id);

		if(result.isPresent()) {
			Game game = result.get();

			return new GameDto(game.getGameId(), game.getGameName(),
				game.getGameChanceOfWinning(), game.getGameWinningMultiplier(),
				game.getGameMaxBet(), game.getGameMinBet());
		}

		throw new IllegalArgumentException("Game was not found with playerId "+id);
	}

	public List<PlayerDto> findByIdPlayers(Long id) {
		List<Player> players = gameRepository.findPlayersById(id);

		return players.stream()
			.map(player -> new PlayerDto(player.getPlayerId(), player.getPlayerName(), player.getPlayerUsername(),
										 player.getPlayerBirthdate(), player.getPlayerBalance(),
									     player.getNumBet(), player.getNumOfWin()))
			.collect(Collectors.toList());
	}

	public GameDto save(NewGameDto game) {
		Game newSavedGame = gameRepository.save(new Game(null, game.name(), game.chanceOfWinning(),
			                                              game.winningMultiplier(), game.maxBet(), game.minBet(),
			                                             null));

		return new GameDto(newSavedGame.getGameId(), newSavedGame.getGameName(),
						   newSavedGame.getGameChanceOfWinning(), newSavedGame.getGameWinningMultiplier(),
						   newSavedGame.getGameMaxBet(), newSavedGame.getGameMinBet());
	}

	public List<GameDto> saveAll(List<NewGameDto> newGameDTOs) {
		List<Game> newGames = newGameDTOs.stream()
			.map(helperService::dtoToEntity)
			.collect(Collectors.toList());

		List<Game> games = gameRepository.saveAll(newGames);

		return games.stream()
					.map(helperService::entityToDto)
					.collect(Collectors.toList());
	}

}
