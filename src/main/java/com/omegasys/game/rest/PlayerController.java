package com.omegasys.game.rest;

import com.omegasys.game.model.dto.DepositMoneyAsPlayerDto;
import com.omegasys.game.model.dto.PlayerAuthDto;
import com.omegasys.game.model.dto.PlayerDto;
import com.omegasys.game.model.dto.PlayerSummaryOfBetDto;
import com.omegasys.game.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

@Controller
@RestController
@RequestMapping("/players")
public class PlayerController implements PlayerInterface {

	private final PlayerService playerService;


	public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }


	@GetMapping(value = "/{idPlayer}")
	@ResponseStatus(HttpStatus.OK)
	@Override
	public double getBalance(@PathVariable Long idPlayer, @RequestBody PlayerAuthDto playerAuthDto) {
		if(!playerService.authenticationPlayer(idPlayer, playerAuthDto)) {
			throw new IllegalArgumentException(format("Authentication fail, username [%s] or birthdate [%s] or idPlayer [%s] Not Match",
				playerAuthDto.username(), playerAuthDto.birthdate(), idPlayer));
		}

		PlayerDto playerDto = playerService.findById(idPlayer);
		return playerDto.balance();
	}

	@PutMapping(value = "/{idPlayer}")
	@ResponseStatus(HttpStatus.OK)
	@Override
	public PlayerDto updatePlayerBalance(@PathVariable Long idPlayer, @RequestBody DepositMoneyAsPlayerDto depositMoneyAsPlayerDto) {
		Objects.requireNonNull(depositMoneyAsPlayerDto.playerAuthDto(), "PlayerAuthDto must not be null");
		PlayerAuthDto playerAuthDto = depositMoneyAsPlayerDto.playerAuthDto();

		if(!playerService.authenticationPlayer(idPlayer, playerAuthDto)) {
			throw new IllegalArgumentException(format("Authentication fail, username [%s] or birthdate [%s] or idPlayer [%s] Not Match",
				playerAuthDto.username(), playerAuthDto.birthdate(), idPlayer));
		}

		// Used only for Deposit money as a player
		if(!Objects.equals(idPlayer, depositMoneyAsPlayerDto.playerId())) {
			throw new IllegalArgumentException("Id form @PathVariable and form @RequestBody must be the same");
		}

		PlayerDto playerDto = playerService.findById(depositMoneyAsPlayerDto.playerId());
		double currentBalance = playerDto.balance();
		double newBalance = currentBalance + depositMoneyAsPlayerDto.moneyToDeposit();


		PlayerDto playerDtoWithNewBalance = new PlayerDto(idPlayer, playerDto.name(), playerDto.username(),
			                                   playerDto.birthdate(), newBalance,
			                                   playerDto.numBet(),
											   playerDto.numOfWin());

		// in order to update existing player
		playerService.updateNewDepositForPlayer(playerDtoWithNewBalance);
		return playerDtoWithNewBalance;
	}

	@PostMapping(value = "/{idPlayer}")
	@ResponseStatus(HttpStatus.OK)
	@Override
	public boolean loginPlayer(@PathVariable Long idPlayer, @RequestBody PlayerAuthDto playerAuthDto){
		return playerService.authenticationPlayer(idPlayer, playerAuthDto);
	}


	@GetMapping(value = "/summaries")
	@ResponseStatus(HttpStatus.OK)
	@Override
	public List<PlayerSummaryOfBetDto> getPlayersSummaryOfBets() {
		return playerService.findAll();
	}
}
