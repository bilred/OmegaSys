package com.omegasys.game.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "games")
public class Game {

    public Game() { }

    public Game(Long gameId, String gameName, double gameChanceOfWinning, int gameWinningMultiplier, int gameMaxBet, int gameMinBet, List<Player> players) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameChanceOfWinning = gameChanceOfWinning;
        this.gameWinningMultiplier = gameWinningMultiplier;
        this.gameMaxBet = gameMaxBet;
        this.gameMinBet = gameMinBet;
        this.players = players;
    }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "game_id")
    private Long gameId;

	@Column(name = "game_name")
    private String gameName;

	@Column(name = "game_chance_of_winning")
    private double gameChanceOfWinning;

	@Column(name = "game_winning_multiplier")
    private int gameWinningMultiplier;

	@Column(name = "game_max_bet")
    private int gameMaxBet;

	@Column(name = "game_min_bet")
    private int gameMinBet;


	@OneToMany(mappedBy = "currentGame", fetch = FetchType.LAZY)
	private List<Player> players;


    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public double getGameChanceOfWinning() {
        return gameChanceOfWinning;
    }

    public void setGameChanceOfWinning(double gameChanceOfWinning) {
        this.gameChanceOfWinning = gameChanceOfWinning;
    }

    public int getGameWinningMultiplier() {
        return gameWinningMultiplier;
    }

    public void setGameWinningMultiplier(int gameWinningMultiplier) {
        this.gameWinningMultiplier = gameWinningMultiplier;
    }

    public int getGameMaxBet() {
        return gameMaxBet;
    }

    public void setGameMaxBet(int gameMaxBet) {
        this.gameMaxBet = gameMaxBet;
    }

    public int getGameMinBet() {
        return gameMinBet;
    }

    public void setGameMinBet(int gameMinBet) {
        this.gameMinBet = gameMinBet;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
