package jogo.logica.dados;

public enum Piece {
	PLAYER1,
	PLAYER2;
	
	public Piece getOther(){
		return this == Piece.PLAYER1 ? Piece.PLAYER2 : Piece.PLAYER1;
	}
}
