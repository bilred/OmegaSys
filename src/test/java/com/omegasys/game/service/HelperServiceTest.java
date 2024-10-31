package com.omegasys.game.service;

import com.omegasys.game.model.BetResult;
import com.omegasys.game.model.BetValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class HelperServiceTest {

	@InjectMocks
	private HelperService helperService;

	@Test
	public void isAbove18Years_return_true() {
		boolean result = helperService.isAbove18Years(LocalDate.of(1988, 2, 27));

		assertTrue(result);
	}

	@Test
	public void isAbove18Years_return_false() {
		boolean result = helperService.isAbove18Years(LocalDate.of(2000, 1, 1));

		assertTrue(result);
	}


	@Test
	public void playYourGame1_loss_regardless_good_or_bad_guess() {
		BetResult result = helperService.playYourGame(0, BetValue.HEADS);
		assertEquals(BetResult.LOSE, result);
	}

	@Test
	public void playYourGame2_loss_regardless_good_or_bad_guess() {
		BetResult result = helperService.playYourGame(0, BetValue.TAILS);
		assertEquals(BetResult.LOSE, result);
	}

	@Test
	public void playYourGame_with_invalid_param() {
		String expectedMessage = "Param chanceOfWinning equal [1.0], must be value btw [0 and 1[ exclusive";
		
		IllegalArgumentException result =
			assertThrows(IllegalArgumentException.class,
					     () -> helperService.playYourGame(1.0, BetValue.TAILS));

		assertEquals(expectedMessage, result.getMessage());
	}

}
