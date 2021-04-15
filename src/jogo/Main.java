package jogo;

import jogo.logica.Connect4Logic;
import jogo.iu.texto.Connect4TextUI;

public class Main {

    public static void main(String[] args) {
        Connect4Logic gameLogic = new Connect4Logic();
        Connect4TextUI ui = new Connect4TextUI(gameLogic);
        ui.start();
    }
}
