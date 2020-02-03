package com.burnyarosh.entity;

public class Game extends Entity {
    private Player[] players = new Player[2];

    public Game(String name, String id, Player first) {
        super(name, id);
        this.players[0] = first;
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
}
