package com.omegasys.game.service;

import com.omegasys.game.model.BetResult;
import com.omegasys.game.model.Player;
import com.omegasys.game.model.dto.*;
import com.omegasys.game.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class PlayerService {

	private final GameService gameService;
	private final PlayerRepository playerRepository;

	private final HelperService helperService;


	public PlayerService(GameService gameService, PlayerRepository playerRepository, HelperService helperService) {
		this.gameService = gameService;
		this.playerRepository = playerRepository;
        this.helperService = helperService;
    }

	public List<Player> getAll() {
		return playerRepository.findAll();
	}

	public PlayerDto findById(Long idPlayer) {
		Player player = playerRepository.findById(idPlayer)
			            .orElseThrow(() -> new IllegalArgumentException("Player NOT FOUND with player_id "+ idPlayer));

		return new PlayerDto(player.getPlayerId(), player.getPlayerName(), player.getPlayerUsername(),
			                 player.getPlayerBirthdate(), player.getPlayerBalance(),
							 player.getNumBet(), player.getNumOfWin());
	}

	public List<PlayerSummaryOfBetDto> findAll() {
		return playerRepository.findAll()
			.stream()
			.map(p -> new PlayerSummaryOfBetDto(p.getPlayerName(), p.getPlayerUsername(),
				                                p.getPlayerBalance(), p.getNumBet(), p.getNumOfWin()))
			.collect(Collectors.toList());
	}

	public PlayerDto updateNewDepositForPlayer(PlayerDto playerDto) {
		PlayerDto savedPlayerDto;
		Optional<Player> result = playerRepository.findById(playerDto.id());

		if(result.isPresent()) {
			Player player = result.get();

			player.setPlayerBalance(playerDto.balance());
			playerRepository.save(player);

			savedPlayerDto = new PlayerDto(player.getPlayerId(), playerDto.name(), playerDto.username(),
				                           player.getPlayerBirthdate(), player.getPlayerBalance(),
				                           player.getNumBet(), player.getNumOfWin());
			return savedPlayerDto;
		}

		throw new IllegalArgumentException(String.format("PlayerId [%s] NOT FOUND ", playerDto.id()));
	}

	private PlayerDto saveNewPlayer(NewPlayerDto newPlayerDto, GameDto currentGame) {
		List<PlayerDto> existingPlayersPlusNewOne = gameService.findByIdPlayers(currentGame.id());
		existingPlayersPlusNewOne.add(new PlayerDto(null, newPlayerDto.name(), newPlayerDto.username(), newPlayerDto.birthdate(), 0, 0, 0));

		Player player = new Player();
		player.setPlayerName(newPlayerDto.name());
		player.setPlayerUsername(newPlayerDto.username());
		player.setPlayerBirthdate(newPlayerDto.birthdate());
		player.setPlayerBalance(0d);
		player.setCurrentGame(helperService.dtoToEntity(currentGame, existingPlayersPlusNewOne));

		Player newPlayer = playerRepository.save(player);

		return new PlayerDto(newPlayer.getPlayerId(), newPlayer.getPlayerName(), newPlayer.getPlayerUsername(),
							 newPlayer.getPlayerBirthdate(), newPlayer.getPlayerBalance(),
			                 newPlayer.getNumBet(), newPlayer.getNumOfWin());
	}

	public PlayerDto addNewPlayerAndRegisterToHisGame(Long idGame, NewPlayerDto newPlayerDto) {
		//List<PlayerDto> players = gameService.findByIdPlayers(idGame);
		GameDto gameDto = gameService.findById(idGame);
		PlayerDto savedNewPlayer = this.saveNewPlayer(newPlayerDto, gameDto);

		// ORM - JPA - Hibernate doing that for me
		//Save new Player And update the associations for both Game and Player
		//players.add(savedNewPlayer);
		//gameService.save(gameDto, players);
		return savedNewPlayer;
	}


	public void incrementStatisticAndSave(PlayerDto playerDto, BetResult betResult, double betAmount) {
		Optional<Player> result = playerRepository.findById(playerDto.id());

		if(result.isPresent()) {
			Player player = result.get();
			player.incrementNumBet();

			if(betResult == BetResult.WIN) {
				player.incrementNumOfWin();

				double initialBalance = player.getPlayerBalance();
				player.setPlayerBalance(initialBalance + betAmount);
			}

			if(betResult == BetResult.LOSE) {
				double initialBalance = player.getPlayerBalance();

				if(initialBalance - betAmount < 0) {
					player.setPlayerBalance(0);
				}else {
					player.setPlayerBalance(initialBalance - betAmount);
				}
			}

			playerRepository.save(player);
		}
	}


	public boolean authenticationPlayer(Long playerId, PlayerAuthDto playerAuthDto) {
		Predicate<Player> isAuth = p -> Objects.equals(p.getPlayerUsername(), playerAuthDto.username()) &&
								        Objects.equals(p.getPlayerBirthdate(), playerAuthDto.birthdate());

		Optional<Player> result = playerRepository.findById(playerId);

		if(result.isPresent()) {
			Player player = result.get();

			return isAuth.test(player);
		}

		return false;
	}
}
