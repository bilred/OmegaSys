package com.omegasys.game.rest;

import com.omegasys.game.HelperUnitTest;
import com.omegasys.game.model.BetResult;
import com.omegasys.game.model.BetValue;
import com.omegasys.game.model.dto.*;
import com.omegasys.game.repository.PlayerRepository;
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
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class GameControllerTest {

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
	public void should_RegisterPlayerToGame_return_http201() throws Exception {
		//GIVE
		Long gameId = 1L;
		Long playerId = 1L;
		String url = "/games/1/players";
		String name = "Billy";
		String username = "bilred";
		LocalDate birthday = LocalDate.of(1990, 2, 28);
		NewPlayerDto newPlayerDto = new NewPlayerDto(name, username, birthday);
		PlayerDto playerDto = new PlayerDto(playerId, name, username, birthday, 0, 0, 0);

		//WHEN
		when(helperService.isAbove18Years(birthday)).thenReturn(true);
		when(playerService.addNewPlayerAndRegisterToHisGame(gameId, newPlayerDto)).thenReturn(playerDto);

		//THEN
		mockMvc.perform(post(url)
				.content(asJsonString(newPlayerDto))
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$", is(1)));
	}

	@Test
	public void should_PlaceBet_return_http_200() throws Exception {
		//GIVE
		Long gameId = 1L;
		Long playerId = 1L;
		String name = "Billy";
		String url = "/games/1/players/1/bet";
		String betValue = "HEADS";
		double betAmount = 10.0;
		String username = "bilred";
		LocalDate birthday = LocalDate.of(1990, 2, 28);
		PlayerAuthDto playerAuthDto = new PlayerAuthDto(username, birthday);
		GameDto gameDto = new GameDto(gameId, "Game1", 4, 0, 0, 0);
		PlayerDto playerDto = new PlayerDto(playerId, name, username, birthday, 0, 0, 0);

		//WHEN
		when(playerService.authenticationPlayer(playerId, playerAuthDto)).thenReturn(true);
		when(gameService.findById(gameId)).thenReturn(gameDto);
		when(gameService.findByIdPlayers(gameId)).thenReturn(List.of(playerDto));
		when(betService.placeBet(gameDto, playerDto, BetValue.HEADS, betAmount)).thenReturn(BetResult.WIN);

		//THEN
		mockMvc.perform(post(url)
				.queryParam("betValue", betValue)
				.queryParam("betAmount", String.valueOf(betAmount))
				.content(asJsonString(playerAuthDto))
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is("WIN")));
	}


	@Test
	public void should_CreateGame_return_http_201() throws Exception {
		//GIVE
		String url = "/games/";
		String name = "Game 1";
		NewGameDto newGame = new NewGameDto(name, 0.5, 2, 5, 0);
		String expected = HelperUnitTest.getResourceFromPath("data/games/POST/201/response.json");
		GameDto gameDto = new GameDto(1L, name, 0.5, 2, 5, 0);

		//WHEN
		when(gameService.save(newGame)).thenReturn(gameDto);

		//THEN
		mockMvc.perform(post(url)
				.content(asJsonString(newGame))
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(content().json(expected));
	}

	@Test
	public void should_GetAllGames_return_http_200() throws Exception {
		//GIVE
		String url = "/games/";
		GameDto gameDto1 = new GameDto(1L, "Game 1", 0.5, 2, 5, 0);
		GameDto gameDto2 = new GameDto(2L, "Game 2", 0.3, 4, 7, 0);
		String expected = HelperUnitTest.getResourceFromPath("data/games/GET/200/response.json");

		//WHEN
		when(gameService.getAll()).thenReturn(List.of(gameDto1, gameDto2));

		//THEN
		mockMvc.perform(get(url)
				.accept(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(expected));
	}

}
