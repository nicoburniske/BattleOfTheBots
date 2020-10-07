package com.burnyarosh.board.bot;

import com.burnyarosh.board.Chess;
import com.burnyarosh.board.common.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NickBotTest {
    NickBot bot;
    Chess board;
    @BeforeEach
    void setUp() {
        board = new Chess();
        bot = new NickBot();
    }

    @Test
    void testCalculateBestMove() {
       Move move;
       while (!board.isGameOver()) {
           System.out.println("Next move");
           move = bot.calculateBestMove(board, 3).getLeft();
           System.out.println(move.toString());
           board.play(move);
           System.out.println(board.getBoard().toString());
       }
    }
}