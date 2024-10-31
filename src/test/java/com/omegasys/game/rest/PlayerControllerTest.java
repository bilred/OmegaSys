package com.omegasys.game.rest;

import com.omegasys.game.HelperUnitTest;
import com.omegasys.game.model.dto.DepositMoneyAsPlayerDto;
import com.omegasys.game.model.dto.PlayerAuthDto;
import com.omegasys.game.model.dto.PlayerDto;
import com.omegasys.game.model.dto.PlayerSummaryOfBetDto;
import com.omegasys.game.service.BetService;
import com.omegasys.game.service.GameService;
import com.omegasys.game.service.HelperService;
import com.omegasys.game.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static com.omegasys.game.HelperUnitTest.asJsonString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class PlayerControllerTest {

	@Autowired
	protected MockMvc mockMvc;

	@MockBean
	protected PlayerService playerService;

	@MockBean
	protected GameService gameService;

	@MockBean
	protected HelperService helperService;

	@MockBean
	protected BetService betService;

	@BeforeEach
	public void setUp() {
		Mockito.reset(gameService, playerService, helperService, betService);
	}

	@Test
	public void should_getBalance_return_http200() throws Exception {
		//GIVE
		Long playerId = 1L;
		String url = "/players/1";
		String name = "Billy";
		String username = "bilred";
		LocalDate birthday = LocalDate.of(1990, 2, 28);
		PlayerDto playerDto = new PlayerDto(playerId, name, username, birthday, 11.5, 0, 0);
		PlayerAuthDto playerAuthDto = new PlayerAuthDto(username, birthday);

		//WHEN
		when(playerService.authenticationPlayer(playerId, playerAuthDto)).thenReturn(true);
		when(playerService.findById(playerId)).thenReturn(playerDto);

		//THEN
		mockMvc.perform(get(url)
				.content(asJsonString(playerAuthDto))
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is(11.5)));
	}

	@Test
	public void should_updatePlayerBalance_return_http200() throws Exception {
		//GIVE
		Long playerId = 1L;
		String url = "/players/1";
		String name = "Billy";
		String username = "bilred";
		double initialBalance = 11.5;
		double moneyToDeposit = 10.0;
		LocalDate birthday = LocalDate.of(1990, 2, 28);
		PlayerDto playerDto = new PlayerDto(playerId, name, username, birthday, initialBalance, 0, 0);
		PlayerDto expectedNewBalance = new PlayerDto(playerId, name, username, birthday, (initialBalance + moneyToDeposit), 0, 0);

		PlayerAuthDto playerAuthDto = new PlayerAuthDto(username, birthday);
		DepositMoneyAsPlayerDto depositMoneyAsPlayerDto = new DepositMoneyAsPlayerDto(playerId, moneyToDeposit, playerAuthDto);
		String expected = HelperUnitTest.getResourceFromPath("data/players/PUT/200/response.json");

		//WHEN
		when(playerService.authenticationPlayer(playerId, playerAuthDto)).thenReturn(true);
		when(playerService.findById(playerId)).thenReturn(playerDto);
		when(playerService.updateNewDepositForPlayer(expectedNewBalance)).thenReturn(expectedNewBalance);

		//THEN
		mockMvc.perform(put(url)
				.content(asJsonString(depositMoneyAsPlayerDto))
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(expected));
	}

	@Test
	public void should_loginPlayer_return_http200() throws Exception {
		//GIVE
		Long playerId = 1L;
		String url = "/players/1";
		String username = "bilred";
		LocalDate birthday = LocalDate.of(1990, 2, 28);
		PlayerAuthDto playerAuthDto = new PlayerAuthDto(username, birthday);

		//WHEN
		when(playerService.authenticationPlayer(playerId, playerAuthDto)).thenReturn(true);

		//THEN
		mockMvc.perform(post(url)
				.content(asJsonString(playerAuthDto))
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is(true)));
	}

	@Test
	public void should_getPlayersSummaryOfBets_return_http200() throws Exception {
		//GIVE
		Long playerId = 1L;
		String url = "/players/summaries";
		String username = "bilred";
		LocalDate birthday = LocalDate.of(1990, 2, 28);
		String expected = HelperUnitTest.getResourceFromPath("data/players/summaries/GET/200/response.json");
		PlayerSummaryOfBetDto playerDto1 = new PlayerSummaryOfBetDto("Billy1", "bilred1", 100.0, 6, 2);
		PlayerSummaryOfBetDto playerDto2 = new PlayerSummaryOfBetDto("Billy2", "bilred2", 11.5, 2, 1);

		//WHEN
		when(playerService.findAll()).thenReturn(List.of(playerDto1, playerDto2));

		//THEN
		mockMvc.perform(get(url)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(expected));
	}

}
