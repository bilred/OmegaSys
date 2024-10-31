package com.omegasys.game.service;

import com.omegasys.game.model.BetResult;
import com.omegasys.game.model.BetValue;
import com.omegasys.game.model.dto.GameDto;
import com.omegasys.game.model.dto.PlayerDto;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class BetService {

	private final PlayerService playerService;
	private final HelperService helperService;

	public BetService(PlayerService playerService, HelperService helperService) {
		this.playerService = playerService;
		this.helperService = helperService;
	}


	public BetResult placeBet(GameDto gameDto, PlayerDto playerDto, BetValue betValue, double betAmount) {
		BetResult winOrLossResult;
		double chanceOfWinning = gameDto.chanceOfWinning();
		int maxBet = gameDto.maxBet();
		int minBet = gameDto.minBet();
		int winningMultiplier = gameDto.winningMultiplier();
		int currentNumBet = playerDto.numBet();
		Predicate<Integer> isValidBet = p -> (/** p >= minBet && **/ p <= maxBet);

		if(isValidBet.test(currentNumBet)) {
			// Go - Player Play your game
			winOrLossResult = helperService.playYourGame(chanceOfWinning, betValue);

			playerService.incrementStatisticAndSave(playerDto, winOrLossResult, betAmount);
			return winOrLossResult;
		}


		throw new IllegalArgumentException(String.format("CurrentNumBet is [%s] out of his range [min(%s), max(%s)]", currentNumBet, minBet, maxBet));
	}


}
