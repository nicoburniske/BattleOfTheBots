package com.burnyarosh.entity;

import com.burnyarosh.board.ChessBoard;

import java.util.Random;

public class Game extends Entity {
    private Player[] players = new Player[2];
    private ChessBoard board;
    private int whitePlayer; // which player is white. corresponds to array index.

    public Game(String name, String id, Player first) {
        super(name, id);
        this.players[0] = first;
        this.board = new ChessBoard();
        this.randomColor();
    }

    public void newGame() {
        this.board = new ChessBoard();
    }

    public Player getPlayer(int index) {
        if (index != 1 || index != 2) throw new IllegalArgumentException("Invalid player number");
        return this.players[index -1];
    }

    public void addSecondPlayer(Player player) {
        if (gameIsReady()) { throw new IllegalStateException(String.format("Game %s is full", super.id));}
        this.players[1] = player;
    }

    public boolean gameIsReady() {
        return players[0] != null && players[1] != null;
    }

    public void randomColor() {
        this.whitePlayer = new Random().nextInt(2);
    }

    /**
     *
     * @param whitePlayer should be 1 or 2.
     */
    public void setWhitePlayer(int whitePlayer) {
        this.whitePlayer = whitePlayer - 1;
    }
}
