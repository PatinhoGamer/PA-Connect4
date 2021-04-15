package jogo.logica.estados.minijogos;

public abstract class TimedGame {
	
	private long time;
	
	public void start() {
		time = System.currentTimeMillis();
	}
	
	public void stop() {
		time = System.currentTimeMillis() - time;
	}
	
	public abstract long availableTime();
	
}
