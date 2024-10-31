package com.omegasys.game.service;

import com.omegasys.game.repository.GameRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

	@Mock
	private PlayerService playerService;

	@InjectMocks
	private GameService gameService;
	@Mock
	private GameRepository gameRepository;

	@Mock
	private HelperService helperService;




	// TODO
}
