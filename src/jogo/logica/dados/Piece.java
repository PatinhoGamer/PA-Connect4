package jogo.logica.dados;

import java.io.Serializable;

public enum Piece implements Serializable {
	PLAYER1,
	PLAYER2;
	
	public Piece getOther() {
		return this == Piece.PLAYER1 ? Piece.PLAYER2 : Piece.PLAYER1;
	}
	
}
