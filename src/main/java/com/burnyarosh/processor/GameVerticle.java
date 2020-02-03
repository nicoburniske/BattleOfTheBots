package com.burnyarosh.processor;

import com.burnyarosh.board.ChessBoard;
import com.burnyarosh.entity.Player;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

import java.util.Random;

public class GameVerticle extends AbstractVerticle {
    private Player[] players = new Player[2];
    private ChessBoard board;
    private int whitePlayer; // which player is white. corresponds to array index.


    @Override
    public void start(Future<Void> future) throws Exception {

    }

    private void newGame() {
        this.board = new ChessBoard();
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
