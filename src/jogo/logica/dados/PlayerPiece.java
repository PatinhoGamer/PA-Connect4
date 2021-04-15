package jogo.logica.dados;

public enum PlayerPiece {
	PLAYER1,
	PLAYER2;
	
	public PlayerPiece getOther(){
		return this == PlayerPiece.PLAYER1 ? PlayerPiece.PLAYER2 : PlayerPiece.PLAYER1;
	}
}
