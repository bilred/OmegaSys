package com.omegasys.game.repository;


import com.omegasys.game.model.Game;
import com.omegasys.game.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface GameRepository extends JpaRepository<Game, Long> {

	@Query(value = "SELECT u.players FROM Game u WHERE u.gameId = ?1")
	List<Player> findPlayersById(Long gameId);

}
