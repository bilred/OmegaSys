package com.omegasys.game.service;

import com.omegasys.game.model.BetResult;
import com.omegasys.game.model.BetValue;
import com.omegasys.game.model.Game;
import com.omegasys.game.model.Player;
import com.omegasys.game.model.dto.GameDto;
import com.omegasys.game.model.dto.NewGameDto;
import com.omegasys.game.model.dto.PlayerDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class HelperService {

	public boolean isAbove18Years(LocalDate birthday) {
		LocalDate today = LocalDate.now();
		LocalDate limit = birthday.plusYears(18);

		return today.isAfter(limit) || today.isEqual(limit);
	}

	public Game dtoToEntity(GameDto gameDto, List<PlayerDto> playerDTOs) {
		List<Player> players = playerDTOs.stream()
			.map(playerDto -> new Player(playerDto.id(), playerDto.name(), playerDto.username(),
										 playerDto.birthdate(), playerDto.balance(),
				                         new AtomicInteger(playerDto.numBet()),
										 new AtomicInteger(playerDto.numOfWin())))
			.toList();

		return new Game(gameDto.id(), gameDto.name(),
			gameDto.chanceOfWinning(), gameDto.winningMultiplier(),
			gameDto.maxBet(), gameDto.minBet(),
			players);
	}

	public Game dtoToEntity(NewGameDto newGameDto) {

		return new Game(null, newGameDto.name(),
			newGameDto.chanceOfWinning(), newGameDto.winningMultiplier(),
			newGameDto.maxBet(), newGameDto.minBet(), null);
	}

	public GameDto entityToDto(Game game) {
		return new GameDto(game.getGameId(), game.getGameName(),
			game.getGameChanceOfWinning(), game.getGameWinningMultiplier(),
			game.getGameMaxBet(), game.getGameMinBet());
	}

	public BetResult playYourGame(double chanceOfWinning, BetValue betValue) {
		if(chanceOfWinning < 0 || chanceOfWinning >= 1) {
			throw new IllegalArgumentException(String.format("Param chanceOfWinning equal [%s], must be value btw [0 and 1[ exclusive", chanceOfWinning));
		}

		// kind of Coin Toss Bet
		ThreadLocalRandom rd = ThreadLocalRandom.current();

		// Determine the outcome based on the chance of winning
		double randomNumber = rd.nextDouble(); // generates a random double value between 0.0 (inclusive) and 1.0 (exclusive)
		boolean playerEffectiveChance = randomNumber < chanceOfWinning;

		// Simulate a coin toss (0 for heads, 1 for tails)
		Map<Integer, BetValue> mapCoinTossResult = Map.of(0, BetValue.HEADS, 1, BetValue.TAILS);
		int coinTossResult = rd.nextInt(2);
		boolean isBetValueAndResultEqual = betValue == mapCoinTossResult.get(coinTossResult);

		//System.out.println("Coin Toss Result: " + (coinTossResult == 0 ? BetValue.HEADS.name() : BetValue.TAILS.name()));
		//System.out.println("PlayerEffectiveChance: " + (playerEffectiveChance? "Lucky" : "Bad luck"));
		//System.out.println("Player guessed: " + betValue.name());
		//System.out.println("Final result: " + (playerEffectiveChance && isBetValueAndResultEqual ? BetResult.WIN : BetResult.LOSE));
		return playerEffectiveChance && isBetValueAndResultEqual ? BetResult.WIN : BetResult.LOSE;
	}

}
