package com.omegasys.game.service;

import com.omegasys.game.model.Player;
import com.omegasys.game.model.dto.PlayerAuthDto;
import com.omegasys.game.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

	@InjectMocks
	private PlayerService playerService;

	@Mock
	private GameService gameService;
	@Mock
	private PlayerRepository playerRepository;

	@Mock
	private HelperService helperService;


	@Test
	public void authenticationPlayer_return_true() {
		boolean expected = true;
		String username = "bilred";
		LocalDate birthday = LocalDate.of(1990, 2, 28);
		PlayerAuthDto playerAuthDto = new PlayerAuthDto(username, birthday);
		Player player = new Player(1L, "Billy", username, birthday, 10.0, new AtomicInteger(0), new AtomicInteger(0));

		when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

		boolean result = playerService.authenticationPlayer(1L, playerAuthDto);

        assertTrue(result);
	}

	// TODO
}
