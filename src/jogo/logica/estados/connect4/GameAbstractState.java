package jogo.logica.estados.connect4;

import jogo.logica.Connect4Logic;
import jogo.logica.dados.PlayerPiece;
import jogo.logica.dados.Player;
import jogo.logica.estados.minigames.TimedGame;

public abstract class GameAbstractState {
	
	protected Connect4Logic game;
	
	public GameAbstractState(Connect4Logic game) {
		this.game = game;
	}
	
	public GameAbstractState playAt(int column) {
		return this;
	}
	
	public GameAbstractState clearColumn(int column) {
		return this;
	}
	
	public GameAbstractState setPlayers(Player player1, Player player2) {
		return this;
	}
	
	public Connect4Logic getGame() {
		return game;
	}
	
	public PlayerPiece getWinner() {
		return null;
	}
	
	public Player getPlayer(PlayerPiece playerPiece) {
		return game.getPlayerFromEnum(playerPiece);
	}
	
	public PlayerPiece getCurrentPlayer() {
		return null;
	}
	
	public TimedGame getMiniGame() {
		return null;
	}
	
	public abstract Connect4States getState();
}
