package com.omegasys.game.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * if etch player we create one bet(at 0) ... with constraint of one game at the time
 */
@Entity
@Table(name = "players")
public class Player {

    public Player() { }

	public Player(Long playerId, String playerName, String playerUsername, LocalDate playerBirthdate, double playerBalance, AtomicInteger numBet, AtomicInteger numOfWin) {
		this.playerId = playerId;
		this.playerName = playerName;
		this.playerUsername = playerUsername;
		this.playerBirthdate = playerBirthdate;
		this.playerBalance = playerBalance;
		this.numBet = numBet;
		this.numOfWin = numOfWin;
	}

	public Player(String playerName, String playerUsername, LocalDate playerBirthdate) {
		this.playerName = playerName;
		this.playerUsername = playerUsername;
		this.playerBirthdate = playerBirthdate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "player_id")
	private Long playerId;

	@Column(name = "player_name")
    private String playerName;

	@Column(name = "player_username", unique = true)
    private String playerUsername;

	@Column(name = "player_birthdate", nullable = false)
    private LocalDate playerBirthdate; // > 18

	@Column(name = "player_balance")
    private double playerBalance; //EUR only // Can't be negative

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "game_id", referencedColumnName = "game_id")
	private Game currentGame;

	@Column(name = "player_num_bet")
	private AtomicInteger numBet = new AtomicInteger(0);


	private AtomicInteger numOfWin = new AtomicInteger(0);

	public void incrementNumBet() {
		this.numBet.incrementAndGet();
	}

	public int getNumBet() {
		return this.numBet.get();
	}

	public void setNumBet(AtomicInteger numBet) {
		this.numBet = numBet;
	}

	public void incrementNumOfWin() {
		this.numOfWin.incrementAndGet();
	}

	public int getNumOfWin() {
		return this.numOfWin.get();
	}

	public void setNumOfWin(AtomicInteger numOfWin) {
		this.numOfWin = numOfWin;
	}

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerUsername() {
        return playerUsername;
    }

    public void setPlayerUsername(String playerUsername) {
        this.playerUsername = playerUsername;
    }

    public LocalDate getPlayerBirthdate() {
        return playerBirthdate;
    }

    public void setPlayerBirthdate(LocalDate playerBirthdate) {
        this.playerBirthdate = playerBirthdate;
    }

    public double getPlayerBalance() {
        return playerBalance;
    }

    public void setPlayerBalance(double playerBalance) {
        this.playerBalance = playerBalance;
    }

	public Game getCurrentGame() {
		return currentGame;
	}

	public void setCurrentGame(Game currentGame) {
		this.currentGame = currentGame;
	}
}
