package com.burnyarosh.board.bot;

import com.burnyarosh.board.Board;
import com.burnyarosh.board.Chess;
import com.burnyarosh.board.common.Move;
import com.burnyarosh.board.common.Tuple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NickBot {
    Map<Board, Tuple<Move, Double>> bestMoves;

    public NickBot() {
        this.bestMoves = new HashMap<>();
    }

    public Tuple<Move, Double> calculateBestMove(Chess game, int depth) {
        return this.calculateBestMove(game, depth, game.getTurn().equals(Chess.PlayerColor.WHITE), Double.NEGATIVE_INFINITY, Double.MAX_VALUE);
    }
    public Tuple<Move, Double> calculateBestMove(Chess game, int depth, boolean isMax, double alpha, double beta) {
        if (depth == 0 || game.isGameOver()) {
            // TODO: review the purpose of isGameOver
            return this.staticEvaluation(game);
        }
        List<Move> moves = game.getAllPossibleMoves();
        // NEGASCOUT Algorithm and transposition tables
        Tuple<Move, Double> bestMove;
        if ((bestMove = this.bestMoves.get(game.getBoard())) != null) {
            return bestMove;
        }
        Tuple<Move, Double> max = new Tuple<>(null, Double.NEGATIVE_INFINITY);
        for (Move move : moves) {
            Tuple<Move, Double> currMove = this.calculateBestMove(game.testMove(move), depth - 1, !isMax, alpha  * -1, beta * -1);
            currMove.setRight(currMove.getRight() * -1);
            if (currMove.getRight() > max.getRight()) {
                this.bestMoves.put(game.getBoard().copy(), currMove);
                max.setLeft(move);
                max.setRight(currMove.getRight());
            }
            alpha = Math.max(currMove.getRight(), alpha);
            beta = Math.min(currMove.getRight(), beta);
            // if (beta <= alpha) break;
        }
        return max;
    }

    private Tuple<Move, Double> staticEvaluation(Chess game) {
        double score = game.getScore();
        return new Tuple<>(null, score);
    }

}
