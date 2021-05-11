package jogo.logica.dados;

import java.io.Serial;
import java.io.Serializable;

public enum Piece implements Serializable {
	PLAYER1,
	PLAYER2;
	
	@Serial
	private static final long serialVersionUID = 0L;
	
	public Piece getOther() {
		return this == Piece.PLAYER1 ? Piece.PLAYER2 : Piece.PLAYER1;
	}
	
}
